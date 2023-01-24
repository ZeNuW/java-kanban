package tasks;

import manager.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    InMemoryTaskManager taskManager;
    Epic epic = new Epic("Epic1", "descriptionEpic1");
    Subtask subtask1 = new Subtask("Subtask1", "descriptionSubtask1", 1);
    Subtask subtask2 = new Subtask("Subtask1", "descriptionSubtask1", 1);
    private final LocalDateTime startTime = LocalDateTime.of(2023,1,23,10,0);

    @BeforeEach
    public void BeforeEach() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    public void EpicShouldBeNewWhenIsEmpty() {
        taskManager.addNewEpic(epic);
        assertEquals(TaskStatus.NEW, epic.getStatus());
    }

    @Test
    public void EpicShouldBeNewWhenAllSubtaskIsNew() {
        taskManager.addNewEpic(epic);
        subtask1.setStartTime(startTime);
        subtask1.setDuration(120);
        subtask2.setStartTime(startTime.plusHours(3));
        subtask2.setDuration(180);
        taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask2);
        assertEquals(TaskStatus.NEW, epic.getStatus());
    }

    @Test
    public void EpicShouldBeDoneWhenAllSubtaskIsDone() {
        taskManager.addNewEpic(epic);
        subtask1.setStatus(TaskStatus.DONE);
        subtask2.setStatus(TaskStatus.DONE);
        subtask1.setStartTime(startTime);
        subtask1.setDuration(120);
        subtask2.setStartTime(startTime.plusHours(3));
        subtask2.setDuration(180);
        taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask2);
        assertEquals(TaskStatus.DONE, epic.getStatus());
    }

    @Test
    public void EpicShouldBeInProgressWhenAllSubtaskIsNewAndDone() {
        taskManager.addNewEpic(epic);
        subtask1.setStatus(TaskStatus.DONE);
        subtask1.setStartTime(startTime);
        subtask1.setDuration(120);
        subtask2.setStartTime(startTime.plusHours(3));
        subtask2.setDuration(180);
        taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask2);
        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus());
    }

    @Test
    public void EpicShouldBeInProgressWhenAllSubtaskIsInProgress() {
        taskManager.addNewEpic(epic);
        subtask1.setStatus(TaskStatus.IN_PROGRESS);
        subtask2.setStatus(TaskStatus.IN_PROGRESS);
        subtask1.setStartTime(startTime);
        subtask1.setDuration(120);
        subtask2.setStartTime(startTime.plusHours(3));
        subtask2.setDuration(180);
        taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask2);
        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus());
    }
}