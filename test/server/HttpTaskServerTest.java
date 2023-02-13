package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskServerTest {

    public Epic epic1;
    public Epic epic2;
    public Subtask subtask1;
    public Subtask subtask2;
    public Task task1;
    public Task task2;
    HttpTaskManager taskManager;
    private KVServer serverKV;
    private HttpTaskServer serverTask;
    private final LocalDateTime startTime = LocalDateTime.of(2023, 1, 23, 10, 0);
    private HttpClient client;
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
            .create();
    HttpRequest request;

    @BeforeEach
    public void BeforeEach() throws IOException, InterruptedException {
        client = HttpClient.newHttpClient();
        epic1 = new Epic("Epic1", "descriptionEpic1");
        epic2 = new Epic("Epic2", "descriptionEpic2");
        subtask1 = new Subtask("Subtask1", "descriptionSubtask1", 1);
        subtask1.setStartTime(startTime);
        subtask1.setDuration(120);
        subtask2 = new Subtask("Subtask2", "descriptionSubtask2", 1);
        subtask2.setStartTime(startTime.plusHours(3));
        subtask2.setDuration(180);
        task1 = new Task("Task1", "descriptionTask1");
        task1.setStartTime(startTime.plusHours(7));
        task1.setDuration(120);
        task2 = new Task("Task2", "descriptionTask2");
        task2.setStartTime(startTime.plusHours(10));
        task2.setDuration(180);

        serverKV = new KVServer();
        serverKV.start();
        serverTask = new HttpTaskServer();
        serverTask.start();
        String url = "http://localhost:8078";
        taskManager = new HttpTaskManager(url);
        request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks/epic"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic1))).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks/epic"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic2))).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks/subtask"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtask1))).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks/subtask"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtask2))).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks/task"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task1))).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks/task"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task2))).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    @AfterEach
    public void AfterEach() {
        serverKV.stop();
        serverTask.stop();
    }

    @Test
    public void tasksEndpoint() throws IOException, InterruptedException {
        //Получаем все задачи в приоритете
        URI url;
        HttpRequest request;
        HttpResponse<String> response;
        url = URI.create("http://localhost:8080/tasks");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String trueResponse = "[\n" +
                "  {\n" +
                "    \"epicId\": 1,\n" +
                "    \"duration\": 120,\n" +
                "    \"startTime\": \"23.01.2023; 10:00\",\n" +
                "    \"name\": \"Subtask1\",\n" +
                "    \"description\": \"descriptionSubtask1\",\n" +
                "    \"id\": 3,\n" +
                "    \"status\": \"NEW\",\n" +
                "    \"type\": \"SUBTASK\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"epicId\": 1,\n" +
                "    \"duration\": 180,\n" +
                "    \"startTime\": \"23.01.2023; 13:00\",\n" +
                "    \"name\": \"Subtask2\",\n" +
                "    \"description\": \"descriptionSubtask2\",\n" +
                "    \"id\": 4,\n" +
                "    \"status\": \"NEW\",\n" +
                "    \"type\": \"SUBTASK\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"duration\": 120,\n" +
                "    \"startTime\": \"23.01.2023; 17:00\",\n" +
                "    \"name\": \"Task1\",\n" +
                "    \"description\": \"descriptionTask1\",\n" +
                "    \"id\": 5,\n" +
                "    \"status\": \"NEW\",\n" +
                "    \"type\": \"TASK\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"duration\": 180,\n" +
                "    \"startTime\": \"23.01.2023; 20:00\",\n" +
                "    \"name\": \"Task2\",\n" +
                "    \"description\": \"descriptionTask2\",\n" +
                "    \"id\": 6,\n" +
                "    \"status\": \"NEW\",\n" +
                "    \"type\": \"TASK\"\n" +
                "  }\n" +
                "]";
        assertEquals(trueResponse, response.body(), "Списки задач не совпадают");
        // Получаем историю
        url = URI.create("http://localhost:8080/tasks/task?id=5");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        url = URI.create("http://localhost:8080/tasks/task?id=6");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        url = URI.create("http://localhost:8080/tasks/history");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        trueResponse = "[\n" +
                "  {\n" +
                "    \"duration\": 120,\n" +
                "    \"startTime\": \"23.01.2023; 17:00\",\n" +
                "    \"name\": \"Task1\",\n" +
                "    \"description\": \"descriptionTask1\",\n" +
                "    \"id\": 5,\n" +
                "    \"status\": \"NEW\",\n" +
                "    \"type\": \"TASK\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"duration\": 180,\n" +
                "    \"startTime\": \"23.01.2023; 20:00\",\n" +
                "    \"name\": \"Task2\",\n" +
                "    \"description\": \"descriptionTask2\",\n" +
                "    \"id\": 6,\n" +
                "    \"status\": \"NEW\",\n" +
                "    \"type\": \"TASK\"\n" +
                "  }\n" +
                "]";
        assertEquals(trueResponse, response.body(), "Списки историй не совпадают");
    }

    @Test
    public void taskEndpoint() throws IOException, InterruptedException {
        URI url;
        HttpRequest request;
        HttpResponse<String> response;
        String trueResponse;

        // GET - получение всех задач
        url = URI.create("http://localhost:8080/tasks/task/");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        trueResponse = "[\n" +
                "  {\n" +
                "    \"duration\": 120,\n" +
                "    \"startTime\": \"23.01.2023; 17:00\",\n" +
                "    \"name\": \"Task1\",\n" +
                "    \"description\": \"descriptionTask1\",\n" +
                "    \"id\": 5,\n" +
                "    \"status\": \"NEW\",\n" +
                "    \"type\": \"TASK\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"duration\": 180,\n" +
                "    \"startTime\": \"23.01.2023; 20:00\",\n" +
                "    \"name\": \"Task2\",\n" +
                "    \"description\": \"descriptionTask2\",\n" +
                "    \"id\": 6,\n" +
                "    \"status\": \"NEW\",\n" +
                "    \"type\": \"TASK\"\n" +
                "  }\n" +
                "]";
        assertEquals(trueResponse, response.body(), "Списки историй не совпадают");

        // GET - получение задачи по ид
        // id5
        url = URI.create("http://localhost:8080/tasks/task?id=5");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        trueResponse = "{\n" +
                "  \"duration\": 120,\n" +
                "  \"startTime\": \"23.01.2023; 17:00\",\n" +
                "  \"name\": \"Task1\",\n" +
                "  \"description\": \"descriptionTask1\",\n" +
                "  \"id\": 5,\n" +
                "  \"status\": \"NEW\",\n" +
                "  \"type\": \"TASK\"\n" +
                "}";
        assertEquals(trueResponse, response.body(), "Списки историй не совпадают");
        // id6
        url = URI.create("http://localhost:8080/tasks/task?id=6");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        trueResponse = "{\n" +
                "  \"duration\": 180,\n" +
                "  \"startTime\": \"23.01.2023; 20:00\",\n" +
                "  \"name\": \"Task2\",\n" +
                "  \"description\": \"descriptionTask2\",\n" +
                "  \"id\": 6,\n" +
                "  \"status\": \"NEW\",\n" +
                "  \"type\": \"TASK\"\n" +
                "}";
        assertEquals(trueResponse, response.body(), "Списки историй не совпадают");

        // POST - добавление задачи/обновление
        // добавим 7 задачу
        url = URI.create("http://localhost:8080/tasks/task/");
        Task newTask = new Task("Task7", "description7");
        request = HttpRequest.newBuilder().uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(newTask))).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        // получаем новую задачу 7
        url = URI.create("http://localhost:8080/tasks/task?id=7");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        trueResponse = "{\n" +
                "  \"duration\": 0,\n" +
                "  \"name\": \"Task7\",\n" +
                "  \"description\": \"description7\",\n" +
                "  \"id\": 7,\n" +
                "  \"status\": \"NEW\",\n" +
                "  \"type\": \"TASK\"\n" +
                "}";
        assertEquals(trueResponse, response.body(), "Задачи не совпадают");
        // обновим 6
        task2.setDescription("Задача обновилась!");
        task2.setId(6);
        url = URI.create("http://localhost:8080/tasks/task/");
        request = HttpRequest.newBuilder().uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task2))).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        // получаем обновлённую 6
        url = URI.create("http://localhost:8080/tasks/task?id=6");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        trueResponse = "{\n" +
                "  \"duration\": 180,\n" +
                "  \"startTime\": \"23.01.2023; 20:00\",\n" +
                "  \"name\": \"Task2\",\n" +
                "  \"description\": \"Задача обновилась!\",\n" +
                "  \"id\": 6,\n" +
                "  \"status\": \"NEW\",\n" +
                "  \"type\": \"TASK\"\n" +
                "}";
        assertEquals(trueResponse, response.body(), "Задачи не совпадают");

        // DELETE - удаление задачи по ид
        url = URI.create("http://localhost:8080/tasks/task?id=6");
        request = HttpRequest.newBuilder().uri(url).DELETE().build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        url = URI.create("http://localhost:8080/tasks/task");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        trueResponse = "[\n" +
                "  {\n" +
                "    \"duration\": 120,\n" +
                "    \"startTime\": \"23.01.2023; 17:00\",\n" +
                "    \"name\": \"Task1\",\n" +
                "    \"description\": \"descriptionTask1\",\n" +
                "    \"id\": 5,\n" +
                "    \"status\": \"NEW\",\n" +
                "    \"type\": \"TASK\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"duration\": 0,\n" +
                "    \"name\": \"Task7\",\n" +
                "    \"description\": \"description7\",\n" +
                "    \"id\": 7,\n" +
                "    \"status\": \"NEW\",\n" +
                "    \"type\": \"TASK\"\n" +
                "  }\n" +
                "]";
        assertEquals(trueResponse, response.body(), "Задачи не совпадают");

        // DELETE - удаление всех задач
        url = URI.create("http://localhost:8080/tasks/task");
        request = HttpRequest.newBuilder().uri(url).DELETE().build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        url = URI.create("http://localhost:8080/tasks/task");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        trueResponse = "[]";
        assertEquals(trueResponse, response.body(), "Задачи не совпадают");
    }

    @Test
    public void subtaskEndpoint() throws IOException, InterruptedException {
        URI url;
        HttpRequest request;
        HttpResponse<String> response;
        String trueResponse;

        /*
        почему-то список подзадач эпика по ТЗ должно лежать в эндпоинте subtask, поэтому тест сделаю тоже тут
        хотя вроде бы логично всё это сделать именно в эндпоинте epic
        */
        // listOfEpicSubtasks
        //1
        url = URI.create("http://localhost:8080/tasks/subtask/epic?id=1");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        trueResponse = "[\n" +
                "  {\n" +
                "    \"epicId\": 1,\n" +
                "    \"duration\": 120,\n" +
                "    \"startTime\": \"23.01.2023; 10:00\",\n" +
                "    \"name\": \"Subtask1\",\n" +
                "    \"description\": \"descriptionSubtask1\",\n" +
                "    \"id\": 3,\n" +
                "    \"status\": \"NEW\",\n" +
                "    \"type\": \"SUBTASK\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"epicId\": 1,\n" +
                "    \"duration\": 180,\n" +
                "    \"startTime\": \"23.01.2023; 13:00\",\n" +
                "    \"name\": \"Subtask2\",\n" +
                "    \"description\": \"descriptionSubtask2\",\n" +
                "    \"id\": 4,\n" +
                "    \"status\": \"NEW\",\n" +
                "    \"type\": \"SUBTASK\"\n" +
                "  }\n" +
                "]";
        assertEquals(trueResponse, response.body(), "Задачи не совпадают");
        //2
        url = URI.create("http://localhost:8080/tasks/subtask/epic?id=2");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        trueResponse = "[]";
        System.out.println(response.body());
        assertEquals(trueResponse, response.body(), "Задачи не совпадают");

        // GET - получение всех задач
        url = URI.create("http://localhost:8080/tasks/subtask/");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        trueResponse = "[\n" +
                "  {\n" +
                "    \"epicId\": 1,\n" +
                "    \"duration\": 120,\n" +
                "    \"startTime\": \"23.01.2023; 10:00\",\n" +
                "    \"name\": \"Subtask1\",\n" +
                "    \"description\": \"descriptionSubtask1\",\n" +
                "    \"id\": 3,\n" +
                "    \"status\": \"NEW\",\n" +
                "    \"type\": \"SUBTASK\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"epicId\": 1,\n" +
                "    \"duration\": 180,\n" +
                "    \"startTime\": \"23.01.2023; 13:00\",\n" +
                "    \"name\": \"Subtask2\",\n" +
                "    \"description\": \"descriptionSubtask2\",\n" +
                "    \"id\": 4,\n" +
                "    \"status\": \"NEW\",\n" +
                "    \"type\": \"SUBTASK\"\n" +
                "  }\n" +
                "]";
        assertEquals(trueResponse, response.body(), "Списки историй не совпадают");

        // GET - получение задачи по ид
        // id5
        url = URI.create("http://localhost:8080/tasks/subtask?id=3");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        trueResponse = "{\n" +
                "  \"epicId\": 1,\n" +
                "  \"duration\": 120,\n" +
                "  \"startTime\": \"23.01.2023; 10:00\",\n" +
                "  \"name\": \"Subtask1\",\n" +
                "  \"description\": \"descriptionSubtask1\",\n" +
                "  \"id\": 3,\n" +
                "  \"status\": \"NEW\",\n" +
                "  \"type\": \"SUBTASK\"\n" +
                "}";
        assertEquals(trueResponse, response.body(), "Списки историй не совпадают");
        // id6
        url = URI.create("http://localhost:8080/tasks/subtask?id=4");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        trueResponse = "{\n" +
                "  \"epicId\": 1,\n" +
                "  \"duration\": 180,\n" +
                "  \"startTime\": \"23.01.2023; 13:00\",\n" +
                "  \"name\": \"Subtask2\",\n" +
                "  \"description\": \"descriptionSubtask2\",\n" +
                "  \"id\": 4,\n" +
                "  \"status\": \"NEW\",\n" +
                "  \"type\": \"SUBTASK\"\n" +
                "}";
        assertEquals(trueResponse, response.body(), "Списки историй не совпадают");

        // POST - добавление задачи/обновление
        // добавим 7 задачу
        url = URI.create("http://localhost:8080/tasks/subtask/");
        Task newTask = new Subtask("SubTaskPOST", "descriptionSubtaskPOST", 2);
        request = HttpRequest.newBuilder().uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(newTask))).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        // получаем новую задачу 7
        url = URI.create("http://localhost:8080/tasks/subtask?id=7");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        trueResponse = "{\n" +
                "  \"epicId\": 2,\n" +
                "  \"duration\": 0,\n" +
                "  \"name\": \"SubTaskPOST\",\n" +
                "  \"description\": \"descriptionSubtaskPOST\",\n" +
                "  \"id\": 7,\n" +
                "  \"status\": \"NEW\",\n" +
                "  \"type\": \"SUBTASK\"\n" +
                "}";
        assertEquals(trueResponse, response.body(), "Задачи не совпадают");
        // обновим 3
        subtask1.setDescription("Задача обновилась!");
        subtask1.setId(3);
        url = URI.create("http://localhost:8080/tasks/subtask/");
        request = HttpRequest.newBuilder().uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtask1))).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        // получаем обновлённую 6
        url = URI.create("http://localhost:8080/tasks/subtask?id=3");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        trueResponse = "{\n" +
                "  \"epicId\": 1,\n" +
                "  \"duration\": 120,\n" +
                "  \"startTime\": \"23.01.2023; 10:00\",\n" +
                "  \"name\": \"Subtask1\",\n" +
                "  \"description\": \"Задача обновилась!\",\n" +
                "  \"id\": 3,\n" +
                "  \"status\": \"NEW\",\n" +
                "  \"type\": \"SUBTASK\"\n" +
                "}";
        assertEquals(trueResponse, response.body(), "Задачи не совпадают");

        // DELETE - удаление задачи по ид
        url = URI.create("http://localhost:8080/tasks/subtask?id=7");
        request = HttpRequest.newBuilder().uri(url).DELETE().build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        url = URI.create("http://localhost:8080/tasks/subtask");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        trueResponse = "[\n" +
                "  {\n" +
                "    \"epicId\": 1,\n" +
                "    \"duration\": 120,\n" +
                "    \"startTime\": \"23.01.2023; 10:00\",\n" +
                "    \"name\": \"Subtask1\",\n" +
                "    \"description\": \"Задача обновилась!\",\n" +
                "    \"id\": 3,\n" +
                "    \"status\": \"NEW\",\n" +
                "    \"type\": \"SUBTASK\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"epicId\": 1,\n" +
                "    \"duration\": 180,\n" +
                "    \"startTime\": \"23.01.2023; 13:00\",\n" +
                "    \"name\": \"Subtask2\",\n" +
                "    \"description\": \"descriptionSubtask2\",\n" +
                "    \"id\": 4,\n" +
                "    \"status\": \"NEW\",\n" +
                "    \"type\": \"SUBTASK\"\n" +
                "  }\n" +
                "]";
        assertEquals(trueResponse, response.body(), "Задачи не совпадают");

        // DELETE - удаление всех задач
        url = URI.create("http://localhost:8080/tasks/subtask");
        request = HttpRequest.newBuilder().uri(url).DELETE().build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        url = URI.create("http://localhost:8080/tasks/subtask");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        trueResponse = "[]";
        assertEquals(trueResponse, response.body(), "Задачи не совпадают");
    }

    @Test
    public void epicEndpoint() throws IOException, InterruptedException {
        URI url;
        HttpRequest request;
        HttpResponse<String> response;
        String trueResponse;

        // GET - получение всех задач
        url = URI.create("http://localhost:8080/tasks/epic/");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        trueResponse = "[\n" +
                "  {\n" +
                "    \"endTime\": \"23.01.2023; 16:00\",\n" +
                "    \"subtasks\": {\n" +
                "      \"3\": {\n" +
                "        \"epicId\": 1,\n" +
                "        \"duration\": 120,\n" +
                "        \"startTime\": \"23.01.2023; 10:00\",\n" +
                "        \"name\": \"Subtask1\",\n" +
                "        \"description\": \"descriptionSubtask1\",\n" +
                "        \"id\": 3,\n" +
                "        \"status\": \"NEW\",\n" +
                "        \"type\": \"SUBTASK\"\n" +
                "      },\n" +
                "      \"4\": {\n" +
                "        \"epicId\": 1,\n" +
                "        \"duration\": 180,\n" +
                "        \"startTime\": \"23.01.2023; 13:00\",\n" +
                "        \"name\": \"Subtask2\",\n" +
                "        \"description\": \"descriptionSubtask2\",\n" +
                "        \"id\": 4,\n" +
                "        \"status\": \"NEW\",\n" +
                "        \"type\": \"SUBTASK\"\n" +
                "      }\n" +
                "    },\n" +
                "    \"duration\": 300,\n" +
                "    \"startTime\": \"23.01.2023; 10:00\",\n" +
                "    \"name\": \"Epic1\",\n" +
                "    \"description\": \"descriptionEpic1\",\n" +
                "    \"id\": 1,\n" +
                "    \"status\": \"NEW\",\n" +
                "    \"type\": \"EPIC\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"subtasks\": {},\n" +
                "    \"duration\": 0,\n" +
                "    \"name\": \"Epic2\",\n" +
                "    \"description\": \"descriptionEpic2\",\n" +
                "    \"id\": 2,\n" +
                "    \"status\": \"NEW\",\n" +
                "    \"type\": \"EPIC\"\n" +
                "  }\n" +
                "]";
        assertEquals(trueResponse, response.body(), "Списки историй не совпадают");

        // GET - получение задачи по ид
        // id1
        url = URI.create("http://localhost:8080/tasks/epic?id=1");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        trueResponse = "{\n" +
                "  \"endTime\": \"23.01.2023; 16:00\",\n" +
                "  \"subtasks\": {\n" +
                "    \"3\": {\n" +
                "      \"epicId\": 1,\n" +
                "      \"duration\": 120,\n" +
                "      \"startTime\": \"23.01.2023; 10:00\",\n" +
                "      \"name\": \"Subtask1\",\n" +
                "      \"description\": \"descriptionSubtask1\",\n" +
                "      \"id\": 3,\n" +
                "      \"status\": \"NEW\",\n" +
                "      \"type\": \"SUBTASK\"\n" +
                "    },\n" +
                "    \"4\": {\n" +
                "      \"epicId\": 1,\n" +
                "      \"duration\": 180,\n" +
                "      \"startTime\": \"23.01.2023; 13:00\",\n" +
                "      \"name\": \"Subtask2\",\n" +
                "      \"description\": \"descriptionSubtask2\",\n" +
                "      \"id\": 4,\n" +
                "      \"status\": \"NEW\",\n" +
                "      \"type\": \"SUBTASK\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"duration\": 300,\n" +
                "  \"startTime\": \"23.01.2023; 10:00\",\n" +
                "  \"name\": \"Epic1\",\n" +
                "  \"description\": \"descriptionEpic1\",\n" +
                "  \"id\": 1,\n" +
                "  \"status\": \"NEW\",\n" +
                "  \"type\": \"EPIC\"\n" +
                "}";
        assertEquals(trueResponse, response.body(), "Списки историй не совпадают");
        // id2
        url = URI.create("http://localhost:8080/tasks/epic?id=2");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        trueResponse = "{\n" +
                "  \"subtasks\": {},\n" +
                "  \"duration\": 0,\n" +
                "  \"name\": \"Epic2\",\n" +
                "  \"description\": \"descriptionEpic2\",\n" +
                "  \"id\": 2,\n" +
                "  \"status\": \"NEW\",\n" +
                "  \"type\": \"EPIC\"\n" +
                "}";
        assertEquals(trueResponse, response.body(), "Списки историй не совпадают");

        // POST - добавление задачи/обновление
        // добавим 7 задачу
        url = URI.create("http://localhost:8080/tasks/epic/");
        Task newTask = new Epic("EpicPOST", "descriptionEpicPOST");
        request = HttpRequest.newBuilder().uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(newTask))).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        // получаем новую задачу 7
        url = URI.create("http://localhost:8080/tasks/epic?id=7");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        trueResponse = "{\n" +
                "  \"subtasks\": {},\n" +
                "  \"duration\": 0,\n" +
                "  \"name\": \"EpicPOST\",\n" +
                "  \"description\": \"descriptionEpicPOST\",\n" +
                "  \"id\": 7,\n" +
                "  \"status\": \"NEW\",\n" +
                "  \"type\": \"EPIC\"\n" +
                "}";
        assertEquals(trueResponse, response.body(), "Задачи не совпадают");
        // обновим 3
        epic1.setDescription("Задача обновилась!");
        epic1.setId(1);
        url = URI.create("http://localhost:8080/tasks/epic/");
        request = HttpRequest.newBuilder().uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic1))).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        // получаем обновлённую 6
        url = URI.create("http://localhost:8080/tasks/epic?id=1");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        trueResponse = "{\n" +
                "  \"endTime\": \"23.01.2023; 16:00\",\n" +
                "  \"subtasks\": {\n" +
                "    \"3\": {\n" +
                "      \"epicId\": 1,\n" +
                "      \"duration\": 120,\n" +
                "      \"startTime\": \"23.01.2023; 10:00\",\n" +
                "      \"name\": \"Subtask1\",\n" +
                "      \"description\": \"descriptionSubtask1\",\n" +
                "      \"id\": 3,\n" +
                "      \"status\": \"NEW\",\n" +
                "      \"type\": \"SUBTASK\"\n" +
                "    },\n" +
                "    \"4\": {\n" +
                "      \"epicId\": 1,\n" +
                "      \"duration\": 180,\n" +
                "      \"startTime\": \"23.01.2023; 13:00\",\n" +
                "      \"name\": \"Subtask2\",\n" +
                "      \"description\": \"descriptionSubtask2\",\n" +
                "      \"id\": 4,\n" +
                "      \"status\": \"NEW\",\n" +
                "      \"type\": \"SUBTASK\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"duration\": 300,\n" +
                "  \"startTime\": \"23.01.2023; 10:00\",\n" +
                "  \"name\": \"Epic1\",\n" +
                "  \"description\": \"Задача обновилась!\",\n" +
                "  \"id\": 1,\n" +
                "  \"status\": \"NEW\",\n" +
                "  \"type\": \"EPIC\"\n" +
                "}";
        assertEquals(trueResponse, response.body(), "Задачи не совпадают");

        // DELETE - удаление задачи по ид
        url = URI.create("http://localhost:8080/tasks/epic?id=7");
        request = HttpRequest.newBuilder().uri(url).DELETE().build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        url = URI.create("http://localhost:8080/tasks/epic");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        trueResponse = "[\n" +
                "  {\n" +
                "    \"endTime\": \"23.01.2023; 16:00\",\n" +
                "    \"subtasks\": {\n" +
                "      \"3\": {\n" +
                "        \"epicId\": 1,\n" +
                "        \"duration\": 120,\n" +
                "        \"startTime\": \"23.01.2023; 10:00\",\n" +
                "        \"name\": \"Subtask1\",\n" +
                "        \"description\": \"descriptionSubtask1\",\n" +
                "        \"id\": 3,\n" +
                "        \"status\": \"NEW\",\n" +
                "        \"type\": \"SUBTASK\"\n" +
                "      },\n" +
                "      \"4\": {\n" +
                "        \"epicId\": 1,\n" +
                "        \"duration\": 180,\n" +
                "        \"startTime\": \"23.01.2023; 13:00\",\n" +
                "        \"name\": \"Subtask2\",\n" +
                "        \"description\": \"descriptionSubtask2\",\n" +
                "        \"id\": 4,\n" +
                "        \"status\": \"NEW\",\n" +
                "        \"type\": \"SUBTASK\"\n" +
                "      }\n" +
                "    },\n" +
                "    \"duration\": 300,\n" +
                "    \"startTime\": \"23.01.2023; 10:00\",\n" +
                "    \"name\": \"Epic1\",\n" +
                "    \"description\": \"Задача обновилась!\",\n" +
                "    \"id\": 1,\n" +
                "    \"status\": \"NEW\",\n" +
                "    \"type\": \"EPIC\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"subtasks\": {},\n" +
                "    \"duration\": 0,\n" +
                "    \"name\": \"Epic2\",\n" +
                "    \"description\": \"descriptionEpic2\",\n" +
                "    \"id\": 2,\n" +
                "    \"status\": \"NEW\",\n" +
                "    \"type\": \"EPIC\"\n" +
                "  }\n" +
                "]";
        assertEquals(trueResponse, response.body(), "Задачи не совпадают");

        // DELETE - удаление всех задач
        url = URI.create("http://localhost:8080/tasks/epic");
        request = HttpRequest.newBuilder().uri(url).DELETE().build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        url = URI.create("http://localhost:8080/tasks/epic");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        trueResponse = "[]";
        assertEquals(trueResponse, response.body(), "Задачи не совпадают");
    }
}
