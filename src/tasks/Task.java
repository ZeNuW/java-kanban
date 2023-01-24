package tasks;

import java.time.LocalDateTime;
import java.util.Objects;

public class Task {

    private int duration;
    private LocalDateTime startTime;
    private String name;
    private String description;
    private int id;
    private TaskStatus status;
    private TaskType type;


    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        status = TaskStatus.NEW;
        this.type = TaskType.TASK;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public int getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getEndTime() {
        return startTime.plusMinutes(duration);
    }

    @Override
    public String toString() {
        String result = "tasks.Task " + id + " {" +
                "name='" + name + '\'';
        result = result + ", description=" + description + ", status=" + status
                + ", startTime=" + startTime + " , duration=" + duration + " }";
        return result + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return duration == task.duration && id == task.id && Objects.equals(startTime, task.startTime)
                && Objects.equals(name, task.name) && Objects.equals(description, task.description)
                && status == task.status && type == task.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(duration, startTime, name, description, id, status, type);
    }
}
