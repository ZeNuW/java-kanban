package manager;

import tasks.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private final HistoryManager history = Managers.getDefaultHistory();
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private int identifier = 0;

    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void updateTask(Task task) {
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

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtasks.size() == 0) {
            System.out.println("Эпики пустые, перед обновлением нужно сначала загрузить списки");
        } else {
            try {
                subtasks.get(subtask.getId()).setName(subtask.getName());
                subtasks.get(subtask.getId()).setDescription(subtask.getDescription());
                subtasks.get(subtask.getId()).setStatus(subtask.getStatus());
                epics.get(subtask.getEpicId()).epicStatus();
            } catch (NullPointerException e) {
                System.out.println("Подзадачи с таким номером не существует");
            }
        }
    }

    @Override
    public void updateEpics(Epic epic) {
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

    @Override
    public void addNewTask(Task task) {
        if (task.getStatus() == null) {
            System.out.println("Задача не добавлена т.к некорректно введён статус задачи");
        } else {
            identifier++;
            task.setId(identifier);
            tasks.put(identifier, task);
        }
    }

    @Override
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

    @Override
    public void addNewEpic(Epic epic) {
        identifier++;
        epic.setId(identifier);
        epics.put(identifier, epic);
    }

    @Override
    public void deleteTasks() {
        tasks.clear();
        System.out.println("Все задачи удалены");
    }

    @Override
    public void deleteSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubtasks().clear();
            epic.epicStatus();
        }
        System.out.println("Все подзадачи удалены");
    }

    @Override
    public void deleteEpics() {
        epics.clear();
        subtasks.clear();
        System.out.println("Все эпики удалены");
    }

    @Override
    public Task getTask(int identifier) {
        if (tasks.get(identifier) == null) {
            System.out.println("Задачи с таким идентификатором не существует");
        } else {
            history.addHistory(tasks.get(identifier));
        }
        return tasks.get(identifier);
    }

    @Override
    public Subtask getSubtask(int identifier) {
        if (subtasks.get(identifier) == null) {
            System.out.println("Подзадачи с таким идентификатором не существует");
        } else {
            history.addHistory(subtasks.get(identifier));
        }
        return subtasks.get(identifier);
    }

    @Override
    public Epic getEpic(int identifier) {
        if (epics.get(identifier) == null) {
            System.out.println("Эпика с таким идентификатором не существует");
        } else {
            history.addHistory(epics.get(identifier));
        }
        return epics.get(identifier);
    }

    @Override
    public void removeTask(int identifier) {
        if (tasks.get(identifier) != null) {
            tasks.remove(identifier);
            System.out.println("Задача №" + identifier + " удалена");
        } else {
            System.out.println("Задачи с таким идентификатором не существует");
        }
    }

    @Override
    public void removeSubtask(int identifier) {
        try {
            Epic epic = epics.get(subtasks.get(identifier).getEpicId());
            epic.getSubtasks().remove(identifier);
            subtasks.remove(identifier);
            epic.epicStatus();
        } catch (NullPointerException e) {
            System.out.println("Подзадачи с таким идентификатором не существует");
        }
    }

    @Override
    public void removeEpic(int identifier) {
        if (epics.get(identifier) != null) {
            subtasks.entrySet().removeIf(It -> It.getValue().getEpicId() == identifier);
            epics.remove(identifier);
            System.out.println("Эпик №" + identifier + " удалён");
        } else {
            System.out.println("Эпика с таким идентификатором не существует");
        }
    }

    @Override
    public ArrayList<Subtask> listOfEpicSubtasks(int identifier) {
        try {
            Epic epic = epics.get(identifier);
            return new ArrayList<>(epic.getSubtasks().values());
        } catch (NullPointerException e) {
            System.out.println("Эпика с таким идентификатором не существует");
            return null;
        }
    }

    @Override
    public List<Task> getHistory() {
        return history.getHistory();
    }
    // Запутал немного видимо последний пункт ТЗ, что нужно действия с историей перенести.
    // Спасибо за объяснение, понял чем будет полезен и удобен метод именно тут.
    // Надеюсь реализовал правильно :)
}
