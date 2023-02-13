package manager;

import server.HttpTaskManager;
import manager.history.HistoryManager;
import manager.history.InMemoryHistoryManager;

public class Managers {

    public static TaskManager getDefault() {
        String url = "http://localhost:8078";
        //return FileBackedTasksManager.loadFromFile(new File("resources/", "history.csv"));
        return new HttpTaskManager(url);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
