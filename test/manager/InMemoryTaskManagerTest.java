package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void BeforeEach() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    public void getTasks() {
        super.getTasks();
    }

    @Test
    public void getSubtasks() {
        super.getSubtasks();
    }

    @Test
    public void getEpics() {
        super.getEpics();
    }

    @Test
    public void updateTask() {
        super.updateTask();
    }

    @Test
    public void updateSubtask() {
        super.updateSubtask();
    }

    @Test
    public void updateEpic() {
        super.updateEpic();
    }

    @Test
    public void addNewTask() {
        super.addNewTask();
    }

    @Test
    public void addNewSubtask() {
        super.addNewSubtask();
    }

    @Test
    public void addNewEpic() {
        super.addNewEpic();
    }

    @Test
    public void deleteTasks() {
        super.deleteTasks();
    }

    @Test
    public void deleteSubtasks() {
        super.deleteSubtasks();
    }

    @Test
    public void deleteEpics() {
        super.deleteEpics();
    }

    @Test
    public void getTask() {
        super.getTask();
    }

    @Test
    public void getSubtask() {
        super.getSubtask();
    }

    @Test
    public void getEpic() {
        super.getEpic();
    }

    @Test
    public void removeTask() {
        super.removeTask();
    }

    @Test
    public void removeSubtask() {
        super.removeSubtask();
    }

    @Test
    public void removeEpic() {
        super.removeEpic();
    }

    @Test
    public void listOfEpicSubtasks() {
        super.listOfEpicSubtasks();
    }

    @Test
    public void getHistory() {
        super.getHistory();
    }

    @Test
    public void getPrioritizedTasks() {
        super.getPrioritizedTasks();
    }
}