package manager.file;

import manager.InMemoryTaskManager;
import manager.history.HistoryManager;
import tasks.*;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final File file;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy; HH:mm");

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
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
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
        Task task = super.getTask(identifier);
        save();
        return task;
    }

    @Override
    public Subtask getSubtask(int identifier) {
        Subtask subtask = super.getSubtask(identifier);
        save();
        return subtask;
    }

    @Override
    public Epic getEpic(int identifier) {
        Epic epic = super.getEpic(identifier);
        save();
        return epic;
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
                btm.setIdentifier(task.getId() - 1);
                //btm.identifier = task.getId() - 1;
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
        ArrayList<Task> allTasks = new ArrayList<>();
        allTasks.addAll(getTasks());
        allTasks.addAll(getSubtasks());
        allTasks.addAll(getEpics());
        allTasks.sort(Comparator.comparingInt(Task::getId));
        try (Writer fileWriter = new FileWriter(file)) {
            fileWriter.write("id,type,name,status,description,startTime,duration,epic\n");
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
        String result = task.getId() + "," + task.getType() + "," + task.getName() + ","
                + task.getStatus() + "," + task.getDescription();
        if (task.getStartTime() != null) {
            result = result + "," + task.getStartTime().format(formatter);
        } else {
            result = result + ", null";
        }
        result = result + "," + task.getDuration() + ",";
        if (task.getType() == TaskType.SUBTASK) {
            result = result + ((Subtask) task).getEpicId();
        }
        return result + "\n";
    }

    public Task fromString(String value) {
        String[] split = value.split(",");
        int id = Integer.parseInt(split[0]);
        TaskType type = TaskType.valueOf(split[1]);
        String name = split[2];
        TaskStatus status = TaskStatus.valueOf(split[3]);
        String description = split[4];
        LocalDateTime startTime;
        try {
            startTime = LocalDateTime.parse(split[5], formatter);
        } catch (DateTimeParseException e) {
            startTime = null;
        }
        int duration = Integer.parseInt(split[6]);
        switch (type) {
            case TASK:
                return new Task(name,description,id,type,status,startTime,duration);
            case SUBTASK:
                int epicId = Integer.parseInt(split[7]);
                return new Subtask(name,description,id,type,status,startTime,duration,epicId);
            case EPIC:
                Epic epic = new Epic(name, description);
                epic.setId(id);
                return epic;
        }
        return null;
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
