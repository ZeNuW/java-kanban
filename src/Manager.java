import java.util.HashMap;

public class Manager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private int subtaskIdentifier = 0;
    private int epicIdentifier = 0;
    private int taskIdentifier = 0;

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    public void updateTask(int identifier, Task task) { //передаём объект с идентификатором \\подумать как изменить обновления, чтобы выбрасывал ошибку сразу
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
            System.out.println("Подзадача №" + identifier + " обновлена");
        }
    }

    public void updateEpics(int identifier, String name, String description, HashMap<Integer, Subtask> subtasks) {
        if (epics.size() == 0) {
            System.out.println("Эпики пустые, перед обновлением нужно сначала загрузить списки");
        } else if (epics.get(identifier) == null) {
            System.out.println("Эпика с таким идентификатором не существует");
        } else {
            epics.put(identifier, new Epic(name, description, subtasks));
            System.out.println("Эпик №" + identifier + " обновлен");
        }
    }

    public void setNewTask(Task task) { //передаём объект с данными, идентификатор определяется в методе
        if (task.status == null) {
            System.out.println("Задача не добавлена т.к некорректно введён статус задачи");
        } else {
            taskIdentifier++;
            tasks.put(taskIdentifier, task);
        }
    }

    public void setNewSubtask(Subtask subtask) { //аналогично
        if (subtask.status == null) {
            System.out.println("Подзадача не добавлена т.к некорректно введён статус подзадачи");
        } else {
            subtaskIdentifier++;
            subtasks.put(subtaskIdentifier, subtask);
        }
    }

    public void setNewEpic(String name, String description) {
        epicIdentifier++;
        epics.put(epicIdentifier, new Epic(name, description, subtasks));
        subtasks.clear(); //мне показалось логичным, что нужно обнулять список подзадач при создании эпика,
        //возможно я думаю не совсем верно
    }

    public void clearTasks() {
        tasks.clear();
        taskIdentifier = 0;
        System.out.println("Все задачи удалены");
    }

    public void clearSubtasks() {
        subtasks.clear();
        subtaskIdentifier = 0;
        System.out.println("Все подзадачи удалены");
    }

    public void clearEpics() {
        epics.clear();
        epicIdentifier = 0;
        System.out.println("Все эпики удалены");
    }

    public void findTasksByIdentifier(int identifier) {
        if (tasks.get(identifier) != null) {
            System.out.println("Задача №" + identifier + " содержит: " + tasks.get(identifier));
        } else {
            System.out.println("Задачи с таким идентификатором не существует");
        }
    }

    public void findSubtasksByIdentifier(int identifier) {
        if (subtasks.get(identifier) != null) {
            System.out.println("Подзадача №" + identifier + " содержит: " + subtasks.get(identifier));
        } else {
            System.out.println("Подзадачи с таким идентификатором не существует");
        }
    }

    public void findEpicsByIdentifier(int identifier) {
        if (subtasks.get(identifier) != null) {
            System.out.println("Эпик №" + identifier + " содержит: " + epics.get(identifier));
        } else {
            System.out.println("Подзадачи с таким идентификатором не существует");
        }
    }

    public void removeTasksByIdentifier(int identifier) {
        if (tasks.get(identifier) != null) {
            tasks.remove(identifier);
            System.out.println("Задача №" + identifier + " удалена");
        } else {
            System.out.println("Задачи с таким идентификатором не существует");
        }
    }

    public void removeSubtasksByIdentifier(int identifier) {
        if (subtasks.get(identifier) != null) {
            subtasks.remove(identifier);
            System.out.println("Подзадача №" + identifier + " удалена");
        } else {
            System.out.println("Подзадачи с таким идентификатором не существует");
        }
    }

    public void removeEpicsByIdentifier(int identifier) {
        if (epics.get(identifier) != null) {
            epics.remove(identifier);
            System.out.println("Эпик №" + identifier + " удалён");
        } else {
            System.out.println("Эпика с таким идентификатором не существует");
        }
    }

    public void listOfEpicSubtasks(int identifier) {
        try {
            Epic epic = epics.get(identifier);
            System.out.println(epic.subtasks);
        } catch (NullPointerException e) {
            System.out.println("Эпика с таким идентификатором не существует");
        }

    }

}
