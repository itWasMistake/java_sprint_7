package memory;


import managers.HistoryManager;
import task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class InMemoryHistoryManager implements HistoryManager {

    final HashMap<Long, Node<Task>> nodeHashMap = new HashMap<>();
    final CustomLinkedList customLinkedList = new CustomLinkedList();

    final List<Task> history = new ArrayList<>();

    @Override
    public List<Task> getHistory() {
        return history;
    }

    @Override
    public void add(Task task) {
        if (Objects.isNull(task)) {
            return;
        } else {
            customLinkedList.linkLast(task);

        }

    }


    @Override
    public String toString() {
        return "InMemoryHistoryManager{" + "history=" + customLinkedList.getTasks() + '}';
    }

    @Override
    public void remove(Long id) {
        customLinkedList.removeNode(id, nodeHashMap.get(id));
    }


public static class Node<Task> {
     Task task;
    Node<Task> prev;
    Node<Task> next;

    public Node(Node<Task> prev, Task task, Node<Task> next) {
        this.task = task;
        this.next = next;
        this.prev = prev;
    }
}


public class CustomLinkedList {

    private Node<Task> head;
    private Node<Task> tale;


    private void linkLast(Task task) {

        if (nodeHashMap.isEmpty()) {
            final Node<Task> emptyNode = new Node<>(tale, task, null);
            nodeHashMap.put(task.getId(), emptyNode);
        } else {
            final Node<Task> oldTale = tale;
            final Node<Task> newNode = new Node<>(oldTale, task, null);
            tale = newNode;
            nodeHashMap.put(task.getId(), tale);
            if (oldTale == null) {

                head = newNode;
                nodeHashMap.put(task.getId(), head);
            } else {
                oldTale.next = newNode;
            }
        }
    }

    private List<Task> getTasks() {
        Node<Task> oldHead = head;
        for (Node<Task> node : nodeHashMap.values()) {
            history.add(node.task);
        }
        return history;
    }

    private void removeNode(long id, Node<Task> taskNode) {
        if (taskNode == null) {
            return;
        } else {

            if (taskNode.prev == null || nodeHashMap.containsKey(id)) {
                Node<Task> oldHead = head;
                Node<Task> newNode = new Node<>(null, taskNode.task, taskNode.next);

                head = newNode;
                if (oldHead == null) {
                    tale = newNode;
                    nodeHashMap.remove(id);
                } else {
                    oldHead.prev = newNode;
                    nodeHashMap.remove(id);
                }
            } else if (taskNode.next == null || nodeHashMap.isEmpty()) {
                Node<Task> oldLast = tale;
                Node<Task> newNode = new Node<>(taskNode.prev, taskNode.task, null);
                tale = newNode;
                if (oldLast == null) {
                    head = newNode;
                    nodeHashMap.remove(id);
                } else {
                    oldLast.prev = newNode;
                    nodeHashMap.remove(id);
                }
            }
        }
    }
}
}

