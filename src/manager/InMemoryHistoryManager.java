package manager;

import tasks.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private static final LinkedList<Task> historyList = new LinkedList<>();

    @Override
    public void addHistory(Task task) {
        if (historyList.size() == 10) {
            //historyList.remove(0);
            historyList.removeFirst();
        }
        historyList.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return historyList;
    }
}
