package manager;

import manager.history.HistoryManager;
import tasks.*;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected final HistoryManager history = Managers.getDefaultHistory();
    protected int identifier = 0;
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final TreeSet<Task> prioritizedTasks = new TreeSet<>((o1, o2) -> {
        if (o1.getStartTime() == null) {
            return 1;
        }
        if (o1.getStartTime().isBefore(o2.getStartTime())) {
            return -1;
        } else if (o2.getStartTime().isBefore(o1.getStartTime())) {
            return 1;
        } else {
            return 0;
        }
    });
    // вопрос возник, а нужно ли вообще эпики добавлять в дерево? (Временно убрал эпики, если я ошибся всё верну)
    // если всё же нужно создавать с эпиками, то по какому принципу сортировать ещё TreeSet т.к тогда
    // будут удаляться все первые подзадачи эпиков (когда делал с эпиками, сортировал ещё по id)
    // пока что сделал поиск за O(n), пока буду работать над О(1)

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
        if (tasks.size() != 0 && tasks.get(task.getId()) != null && checkTasksOverlap(task)) {
            tasks.get(task.getId()).setName(task.getName());
            tasks.get(task.getId()).setDescription(task.getDescription());
            tasks.get(task.getId()).setStatus(task.getStatus());
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtasks.size() != 0 && subtasks.get(subtask.getId()) != null && checkTasksOverlap(subtask)) {
            subtasks.get(subtask.getId()).setName(subtask.getName());
            subtasks.get(subtask.getId()).setDescription(subtask.getDescription());
            subtasks.get(subtask.getId()).setStatus(subtask.getStatus());
            epics.get(subtask.getEpicId()).epicStatus();
            epics.get(subtask.getEpicId()).getEndTime();
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.size() != 0 && epics.get(epic.getId()) != null) {
            epics.get(epic.getId()).setName(epic.getName());
            epics.get(epic.getId()).setDescription(epic.getDescription());
        }
    }

    @Override
    public void addNewTask(Task task) {
        if (task.getStatus() != null && checkTasksOverlap(task)) {
            identifier++;
            task.setId(identifier);
            tasks.put(identifier, task);
            prioritizedTasks.add(task);
        }
    }

    @Override
    public void addNewSubtask(Subtask subtask) {
        if (subtask.getStatus() != null && epics.get(subtask.getEpicId()) != null && checkTasksOverlap(subtask)) {
            identifier++;
            subtask.setId(identifier);
            subtasks.put(identifier, subtask);
            epics.get(subtask.getEpicId()).addSubtask(identifier, subtask);
            epics.get(subtask.getEpicId()).epicStatus();
            epics.get(subtask.getEpicId()).getEndTime();
            prioritizedTasks.add(subtask);
        }
    }

    @Override
    public void addNewEpic(Epic epic) {
        identifier++;
        epic.setId(identifier);
        epics.put(identifier, epic);
        //prioritizedTasks.add(epic);
    }

    @Override
    public void deleteTasks() {
        for (Iterator<Task> iterator = tasks.values().iterator(); iterator.hasNext(); ) {
            Task value = iterator.next();
            history.remove(value.getId());
            iterator.remove();
        }
    }

    @Override
    public void deleteSubtasks() {
        for (Iterator<Subtask> iterator = subtasks.values().iterator(); iterator.hasNext(); ) {
            Subtask value = iterator.next();
            history.remove(value.getId());
            iterator.remove();
        }
        for (Epic epic : epics.values()) {
            epic.getSubtasks().clear();
            epic.epicStatus();
            epic.getEndTime();
        }
    }

    @Override
    public void deleteEpics() {
        for (Iterator<Epic> iterator = epics.values().iterator(); iterator.hasNext(); ) {
            Epic value = iterator.next();
            history.remove(value.getId());
            iterator.remove();
        }
        deleteSubtasks();
    }

    @Override
    public Task getTask(int identifier) {
        if (tasks.get(identifier) != null) {
            history.add(tasks.get(identifier));
        }
        return tasks.get(identifier);
    }

    @Override
    public Subtask getSubtask(int identifier) {
        if (subtasks.get(identifier) != null) {
            history.add(subtasks.get(identifier));
        }
        return subtasks.get(identifier);
    }

    @Override
    public Epic getEpic(int identifier) {
        if (epics.get(identifier) != null) {
            history.add(epics.get(identifier));
        }
        return epics.get(identifier);
    }

    @Override
    public void removeTask(int identifier) {
        if (tasks.get(identifier) != null) {
            tasks.remove(identifier);
            history.remove(identifier);
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
            epic.getEndTime();
            history.remove(identifier);
        } catch (NullPointerException e) {
            System.out.println("Подзадачи с таким идентификатором не существует");
        }
    }

    @Override
    public void removeEpic(int identifier) {
        if (epics.get(identifier) != null) {
            for (Iterator<Subtask> iterator = subtasks.values().iterator(); iterator.hasNext(); ) {
                Subtask value = iterator.next();
                if (value.getEpicId() == identifier) {
                    history.remove(value.getId());
                    iterator.remove();
                }
            }
            epics.remove(identifier);
            history.remove(identifier);
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

    @Override
    public TreeSet<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    private boolean checkTasksOverlap(Task task) { // false - есть пересечения, true - нет
        boolean sort1;
        if (task.getStartTime() == null) {
            return true;
        }
        for (Task sortedTask : prioritizedTasks) {
            sort1 = task.getStartTime().isAfter(sortedTask.getEndTime())
                    || task.getStartTime().equals(sortedTask.getEndTime())
                    || task.getEndTime().isBefore(sortedTask.getStartTime())
                    || task.getEndTime().equals(sortedTask.getStartTime());
            if (!sort1) {
                return false;
            }
        }
        return true;
    }
}
