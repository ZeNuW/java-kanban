import manager.Managers;
import manager.TaskManager;
import tasks.*;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        String name;
        String description;
        String statusIn;
        TaskStatus status;

        Scanner scanner = new Scanner(System.in);
        final TaskManager manager = Managers.getDefault();
        int identifier;
        int userInput;
        int epicId;

        while (true) {
            printMenu();
            userInput = Integer.parseInt(scanner.nextLine());
            switch (userInput) {
                case 0:
                    return;
                case 1:
                    printMenuInCase();
                    userInput = Integer.parseInt(scanner.nextLine());
                    switch (userInput) {
                        case 1: //для быстрой проверки, чтобы каждый раз не вбивать данные
                            manager.addNewTask(new Task("Забрать справки",
                                    "Нужно пойти в поликлинику"));
                            manager.addNewTask(new Task("Забрать справки2",
                                    "Нужно пойти в поликлинику2"));
                            break;
                        case 2:
                            System.out.println(manager.getTasks());
                            break;
                        case 3:
                            manager.deleteTasks();
                            break;
                        case 4:
                            System.out.println("Введите номер объекта");
                            identifier = Integer.parseInt(scanner.nextLine());
                            System.out.println(manager.getTask(identifier));
                            break;
                        case 5: //ввод где всё вводится "вручную"
                            System.out.println("Введите название задачи");
                            name = scanner.nextLine();
                            System.out.println("Введите описание задачи");
                            description = scanner.nextLine();
                            manager.addNewTask(new Task(name, description));
                            break;
                        case 6:
                            System.out.println("Введите название задачи");
                            name = scanner.nextLine();
                            System.out.println("Введите описание задачи");
                            description = scanner.nextLine();
                            System.out.println("Введите  статус задачи: 1 - NEW, 2 - IN_PROGRESS, 3 - DONE");
                            status = null;
                            while (status == null) {
                                statusIn = scanner.nextLine();
                                switch (statusIn) {
                                    case "1":
                                        status = TaskStatus.NEW;
                                        break;
                                    case "2":
                                        status = TaskStatus.IN_PROGRESS;
                                        break;
                                    case "3":
                                        status = TaskStatus.DONE;
                                        break;
                                    default:
                                        System.out.println("ошибка...");
                                }
                            }
                            Task testTask = new Task(name, description);
                            System.out.println("Введите номер задачи");
                            identifier = Integer.parseInt(scanner.nextLine());
                            testTask.setId(identifier);
                            testTask.setStatus(status);
                            manager.updateTask(testTask);
                            break;
                        case 7:
                            System.out.println("Введите номер удаляемого объекта");
                            identifier = Integer.parseInt(scanner.nextLine());
                            manager.removeTask(identifier);
                            break;
                        default:
                            System.out.println("Такой команды нет");
                    }
                    break;
                case 2:
                    printMenuInCase();
                    userInput = Integer.parseInt(scanner.nextLine());
                    switch (userInput) {
                        case 1:
                            manager.addNewSubtask(new Subtask("Купить овощи",
                                    "Купить кабачки, огурцы, помидоры", 1));
                            manager.addNewSubtask(new Subtask("Купить фрукты",
                                    "Купить яблоки, бананы", 1));
                            manager.addNewSubtask(new Subtask("Собрать вещи",
                                    "Нужно забрать посуду и кухонный стол", 2));
                            break;
                        case 2:
                            System.out.println(manager.getSubtasks());
                            break;
                        case 3:
                            manager.deleteSubtasks();
                            break;
                        case 4:
                            System.out.println("Введите номер объекта");
                            identifier = Integer.parseInt(scanner.nextLine());
                            System.out.println(manager.getSubtask(identifier));
                            break;
                        case 5:
                            System.out.println("Введите название подзадачи");
                            name = scanner.nextLine();
                            System.out.println("Введите описание подзадачи");
                            description = scanner.nextLine();
                            System.out.println("Введите номер эпика к которому относится подзадача");
                            epicId = Integer.parseInt(scanner.nextLine());
                            manager.addNewSubtask(new Subtask(name, description, epicId));
                            break;
                        case 6:
                            System.out.println("Введите название задачи");
                            name = scanner.nextLine();
                            System.out.println("Введите описание задачи");
                            description = scanner.nextLine();
                            System.out.println("Введите  статус задачи: 1 - NEW, 2 - IN_PROGRESS, 3 - DONE");
                            status = null;
                            while (status == null) {
                                statusIn = scanner.nextLine();
                                switch (statusIn) {
                                    case "1":
                                        status = TaskStatus.NEW;
                                        break;
                                    case "2":
                                        status = TaskStatus.IN_PROGRESS;
                                        break;
                                    case "3":
                                        status = TaskStatus.DONE;
                                        break;
                                    default:
                                        System.out.println("ошибка...");
                                }
                            }
                            System.out.println("Введите номер эпика к которому относится подзадача");
                            epicId = Integer.parseInt(scanner.nextLine());
                            Subtask testSubtask = new Subtask(name, description, epicId);
                            System.out.println("Введите номер подзадачи");
                            identifier = Integer.parseInt(scanner.nextLine());
                            testSubtask.setId(identifier);
                            testSubtask.setStatus(status);
                            manager.updateSubtask(testSubtask);
                            break;
                        case 7:
                            System.out.println("Введите номер удаляемого объекта");
                            identifier = Integer.parseInt(scanner.nextLine());
                            manager.removeSubtask(identifier);
                            break;
                        default:
                            System.out.println("Такой команды нет");
                    }
                    break;
                case 3:
                    printMenuInCase();
                    System.out.println("8 - Получение списка всех подзадач определённого эпика.");
                    userInput = Integer.parseInt(scanner.nextLine());
                    switch (userInput) {
                        case 1:
                            manager.addNewEpic(new Epic("Эпик№1", "Какое-то описание"));
                            manager.addNewEpic(new Epic("Эпик№2", "Какое-то описание"));
                            break;
                        case 2:
                            System.out.println(manager.getEpics());
                            break;
                        case 3:
                            manager.deleteEpics();
                            break;
                        case 4:
                            System.out.println("Введите номер объекта");
                            identifier = Integer.parseInt(scanner.nextLine());
                            System.out.println(manager.getEpic(identifier));
                            break;
                        case 5:
                            System.out.println("Введите название эпика");
                            name = scanner.nextLine();
                            System.out.println("Введите описание эпика");
                            description = scanner.nextLine();
                            manager.addNewEpic(new Epic(name, description));
                            break;
                        case 6:
                            System.out.println("Введите название эпика");
                            name = scanner.nextLine();
                            System.out.println("Введите описание эпика");
                            description = scanner.nextLine();
                            Epic testEpic = new Epic(name, description);
                            System.out.println("Введите номер эпика");
                            identifier = Integer.parseInt(scanner.nextLine());
                            testEpic.setId(identifier);
                            manager.updateEpics(testEpic);
                            break;
                        case 7:
                            System.out.println("Введите номер удаляемого объекта");
                            identifier = Integer.parseInt(scanner.nextLine());
                            manager.removeEpic(identifier);
                            break;
                        case 8:
                            System.out.println("Введите идентификатор эпика, подзадачи которого нужно вывести");
                            identifier = Integer.parseInt(scanner.nextLine());
                            System.out.println(manager.listOfEpicSubtasks(identifier));
                            break;
                        default:
                            System.out.println("Такой команды нет");
                    }
                    break;
                case 4:
                    System.out.println(manager.getHistory());
                    break;
                default:
                    System.out.println("Такой команды нет");
            }
        }
    }

    public static void printMenu() {
        System.out.println("0 - Выход\n" +
                "1 - Операции с задачами\n" +
                "2 - Операции с подзадачами\n" +
                "3 - Операции с эпиками\n" +
                "4 - История запросов");
    }

    public static void printMenuInCase() {
        System.out.println("1 - Быстрое сохранение задач для проверки(для проверки, как в совете из практикума :) )\n" +
                "2 - Получение списка всех задач\n" +
                "3 - Удаление всех задач\n" +
                "4 - Получение задачи по идентификатору\n" +
                "5 - Создание задачи\n" +
                "6 - Обновление задачи\n" +
                "7 - Удаление задачи по идентификатору");
    }
}