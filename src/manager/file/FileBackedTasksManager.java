package manager.file;

import manager.InMemoryTaskManager;
import manager.history.HistoryManager;
import tasks.*;

import java.io.*;
import java.util.*;


public class FileBackedTasksManager extends InMemoryTaskManager {
    // не уверен, что у меня получилось хорошо раскидать всё по пакетам, но надеюсь что более-менее)
    private final File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void updateEpics(Epic epic) {
        super.updateEpics(epic);
        save();
    }

    @Override
    public void addNewTask(Task task) {
        super.addNewTask(task);
        save();
    }

    @Override
    public void addNewSubtask(Subtask subtask) {
        super.addNewSubtask(subtask);
        save();
    }

    @Override
    public void addNewEpic(Epic epic) {
        super.addNewEpic(epic);
        save();
    }

    @Override
    public void deleteTasks() {
        super.deleteTasks();
        save();
    }

    @Override
    public void deleteSubtasks() {
        super.deleteSubtasks();
        save();
    }

    @Override
    public void deleteEpics() {
        super.deleteEpics();
        save();
    }

    @Override
    public Task getTask(int identifier) {
        Task temp = super.getTask(identifier);
        save();
        return temp;
    }

    @Override
    public Subtask getSubtask(int identifier) {
        Subtask temp = super.getSubtask(identifier);
        save();
        return temp;
    }

    @Override
    public Epic getEpic(int identifier) {
        Epic temp = super.getEpic(identifier);
        save();
        return temp;
    }

    @Override
    public void removeTask(int identifier) {
        super.removeTask(identifier);
        save();
    }

    @Override
    public void removeSubtask(int identifier) {
        super.removeSubtask(identifier);
        save();
    }

    @Override
    public void removeEpic(int identifier) {
        super.removeEpic(identifier);
        save();
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager btm = new FileBackedTasksManager(file);
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine();
            while (br.ready()) {
                String line = br.readLine();
                if (line.equals("")) {
                    break;
                }
                Task task = btm.fromString(line);
                btm.identifier = task.getId() - 1;
                switch (task.getType()) {
                    case TASK:
                        btm.addNewTask(task);
                        break;
                    case SUBTASK:
                        btm.addNewSubtask((Subtask) task);
                        break;
                    case EPIC:
                        btm.addNewEpic((Epic) task);
                        break;
                }
            }
            for (Integer id : historyFromString(br.readLine())) {
                btm.getTask(id);
                btm.getSubtask(id);
                btm.getEpic(id);
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения файла", e);
        }
        return btm;
    }

    public void save() {
        try (Writer fileWriter = new FileWriter(file)) {
            fileWriter.write("id,type,name,status,description,epic\n");
            ArrayList<Task> allTasks = new ArrayList<>();
            allTasks.addAll(getTasks());
            allTasks.addAll(getSubtasks());
            allTasks.addAll(getEpics());
            allTasks.sort(new TasksComparator());
            for (Task task : allTasks) {
                fileWriter.write(toString(task));
            }
            if (!history.getHistory().isEmpty()) {
                fileWriter.write("\n" + historyToString(history));
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи файла", e);
        }
    }

    public String toString(Task task) {
        String result = task.getId() + ",";
        switch (task.getType()) {
            case TASK:
                result = result + TaskType.TASK;
                break;
            case SUBTASK:
                result = result + TaskType.SUBTASK;
                break;
            case EPIC:
                result = result + TaskType.EPIC;
                break;
        }
        result = result + "," + task.getName() + "," + task.getStatus() + "," + task.getDescription() + ",";
        if (task.getType() == TaskType.SUBTASK) {
            result = result + ((Subtask) task).getEpicId();
        }
        return result + "\n";
    }

    public Task fromString(String value) {
        String[] split = value.split(",");
        // 0 = id 1 = type 2 = name 3 = status 4 = descr 5 = epicId //
        Task task = null;
        switch (split[1]) {
            case "TASK":
                task = new Task(split[2], split[4]);
                if (split[3].equals("NEW")) {
                    task.setStatus(TaskStatus.NEW);
                } else if (split[3].equals("IN_PROGRESS")) {
                    task.setStatus(TaskStatus.IN_PROGRESS);
                } else {
                    task.setStatus(TaskStatus.DONE);
                }
                break;
            case "SUBTASK":
                task = new Subtask(split[2], split[4], Integer.parseInt(split[5]));
                if (split[3].equals("NEW")) {
                    task.setStatus(TaskStatus.NEW);
                } else if (split[3].equals("IN_PROGRESS")) {
                    task.setStatus(TaskStatus.IN_PROGRESS);
                } else {
                    task.setStatus(TaskStatus.DONE);
                }
                break;
            case "EPIC":
                task = new Epic(split[2], split[4]);
                break;
        }
        Objects.requireNonNull(task).setId(Integer.parseInt(split[0]));
        return task;
    }

    public static String historyToString(HistoryManager manager) {
        StringBuilder result = new StringBuilder();
        for (Task task : manager.getHistory()) {
            result.append(task.getId()).append(",");
        }
        if (result.lastIndexOf(",") != -1) {
            result.deleteCharAt(result.lastIndexOf(","));
        }
        return result.toString();
    }

    public static List<Integer> historyFromString(String value) {
        List<Integer> historyList = new ArrayList<>();
        if (value == null) {
            return historyList;
        }
        String[] split = value.split(",");
        for (String s : split) {
            historyList.add(Integer.parseInt(s));
        }
        return historyList;
    }
}
