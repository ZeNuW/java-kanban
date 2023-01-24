package manager.history;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    Epic epic1 = new Epic("Epic1", "descriptionEpic1");
    Epic epic2 = new Epic("Epic2", "descriptionEpic2");
    Subtask subtask1 = new Subtask("Subtask1", "descriptionSubtask1", 3);
    Task task1 = new Task("Task1", "descriptionTask1");

    private InMemoryHistoryManager historyManager;

    @BeforeEach
    public void BeforeEach() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    public void add() {
        historyManager.add(null);
        assertTrue(historyManager.getHistory().isEmpty(), "Список не пуст");
        task1.setId(1);
        subtask1.setId(2);
        epic1.setId(3);
        historyManager.add(task1);
        historyManager.add(task1);
        historyManager.add(subtask1);
        historyManager.add(subtask1);
        historyManager.add(epic1);
        assertEquals(List.of(task1, subtask1, epic1), historyManager.getHistory(),
                "Истории отличаются");
    }

    @Test
    public void remove() {
        assertDoesNotThrow(() -> historyManager.remove(5));
        task1.setId(1);
        subtask1.setId(2);
        epic1.setId(3);
        epic2.setId(4);
        historyManager.add(task1);
        historyManager.add(subtask1);
        historyManager.add(epic1);
        historyManager.add(epic2);
        historyManager.remove(2);
        assertEquals(List.of(task1, epic1, epic2), historyManager.getHistory(),
                "Истории отличаются");
        historyManager.remove(4);
        assertEquals(List.of(task1, epic1), historyManager.getHistory(), "Истории отличаются");
        historyManager.remove(1);
        assertEquals(List.of(epic1), historyManager.getHistory(), "Истории отличаются");
    }

    @Test
    public void getHistory() {
        historyManager.add(null);
        assertTrue(historyManager.getHistory().isEmpty());
        task1.setId(1);
        subtask1.setId(2);
        epic1.setId(3);
        historyManager.add(task1);
        historyManager.add(task1);
        historyManager.add(subtask1);
        historyManager.add(subtask1);
        historyManager.add(epic1);
        assertEquals(List.of(task1, subtask1, epic1), historyManager.getHistory(),
                "Истории отличаются");
        historyManager.remove(1);
        assertEquals(List.of(subtask1, epic1), historyManager.getHistory(),
                "Истории отличаются");
        historyManager.remove(2);
        assertEquals(List.of(epic1), historyManager.getHistory(), "Истории отличаются");
    }
}