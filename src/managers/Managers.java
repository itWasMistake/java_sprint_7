package managers;

import memory.InMemoryHistoryManager;
import memory.InMemoryTaskManager;

public class Managers {


    public static FileBackedTasksManager getDefault() {
        return new FileBackedTasksManager();
    }

    public static InMemoryHistoryManager getHistoryDefault() {
        return new InMemoryHistoryManager();
    }
}
