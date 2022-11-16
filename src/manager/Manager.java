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

    public void updateTask(Task task) { //предполагая, что на вход приходит Task с заданным id и во входных данных нет ошибок
        if (tasks.size() == 0) {
            System.out.println("Эпики пустые, перед обновлением нужно сначала загрузить списки");
        } else {
            try {
                tasks.get(task.getId()).setName(task.getName());
                tasks.get(task.getId()).setDescription(task.getDescription());
                tasks.get(task.getId()).setStatus(task.getStatus());
            } catch (NullPointerException e) {
                System.out.println("Задачи с таким номером не существует");
            }
        }
    }

    public void updateSubtask(Subtask subtask) { //предполагая, что на вход приходит Subtask с заданным id и во входных данных нет ошибок
        if (subtasks.size() == 0) {
            System.out.println("Эпики пустые, перед обновлением нужно сначала загрузить списки");
        } else {
            try {
                subtasks.get(subtask.getId()).setName(subtask.getName());
                subtasks.get(subtask.getId()).setDescription(subtask.getDescription());
                subtasks.get(subtask.getId()).setStatus(subtask.getStatus());
            } catch (NullPointerException e) {
                System.out.println("Подзадачи с таким номером не существует");
            }
        }
    }

    public void updateEpics(Epic epic) { //предполагая, что на вход приходит Epic с заданным id и во входных данных нет ошибок
        if (epics.size() == 0) {
            System.out.println("Эпики пустые, перед обновлением нужно сначала загрузить списки");
        } else {
            try {
                epics.get(epic.getId()).setName(epic.getName());
                epics.get(epic.getId()).setDescription(epic.getDescription());
            } catch (NullPointerException e) {
                System.out.println("Эпика с таким номером не существует");
            }
        }
    }

    public void addNewTask(Task task) {
        if (task.getStatus() == null) {
            System.out.println("Задача не добавлена т.к некорректно введён статус задачи");
        } else {
            identifier++;
            task.setId(identifier);
            tasks.put(identifier, task);
        }
    }

    public void addNewSubtask(Subtask subtask) {
        if (subtask.getStatus() == null) {
            System.out.println("Подзадача не добавлена т.к некорректно введён статус подзадачи");
        } else {
            if (epics.get(subtask.getEpicId()) != null) {
                identifier++;
                subtasks.put(identifier, subtask);
                subtasks.get(identifier).setId(identifier);
                epics.get(subtask.getEpicId()).addSubtask(identifier, subtask);
                epics.get(subtask.getEpicId()).epicStatus();
            } else {
                System.out.println("Эпика с таким идентификатором не существует.");
            }
        }
    }

    public void addNewEpic(Epic epic) {
        identifier++;
        epic.setId(identifier);
        epics.put(identifier, epic);
    }

    public void deleteTasks() {
        tasks.clear();
        System.out.println("Все задачи удалены");
    }

    public void deleteSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubtasks().clear();
            epic.epicStatus();
        }
        System.out.println("Все подзадачи удалены");
    }

    public void deleteEpics() {
        epics.clear();
        subtasks.clear();
        System.out.println("Все эпики удалены");
    }

    public Task findTasksByIdentifier(int identifier) {
        if (tasks.get(identifier) == null) {
            System.out.println("Задачи с таким идентификатором не существует");
        }
        return tasks.get(identifier);
    }

    public Subtask findSubtasksByIdentifier(int identifier) {
        if (subtasks.get(identifier) == null) {
            System.out.println("Подзадачи с таким идентификатором не существует");
        }
        return subtasks.get(identifier);
    }

    public Epic findEpicsByIdentifier(int identifier) {
        if (epics.get(identifier) == null) {
            System.out.println("Эпика с таким идентификатором не существует");
        }
        return epics.get(identifier);
    }

    public void removeTaskByIdentifier(int identifier) {
        if (tasks.get(identifier) != null) {
            tasks.remove(identifier);
            System.out.println("Задача №" + identifier + " удалена");
        } else {
            System.out.println("Задачи с таким идентификатором не существует");
        }
    }

    public void removeSubtaskByIdentifier(int identifier) {
        try {
            Epic epic = epics.get(subtasks.get(identifier).getEpicId());
            epic.getSubtasks().remove(identifier);
            subtasks.remove(identifier);
            epic.epicStatus();
        } catch (NullPointerException e) {
            System.out.println("Подзадачи с таким идентификатором не существует");
        }
    }

    public void removeEpicByIdentifier(int identifier) {
        if (epics.get(identifier) != null) {
            subtasks.entrySet().removeIf(It -> It.getValue().getEpicId() == identifier);
            epics.remove(identifier);
            System.out.println("Эпик №" + identifier + " удалён");
        } else {
            System.out.println("Эпика с таким идентификатором не существует");
        }
    }

    public ArrayList<Subtask> listOfEpicSubtasks(int identifier) {
        try {
            Epic epic = epics.get(identifier);
            return new ArrayList<>(epic.getSubtasks().values());
        } catch (NullPointerException e) {
            System.out.println("Эпика с таким идентификатором не существует");
            return null;
        }
    }
}
