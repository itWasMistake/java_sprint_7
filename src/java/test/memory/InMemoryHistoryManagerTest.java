package test.memory;

import memory.InMemoryHistoryManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.EpicTask;
import task.SubTask;
import task.Task;
import task.TaskStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    InMemoryHistoryManager historyManager;

    Task task;

    EpicTask epicTask;

    SubTask subTask;


    @BeforeEach
    public void beforeEach() {
        historyManager = new InMemoryHistoryManager();


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
        assertNotNull(history);
        assertTrue(history.isEmpty());

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
        assertEquals(subTask, history.get(1));
        assertEquals(epicTask, history.get(0));

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
    @Test
    void expectedException() {
        NumberFormatException thrown = Assertions.assertThrows(
                NumberFormatException.class, () -> Integer.parseInt("One"),
                "NumberFormatException was expected");
        Assertions.assertEquals("For input string: \"One\"", thrown.getMessage());

    }
}