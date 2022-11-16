package tasks;

import java.util.Objects;

public class Task {

    protected String name;
    protected String description;
    protected String status;
    protected int id;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Task(String name, String description, String status) {
        this.name = name;
        this.description = description;
        this.status = taskStatus(status);
    }

    public Task() {
    }

    protected String taskStatus(String status) {
        if ((status.equals("NEW")) || (status.equals("IN_PROGRESS")) || (status.equals("DONE"))) {
            return status;
        } else {
            return null;
        }
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
