package main.java.managers;

import main.java.task.Task;

import java.util.List;

public interface HistoryManager {

    List<Task> getHistory();

    void remove(Long id);

    void add(Task task);
}
