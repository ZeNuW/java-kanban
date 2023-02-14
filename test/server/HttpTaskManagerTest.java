package server;

import manager.TaskManagerTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {

    private final String url = "http://localhost:8078";
    private KVServer server;

    @BeforeEach
    public void BeforeEach() throws IOException {
        server = new KVServer();
        server.start();
        taskManager = new HttpTaskManager(url);
        taskManager.addNewEpic(epic1);
        taskManager.addNewEpic(epic2);
        taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask2);
        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);
    }

    @AfterEach
    public void AfterEach() {
        server.stop();
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
    public void save() {
        taskManager.getEpic(1);
        taskManager.getEpic(2);
        taskManager.getTask(5);
        taskManager.getSubtask(3);
        taskManager.save();
        KVTaskClient client = new KVTaskClient(url);
        String tasks = client.load("tasks");
        String subtasks = client.load("subtasks");
        String epics = client.load("epics");
        String history = client.load("history");
        String tasksExp = "[\n" +
                "  {\n" +
                "    \"duration\": 120,\n" +
                "    \"startTime\": \"23.01.2023; 17:00\",\n" +
                "    \"name\": \"Task1\",\n" +
                "    \"description\": \"descriptionTask1\",\n" +
                "    \"id\": 5,\n" +
                "    \"status\": \"NEW\",\n" +
                "    \"type\": \"TASK\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"duration\": 180,\n" +
                "    \"startTime\": \"23.01.2023; 20:00\",\n" +
                "    \"name\": \"Task2\",\n" +
                "    \"description\": \"descriptionTask2\",\n" +
                "    \"id\": 6,\n" +
                "    \"status\": \"NEW\",\n" +
                "    \"type\": \"TASK\"\n" +
                "  }\n" +
                "]";
        String subtasksExp = "[\n" +
                "  {\n" +
                "    \"epicId\": 1,\n" +
                "    \"duration\": 120,\n" +
                "    \"startTime\": \"23.01.2023; 10:00\",\n" +
                "    \"name\": \"Subtask1\",\n" +
                "    \"description\": \"descriptionSubtask1\",\n" +
                "    \"id\": 3,\n" +
                "    \"status\": \"NEW\",\n" +
                "    \"type\": \"SUBTASK\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"epicId\": 1,\n" +
                "    \"duration\": 180,\n" +
                "    \"startTime\": \"23.01.2023; 13:00\",\n" +
                "    \"name\": \"Subtask2\",\n" +
                "    \"description\": \"descriptionSubtask2\",\n" +
                "    \"id\": 4,\n" +
                "    \"status\": \"NEW\",\n" +
                "    \"type\": \"SUBTASK\"\n" +
                "  }\n" +
                "]";
        String epicsExp = "[\n" +
                "  {\n" +
                "    \"endTime\": \"23.01.2023; 16:00\",\n" +
                "    \"subtasks\": {\n" +
                "      \"3\": {\n" +
                "        \"epicId\": 1,\n" +
                "        \"duration\": 120,\n" +
                "        \"startTime\": \"23.01.2023; 10:00\",\n" +
                "        \"name\": \"Subtask1\",\n" +
                "        \"description\": \"descriptionSubtask1\",\n" +
                "        \"id\": 3,\n" +
                "        \"status\": \"NEW\",\n" +
                "        \"type\": \"SUBTASK\"\n" +
                "      },\n" +
                "      \"4\": {\n" +
                "        \"epicId\": 1,\n" +
                "        \"duration\": 180,\n" +
                "        \"startTime\": \"23.01.2023; 13:00\",\n" +
                "        \"name\": \"Subtask2\",\n" +
                "        \"description\": \"descriptionSubtask2\",\n" +
                "        \"id\": 4,\n" +
                "        \"status\": \"NEW\",\n" +
                "        \"type\": \"SUBTASK\"\n" +
                "      }\n" +
                "    },\n" +
                "    \"duration\": 300,\n" +
                "    \"startTime\": \"23.01.2023; 10:00\",\n" +
                "    \"name\": \"Epic1\",\n" +
                "    \"description\": \"descriptionEpic1\",\n" +
                "    \"id\": 1,\n" +
                "    \"status\": \"NEW\",\n" +
                "    \"type\": \"EPIC\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"subtasks\": {},\n" +
                "    \"duration\": 0,\n" +
                "    \"name\": \"Epic2\",\n" +
                "    \"description\": \"descriptionEpic2\",\n" +
                "    \"id\": 2,\n" +
                "    \"status\": \"NEW\",\n" +
                "    \"type\": \"EPIC\"\n" +
                "  }\n" +
                "]";
        String historyExp = "[\n" +
                "  {\n" +
                "    \"endTime\": \"23.01.2023; 16:00\",\n" +
                "    \"subtasks\": {\n" +
                "      \"3\": {\n" +
                "        \"epicId\": 1,\n" +
                "        \"duration\": 120,\n" +
                "        \"startTime\": \"23.01.2023; 10:00\",\n" +
                "        \"name\": \"Subtask1\",\n" +
                "        \"description\": \"descriptionSubtask1\",\n" +
                "        \"id\": 3,\n" +
                "        \"status\": \"NEW\",\n" +
                "        \"type\": \"SUBTASK\"\n" +
                "      },\n" +
                "      \"4\": {\n" +
                "        \"epicId\": 1,\n" +
                "        \"duration\": 180,\n" +
                "        \"startTime\": \"23.01.2023; 13:00\",\n" +
                "        \"name\": \"Subtask2\",\n" +
                "        \"description\": \"descriptionSubtask2\",\n" +
                "        \"id\": 4,\n" +
                "        \"status\": \"NEW\",\n" +
                "        \"type\": \"SUBTASK\"\n" +
                "      }\n" +
                "    },\n" +
                "    \"duration\": 300,\n" +
                "    \"startTime\": \"23.01.2023; 10:00\",\n" +
                "    \"name\": \"Epic1\",\n" +
                "    \"description\": \"descriptionEpic1\",\n" +
                "    \"id\": 1,\n" +
                "    \"status\": \"NEW\",\n" +
                "    \"type\": \"EPIC\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"subtasks\": {},\n" +
                "    \"duration\": 0,\n" +
                "    \"name\": \"Epic2\",\n" +
                "    \"description\": \"descriptionEpic2\",\n" +
                "    \"id\": 2,\n" +
                "    \"status\": \"NEW\",\n" +
                "    \"type\": \"EPIC\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"duration\": 120,\n" +
                "    \"startTime\": \"23.01.2023; 17:00\",\n" +
                "    \"name\": \"Task1\",\n" +
                "    \"description\": \"descriptionTask1\",\n" +
                "    \"id\": 5,\n" +
                "    \"status\": \"NEW\",\n" +
                "    \"type\": \"TASK\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"epicId\": 1,\n" +
                "    \"duration\": 120,\n" +
                "    \"startTime\": \"23.01.2023; 10:00\",\n" +
                "    \"name\": \"Subtask1\",\n" +
                "    \"description\": \"descriptionSubtask1\",\n" +
                "    \"id\": 3,\n" +
                "    \"status\": \"NEW\",\n" +
                "    \"type\": \"SUBTASK\"\n" +
                "  }\n" +
                "]";
        assertEquals(tasksExp, tasks, "JSON задач не совпадает");
        assertEquals(subtasksExp, subtasks, "JSON подзадач не совпадает");
        assertEquals(epicsExp, epics, "JSON эпиков не совпадает");
        assertEquals(historyExp, history, "JSON истории не совпадает");
    }

    @Test
    public void load() {
        taskManager.getEpic(1);
        taskManager.getEpic(2);
        taskManager.getTask(5);
        taskManager.getSubtask(3);
        HttpTaskManager newManager = new HttpTaskManager(url); //url - аналогичен url taskManager
        assertEquals(taskManager.getTasks(),newManager.getTasks(),"Задачи не совпадают");
        assertEquals(taskManager.getSubtasks(),newManager.getSubtasks(),"Задачи не совпадают");
        assertEquals(taskManager.getEpics(),newManager.getEpics(),"Задачи не совпадают");
        newManager.getHistory();
        assertEquals(taskManager.getHistory(),newManager.getHistory(),"Задачи не совпадают");
        assertEquals( new ArrayList<>(taskManager.getPrioritizedTasks()),
                new ArrayList<>(newManager.getPrioritizedTasks()),"Задачи не совпадают");
    }
}
