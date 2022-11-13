
public class Subtask extends Task{
    public Subtask(String name, String description, String status) {
        super(name, description, status);
    }

    @Override
    public String toString() {
        String result = "Subtask{" +
                "name='" + name + '\'';
        if (description != null) {
            result = result + ", description.Length=" + description.length();
        } else {
            result = result + ", description.Length=null";
        }
        result = result + ", status=" + status + "},";

        return result + "\n";
    }
}
