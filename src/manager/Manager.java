package manager;

import tasks.*;

import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private int identifier = 0;

    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public void addSubtaskToEpic(int epicId, int identifier) { // добавляет подзадачу и изменяет статус
        epics.get(epicId).addSubtask(identifier, subtasks.get(identifier));
        epics.get(epicId).epicStatus();
        subtasks.get(identifier).setEpicId(epicId);
    }

    public void updateTask(int identifier, Task task) {
        if (tasks.size() == 0) {
            System.out.println("Списки задач пустые, перед обновлением нужно сначала загрузить списки");
        } else if (tasks.get(identifier) == null) {
            System.out.println("Задачи под этим номером не существует");
        } else {
            tasks.put(identifier, task);
            System.out.println("Задача №" + identifier + " обновлена");
        }
    }

    public void updateSubtask(int identifier, Subtask subtask) { //аналогично
        if (subtasks.size() == 0) {
            System.out.println("Списки задач пустые, перед обновлением нужно сначала загрузить списки");
        } else if (subtasks.get(identifier) == null) {
            System.out.println("Подзадачи с таким идентификатором не существует");
        } else {
            subtasks.put(identifier, subtask);
            epics.get(subtask.getEpicId()).epicStatus(); // обновили статус эпика
            System.out.println("Подзадача №" + identifier + " обновлена");
        }
    }

    public void updateEpics(int identifier, Epic epic) { //проверить
        if (epics.size() == 0) {
            System.out.println("Эпики пустые, перед обновлением нужно сначала загрузить списки");
        } else if (epics.get(identifier) == null) {
            System.out.println("Эпика с таким идентификатором не существует");
        } else {
            epics.put(identifier, epic);
            System.out.println("Эпик №" + identifier + " обновлен");
        }
    }

    public void addNewTask(Task task) { //верно
        if (task.getStatus() == null) {
            System.out.println("Задача не добавлена т.к некорректно введён статус задачи");
        } else {
            identifier++;
            tasks.put(identifier, task);
            tasks.get(identifier).setId(identifier);
        }
    }

    public void addNewSubtask(Subtask subtask) { //верно
        if (subtask.getStatus() == null) {
            System.out.println("Подзадача не добавлена т.к некорректно введён статус подзадачи");
        } else {
            identifier++;
            subtasks.put(identifier, subtask);
            subtasks.get(identifier).setId(identifier);
        }
    }

    public void addNewEpic(Epic epic) { //верно
        identifier++;
        epics.put(identifier, epic);
        epics.get(identifier).setId(identifier);
    }

    public void deleteTasks() { //верно
        tasks.clear();
        System.out.println("Все задачи удалены");
    }

    public void deleteSubtasks() { //вроде верно, проверить
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubtasks().clear();
            epic.epicStatus();
        }
        System.out.println("Все подзадачи удалены");
    }

    public void delete() { //верно, обнуляем как эпики так и подзадачи
        epics.clear();
        subtasks.clear();
        System.out.println("Все эпики удалены");
    }

    public void findTasksByIdentifier(int identifier) { //верно
        if (tasks.get(identifier) != null) {
            System.out.println("Задача №" + identifier + " содержит: " + tasks.get(identifier));
        } else {
            System.out.println("Задачи с таким идентификатором не существует");
        }
    }

    public void findSubtasksByIdentifier(int identifier) { //верно
        if (subtasks.get(identifier) != null) {
            System.out.println("Подзадача №" + identifier + " содержит: " + subtasks.get(identifier));
        } else {
            System.out.println("Подзадачи с таким идентификатором не существует");
        }
    }

    public void findEpicsByIdentifier(int epicId) { //верно
        if (subtasks.get(epicId) != null) {
            System.out.println("Эпик №" + epicId + " содержит: " + epics.get(epicId));
        } else {
            System.out.println("Подзадачи с таким идентификатором не существует");
        }
    }

    public void removeTaskByIdentifier(int identifier) { //верно
        if (tasks.get(identifier) != null) {
            tasks.remove(identifier);
            System.out.println("Задача №" + identifier + " удалена");
        } else {
            System.out.println("Задачи с таким идентификатором не существует");
        }
    }

    public void removeSubtaskByIdentifier(int identifier) {
        int tempIdentifier = 0;
        if (subtasks.get(identifier) != null) {
            Epic epic = epics.get(subtasks.get(identifier).getEpicId());
            for (Subtask subtask : epic.getSubtasks().values()) {
                if (identifier == subtask.getId()) {
                    tempIdentifier = identifier;
                    break;
                }
            }
            epic.getSubtasks().remove(tempIdentifier);
            subtasks.remove(tempIdentifier);
            epic.epicStatus();
        } else {
            System.out.println("Подзадачи с таким идентификатором не существует");
        }
    }

    public void removeEpicByIdentifier(int epicId) { //верно
        if (epics.get(epicId) != null) {
            subtasks.entrySet().removeIf(It-> It.getValue().getEpicId() == epicId);
            epics.remove(epicId);
            System.out.println("Эпик №" + epicId + " удалён");
        } else {
            System.out.println("Эпика с таким идентификатором не существует");
        }
    }

    public ArrayList<Subtask> listOfEpicSubtasks(int epicId) {
        try {
            Epic epic = epics.get(epicId);
            return new ArrayList<>(epic.getSubtasks().values());
        } catch (NullPointerException e) {
            System.out.println("Эпика с таким идентификатором не существует");
            return null;
        }
    }
}
