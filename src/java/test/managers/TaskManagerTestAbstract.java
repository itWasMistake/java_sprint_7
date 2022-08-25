package test.managers;

import managers.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.EpicTask;
import task.SubTask;
import task.Task;
import task.TaskStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTestAbstract<T extends TaskManager> {

    protected T taskManager;
    protected Task task;
    protected EpicTask epicTask;
    protected SubTask subTask;

    @BeforeEach
    protected void taskManagerSetUp() {
        task = new Task("Name", "DESCRIPTION",
                1, TaskStatus.NEW, LocalDateTime.now(), LocalDateTime.now().plusMinutes(100),
                100L);

        epicTask = new EpicTask("Name", "DESCRIPTION",
                2, TaskStatus.NEW, LocalDateTime.now().plusMinutes(1), LocalDateTime.now().plusMinutes(101+1),
                101L);

        subTask = new SubTask("Name", "DESCRIPTION",
                3, TaskStatus.NEW, epicTask.getId(), LocalDateTime.now().plusMinutes(2), LocalDateTime.now().plusMinutes(102+2),
                102L);
    }

    @Test
    void getPrioritizedTasks() {
        taskManager.addTask(task);
        taskManager.addEpic(epicTask);
        taskManager.addSubTask(epicTask, subTask);


        assertNotNull(taskManager.getPrioritizedTasks());
        assertEquals(taskManager.getPrioritizedTasks().get(0), task);
        assertEquals(taskManager.getPrioritizedTasks().get(1), epicTask);
        assertEquals(taskManager.getPrioritizedTasks().get(2), subTask);
    }

    @Test
    void addTask() {
        taskManager.addTask(task);

        assertNotEquals(0, taskManager.getAllTasks().size());
        assertNotNull(task.getId());
        assertNotEquals(1000000000000000000L, task.getId());
        assertEquals(task, taskManager.getTaskById(task.getId()));
    }

    @Test
    void addEpic() {
        taskManager.addEpic(epicTask);

        assertNotEquals(0, taskManager.getAllEpics().size());
        assertNotNull(taskManager.getAllEpics());
        assertNotNull(epicTask.getId());
        assertNotEquals(1000000000000000000L, epicTask.getId());
        assertEquals(epicTask, taskManager.getEpicById(epicTask.getId()));
    }

    @Test
    void addSubTask() {

        taskManager.addSubTask(epicTask, subTask);
        int indexOfSub = epicTask.getSubTaskEpic().indexOf(subTask);

        assertNotEquals(0, taskManager.getEpicSubTask(epicTask).size());
        assertNotNull(taskManager.getEpicSubTask(epicTask));
        assertNotNull(subTask.getId());
        assertNotEquals(1000000000000000000L, subTask.getId());
        assertEquals(subTask, epicTask.getSubTaskEpic().get(indexOfSub));
    }

    @Test
    void delTask() {
        taskManager.addTask(task);
        taskManager.delTask(task.getId());

        assertNull(taskManager.getTaskById(task.getId()));

    }

    @Test
    void delEpic() {
        taskManager.addEpic(epicTask);
        taskManager.delEpic(epicTask.getId());

        assertNull(taskManager.getEpicById(epicTask.getId()));
    }

    @Test
    void delSubTask() {
        taskManager.addSubTask(epicTask, subTask);
        int indexOfSub = epicTask.getSubTaskEpic().indexOf(subTask);
        taskManager.delSubTask((long) indexOfSub, epicTask);

        assertTrue(taskManager.getEpicSubTask(epicTask).isEmpty());
    }

    @Test
    void updateTask() {
        taskManager.addTask(task);
        Task task2 = new Task("Name", "DESCRIPTION", taskManager.generateId(), TaskStatus.NEW);
        taskManager.updateTask(task2);

        assertEquals(task2, taskManager.getTaskById(task2.getId()));
    }

    @Test
    void updateEpic() {
        taskManager.addEpic(epicTask);
        assertEquals(epicTask, taskManager.getEpicById(epicTask.getId()));

        EpicTask epicTask1 = new EpicTask("Epic", "Updated", 1000L, TaskStatus.NEW);
        assertEquals("Updated", taskManager.getEpicById(1000L).getDescription());

    }

    @Test
    void updateSubTask() {
        taskManager.addSubTask(epicTask, subTask);
        SubTask subTask1 = new SubTask("Name", "Updated",
                12991299L, TaskStatus.NEW, epicTask.getId());

        taskManager.updateSubTask(subTask1, epicTask.getId());
        int indexOfSub = taskManager.getEpicSubTask(epicTask).indexOf(subTask1);

        Assertions.assertEquals("Updated", taskManager.getEpicSubTask(epicTask).get(indexOfSub).getDescription());
        Assertions.assertEquals(subTask1, taskManager.getEpicSubTask(epicTask).get(indexOfSub));
    }

    @Test
    void getEpicById() {
        taskManager.addEpic(epicTask);
        EpicTask epicTask1 = taskManager.getEpicById(epicTask.getId());
        assertEquals(epicTask, epicTask1);
    }

    @Test
    void getTaskById() {
        taskManager.addTask(task);
        Task task1 = taskManager.getTaskById(task.getId());

        assertEquals(task, task1);
    }

    @Test
    void getSubTaskByID() {
        taskManager.addEpic(epicTask);
        taskManager.addSubTask(epicTask, subTask);
        int indexOfSub = epicTask.getSubTaskEpic().indexOf(subTask);
        SubTask subTask1 = taskManager.getSubTaskByID(epicTask.getId(), indexOfSub);
        assertEquals(subTask, subTask1);
    }

    @Test
    void getAllTasks() {
        taskManager.addTask(task);
        Map<Long, Task> taskMap = taskManager.getAllTasks();


        assertNotNull(taskManager.getAllTasks());
        assertEquals(taskMap, taskManager.getAllTasks());
        assertEquals(task, taskManager.getAllTasks().get(task.getId()));
        assertEquals(1, taskManager.getAllTasks().size());
    }

    @Test
    void getAllEpics() {
        taskManager.addEpic(epicTask);
        Map<Long, EpicTask> epicTaskMap = taskManager.getAllEpics();


        assertNotNull(taskManager.getAllEpics());
        assertEquals(epicTaskMap, taskManager.getAllEpics());
        assertEquals(epicTask, taskManager.getAllEpics().get(epicTask.getId()));
        assertEquals(1, taskManager.getAllEpics().size());
    }

    @Test
    void getEpicSubTask() {
        taskManager.addEpic(epicTask);
        taskManager.addSubTask(epicTask, subTask);
        int indexInList = taskManager.getEpicSubTask(epicTask).indexOf(subTask);
        List<SubTask> subTaskList = taskManager.getEpicSubTask(epicTask);


        assertNotNull(taskManager.getEpicSubTask(epicTask));
        assertEquals(subTaskList, taskManager.getEpicSubTask(epicTask));
        assertEquals(subTask, taskManager.getEpicSubTask(epicTask).get(indexInList));
        assertEquals(1, taskManager.getEpicSubTask(epicTask).size());
    }


    @Test
    void generateId() {
        Task task1 = new Task("Name", "DESCRIPTION", taskManager.generateId(), TaskStatus.NEW);
        Task task2 = new Task("Name", "DESCRIPTION", taskManager.generateId(), TaskStatus.NEW);
        Task task3 = new Task("Name", "DESCRIPTION", taskManager.generateId(), TaskStatus.NEW);


        assertEquals(1, task1.getId());
        assertEquals(2, task2.getId());
        assertEquals(3, task3.getId());
    }

    @Test
    void clearAllTasks() {
        taskManager.addTask(task);
        taskManager.addEpic(epicTask);
        taskManager.addSubTask(epicTask, subTask);
        taskManager.clearAllTasks();

        assertEquals(0, taskManager.getAllTasks().size());
        assertEquals(0, taskManager.getAllTasks().size());
    }

    @Test
    void getHistory() {
        List<Task> history = taskManager.getHistory();
        assertNotNull(history);
    }
}