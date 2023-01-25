package manager;

import org.junit.jupiter.api.BeforeEach;
import tasks.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract public class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;
    public Epic epic1;
    public Epic epic2;
    public Subtask subtask1;
    public Subtask subtask2;
    public Task task1;
    public Task task2;
    protected final LocalDateTime startTime = LocalDateTime.of(2023, 1, 23, 10, 0);

    @BeforeEach
    public void beforeEach() {
        epic1 = new Epic("Epic1", "descriptionEpic1");
        epic2 = new Epic("Epic2", "descriptionEpic2");
        subtask1 = new Subtask("Subtask1", "descriptionSubtask1", 1);
        subtask1.setStartTime(startTime);
        subtask1.setDuration(120);
        subtask2 = new Subtask("Subtask2", "descriptionSubtask2", 1);
        subtask2.setStartTime(startTime.plusHours(3));
        subtask2.setDuration(180);
        task1 = new Task("Task1", "descriptionTask1");
        task1.setStartTime(startTime.plusHours(7));
        task1.setDuration(120);
        task2 = new Task("Task2", "descriptionTask2");
        task2.setStartTime(startTime.plusHours(10));
        task2.setDuration(180);
    }

    public void getTasks() {
        List<Task> tasks = taskManager.getTasks();
        assertEquals(List.of(task1, task2), tasks,
                "Список не совпадает с заданным");
        taskManager.deleteTasks();
        tasks = taskManager.getTasks();
        assertEquals(List.of(), tasks,
                "Список не совпадает с заданным");
    }

    public void getSubtasks() {
        List<Subtask> subtasks = taskManager.getSubtasks();
        assertEquals(List.of(subtask1, subtask2), subtasks,
                "Список не совпадает с заданным");
        taskManager.deleteSubtasks();
        subtasks = taskManager.getSubtasks();
        assertEquals(List.of(), subtasks,
                "Список не совпадает с заданным");
    }

    public void getEpics() {
        List<Epic> epics = taskManager.getEpics();
        assertEquals(List.of(epic1, epic2), epics,
                "Список не совпадает с заданным");
        taskManager.deleteEpics();
        epics = taskManager.getEpics();
        assertEquals(List.of(), epics,
                "Список не совпадает с заданным");
    }

    public void updateTask() {
        taskManager.addNewTask(task1);
        task1.setStatus(TaskStatus.IN_PROGRESS);
        task1.setName("UpdatedTask1");
        taskManager.updateTask(task1);
        assertNotNull(taskManager.getTask(task1.getId()), "Задача не была обновлена");
        assertEquals(task1, taskManager.getTask(task1.getId()), "Задачи различаются");
    }

    public void updateSubtask() {
        subtask1.setStatus(TaskStatus.IN_PROGRESS);
        subtask1.setName("UpdatedSubtask1");
        taskManager.updateSubtask(subtask1);
        assertNotNull(taskManager.getSubtask(subtask1.getId()), "Задача не была обновлена");
        assertEquals(subtask1, taskManager.getSubtask(subtask1.getId()), "Задачи различаются");
    }

    public void updateEpic() {
        epic1.setName("UpdatedEpic1");
        taskManager.updateEpic(epic1);
        assertNotNull(taskManager.getEpic(epic1.getId()), "Задача не была обновлена");
        assertEquals(epic1, taskManager.getEpic(epic1.getId()), "Задачи различаются");
    }

    public void addNewTask() {
        Task task3 = new Task("Temptask","tempdescr");
        taskManager.addNewTask(task3);
        assertEquals(3, taskManager.getTasks().size(), "Задача не была добавлена");
        final int taskId = taskManager.getTask(task3.getId()).getId();
        final Task savedTask = taskManager.getTask(taskId);
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task3, savedTask, "Задачи не совпадают.");
        final List<Task> tasks = taskManager.getTasks();
        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(3, tasks.size(), "Неверное количество задач.");
        assertEquals(task3, tasks.get(2), "Задачи не совпадают.");
    }

    public void addNewSubtask() {
        Subtask subtask3 = new Subtask("Tempsubtask","tempdescr",2);
        taskManager.addNewSubtask(subtask3);
        assertEquals(3, taskManager.getSubtasks().size(), "Задача не была добавлена");
        final int subtaskId = taskManager.getSubtask(7).getId();
        final Subtask savedSubtask = taskManager.getSubtask(subtaskId);
        assertNotNull(savedSubtask, "Задача не найдена.");
        assertEquals(subtask3, savedSubtask, "Задачи не совпадают.");
        final List<Subtask> subtasks = taskManager.getSubtasks();
        assertNotNull(subtasks, "Задачи на возвращаются.");
        assertEquals(3, subtasks.size(), "Неверное количество задач.");
        assertEquals(subtask3, subtasks.get(2), "Задачи не совпадают.");
    }

    public void addNewEpic() {
        Epic epic3 = new Epic("Tempepic","tempdescr");
        taskManager.addNewEpic(epic3);
        assertEquals(3, taskManager.getEpics().size(), "Задача не была добавлена");
        final int epicId = epic3.getId();
        final Epic savedEpic = taskManager.getEpic(epicId);
        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic3, savedEpic, "Задачи не совпадают.");
        final List<Epic> epics = taskManager.getEpics();
        assertNotNull(epics, "Задачи на возвращаются.");
        assertEquals(3, epics.size(), "Неверное количество задач.");
        assertEquals(epic3, epics.get(2), "Задачи не совпадают.");
        assertEquals(TaskStatus.NEW, epics.get(2).getStatus(), "Статусы не совпадают.");
    }

    public void deleteTasks() {
        assertEquals(List.of(task1, task2), taskManager.getTasks(),
                "Список не совпадает с заданным");
        taskManager.deleteTasks();
        assertDoesNotThrow(() -> taskManager.deleteTasks(), "Ошибка при очистке пустого списка");
        assertTrue(taskManager.getTasks().isEmpty(), "Размеры списков задач не совпадают");
    }

    public void deleteSubtasks() {
        assertEquals(List.of(subtask1, subtask2), taskManager.getSubtasks(),
                "Список не совпадает с заданным");
        taskManager.deleteSubtasks();
        assertDoesNotThrow(() -> taskManager.deleteSubtasks(), "Ошибка при очистке пустого списка");
        assertTrue(taskManager.getSubtasks().isEmpty(), "Размеры списков задач не совпадают");
    }

    public void deleteEpics() {
        assertEquals(List.of(epic1, epic2), taskManager.getEpics(),
                "Список не совпадает с заданным");
        taskManager.deleteEpics();
        assertDoesNotThrow(() -> taskManager.deleteEpics(), "Ошибка при очистке пустого списка");
        assertTrue(taskManager.getEpics().isEmpty(), "Размеры списков задач не совпадают");
    }

    public void getTask() {
        assertDoesNotThrow(() -> taskManager.getTask(0), "Ошибка при попытке получить " +
                "несуществующую задачу");
        assertEquals(task1, taskManager.getTasks().get(0), "Задачи не совпадают.");
        assertEquals(task2, taskManager.getTasks().get(1), "Задачи не совпадают.");
    }

    public void getSubtask() {
        assertDoesNotThrow(() -> taskManager.getSubtask(0), "Ошибка при попытке получить " +
                "несуществующую задачу");
        assertEquals(subtask1, taskManager.getSubtasks().get(0), "Задачи не совпадают.");
        assertEquals(subtask2, taskManager.getSubtasks().get(1), "Задачи не совпадают.");
    }

    public void getEpic() {
        assertDoesNotThrow(() -> taskManager.getEpic(0), "Ошибка при попытке получить " +
                "несуществующую задачу");
        assertEquals(epic1, taskManager.getEpics().get(0), "Задачи не совпадают.");
        assertEquals(epic2, taskManager.getEpics().get(1), "Задачи не совпадают.");
    }

    public void removeTask() {
        assertDoesNotThrow(() -> taskManager.removeTask(0), "Ошибка при попытке удалить " +
                "несуществующую задачу");
        taskManager.removeTask(task1.getId());
        assertEquals(List.of(task2), taskManager.getTasks(),
                "Список не совпадает с заданным");
        assertEquals(task2, taskManager.getTask(task2.getId()),
                "Список содержит иную задачу");
    }

    public void removeSubtask() {
        assertDoesNotThrow(() -> taskManager.removeSubtask(0), "Ошибка при попытке удалить " +
                "несуществующую задачу");
        taskManager.removeSubtask(subtask1.getId());
        assertEquals(List.of(subtask2), taskManager.getSubtasks(),
                "Список не совпадает с заданным");
        assertEquals(subtask2, taskManager.getSubtask(subtask2.getId()),
                "Список содержит иную задачу");
    }

    public void removeEpic() {
        assertDoesNotThrow(() -> taskManager.removeEpic(0), "Ошибка при попытке удалить " +
                "несуществующую задачу");
        taskManager.removeEpic(epic1.getId());
        assertEquals(List.of(epic2), taskManager.getEpics(),
                "Список не совпадает с заданным");
        assertEquals(epic2, taskManager.getEpic(epic2.getId()),
                "Список содержит иную задачу");
    }

    public void listOfEpicSubtasks() {
        List<Subtask> subtasks = taskManager.listOfEpicSubtasks(0);
        assertNull(subtasks, "Список не пуст");
        subtasks = taskManager.listOfEpicSubtasks(1);
        assertEquals(List.of(subtask1, subtask2), subtasks);
    }

    public void getHistory() {
        List<Task> history = taskManager.getHistory();
        assertTrue(history.isEmpty(), "Список не пуст");
        taskManager.getTask(5);
        taskManager.getTask(6);
        taskManager.getSubtask(3);
        taskManager.getSubtask(3);
        taskManager.getSubtask(4);
        taskManager.getEpic(1);
        taskManager.getEpic(2);
        history = taskManager.getHistory();
        assertEquals(List.of(task1, task2, subtask1, subtask2, epic1,epic2),
                history,"Список задач не совпадает с заданным");
    }

    public void getPrioritizedTasks() {
        taskManager.addNewTask(new Task("NullTask","NullDescription"));
        assertEquals(List.of(subtask1,subtask2,task1,task2,taskManager.getTask(7)),
                new ArrayList<>(taskManager.getPrioritizedTasks()));
    }
}