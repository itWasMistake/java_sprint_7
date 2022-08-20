package managers;

import memory.InMemoryHistoryManager;
import memory.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import task.EpicTask;
import task.SubTask;
import task.Task;
import task.TaskStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {
    static InMemoryHistoryManager historyManager;

    static Task task;

    static EpicTask epicTask;

    static SubTask subTask;

    static InMemoryTaskManager taskManager;

    @BeforeAll
    public static void beforeAll() {
        historyManager = new InMemoryHistoryManager();
        taskManager = new InMemoryTaskManager();

        task = new Task("Name", "DESCRIPTION",
                1, TaskStatus.NEW);
        epicTask = new EpicTask("Name", "DESCRIPTION",
                2, TaskStatus.NEW);
        subTask = new SubTask("Name", "DESCRIPTION",
                3, TaskStatus.NEW, epicTask.getId());
    }

    @Test
    public void shouldCreateHistoryList() {
        List<Task> history = historyManager.getHistory();

        assertNotNull(history, "тут что-то есть....");
        assertTrue(history.isEmpty(), "пустовато....");

    }

    @Test
    public void shouldAddTaskInHistory() {
        historyManager.add(task);
        List<Task> history = historyManager.getHistory();
        assertNotNull(history);
        assertEquals(1, history.size());
    }

    @Test
    public void shouldNotAddTaskInHistoryTwice() {
        historyManager.add(task);
        historyManager.add(task);
        List<Task> history = historyManager.getHistory();
        assertNotNull(history);
        assertEquals(1, history.size());
    }

    @Test
    void removeFirst() {
        historyManager.add(task);
        historyManager.add(epicTask);
        historyManager.add(subTask);

        List<Task> history = historyManager.getHistory();
        assertNotNull(history);
        assertEquals(3, history.size());

        historyManager.remove(task.getId());
        history = historyManager.getHistory();
        assertNotNull(history);
        assertEquals(2, history.size());
        assertEquals(subTask, history.get(0));
        assertEquals(epicTask, history.get(1));

    }

    @Test
    void removeMiddle() {
        historyManager.add(task);
        historyManager.add(epicTask);
        historyManager.add(subTask);

        List<Task> history = historyManager.getHistory();
        assertNotNull(history);
        assertEquals(3, history.size());
        historyManager.remove(2L);

        history = historyManager.getHistory();
        assertNotNull(history);
        assertEquals(2, history.size());
        assertEquals(task, history.get(0));
        assertEquals(subTask, history.get(1));
    }

    @Test
    void removeLast() {
        historyManager.add(task);
        historyManager.add(epicTask);
        historyManager.add(subTask);

        List<Task> history = historyManager.getHistory();
        assertNotNull(history);
        assertEquals(3, history.size());

        historyManager.remove(subTask.getId());
        history = historyManager.getHistory();
        assertNotNull(history);
        assertEquals(2, history.size());
        assertEquals(task, history.get(0));
        assertEquals(epicTask, history.get(1));
    }

    @Test
    void removeSingle() {
        historyManager.add(task);
        List<Task> history = historyManager.getHistory();
        assertNotNull(history);
        assertEquals(1, history.size());

        historyManager.remove(task.getId());
        history = historyManager.getHistory();
        assertNotNull(history);
        assertEquals(0, history.size());


    }
}