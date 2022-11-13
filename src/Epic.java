import java.util.HashMap;

public class Epic extends Task {

    HashMap<Integer, Subtask> subtasks;

    public Epic(String name, String description, HashMap<Integer, Subtask> subtasks) {
        this.name = name;
        this.description = description;
        this.subtasks = new HashMap<>(subtasks);
        this.status = epicStatus(subtasks);
    }

    @Override
    public String toString() {
        String result = "Epic{" +
                "name='" + name + '\'';
        if (description != null) {
            result = result + ", description.Length=" + description.length();
        } else {
            result = result + ", description.Length=null";
        }
        result = result + ", status=" + status + '}';

        return result;
    }
    private String epicStatus(HashMap<Integer, Subtask> subtasks) {
        int statusNew = 0;
        int statusDone = 0;
        for (Subtask exp : subtasks.values()) {
            //смысла считать in_progress нет т.к он всё равно будет в оставшихся случаях, кроме тех что уже отсеяны
            switch (exp.status) {
                case "NEW":
                    statusNew++;
                    break;
                case "DONE":
                    statusDone++;
                    break;
            }
        }
        if ((statusNew == subtasks.size() && (statusNew != 0)) || subtasks.size() == 0) {
            return "NEW";
        } else if (statusDone == subtasks.size()) {
            return "DONE";
        } else {
            return "IN_PROGRESS";
        }
    }

}
