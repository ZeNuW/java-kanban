package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import manager.file.FileBackedTasksManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager {

    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
            .create();
    private final KVTaskClient client;

    public HttpTaskManager(String url) {
        super(null);
        client = new KVTaskClient(url);
    }

    @Override
    public void save() {
        client.put("tasks", gson.toJson(getTasks()));
        client.put("subtasks", gson.toJson(getSubtasks()));
        client.put("epics", gson.toJson(getEpics()));
        client.put("history", gson.toJson(getHistory()));
    }

    public static HttpTaskManager load(String url) {
        /*
        Не совсем понял к чему тут метод load, т.к вроде при старте сервера у нас всё будет пустое, ибо data с KVServer
        нигде не храниться ну и соответственно нечего считывать, не уверен, что это всё вообще правильно работает.
        Поэтому у меня и вопрос, нужен ли этот метод? И где его применять в таком случае.
         */
        HttpTaskManager htm = new HttpTaskManager(url);
        List<Task> allTasks = new ArrayList<>();
        String tasksString = htm.client.load("tasks");
        String subtasksString = htm.client.load("subtasks");
        String epicsString = htm.client.load("epics");
        String historyString = htm.client.load("history");
        if (tasksString != null) {
            List<Task> tasksList = gson.fromJson(tasksString,
                    TypeToken.getParameterized(ArrayList.class, Task.class).getType());
            allTasks.addAll(tasksList);
        }
        if (subtasksString != null) {
            List<Task> subtasksList = gson.fromJson(subtasksString,
                    TypeToken.getParameterized(ArrayList.class, Subtask.class).getType());
            allTasks.addAll(subtasksList);
        }
        if (subtasksString != null) {
            List<Task> epicsList = gson.fromJson(epicsString,
                    TypeToken.getParameterized(ArrayList.class, Epic.class).getType());
            allTasks.addAll(epicsList);
        }
        allTasks.sort(Comparator.comparingInt(Task::getId));
        for (Task task : allTasks) {
            htm.setIdentifier(task.getId() - 1);
            switch (task.getType()) {
                case TASK:
                    htm.addNewTask(task);
                    break;
                case SUBTASK:
                    htm.addNewSubtask((Subtask) task);
                    break;
                case EPIC:
                    htm.addNewEpic((Epic) task);
                    break;
            }
        }

        if (historyString != null) {
            List<Task> historyList = gson.fromJson(historyString,
                    TypeToken.getParameterized(ArrayList.class, Task.class).getType());
            for (Task task : historyList) {
                htm.getTask(task.getId());
                htm.getSubtask(task.getId());
                htm.getEpic(task.getId());
            }
        }
        return htm;
    }
}
