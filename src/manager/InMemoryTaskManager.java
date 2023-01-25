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
    private final TreeSet<Task> prioritizedTasks = new TreeSet<>(((Comparator<Task>) (o1, o2) -> {
        if (o1.getStartTime() == null || o2.getStartTime() == null) {
            return 1;
        } else {
            return 0;
        }
    }).thenComparing(Task::getStartTime));
    // Не знаю насколько я правильно компаратор сделал, но чисто по времени старта я сортировать не могу т.к. могут быть
    // задачи с временем null, а их нужно поставить в конец
    // А если использовать просто Comparator.comparing(Task::getStartTime), будет NullPointerException

    // Я имел в виду время поиска пересечений, не до конца написал просто :).

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
    }

    @Override
    public void deleteTasks() {
        for (Task task : tasks.values()) {
            history.remove(task.getId());
        }
        tasks.clear();
        //Вопрос по поводу этого исправления, разве не получается хуже, в плане эффективности?
        //У меня теперь 2 цикла вместо 1, т.к clear по сути и есть тот же цикл удаления
    }

    @Override
    public void deleteSubtasks() {
        for (Subtask subtask : subtasks.values()) {
            history.remove(subtask.getId());
        }
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubtasks().clear();
            epic.epicStatus();
            epic.getEndTime();
        }
    }

    @Override
    public void deleteEpics() {
        for (Epic epic : epics.values()) {
            history.remove(epic.getId());
        }
        epics.clear();
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
            e.getStackTrace();
        }
    }

    @Override
    public void removeEpic(int identifier) {
        if (epics.get(identifier) != null) {
            for (Iterator<Subtask> iterator = epics.get(identifier).getSubtasks().values().iterator(); iterator.hasNext(); ) {
                Subtask value = iterator.next();
                if (value.getEpicId() == identifier) {
                    history.remove(value.getId());
                    iterator.remove();
                }
            }
            epics.remove(identifier);
            history.remove(identifier);
        }
    }

    @Override
    public ArrayList<Subtask> listOfEpicSubtasks(int identifier) {
        try {
            Epic epic = epics.get(identifier);
            return new ArrayList<>(epic.getSubtasks().values());
        } catch (NullPointerException e) {
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
