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
        loadFromServer();
    }

    @Override
    public void save() {
        client.put("tasks", gson.toJson(getTasks()));
        client.put("subtasks", gson.toJson(getSubtasks()));
        client.put("epics", gson.toJson(getEpics()));
        client.put("history", gson.toJson(getHistory()));
    }

    private void loadFromServer() {
        // вроде как разобрался, надеюсь теперь всё корректно и соответствует ТЗ
        List<Task> allTasks = new ArrayList<>();
        String tasksString = client.load("tasks");
        String subtasksString = client.load("subtasks");
        String epicsString = client.load("epics");
        String historyString = client.load("history");
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
            setIdentifier(task.getId() - 1);
            switch (task.getType()) {
                case TASK:
                    addNewTask(task);
                    break;
                case SUBTASK:
                    addNewSubtask((Subtask) task);
                    break;
                case EPIC:
                    addNewEpic((Epic) task);
                    break;
            }
        }
        if (historyString != null) {
            List<Task> historyList = gson.fromJson(historyString,
                    TypeToken.getParameterized(ArrayList.class, Task.class).getType());
            for (Task task : historyList) {
                getTask(task.getId());
                getSubtask(task.getId());
                getEpic(task.getId());
            }
        }
    }
}
