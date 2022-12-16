package manager;

import tasks.Task;

import java.util.*;


public class InMemoryHistoryManager implements HistoryManager {

    private static final CustomLinkedList<Task> historyList = new CustomLinkedList<>();
    private static final Map<Integer, Node<Task>> historyMap = new HashMap<>();


    @Override
    public void add(Task task) {
        this.remove(task.getId());
        historyMap.put(task.getId(), historyList.linkLast(task));
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
            final Node<T> oldTail = tail;
            final Node<T> newNode = new Node<>(element);
            newNode.prev = tail;
            tail = newNode;
            if (oldTail == null)
                head = newNode;
            else
                oldTail.next = newNode;
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
            if (historyList.head == historyList.tail) {
                historyList.head = null;
                historyList.tail = null;
            } else if (node.prev == null) {
                node.next.prev = null;
                head = node.next;
            } else if (node.next == null) {
                node.prev.next = null;
                tail = node.prev;
            } else {
                node.prev.next = node.next;
                node.next.prev = node.prev;
            }
        }
    }
}
