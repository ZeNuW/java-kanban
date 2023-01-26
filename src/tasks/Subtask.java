package tasks;

import java.time.LocalDateTime;
import java.util.Objects;

public class Subtask extends Task {

    private final int epicId;

    public Subtask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
        this.setType(TaskType.SUBTASK);
    }

    public Subtask(String name, String description, int id, TaskType type, TaskStatus status, LocalDateTime startTime,
                   int duration,int epicId) {
        super(name,description,id,type,status,startTime,duration);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        String result = "tasks.Subtask " + getId() + " {" +
                "name='" + getName() + '\'';
        result = result + ", description=" + getDescription();
        result = result + ", status=" + getStatus() + ", startTime=" + getStartTime()
                + ", duration=" + getDuration() + " }";

        return result + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return epicId == subtask.epicId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }
}
