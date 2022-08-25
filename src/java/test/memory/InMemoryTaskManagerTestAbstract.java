package test.memory;


import memory.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test.managers.TaskManagerTestAbstract;

class InMemoryTaskManagerTestAbstract extends TaskManagerTestAbstract<InMemoryTaskManager> {

    @BeforeEach
    void setUp() {
        taskManager = new InMemoryTaskManager();
        taskManagerSetUp();
    }

    @Test
    void addTask() {
    }

    @Test
    void addEpic() {
    }

    @Test
    void addSubTask() {
    }

    @Test
    void delTask() {
    }

    @Test
    void delEpic() {
    }

    @Test
    void delSubTask() {
    }

    @Test
    void updateTask() {
    }

    @Test
    void updateEpic() {
    }

    @Test
    void updateSubTask() {
    }

    @Test
    void getEpicById() {
    }

    @Test
    void getTaskById() {
    }

    @Test
    void getSubTaskByID() {
    }

    @Test
    void getAllTasks() {
    }

    @Test
    void getAllEpics() {
    }

    @Test
    void getEpicSubTask() {
    }

    @Test
    void generateId() {
    }

    @Test
    void clearAllTasks() {
    }

    @Test
    void getHistory() {
    }
}