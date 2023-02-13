package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import manager.Managers;
import manager.TaskManager;
import tasks.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Optional;


public class HttpTaskServer {

    private final HttpServer server;
    public static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
            .create();

    /*
    Сделано ужасно, я понимаю, но к сожалению, у меня были очень трудные недели, я немного потерялся и успел слепить только вот такую штуку.
    До меня не дошло как можно унифицировать всё это, чтобы не писать однотипные хендлеры, хотя бы для task/subtask/epic
    Есть вообще такая возможность? А то это дублирование кода совсем некрасиво выглядит
     */

    public HttpTaskServer() throws IOException {
        HttpTaskManager taskManager = (HttpTaskManager) Managers.getDefault();
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks", new TasksHandler(taskManager));
        server.createContext("/tasks/task", new TaskHandler(taskManager));
        server.createContext("/tasks/subtask", new SubtaskHandler(taskManager));
        server.createContext("/tasks/epic", new EpicHandler(taskManager));
    }

    static class TasksHandler implements HttpHandler {
        private final TaskManager taskManager;

        public TasksHandler(TaskManager taskManager) {
            this.taskManager = taskManager;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {

            Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath());
            switch (endpoint) {
                case GET_PRIORITIZED_TASKS:
                    handleGetPrioritizedTasks(exchange);
                    break;
                case GET_HISTORY:
                    handleGetHistory(exchange);
                    break;
                default:
                    writeResponse(exchange, "Такого эндпоинта не существует", 404);
            }
        }

        private void handleGetPrioritizedTasks(HttpExchange exchange) throws IOException {
            System.out.println(gson.toJson(taskManager.getPrioritizedTasks()));
            writeResponse(exchange, gson.toJson(taskManager.getPrioritizedTasks()), 200);
        }

        private void handleGetHistory(HttpExchange exchange) throws IOException {
            writeResponse(exchange, gson.toJson(taskManager.getHistory()), 200);
        }

        private Endpoint getEndpoint(String requestPath) {
            String[] pathParts = requestPath.split("/");
            if (pathParts.length == 2 && pathParts[1].equals("tasks")) {
                return Endpoint.GET_PRIORITIZED_TASKS;
            }
            if (pathParts.length == 3 && pathParts[1].equals("tasks") && pathParts[2].equals("history")) {
                return Endpoint.GET_HISTORY;
            }
            return Endpoint.UNKNOWN;
        }
    }

    static class TaskHandler implements HttpHandler {

        private final TaskManager taskManager;

        public TaskHandler(TaskManager taskManager) {
            this.taskManager = taskManager;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod(), exchange.getRequestURI().getQuery());
            switch (endpoint) {
                case GET_TASK:
                    handleGetTaskById(exchange);
                    break;
                case POST_TASK:
                    handlePostTaskById(exchange);
                    break;
                case DELETE_TASK:
                    handleDeleteTaskById(exchange);
                    break;
                case GET_TASKS:
                    handleGetAllTasks(exchange);
                    break;
                case DELETE_TASKS:
                    handleDeleteAllTasks(exchange);
                    break;
                default:
                    writeResponse(exchange, "Такого эндпоинта не существует", 404);
            }
        }

        private Endpoint getEndpoint(String requestPath, String requestMethod, String query) {
            String[] pathParts = requestPath.split("/");
            if (query != null && query.contains("id=") && pathParts.length == 3 && pathParts[1].equals("tasks") && pathParts[2].equals("task")) {
                switch (requestMethod) {
                    case "GET":
                        return Endpoint.GET_TASK;
                    case "DELETE":
                        return Endpoint.DELETE_TASK;
                }
            }
            if (pathParts.length == 3 && pathParts[1].equals("tasks") && pathParts[2].equals("task")) {
                switch (requestMethod) {
                    case "GET":
                        return Endpoint.GET_TASKS;
                    case "DELETE":
                        return Endpoint.DELETE_TASKS;
                    case "POST":
                        return Endpoint.POST_TASK;
                }
            }
            return Endpoint.UNKNOWN;
        }

        private void handleGetAllTasks(HttpExchange exchange) throws IOException {
            writeResponse(exchange, gson.toJson(taskManager.getTasks()), 200);
        }

        private void handleDeleteAllTasks(HttpExchange exchange) throws IOException {
            taskManager.deleteTasks();
            writeResponse(exchange, gson.toJson(taskManager.getTasks()), 200);
        }

        private void handleGetTaskById(HttpExchange exchange) throws IOException {
            if (getTaskId(exchange).isEmpty()) {
                writeResponse(exchange, "Некорректный идентификатор задачи", 400);
                return;
            }
            int id = getTaskId(exchange).get();
            writeResponse(exchange, gson.toJson(taskManager.getTask(id)), 200);
        }

