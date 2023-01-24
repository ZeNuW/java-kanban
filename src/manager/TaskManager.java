package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public interface TaskManager {

    ArrayList<Task> getTasks();

    ArrayList<Subtask> getSubtasks();

    ArrayList<Epic> getEpics();

    void updateTask(Task task);

    void updateSubtask(Subtask subtask);

    void updateEpic(Epic epic);

    void addNewTask(Task task);

    void addNewSubtask(Subtask subtask);

    void addNewEpic(Epic epic);

    void deleteTasks();

    void deleteSubtasks();

    void deleteEpics();

    Task getTask(int identifier);

    Subtask getSubtask(int identifier);

    Epic getEpic(int identifier);

    void removeTask(int identifier);

    void removeSubtask(int identifier);

    void removeEpic(int identifier);

    ArrayList<Subtask> listOfEpicSubtasks(int identifier);

    List<Task> getHistory();

    TreeSet<Task> getPrioritizedTasks();
}
