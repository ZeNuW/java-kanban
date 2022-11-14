package tasks;


import java.util.Objects;

public class Subtask extends Task {

    private int epicId;

    public Subtask(String name, String description, String status) {
        super(name, description, status);
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        String result = "tasks.Subtask " + id + " {" +
                "name='" + name + '\'';
        result = result + ", description=" + description;
        result = result + ", status=" + status + "},";

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
