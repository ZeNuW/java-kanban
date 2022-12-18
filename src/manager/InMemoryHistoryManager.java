package manager;

import tasks.Task;

import java.util.*;


public class InMemoryHistoryManager implements HistoryManager {

    private final CustomLinkedList<Task> historyList = new CustomLinkedList<>();
    private final Map<Integer, Node<Task>> historyMap = new HashMap<>();

    @Override
    public void add(Task task) {
        if (task != null) {
            remove(task.getId());
            historyMap.put(task.getId(), historyList.linkLast(task));
        }
    }

    @Override
    public void remove(int id) {
        if (historyMap.containsKey(id)) {
            historyList.removeNode(historyMap.get(id));
            historyMap.remove(id);
            historyList.size--;
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyList.getTasks();
    }

    public static class CustomLinkedList<T> {
        public Node<T> head;
        public Node<T> tail;
        public int size = 0;

        public Node<T> linkLast(T element) {
            final Node<T> newNode = new Node<>(tail, element, null);
            if (head == null) {
                head = newNode;
            } else {
                tail.next = newNode;
            }
            tail = newNode;
            size++;
            return tail;
        }

        public ArrayList<T> getTasks() {
            Node<T> node = head;
            ArrayList<T> list = new ArrayList<>();
            while (node != null) {
                list.add(node.data);
                node = node.next;
            }
            return list;
        }

        public void removeNode(Node<T> node) {
            if (node.prev != null) {
                node.prev.next = node.next;
                if (node.next == null) {
                    tail = node.prev;
                } else {
                    node.next.prev = node.prev;
                }
            } else {
                head = node.next;
                if (head == null) {
                    tail = null;
                } else {
                    head.prev = null;
                }
            }
        }
    }
}
