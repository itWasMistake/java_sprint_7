package tests;

import managers.HistoryManager;
import memory.InMemoryHistoryManager;
import memory.InMemoryTaskManager;
import org.junit.jupiter.api.*;
import task.EpicTask;
import task.SubTask;
import task.Task;
import task.TaskStatus;

public class HistoryTest<T extends HistoryManager> {
    static InMemoryHistoryManager historyManager;

    static Task task;

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
        epicTask = new EpicTask("Name", "DESCRIPTION",
                50L, TaskStatus.NEW);
        subTask = new SubTask("Name", "DESCRIPTION",
                1L, TaskStatus.NEW, epicTask.getId());
        taskManager.addEpic(epicTask);
        taskManager.addTask(task);
        taskManager.addSubTask(epicTask, subTask);

    }

    @Test
    public void shouldBeReturnHistoryList() {

        taskManager.getTaskById(task.getId());
        taskManager.getEpicById(epicTask.getId());
        taskManager.getSubTaskByID(epicTask.getId(), epicTask.getSubTaskEpic().indexOf(subTask));
        Assertions.assertFalse(historyManager.getHistory().isEmpty());
        Assertions.assertNotNull(historyManager.getHistory());

    }

    @Test
    public void shouldRemoveTaskFromHistory() {

        int indexInList = historyManager.getHistory().indexOf(task);

        historyManager.remove(task.getId());

        Assertions.assertTrue(historyManager.getHistory().isEmpty());

    }

    @Test
    public void shouldAddTaskInHistory() {
        taskManager.getTaskById(task.getId());
        taskManager.getEpicById(epicTask.getId());
        taskManager.getSubTaskByID(epicTask.getId(), epicTask.getSubTaskEpic().indexOf(subTask));

        Assertions.assertFalse(historyManager.getHistory().isEmpty());

    }
}
