package memory;

import managers.HistoryManager;
import task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class InMemoryHistoryManager implements HistoryManager {

    private Node first;
    private Node last;
    final HashMap<Long, Node> nodeHashMap = new HashMap<>();
    final CustomLinkedList customLinkedList = new CustomLinkedList();

    private ArrayList<Task> history;

    @Override
    public List<Task> getHistory() {
        return customLinkedList.getTasks();
    }

    @Override
    public void remove(Long id) {
        Node node = nodeHashMap.get(id);
        customLinkedList.removeNode(node);
        nodeHashMap.remove(id);
    }

    @Override
    public void add(Task task) {
        if (Objects.isNull(task)) {
            return;
        }
        customLinkedList.linkLast(task);


    }

    @Override
    public String toString() {
        return "InMemoryHistoryManager{" + "history=" + customLinkedList.getTasks() + '}';
    }


    public static class Node {
        Task data;
        Node prev;
        Node next;


        public Node(Node prev, Task data, Node next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }

        public Task getData() {
            return data;
        }

        public void setData(Task data) {
            this.data = data;
        }

        public Node getPrev() {
            return prev;
        }

        public void setPrev(Node prev) {
            this.prev = prev;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }

        @Override
        public String toString() {
            return "Node{" + "data=" + data + ", prev=" + prev + ", next=" + next + '}';
        }
    }


    public class CustomLinkedList {

        private void linkLast(Task task) {
            if (!nodeHashMap.containsKey(task.getId())) {
                final Node oldLast = last;
                final Node newNode = new Node(oldLast, task, null);
                last = newNode;
                if (oldLast == null) {
                    first = newNode;
                    nodeHashMap.put(task.getId(), newNode);
                } else {
                    oldLast.next = newNode;
                    nodeHashMap.put(task.getId(), newNode);
                }
                last = newNode;
            } else {
                return;
            }
        }


        private List<Task> getTasks() {
            history = new ArrayList<>();
            Node node = first;
            while (node != null) {
                history.add(node.data);
                node = node.next;
            }
            return history;
        }


        private void removeNode(Node node) {
            if (node == null) {
                return;
            }

            if (node.next != null && node.prev != null) {
                // середина
                node.prev.next = node.next;
                node.next.prev = node.prev;
            }

            if (node.next == null && node.prev != null) {
                // node == tail
                // tail
                node.prev.next = null;
                last = node.prev;
            }

            if (node.next != null && node.prev == null) {
                // node == head
                // head
                node.next.prev = null;
                first = node.next;
            }

            if (node.next == null && node.prev == null) {
                // node == tail && node == head
                first = null;
                last = null;
            }

        }
    }
}

