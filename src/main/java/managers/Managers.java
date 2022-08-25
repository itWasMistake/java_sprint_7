package main.java.managers;

import main.java.memory.InMemoryHistoryManager;

public class Managers {


    public static FileBackedTasksManager getDefault() {
        return new FileBackedTasksManager();
    }

    public static InMemoryHistoryManager getHistoryDefault() {
        return new InMemoryHistoryManager();
    }
}