        private void handleDeleteTaskById(HttpExchange exchange) throws IOException {
            if (getTaskId(exchange).isEmpty()) {
                writeResponse(exchange, "Некорректный идентификатор задачи", 400);
                return;
            }
            int id = getTaskId(exchange).get();
            boolean taskIsPresent = taskManager.getTasks().stream()
                    .anyMatch((task1) -> (task1.getId() == id));
            if (taskIsPresent) {
                taskManager.removeTask(id);
                writeResponse(exchange, "Задача №" + id + " удалена", 200);
            } else {
                writeResponse(exchange, "Задача №" + id + " не существует", 200);
            }
        }

        private void handlePostTaskById(HttpExchange exchange) throws IOException {
            String body = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
            Task task = gson.fromJson(body, Task.class);
            boolean taskIsPresent = taskManager.getTasks().stream()
                    .anyMatch(task1 -> (task1.getId() == task.getId()));
            if (taskIsPresent) {
                taskManager.updateTask(task);
                writeResponse(exchange, "Задача №" + task.getId() + " обновлена", 200);
            } else {
                if (task.getId() != 0) {
                    ((HttpTaskManager) taskManager).setIdentifier(task.getId() - 1);
                }
                taskManager.addNewTask(task);
                writeResponse(exchange, "Новая задача добавлена", 200);
            }
        }
    }

    static class SubtaskHandler implements HttpHandler {
        private final TaskManager taskManager;

        public SubtaskHandler(TaskManager taskManager) {
            this.taskManager = taskManager;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod(), exchange.getRequestURI().getQuery());
            switch (endpoint) {
                case GET_TASK:
                    handleGetSubtaskById(exchange);
                    break;
                case POST_TASK:
                    handlePostSubtaskById(exchange);
                    break;
                case DELETE_TASK:
                    handleDeleteSubtaskById(exchange);
                    break;
                case GET_TASKS:
                    handleGetAllSubtasks(exchange);
                    break;
                case DELETE_TASKS:
                    handleDeleteAllSubtasks(exchange);
                    break;
                case GET_LIST_OF_EPIC_SUBTASKS:
                    handleGetAllEpicSubtasks(exchange);
                    break;
                default:
                    writeResponse(exchange, "Такого эндпоинта не существует", 404);
            }
        }

        private Endpoint getEndpoint(String requestPath, String requestMethod, String query) {
            String[] pathParts = requestPath.split("/");
            if (query != null && query.contains("id=") && pathParts.length == 3 && pathParts[1].equals("tasks") && pathParts[2].equals("subtask")) {
                switch (requestMethod) {
                    case "GET":
                        return Endpoint.GET_TASK;
                    case "DELETE":
                        return Endpoint.DELETE_TASK;
                }
            }
            if (pathParts.length == 3 && pathParts[1].equals("tasks") && pathParts[2].equals("subtask")) {
                switch (requestMethod) {
                    case "GET":
                        return Endpoint.GET_TASKS;
                    case "DELETE":
                        return Endpoint.DELETE_TASKS;
                    case "POST":
                        return Endpoint.POST_TASK;
                }
            }
            if (pathParts.length == 4 && pathParts[1].equals("tasks") && pathParts[2].equals("subtask")
                    && pathParts[3].equals("epic") && requestMethod.equals("GET")) {
                return Endpoint.GET_LIST_OF_EPIC_SUBTASKS;
            }
            return Endpoint.UNKNOWN;
        }

        private void handleGetAllEpicSubtasks(HttpExchange exchange) throws IOException {
            if (getTaskId(exchange).isEmpty()) {
                writeResponse(exchange, "Некорректный идентификатор задачи", 400);
                return;
            }
            int id = getTaskId(exchange).get();
            taskManager.listOfEpicSubtasks(id);
            writeResponse(exchange, gson.toJson(taskManager.listOfEpicSubtasks(id)), 200);
        }

        private void handleGetAllSubtasks(HttpExchange exchange) throws IOException {
            writeResponse(exchange, gson.toJson(taskManager.getSubtasks()), 200);
        }

        private void handleDeleteAllSubtasks(HttpExchange exchange) throws IOException {
            taskManager.deleteSubtasks();
            writeResponse(exchange, gson.toJson(taskManager.getSubtasks()), 200);
        }

        private void handleGetSubtaskById(HttpExchange exchange) throws IOException {
            if (getTaskId(exchange).isEmpty()) {
                writeResponse(exchange, "Некорректный идентификатор задачи", 400);
                return;
            }
            int id = getTaskId(exchange).get();
            writeResponse(exchange, gson.toJson(taskManager.getSubtask(id)), 200);
        }

        private void handleDeleteSubtaskById(HttpExchange exchange) throws IOException {
            if (getTaskId(exchange).isEmpty()) {
                writeResponse(exchange, "Некорректный идентификатор задачи", 400);
                return;
            }
            int id = getTaskId(exchange).get();
            boolean taskIsPresent = taskManager.getSubtasks().stream()
                    .anyMatch((task1) -> (task1.getId() == id));
            if (taskIsPresent) {
                taskManager.removeSubtask(id);
                writeResponse(exchange, "Подзадача №" + id + " удалена", 200);
            } else {
                writeResponse(exchange, "Подзадачи №" + id + " не существует", 200);
            }
        }

        private void handlePostSubtaskById(HttpExchange exchange) throws IOException {
            String body = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
            Subtask task = gson.fromJson(body, Subtask.class);
            boolean taskIsPresent = taskManager.getSubtasks().stream()
                    .anyMatch(task1 -> (task1.getId() == task.getId()));
            if (taskIsPresent) {
                taskManager.updateSubtask(task);
                writeResponse(exchange, "Подзадача №" + task.getId() + " обновлена", 200);
            } else {
                taskManager.addNewSubtask(task);
                writeResponse(exchange, "Новая подзадача добавлена", 200);
            }
        }
    }

    static class EpicHandler implements HttpHandler {

        private final TaskManager taskManager;

        public EpicHandler(TaskManager taskManager) {
            this.taskManager = taskManager;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod(), exchange.getRequestURI().getQuery());
            switch (endpoint) {
                case GET_TASK:
                    handleGetEpicById(exchange);
                    break;
                case POST_TASK:
                    handlePostEpicById(exchange);
                    break;
                case DELETE_TASK:
                    handleDeleteEpicById(exchange);
                    break;
                case GET_TASKS:
                    handleGetAllEpics(exchange);
                    break;
                case DELETE_TASKS:
                    handleDeleteAllEpics(exchange);
                    break;
                default:
                    writeResponse(exchange, "Такого эндпоинта не существует", 404);
            }
        }

        private Endpoint getEndpoint(String requestPath, String requestMethod, String query) {
            String[] pathParts = requestPath.split("/");
            if (query != null && query.contains("id=") && pathParts.length == 3 && pathParts[1].equals("tasks") && pathParts[2].equals("epic")) {
                switch (requestMethod) {
                    case "GET":
                        return Endpoint.GET_TASK;
                    case "DELETE":
                        return Endpoint.DELETE_TASK;
                }
            }
            if (pathParts.length == 3 && pathParts[1].equals("tasks") && pathParts[2].equals("epic")) {
                switch (requestMethod) {
                    case "GET":
                        return Endpoint.GET_TASKS;
                    case "DELETE":
                        return Endpoint.DELETE_TASKS;
                    case "POST":
                        return Endpoint.POST_TASK;
                }
            }
            return Endpoint.UNKNOWN;
        }

        private void handleGetAllEpics(HttpExchange exchange) throws IOException {
            writeResponse(exchange, gson.toJson(taskManager.getEpics()), 200);
        }

        private void handleDeleteAllEpics(HttpExchange exchange) throws IOException {
            taskManager.deleteEpics();
            writeResponse(exchange, gson.toJson(taskManager.getEpics()), 200);
        }

        private void handleGetEpicById(HttpExchange exchange) throws IOException {
            if (getTaskId(exchange).isEmpty()) {
                writeResponse(exchange, "Некорректный идентификатор задачи", 400);
                return;
            }
            int id = getTaskId(exchange).get();
            writeResponse(exchange, gson.toJson(taskManager.getEpic(id)), 200);
        }

        private void handleDeleteEpicById(HttpExchange exchange) throws IOException {
            if (getTaskId(exchange).isEmpty()) {
                writeResponse(exchange, "Некорректный идентификатор задачи", 400);
                return;
            }
            int id = getTaskId(exchange).get();
            boolean taskIsPresent = taskManager.getEpics().stream()
                    .anyMatch((task1) -> (task1.getId() == id));
            if (taskIsPresent) {
                taskManager.removeEpic(id);
                writeResponse(exchange, "Эпик №" + id + " удалён", 200);
            } else {
                writeResponse(exchange, "Эпик №" + id + " не существует", 200);
            }
        }

        private void handlePostEpicById(HttpExchange exchange) throws IOException {
            String body = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
            Epic task = gson.fromJson(body, Epic.class);
            boolean taskIsPresent = taskManager.getEpics().stream()
                    .anyMatch(task1 -> (task1.getId() == task.getId()));
            if (taskIsPresent) {
                taskManager.updateEpic(task);
                writeResponse(exchange, "Эпик №" + task.getId() + " обновлён", 200);
            } else {
                taskManager.addNewEpic(task);
                writeResponse(exchange, "Новый эпик добавлен", 200);
            }
        }
    }

    private static void writeResponse(HttpExchange exchange, String responseString, int responseCode) throws IOException {
        if (responseString.isBlank()) {
            exchange.sendResponseHeaders(responseCode, 0);
        } else {
            byte[] bytes = responseString.getBytes(DEFAULT_CHARSET);
            exchange.sendResponseHeaders(responseCode, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
        exchange.close();
    }

    private static Optional<Integer> getTaskId(HttpExchange exchange) {
        String query = exchange.getRequestURI().getRawQuery();
        int id = Integer.parseInt(query.substring(query.lastIndexOf("id=") + 3).trim());
        try {
            return Optional.of(id);
        } catch (NumberFormatException exception) {
            return Optional.empty();
        }
    }

    public void start() {
        System.out.println("Запускаем HttpTaskServer на порту " + PORT);
        System.out.println("Открой в браузере http://localhost:" + PORT + "/");
        server.start();
    }

    public void stop() {
        server.stop(0);
    }
}
