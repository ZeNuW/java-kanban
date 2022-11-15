package tasks;

import java.util.HashMap;
import java.util.Objects;

public class Epic extends Task {

    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public Epic(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = "NEW";
    }

    public void addSubtask(int identifier, Subtask subtask) {
        subtasks.put(identifier, subtask);
    }

    public void epicStatus() {
        int statusNew = 0;
        int statusDone = 0;
        for (Subtask exp : subtasks.values()) {
            switch (exp.getStatus()) {
                case "NEW":
                    statusNew++;
                    break;
                case "DONE":
                    statusDone++;
                    break;
            }
        }
        if ((statusNew == subtasks.size() && (statusNew != 0)) || subtasks.size() == 0) {
            status = "NEW";
        } else if (statusDone == subtasks.size()) {
            status = "DONE";
        } else {
            status = "IN_PROGRESS";
        }
    }


    @Override
    public String toString() {
        String result = "tasks.Epic " + id + " {" +
                "name='" + name + '\'';
        result = result + ", description=" + description;
        result = result + ", status=" + status + '}';
        return result + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtasks, epic.subtasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtasks);
    }
}
