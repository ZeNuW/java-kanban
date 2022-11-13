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
                            manager.setNewTask(new Task("Забрать справки", "Нужно пойти в поликлинику", "NEW"));
                            manager.setNewTask(new Task("Забрать справки", "Нужно пойти в поликлинику", "DONE"));
                            break;
                        case 2:
                            System.out.println(manager.getTasks());
                            break;
                        case 3:
                            manager.clearTasks();
                            break;
                        case 4:
                            System.out.println("Введите номер объекта");
                            identifier = Integer.parseInt(scanner.nextLine());
                            manager.findTasksByIdentifier(identifier);
                            break;
                        case 5: //ввод где всё вводится "вручную"
                            System.out.println("Введите название задачи");
                            name = scanner.nextLine();
                            System.out.println("Введите описание задачи");
                            description = scanner.nextLine();
                            System.out.println("Введите статус задачи");
                            status = scanner.nextLine();
                            manager.setNewTask(new Task(name, description, status));
                            break;
                        case 6:
                            System.out.println("Введите номер обновляемого объекта");
                            identifier = Integer.parseInt(scanner.nextLine());
                            System.out.println("Введите название задачи");
                            name = scanner.nextLine();
                            System.out.println("Введите описание задачи");
                            description = scanner.nextLine();
                            System.out.println("Введите статус задачи");
                            status = scanner.nextLine();
                            manager.updateTask(identifier, new Task(name, description, status));
                            break;
                        case 7:
                            System.out.println("Введите номер удаляемого объекта");
                            identifier = Integer.parseInt(scanner.nextLine());
                            manager.removeTasksByIdentifier(identifier);
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
                            manager.setNewSubtask(new Subtask("Купить овощи", "Купить кабачки, огурцы, помидоры", "NEW"));
                            manager.setNewSubtask(new Subtask("Купить фрукты", "Купить яблоки, бананы", "DONE"));
                            manager.setNewSubtask(new Subtask("Собрать вещи", "Нужно забрать посуду и кухонный стол", "NEW"));
                            break;
                        case 2:
                            System.out.println(manager.getSubtasks());
                            break;
                        case 3:
                            manager.clearSubtasks();
                            break;
                        case 4:
                            System.out.println("Введите номер объекта");
                            identifier = Integer.parseInt(scanner.nextLine());
                            manager.findSubtasksByIdentifier(identifier);
                            break;
                        case 5:
                            System.out.println("Введите название подзадачи");
                            name = scanner.nextLine();
                            System.out.println("Введите описание подзадачи");
                            description = scanner.nextLine();
                            System.out.println("Введите статус подзадачи");
                            status = scanner.nextLine();
                            manager.setNewSubtask(new Subtask(name, description, status));
                            break;
                        case 6:
                            System.out.println("Введите номер обновляемого объекта");
                            identifier = Integer.parseInt(scanner.nextLine());
                            System.out.println("Введите название задачи");
                            name = scanner.nextLine();
                            System.out.println("Введите описание задачи");
                            description = scanner.nextLine();
                            System.out.println("Введите статус задачи");
                            status = scanner.nextLine();
                            manager.updateSubtask(identifier, new Subtask(name, description, status));
                            break;
                        case 7:
                            System.out.println("Введите номер удаляемого объекта");
                            identifier = Integer.parseInt(scanner.nextLine());
                            manager.removeSubtasksByIdentifier(identifier);
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
                            manager.setNewEpic("Эпик№1", "Какое-то описание");
                            break;
                        case 2:
                            System.out.println(manager.getEpics());
                            break;
                        case 3:
                            manager.clearEpics();
                            break;
                        case 4:
                            System.out.println("Введите номер объекта");
                            identifier = Integer.parseInt(scanner.nextLine());
                            manager.findEpicsByIdentifier(identifier);
                            break;
                        case 5:
                            System.out.println("Введите название эпика");
                            name = scanner.nextLine();
                            System.out.println("Введите описание эпика");
                            description = scanner.nextLine();
                            manager.setNewEpic(name, description);
                            break;
                        case 6:
                            System.out.println("Введите номер обновляемого эпика");
                            identifier = Integer.parseInt(scanner.nextLine());
                            System.out.println("Введите название эпика");
                            name = scanner.nextLine();
                            System.out.println("Введите описание эпика");
                            description = scanner.nextLine();
                            manager.updateEpics(identifier, name, description, manager.getSubtasks());
                            break;
                        case 7:
                            System.out.println("Введите номер удаляемого объекта");
                            identifier = Integer.parseInt(scanner.nextLine());
                            manager.removeEpicsByIdentifier(identifier);
                            break;
                        case 8:
                            System.out.println("Введите идентификатор эпика, подзадачи которого нужно вывести");
                            identifier = Integer.parseInt(scanner.nextLine());
                            manager.listOfEpicSubtasks(identifier);
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