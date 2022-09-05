package main.java.http;

import main.java.managers.Managers;
import main.java.server.KVServer;
import main.java.task.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.managers.TaskManagerTestAbstract;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class HttpTaskManagerTest extends TaskManagerTestAbstract<HttpTaskManager> {

    private KVServer kvServer;

    @BeforeEach
    void setUp() throws IOException {
        taskManager = new HttpTaskManager(KVServer.PORT);
        kvServer = Managers.getDefaultKvServer();
        taskManagerSetUp();
        kvServer.start();
    }

    @AfterEach
    void tearDown() {
        kvServer.stop();
    }

    @Test
    void save() {
    }

    @Test
    void load() {
        List<Task> tasks = new ArrayList<>(taskManager.getAllTasks().values());
        assertNotNull(tasks);
        assertEquals(1, tasks.size());
        assertEquals(task, tasks.get(0));
    }
}