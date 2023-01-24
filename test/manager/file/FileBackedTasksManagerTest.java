package manager.file;

import manager.TaskManagerTest;
import manager.history.HistoryManager;
import manager.history.InMemoryHistoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.*;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    private final LocalDateTime startTime = LocalDateTime.of(2023,1,23,10,0);
    private final File file = new File("test/resources/", "history.csv");

    @BeforeEach
    public void BeforeEach() {
        taskManager = new FileBackedTasksManager(file);
        task1.setStartTime(startTime);
        task1.setDuration(120);
        task2.setStartTime(startTime.plusHours(3));
        task2.setDuration(180);
        subtask1.setStartTime(startTime.plusHours(7));
        subtask1.setDuration(120);
        subtask2.setStartTime(startTime.plusHours(10));
        subtask2.setDuration(180);
    }

    @Test
    public void getTasks() {
        super.getTasks();
        taskManager.save();
    }

    @Test
    public void getSubtasks() {
        super.getSubtasks();
        taskManager.save();
    }

    @Test
    public void getEpics() {
        super.getEpics();
        taskManager.save();
    }

    @Test
    public void updateTask() {
        super.updateTask();
        taskManager.save();
    }

    @Test
    public void updateSubtask() {
        super.updateSubtask();
        taskManager.save();
    }

    @Test
    public void updateEpic() {
        super.updateEpic();
        taskManager.save();
    }

    @Test
    public void addNewTask() {
        super.addNewTask();
        taskManager.save();
    }

    @Test
    public void addNewSubtask() {
        super.addNewSubtask();
        taskManager.save();
    }

    @Test
    public void addNewEpic() {
        super.addNewEpic();
        taskManager.save();
    }

    @Test
    public void deleteTasks() {
        super.deleteTasks();
        taskManager.save();
    }

    @Test
    public void deleteSubtasks() {
        super.deleteSubtasks();
        taskManager.save();
    }

    @Test
    public void deleteEpics() {
        super.deleteEpics();
        taskManager.save();
    }

    @Test
    public void getTask() {
        super.getTask();
        taskManager.save();
    }

    @Test
    public void getSubtask() {
        super.getSubtask();
        taskManager.save();
    }

    @Test
    public void getEpic() {
        super.getEpic();
        taskManager.save();
    }

    @Test
    public void removeTask() {
        super.removeTask();
        taskManager.save();
    }

    @Test
    public void removeSubtask() {
        super.removeSubtask();
        taskManager.save();
    }

    @Test
    public void removeEpic() {
        super.removeEpic();
        taskManager.save();
    }

    @Test
    public void listOfEpicSubtasks() {
        super.listOfEpicSubtasks();
        taskManager.save();
    }

    @Test
    public void getHistory() {
        super.getHistory();
        taskManager.save();
    }

    @Test
    public void getPrioritizedTasks() {
        super.getPrioritizedTasks();
    }

    @Test
    public void loadFromFile() {
        try (Writer fileWriter = new FileWriter(file)) {
            fileWriter.write("id,type,name,status,description,epic\n");
            fileWriter.write("1,TASK,Task1,NEW,descriptionTask1,23.01.2023; 10:00,120,\n");
            fileWriter.write("2,TASK,Task2,DONE,descriptionTask2,23.01.2023; 12:00,180,\n");
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи файла", e);
        }
        Task tTask1 = new Task("Task1", "descriptionTask1");
        tTask1.setId(1);
        tTask1.setStartTime(startTime);
        tTask1.setDuration(120);
        Task tTask2 = new Task("Task2", "descriptionTask2");
        tTask2.setId(2);
        tTask2.setStatus(TaskStatus.DONE);
        tTask2.setStartTime(startTime.plusHours(2));
        tTask2.setDuration(180);
        taskManager = FileBackedTasksManager.loadFromFile(file);
        assertEquals(List.of(tTask1, tTask2), taskManager.getTasks(), "Списки различаются");
    }

    @Test
    public void save() {
        taskManager.addNewEpic(epic1);
        taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask2);
        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);
        List<Task> allTasksTrue = new ArrayList<>();
        allTasksTrue.add(epic1);
        allTasksTrue.add(subtask1);
        allTasksTrue.add(subtask2);
        allTasksTrue.add(task1);
        allTasksTrue.add(task2);
        taskManager.save();
        List<Task> allTasks = new ArrayList<>();
        taskManager = FileBackedTasksManager.loadFromFile(file);
        allTasks.addAll(taskManager.getEpics());
        allTasks.addAll(taskManager.getSubtasks());
        allTasks.addAll(taskManager.getTasks());
        System.out.println(allTasks);
        for (Task task : allTasksTrue) {
            assertEquals(allTasksTrue.get(task.getId() - 1).toString(),
                    allTasks.get(task.getId() - 1).toString(), "Задачи не совпадают");
        }
    }

    @Test
    public void toStringTest() {
        Epic tEpic1 = new Epic("Epic1", "descriptionEpic1");
        tEpic1.setId(1);
        Subtask tSubtask1 = new Subtask("Subtask1", "descriptionSubtask1", 1);
        tSubtask1.setId(2);
        tSubtask1.setStartTime(startTime);
        tSubtask1.setDuration(120);
        taskManager.addNewEpic(tEpic1);
        taskManager.addNewSubtask(tSubtask1);
        String trueS1 = "1,EPIC,Epic1,NEW,descriptionEpic1,23.01.2023; 10:00,120,\n";
        String trueS2 = "2,SUBTASK,Subtask1,NEW,descriptionSubtask1,23.01.2023; 10:00,120,1\n";
        String s1 = taskManager.toString(tEpic1);
        String s2 = taskManager.toString(tSubtask1);
        assertEquals(trueS1, s1, "Строки не совпадают");
        assertEquals(trueS2, s2, "Строки не совпадают");
    }

    @Test
    public void fromString() {
        String s1 = "1,EPIC,Epic1,NEW,descriptionEpic1,23.01.2023; 10:00,120,";
        String s2 = "2,SUBTASK,Subtask1,NEW,descriptionSubtask1,23.01.2023; 10:00,120,1";
        Task tEpic1 = taskManager.fromString(s1);
        Task subTask1 = taskManager.fromString(s2);
        Task trueEpic1 = new Epic("Epic1", "descriptionEpic1");
        trueEpic1.setId(1);
        Task trueSubtask1 = new Subtask("Subtask1", "descriptionSubtask1", 1);
        trueSubtask1.setId(2);
        trueSubtask1.setStartTime(startTime);
        trueSubtask1.setDuration(120);
        assertEquals(trueEpic1, tEpic1, "Задачи не совпадают");
        assertEquals(trueSubtask1, subTask1, "Задачи не совпадают");
    }

    @Test
    public void historyToString() {
        epic1.setId(1);
        subtask1.setId(2);
        subtask2.setId(3);
        task1.setId(4);
        task2.setId(5);
        HistoryManager historyManager = new InMemoryHistoryManager();
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(subtask1);
        historyManager.add(subtask2);
        historyManager.add(epic1);
        String s = FileBackedTasksManager.historyToString(historyManager);
        assertEquals("4,5,2,3,1", s, "Строки не совпадают");
    }

    @Test
    public void historyFromString() {
        String s = "1,3,2,4,10";
        List<Integer> history = FileBackedTasksManager.historyFromString(s);
        assertEquals(List.of(1, 3, 2, 4, 10), history, "Списки не совпадают");
    }
}
