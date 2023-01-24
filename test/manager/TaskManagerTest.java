package manager;

import tasks.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract public class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;
    public Epic epic1 = new Epic("Epic1", "descriptionEpic1");
    public Epic epic2 = new Epic("Epic2", "descriptionEpic2");
    public Subtask subtask1 = new Subtask("Subtask1", "descriptionSubtask1", 1);
    public Subtask subtask2 = new Subtask("Subtask2", "descriptionSubtask2", 1);
    public Task task1 = new Task("Task1", "descriptionTask1");
    public Task task2 = new Task("Task2", "descriptionTask2");
    private final LocalDateTime startTime = LocalDateTime.of(2023, 1, 23, 10, 0);

    public void getTasks() {
        List<Task> tasks = taskManager.getTasks();
        assertEquals(List.of(), tasks,
                "Список не совпадает с заданным");
        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);
        tasks = taskManager.getTasks();
        assertEquals(List.of(task1, task2), tasks,
                "Список не совпадает с заданным");
    }

    public void getSubtasks() {
        List<Subtask> subtasks = taskManager.getSubtasks();
        assertEquals(List.of(), subtasks,
                "Список не совпадает с заданным");
        taskManager.addNewEpic(epic1);
        subtask1.setStartTime(startTime);
        subtask1.setDuration(120);
        subtask2.setStartTime(startTime.plusHours(3));
        subtask2.setDuration(180);
        taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask2);
        subtasks = taskManager.getSubtasks();
        assertEquals(List.of(subtask1, subtask2), subtasks,
                "Список не совпадает с заданным");
    }

    public void getEpics() {
        List<Epic> epics = taskManager.getEpics();
        assertEquals(List.of(), epics,
                "Список не совпадает с заданным");
        taskManager.addNewEpic(epic1);
        taskManager.addNewEpic(epic2);
        epics = taskManager.getEpics();
        assertEquals(List.of(epic1, epic2), epics,
                "Список не совпадает с заданным");
    }

    public void updateTask() {
        taskManager.updateTask(task1);
        assertNull(taskManager.getTask(task1.getId()), "Задача была обновлена");
        taskManager.addNewTask(task1);
        task1.setStatus(TaskStatus.IN_PROGRESS);
        task1.setName("UpdatedTask1");
        taskManager.updateTask(task1);
        assertNotNull(taskManager.getTask(task1.getId()), "Задача не была обновлена");
        assertEquals(task1, taskManager.getTask(task1.getId()), "Задачи различаются");
    }

    public void updateSubtask() {
        taskManager.updateSubtask(subtask1);
        assertNull(taskManager.getSubtask(subtask1.getId()), "Задача была обновлена");
        taskManager.addNewEpic(epic1);
        subtask1.setStartTime(startTime);
        subtask1.setDuration(120);
        taskManager.addNewSubtask(subtask1);
        subtask1.setStatus(TaskStatus.IN_PROGRESS);
        subtask1.setName("UpdatedSubtask1");
        taskManager.updateSubtask(subtask1);
        assertNotNull(taskManager.getSubtask(subtask1.getId()), "Задача не была обновлена");
        assertEquals(subtask1, taskManager.getSubtask(subtask1.getId()), "Задачи различаются");
    }

    public void updateEpic() {
        taskManager.updateEpic(epic1);
        assertNull(taskManager.getEpic(epic1.getId()), "Задача была обновлена");
        taskManager.addNewEpic(epic1);
        epic1.setName("UpdatedEpic1");
        taskManager.updateEpic(epic1);
        assertNotNull(taskManager.getEpic(epic1.getId()), "Задача не была обновлена");
        assertEquals(epic1, taskManager.getEpic(epic1.getId()), "Задачи различаются");
    }

    public void addNewTask() {
        taskManager.addNewTask(task1);
        final int taskId = taskManager.getTask(task1.getId()).getId();
        final Task savedTask = taskManager.getTask(taskId);
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task1, savedTask, "Задачи не совпадают.");
        final List<Task> tasks = taskManager.getTasks();
        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task1, tasks.get(0), "Задачи не совпадают.");
    }

    public void addNewSubtask() {
        taskManager.addNewSubtask(subtask1);
        assertEquals(0, taskManager.getSubtasks().size(), "Задача была добавлена");
        subtask1.setStartTime(startTime);
        subtask1.setDuration(120);
        taskManager.addNewEpic(epic1);
        taskManager.addNewSubtask(subtask1);
        final int subtaskId = subtask1.getId();
        final Subtask savedSubtask = taskManager.getSubtask(subtaskId);
        assertNotNull(savedSubtask, "Задача не найдена.");
        assertEquals(subtask1, savedSubtask, "Задачи не совпадают.");
        final List<Subtask> subtasks = taskManager.getSubtasks();
        assertNotNull(subtasks, "Задачи на возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество задач.");
        assertEquals(subtask1, subtasks.get(0), "Задачи не совпадают.");
    }

    public void addNewEpic() {
        taskManager.addNewEpic(epic1);
        final int epicId = epic1.getId();
        final Epic savedEpic = taskManager.getEpic(epicId);
        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic1, savedEpic, "Задачи не совпадают.");
        final List<Epic> epics = taskManager.getEpics();
        assertNotNull(epics, "Задачи на возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(epic1, epics.get(0), "Задачи не совпадают.");
        assertEquals(TaskStatus.NEW, epics.get(0).getStatus(), "Статусы не совпадают.");
    }

    public void deleteTasks() {
        assertDoesNotThrow(() -> taskManager.deleteTasks(), "Ошибка при очистке пустого списка");
        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);
        assertEquals(List.of(task1, task2), taskManager.getTasks(),
                "Список не совпадает с заданным");
        taskManager.deleteTasks();
        assertTrue(taskManager.getTasks().isEmpty(), "Размеры списков задач не совпадают");
    }

    public void deleteSubtasks() {
        assertDoesNotThrow(() -> taskManager.deleteSubtasks(), "Ошибка при очистке пустого списка");
        taskManager.addNewEpic(epic1);
        subtask1.setStartTime(startTime);
        subtask1.setDuration(120);
        subtask2.setStartTime(startTime.plusHours(3));
        subtask2.setDuration(180);
        taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask2);
        assertEquals(List.of(subtask1, subtask2), taskManager.getSubtasks(),
                "Список не совпадает с заданным");
        taskManager.deleteSubtasks();
        assertTrue(taskManager.getSubtasks().isEmpty(), "Размеры списков задач не совпадают");
    }

    public void deleteEpics() {
        assertDoesNotThrow(() -> taskManager.deleteEpics(), "Ошибка при очистке пустого списка");
        taskManager.addNewEpic(epic1);
        taskManager.addNewEpic(epic2);
        assertEquals(List.of(epic1, epic2), taskManager.getEpics(),
                "Список не совпадает с заданным");
        taskManager.deleteEpics();
        assertTrue(taskManager.getEpics().isEmpty(), "Размеры списков задач не совпадают");
    }

    public void getTask() {
        assertDoesNotThrow(() -> taskManager.getTask(0), "Ошибка при попытке получить " +
                "несуществующую задачу");
        task1.setStartTime(startTime);
        task1.setDuration(120);
        task2.setStartTime(startTime.plusHours(3));
        task2.setDuration(180);
        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);
        assertEquals(task1, taskManager.getTasks().get(0), "Задачи не совпадают.");
        assertEquals(task2, taskManager.getTasks().get(1), "Задачи не совпадают.");
    }

    public void getSubtask() {
        assertDoesNotThrow(() -> taskManager.getSubtask(0), "Ошибка при попытке получить " +
                "несуществующую задачу");
        taskManager.addNewEpic(epic1);
        subtask1.setStartTime(startTime);
        subtask1.setDuration(120);
        subtask2.setStartTime(startTime.plusHours(3));
        subtask2.setDuration(180);
        taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask2);
        assertEquals(subtask1, taskManager.getSubtasks().get(0), "Задачи не совпадают.");
        assertEquals(subtask2, taskManager.getSubtasks().get(1), "Задачи не совпадают.");
    }

    public void getEpic() {
        assertDoesNotThrow(() -> taskManager.getEpic(0), "Ошибка при попытке получить " +
                "несуществующую задачу");
        taskManager.addNewEpic(epic1);
        taskManager.addNewEpic(epic2);
        assertEquals(epic1, taskManager.getEpics().get(0), "Задачи не совпадают.");
        assertEquals(epic2, taskManager.getEpics().get(1), "Задачи не совпадают.");
    }

    public void removeTask() {
        assertDoesNotThrow(() -> taskManager.removeTask(0), "Ошибка при попытке удалить " +
                "несуществующую задачу");
        task1.setStartTime(startTime);
        task1.setDuration(120);
        task2.setStartTime(startTime.plusHours(3));
        task2.setDuration(180);
        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);
        taskManager.removeTask(task1.getId());
        assertEquals(List.of(task2), taskManager.getTasks(),
                "Список не совпадает с заданным");
        assertEquals(task2, taskManager.getTask(task2.getId()),
                "Список содержит иную задачу");
    }

    public void removeSubtask() {
        assertDoesNotThrow(() -> taskManager.removeSubtask(0), "Ошибка при попытке удалить " +
                "несуществующую задачу");
        taskManager.addNewEpic(epic1);
        subtask1.setStartTime(startTime);
        subtask1.setDuration(120);
        subtask2.setStartTime(startTime.plusHours(3));
        subtask2.setDuration(180);
        taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask2);
        taskManager.removeSubtask(subtask1.getId());
        assertEquals(List.of(subtask2), taskManager.getSubtasks(),
                "Список не совпадает с заданным");
        assertEquals(subtask2, taskManager.getSubtask(subtask2.getId()),
                "Список содержит иную задачу");
    }

    public void removeEpic() {
        assertDoesNotThrow(() -> taskManager.removeEpic(0), "Ошибка при попытке удалить " +
                "несуществующую задачу");
        taskManager.addNewEpic(epic1);
        taskManager.addNewEpic(epic2);
        taskManager.removeEpic(epic1.getId());
        assertEquals(List.of(epic2), taskManager.getEpics(),
                "Список не совпадает с заданным");
        assertEquals(epic2, taskManager.getEpic(epic2.getId()),
                "Список содержит иную задачу");
    }

    public void listOfEpicSubtasks() {
        List<Subtask> subtasks = taskManager.listOfEpicSubtasks(0);
        assertNull(subtasks, "Список не пуст");
        taskManager.addNewEpic(epic1);
        subtask1.setStartTime(startTime);
        subtask1.setDuration(120);
        subtask2.setStartTime(startTime.plusHours(3));
        subtask2.setDuration(180);
        taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask2);
        subtasks = taskManager.listOfEpicSubtasks(1);
        assertEquals(List.of(subtask1, subtask2), subtasks);
    }

    public void getHistory() {
        List<Task> history = taskManager.getHistory();
        assertTrue(history.isEmpty(), "Список не пуст");
        taskManager.addNewEpic(epic1);
        subtask1.setStartTime(startTime);
        subtask1.setDuration(120);
        subtask2.setStartTime(startTime.plusHours(3));
        subtask2.setDuration(180);
        taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask2);
        task1.setStartTime(startTime.plusHours(7));
        task1.setDuration(120);
        task2.setStartTime(startTime.plusHours(10));
        task2.setDuration(180);
        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);
        taskManager.getTask(4);
        taskManager.getTask(5);
        taskManager.getSubtask(2);
        taskManager.getSubtask(2);
        taskManager.getSubtask(3);
        taskManager.getEpic(1);
        history = taskManager.getHistory();
        assertEquals(List.of(task1, task2, subtask1, subtask2, epic1), history,
                "Список задач не совпадает с заданным");
    }

    public void getPrioritizedTasks() {
        taskManager.addNewEpic(epic1);
        subtask1.setStartTime(startTime.plusHours(1));
        subtask1.setDuration(120);
        subtask2.setStartTime(startTime.plusHours(3));
        subtask2.setDuration(180);
        taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask2);
        task1.setStartTime(startTime.plusHours(8));
        task1.setDuration(120);
        task2.setStartTime(startTime.plusHours(11));
        task2.setDuration(180);
        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);
        taskManager.addNewTask(new Task("NullTask","NullDescription"));
        assertEquals(List.of(subtask1,subtask2,task1,task2,taskManager.getTask(6)),
                new ArrayList<>(taskManager.getPrioritizedTasks()));
    }
}