import manager.Manager;
import tasks.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        String name;
        String description;
        String status;

        Scanner scanner = new Scanner(System.in);
        Manager manager = new Manager();
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
                            manager.addNewTask(new Task("Забрать справки", "Нужно пойти в поликлинику", "NEW"));
                            manager.addNewTask(new Task("Забрать справки2", "Нужно пойти в поликлинику2", "DONE"));
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
                            System.out.println(manager.findTasksByIdentifier(identifier));
                            break;
                        case 5: //ввод где всё вводится "вручную"
                            System.out.println("Введите название задачи");
                            name = scanner.nextLine();
                            System.out.println("Введите описание задачи");
                            description = scanner.nextLine();
                            System.out.println("Введите новый статус задачи");
                            status = scanner.nextLine();
                            manager.addNewTask(new Task(name, description, status));
                            break;
                        case 6:
                            System.out.println("Введите название задачи");
                            name = scanner.nextLine();
                            System.out.println("Введите описание задачи");
                            description = scanner.nextLine();
                            System.out.println("Введите статус задачи");
                            status = scanner.nextLine();
                            manager.updateTask(new Task(name, description, status));
                            break;
                        case 7:
                            System.out.println("Введите номер удаляемого объекта");
                            identifier = Integer.parseInt(scanner.nextLine());
                            manager.removeTaskByIdentifier(identifier);
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
                            manager.addNewSubtask(new Subtask("Купить овощи", "Купить кабачки, огурцы, помидоры", "NEW",1));
                            manager.addNewSubtask(new Subtask("Купить фрукты", "Купить яблоки, бананы", "DONE",1));
                            manager.addNewSubtask(new Subtask("Собрать вещи", "Нужно забрать посуду и кухонный стол", "NEW",2));
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
                            System.out.println(manager.findSubtasksByIdentifier(identifier));
                            break;
                        case 5:
                            System.out.println("Введите название подзадачи");
                            name = scanner.nextLine();
                            System.out.println("Введите описание подзадачи");
                            description = scanner.nextLine();
                            System.out.println("Введите статус подзадачи");
                            status = scanner.nextLine();
                            System.out.println("Введите номер эпика к которому относится подзадача");
                            epicId = Integer.parseInt(scanner.nextLine());
                            manager.addNewSubtask(new Subtask(name, description, status,epicId));
                            break;
                        case 6:
                            System.out.println("Введите название задачи");
                            name = scanner.nextLine();
                            System.out.println("Введите описание задачи");
                            description = scanner.nextLine();
                            System.out.println("Введите статус задачи");
                            status = scanner.nextLine();
                            System.out.println("Введите номер эпика к которому относится подзадача");
                            epicId = Integer.parseInt(scanner.nextLine());
                            manager.updateSubtask(new Subtask(name, description, status, epicId));
                            break;
                        case 7:
                            System.out.println("Введите номер удаляемого объекта");
                            identifier = Integer.parseInt(scanner.nextLine());
                            manager.removeSubtaskByIdentifier(identifier);
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
                            System.out.println(manager.findEpicsByIdentifier(identifier));
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
                            manager.updateEpics(new Epic (name, description));
                            break;
                        case 7:
                            System.out.println("Введите номер удаляемого объекта");
                            identifier = Integer.parseInt(scanner.nextLine());
                            manager.removeEpicByIdentifier(identifier);
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
                default:
                    System.out.println("Такой команды нет");
            }
        }
    }

    public static void printMenu() {
        System.out.println("0 - Выход\n" +
                "1 - Операции с задачами\n" +
                "2 - Операции с подзадачами\n" +
                "3 - Операции с эпиками");
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