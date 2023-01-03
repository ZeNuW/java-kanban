package tasks;

import manager.TaskType;

import java.util.Objects;

public class Task {

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

    @Override
    public String toString() {
        String result = "tasks.Task " + id + " {" +
                "name='" + name + '\'';
        result = result + ", description=" + description;
        result = result + ", status=" + status + "}";
        return result + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(name, task.name) && Objects.equals(description, task.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description);
    }

}
