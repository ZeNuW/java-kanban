package manager.file;

import manager.TaskManagerTest;
import manager.history.HistoryManager;
import manager.history.InMemoryHistoryManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.*;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    private final LocalDateTime startTime = LocalDateTime.of(2023, 1, 23, 10, 0);
    private static final File file = new File("test/", "history.csv");

    @BeforeEach
    public void BeforeEach() {
        taskManager = new FileBackedTasksManager(file);
        taskManager.addNewEpic(epic1);
        taskManager.addNewEpic(epic2);
        taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask2);
        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);
        try (Writer fileWriter = new FileWriter(file)) {
            fileWriter.write("id,type,name,status,description,startTime,duration,epic\n");
            fileWriter.write("1,EPIC,Epic1,NEW,descriptionEpic1,23.01.2023; 10:00,300,\n");
            fileWriter.write("2,EPIC,Epic2,NEW,descriptionEpic2,31.12.+999999999; 23:59,0,\n");
            fileWriter.write("3,SUBTASK,Subtask1,NEW,descriptionSubtask1,23.01.2023; 10:00,120,1\n");
            fileWriter.write("4,SUBTASK,Subtask2,NEW,descriptionSubtask2,23.01.2023; 13:00,180,1\n");
            fileWriter.write("5,TASK,Task1,NEW,descriptionTask1,23.01.2023; 17:00,120,\n");
            fileWriter.write("6,TASK,Task2,NEW,descriptionTask2,23.01.2023; 20:00,180,\n");
            fileWriter.write("\n");
            fileWriter.write("3,4,2,1,5");
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи файла", e);
        }
    }

    @AfterAll
    public static void AfterAll() {
        if (file.delete()) {
            System.out.println("Файл удалён");
        }
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
        FileBackedTasksManager loadFromFile = FileBackedTasksManager.loadFromFile(file);
        assertNotNull(loadFromFile.getTasks(), "Задачи пустые");
        assertNotNull(loadFromFile.getSubtasks(), "Задачи пустые");
        assertNotNull(loadFromFile.getEpics(), "Задачи пустые");
        assertEquals(taskManager.getTasks(), loadFromFile.getTasks(), "Задачи не совпадают");
        assertEquals(taskManager.getSubtasks(), loadFromFile.getSubtasks(), "Задачи не совпадают");
        assertEquals(taskManager.getEpics(), loadFromFile.getEpics(), "Задачи не совпадают");
        taskManager.getSubtask(3);
        taskManager.getSubtask(4);
        taskManager.getEpic(2);
        taskManager.getEpic(1);
        taskManager.getTask(5);
        assertEquals(taskManager.getHistory(), loadFromFile.getHistory(), "История не совпадает");
        assertEquals(new ArrayList<>(taskManager.getPrioritizedTasks()), new ArrayList<>(loadFromFile.getPrioritizedTasks()),
                "Сортировка по приоритету не совпадает");
    }

    @Test
    public void save() {
        List<Task> allTasksTrue = new ArrayList<>();
        allTasksTrue.add(epic1);
        allTasksTrue.add(epic2);
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
        for (Task task : allTasksTrue) {
            assertEquals(allTasksTrue.get(task.getId() - 1).toString(),
                    allTasks.get(task.getId() - 1).toString(), "Задачи не совпадают");
        }
    }

    @Test
    public void toStringTest() {
        String trueS1 = "1,EPIC,Epic1,NEW,descriptionEpic1,23.01.2023; 10:00,300,\n";
        String trueS2 = "3,SUBTASK,Subtask1,NEW,descriptionSubtask1,23.01.2023; 10:00,120,1\n";
        String s1 = taskManager.toString(epic1);
        String s2 = taskManager.toString(subtask1);
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
        HistoryManager historyManager = new InMemoryHistoryManager();
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(subtask1);
        historyManager.add(subtask2);
        historyManager.add(epic1);
        String s = FileBackedTasksManager.historyToString(historyManager);
        assertEquals("5,6,3,4,1", s, "Строки не совпадают");
    }

    @Test
    public void historyFromString() {
        String s = "1,3,2,4,10";
        List<Integer> history = FileBackedTasksManager.historyFromString(s);
        assertEquals(List.of(1, 3, 2, 4, 10), history, "Списки не совпадают");
    }
}
