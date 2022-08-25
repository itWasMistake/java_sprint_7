package java.managers;

import main.java.managers.FileBackedTasksManager;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import main.java.task.Task;

import java.io.File;
import java.util.Map;

class FileBackedTasksManagerTestAbstract extends TaskManagerTestAbstract<FileBackedTasksManager> {

    private File file;


    @BeforeEach
    void setUp() {
        file = new File("resources/tasks.csv");
        taskManager = new FileBackedTasksManager();
    }

    @BeforeAll
    static void beforeAll() {

    }


    @Test
    void save() {

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
    void getHistory() {
    }

    @Test
    void loadFromFile() throws Exception {
        FileBackedTasksManager tasksManager = FileBackedTasksManager.loadFromFile("resources/tasks.csv");
        Map<Long, Task> taskMap = tasksManager.getAllTasks();

        assertNotNull(taskMap);
        assertEquals(5, taskMap.size());


    }
    @Disabled
    @Test
    void getPrioritizedTasks() {
    }
}