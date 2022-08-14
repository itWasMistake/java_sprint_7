package tests;

import managers.HistoryManager;
import memory.InMemoryHistoryManager;
import memory.InMemoryTaskManager;
import org.junit.jupiter.api.*;
import task.EpicTask;
import task.SubTask;
import task.Task;
import task.TaskStatus;

import java.util.List;

public class HistoryTest<T extends HistoryManager> {
    static InMemoryHistoryManager historyManager;

    static Task task;

    static  Task taskOne;
    static EpicTask epicTask;

    static SubTask subTask;

    static InMemoryTaskManager taskManager;

    @BeforeAll
    public static void beforeAll() {
        historyManager = new InMemoryHistoryManager();

        taskManager = new InMemoryTaskManager();
    }

    @AfterEach
    void afterEach() {
        historyManager.getHistory().clear();
    }

    @BeforeEach
    void beforeEach() {


        task = new Task("Name", "DESCRIPTION",
                1900L, TaskStatus.NEW);
        taskOne = new Task("Name", "DESCRIPTION",
                190110L, TaskStatus.NEW);
        epicTask = new EpicTask("Name", "DESCRIPTION",
                50L, TaskStatus.NEW);
        subTask = new SubTask("Name", "DESCRIPTION",
                1L, TaskStatus.NEW, epicTask.getId());
    }

    @Test
    public void shouldCreateHistoryList() {
        List<Task> history = historyManager.getHistory();

        Assertions.assertNotNull(history, "тут что-то есть....");
        Assertions.assertTrue(history.isEmpty(), "пустовато....");

    }
    @Test
    public void shouldAddTaskInHistory() {
        historyManager.add(task);
        List<Task> history = historyManager.getHistory();
        Assertions.assertNotNull(history);
        Assertions.assertEquals(1, history.size());
    }

    @Test
    public void shouldNotAddTwice() {
        historyManager.add(task);
        historyManager.add(task);
        List<Task> history = historyManager.getHistory();
        Assertions.assertNotNull(history);
        Assertions.assertEquals(1, history.size());
    }


    @Test
    public void shouldRemoveTaskFromHistory() {

        int indexInList = historyManager.getHistory().indexOf(task);

        historyManager.remove(task.getId());

        Assertions.assertTrue(historyManager.getHistory().isEmpty());

    }
}
