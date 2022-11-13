
public class Task {

    String name;
    String description;
    String status;


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

    @Override
    public String toString() {
        String result = "Task{" +
                "name='" + name + '\'';
        if (description != null) {
            result = result + ", description.Length=" + description.length();
        } else {
            result = result + ", description.Length=null";
        }
        result = result + ", status=" + status + "}\n";

        return result;
    }
}
