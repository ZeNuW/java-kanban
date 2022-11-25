package manager;

import tasks.Task;

import java.util.ArrayList;
import java.util.List;


public class InMemoryHistoryManager implements HistoryManager {

    private static final List<Task> historyList = new ArrayList<>();

    @Override
    public void addHistory(Task task) {
        if (historyList.size() < 10) {
            historyList.add(task);
        } else if (historyList.size() == 10) {
            historyList.remove(0);
            historyList.add(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyList;
    }
}
