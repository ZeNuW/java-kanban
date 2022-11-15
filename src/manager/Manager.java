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

    public void updateTask(Task task) {
        if (tasks.size() == 0) {
            System.out.println("Списки задач пустые, перед обновлением нужно сначала загрузить списки");
        } else {
            for (Task tsk : tasks.values()) {
                if (tsk.equals(task)) {
                    task.setId(tsk.getId());
                    tasks.put(task.getId(),task);
                    System.out.println("Задача №" + tsk.getId() + " обновлена");
                    return;
                }
            }
            System.out.println("Невозможно обновить несуществующую задачу");
        }
    }

    public void updateSubtask(Subtask subtask) {
        if (subtasks.size() == 0) {
            System.out.println("Списки подзадач пустые, перед обновлением нужно сначала загрузить списки");
        } else {
            for (Subtask sbt: subtasks.values()) {
                if (sbt.equals(subtask)) {
                    subtask.setId(sbt.getId());
                    subtasks.put(subtask.getId(),subtask);
                    epics.get(subtask.getEpicId()).getSubtasks().put(subtask.getId(),subtask);
                    epics.get(subtask.getEpicId()).epicStatus();
                    System.out.println("Подзадача №" + subtask.getId() + " обновлена");
                    return;
                }
            }
            System.out.println("Невозможно обновить несуществующую подзадачу");
        }
    }

    public void updateEpics(Epic epic) {
        /* Не совсем понял назначение этого метода, статус менять он не должен, т.к. статус меняется в других ситуациях.
        Но и не могу изменить список подзадач принадлежащих этому эпику, потому что их нет в конструкторе и я их не передаю.
        Если это изменить, то получается противоречие с ТЗ, т.к. я должен иметь возможность создавать пустой эпик
        Да и просто логика работы нарушиться, если я всё правильно подумал. :)
        Ну а в таком виде, он либо ничего не делает т.е. заменяет эпик абсолютно аналогичным эпиком без подзадач
        либо выдаёт ошибку о не существовании такого эпика
        Опять много слов: Вопрос в том, что должен обновлять в эпике этот метод.
        У меня есть предположение основанное на ТЗ, что он должен изменять как раз таки статус, но тогда выходит он мне не нужен.
        Ведь статус я меняю методом в самом классе Epic */
        if (epics.size() == 0) {
            System.out.println("Эпики пустые, перед обновлением нужно сначала загрузить списки");
        } else {
            for (Epic epc : epics.values()) {
                if (epc.equals(epic)) {
                    epic.setId(epc.getId());
                    epics.put(epic.getId(), epic);
                    epics.get(epic.getId()).epicStatus();
                    System.out.println("Эпик №" + epic.getId() + " обновлен");
                    return;
                }
            }
            System.out.println("Невозможно обновить несуществующий эпик");
        }
    }

    public void addNewTask(Task task) {
        if (task.getStatus() == null) {
            System.out.println("Задача не добавлена т.к некорректно введён статус задачи");
        } else {
            identifier++;
            tasks.put(identifier, task);
            tasks.get(identifier).setId(identifier);
        }
    }

    public void addNewSubtask(Subtask subtask) {
        if (subtask.getStatus() == null) {
            System.out.println("Подзадача не добавлена т.к некорректно введён статус подзадачи");
        } else {
            identifier++;
            subtasks.put(identifier, subtask);
            subtasks.get(identifier).setId(identifier);
            epics.get(subtask.getEpicId()).addSubtask(identifier, subtask);
            epics.get(subtask.getEpicId()).epicStatus();
        }
    }

    public void addNewEpic(Epic epic) {
        identifier++;
        epics.put(identifier, epic);
        epics.get(identifier).setId(identifier);
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

    public Task findTasksByIdentifier(int identifier) { //верно
        if (tasks.get(identifier) != null) {
            return (tasks.get(identifier));
        } else {
            System.out.println("Задачи с таким идентификатором не существует");
            return null;
        }
    }

    public Subtask findSubtasksByIdentifier(int identifier) {
        if (subtasks.get(identifier) != null) {
            return subtasks.get(identifier);
        } else {
            System.out.println("Подзадачи с таким идентификатором не существует");
            return null;
        }
    }

    public Epic findEpicsByIdentifier(int epicId) {
        if (epics.get(epicId) != null) {
            return epics.get(epicId);
        } else {
            System.out.println("Подзадачи с таким идентификатором не существует");
            return null;
        }
    }

    public void removeTaskByIdentifier(int identifier) {
        if (tasks.get(identifier) != null) {
            tasks.remove(identifier);
            System.out.println("Задача №" + identifier + " удалена");
        } else {
            System.out.println("Задачи с таким идентификатором не существует");
        }
    }

    public void removeSubtaskByIdentifier(int identifier) { //проверить
        if (subtasks.get(identifier) != null) {
            try {
                Epic epic = epics.get(subtasks.get(identifier).getEpicId());
                epic.getSubtasks().remove(identifier);
                subtasks.remove(identifier);
                epic.epicStatus();
            } catch (NullPointerException e) {
                System.out.println("Ошибка, нет подзадачи с таким номером");
            }
        } else {
            System.out.println("Подзадачи с таким идентификатором не существует");
        }
    }

    public void removeEpicByIdentifier(int epicId) {
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
