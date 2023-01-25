package tasks;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Objects;

public class Epic extends Task {

    private LocalDateTime endTime;
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public Epic(String name, String description) {
        super(name, description);
        this.setType(TaskType.EPIC);
        endTime = LocalDateTime.MIN;
        setStartTime(LocalDateTime.MAX);
    }

    public void addSubtask(int identifier, Subtask subtask) {
        subtasks.put(identifier, subtask);
    }

    public void epicStatus() {
        int statusNew = 0;
        int statusDone = 0;
        for (Subtask exp : subtasks.values()) {
            switch (exp.getStatus()) {
                case NEW:
                    statusNew++;
                    break;
                case DONE:
                    statusDone++;
                    break;
            }
        }
        if ((statusNew == subtasks.size() && (statusNew != 0)) || subtasks.size() == 0) {
            setStatus(TaskStatus.NEW);
        } else if (statusDone == subtasks.size()) {
            setStatus(TaskStatus.DONE);
        } else {
            setStatus(TaskStatus.IN_PROGRESS);
        }
    }

    @Override
    public String toString() {
        String result = "tasks.Epic " + getId() + " {" +
                "name='" + getName() + '\'';
        result = result + ", description=" + getDescription() + ", status=" + getStatus();
        if (getStartTime() == LocalDateTime.MAX) {
            result = result + ", startTime=null" + " , duration=" + getDuration() + " }";
        } else {
            result = result + ", startTime=" + getStartTime() + " , duration=" + getDuration() + " }";
        }
        return result + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(endTime, epic.endTime) && Objects.equals(subtasks, epic.subtasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), endTime, subtasks);
    }

    @Override
    public LocalDateTime getEndTime() {
        if (subtasks.isEmpty()) {
            setDuration(0);
            endTime = LocalDateTime.MIN;
            setStartTime(LocalDateTime.MAX);
            return null;
        }
        int epicDuration = 0;
        for (Subtask subtask : subtasks.values()) {
            if (subtask.getStartTime() == null) {
                continue;
            }
            epicDuration += subtask.getDuration();
            if (getStartTime().isAfter(subtask.getStartTime())) {
                setStartTime(subtask.getStartTime());
            }
            if (endTime.isBefore(subtask.getEndTime())) {
                endTime = subtask.getEndTime();
            }
        }
        setDuration(epicDuration);
        return endTime;
    }
}
