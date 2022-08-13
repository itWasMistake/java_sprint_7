package tests;

import managers.FileBackedTasksManager;
import managers.TaskManager;
import memory.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.EpicTask;
import task.SubTask;
import task.Task;
import task.TaskStatus;
import org.junit.jupiter.api.Assertions;

import java.util.List;
import java.util.Map;

public class ManagerTest<T extends TaskManager> {

    // Создание окружения

    Task task;

    SubTask subTask;

    EpicTask epicTask;

    static InMemoryTaskManager inMemoryTaskManager;

    static FileBackedTasksManager fileBackedTasksManager;

    @BeforeAll
    public static void beforeAll() {
        inMemoryTaskManager = new InMemoryTaskManager();
    }

    @BeforeEach
    public void beforeEach() {
        task = new Task("Name", "DESCRIPTION",
                inMemoryTaskManager.generateId(), TaskStatus.NEW);
        epicTask = new EpicTask("Name", "DESCRIPTION",
                inMemoryTaskManager.generateId(), TaskStatus.NEW);
        subTask = new SubTask("Name", "DESCRIPTION",
                inMemoryTaskManager.generateId(), TaskStatus.NEW, epicTask.getId());
    }

    // Тесты на добавление задач

    @Test
    public void shouldAddTaskInMap() {
        inMemoryTaskManager.addTask(task);

        Assertions.assertNotEquals(0, inMemoryTaskManager.getAllTasks().size());
        Assertions.assertNotNull(task.getId());
        Assertions.assertNotEquals(1000000000000000000L, task.getId());
        Assertions.assertEquals(task, inMemoryTaskManager.getTaskById(task.getId()));
    }

    @Test
    public void shouldAddSubTaskInList() {
        inMemoryTaskManager.addSubTask(epicTask, subTask);
        int indexOfSub = epicTask.getSubTaskEpic().indexOf(subTask);

        Assertions.assertNotEquals(0, inMemoryTaskManager.getEpicSubTask(epicTask).size());
        Assertions.assertNotNull(inMemoryTaskManager.getEpicSubTask(epicTask));
        Assertions.assertNotNull(subTask.getId());
        Assertions.assertNotEquals(1000000000000000000L, subTask.getId());
        Assertions.assertEquals(subTask, epicTask.getSubTaskEpic().get(indexOfSub));
    }

    @Test
    public void shouldAddEpicTaskInMap() {
        inMemoryTaskManager.addEpic(epicTask);

        Assertions.assertNotEquals(0, inMemoryTaskManager.getAllEpics().size());
        Assertions.assertNotNull(inMemoryTaskManager.getAllEpics());
        Assertions.assertNotNull(epicTask.getId());
        Assertions.assertNotEquals(1000000000000000000L, epicTask.getId());
        Assertions.assertEquals(epicTask, inMemoryTaskManager.getEpicById(epicTask.getId()));

    }


    // Тесты на удаление задач

    @Test
    public void shouldDeleteTask() {
        inMemoryTaskManager.addTask(task);
        inMemoryTaskManager.delTask(task.getId());

        Assertions.assertNull(inMemoryTaskManager.getTaskById(task.getId()));


    }

    @Test
    public void shouldDeleteSubTask() {
        inMemoryTaskManager.addSubTask(epicTask, subTask);
        int indexOfSub = epicTask.getSubTaskEpic().indexOf(subTask);
        inMemoryTaskManager.delSubTask((long) indexOfSub, epicTask);

        Assertions.assertTrue(inMemoryTaskManager.getEpicSubTask(epicTask).isEmpty());
    }

    @Test
    public void shouldDeleteEpicTask() {
        inMemoryTaskManager.addEpic(epicTask);
        inMemoryTaskManager.delEpic(epicTask.getId());

        Assertions.assertNull(inMemoryTaskManager.getEpicById(epicTask.getId()));
    }

    // Тесты на обновление  задачи

    @Test
    public void shouldUpdateTask() {
        inMemoryTaskManager.addTask(task);
        Task task2 = new Task("Name", "DESCRIPTION", inMemoryTaskManager.generateId(), TaskStatus.NEW);
        inMemoryTaskManager.updateTask(task2);

        Assertions.assertEquals(task2, inMemoryTaskManager.getTaskById(task2.getId()));
    }

    @Test
    public void shouldUpdateSubTaskStatus() {
        inMemoryTaskManager.addSubTask(epicTask, subTask);
        SubTask subTask2 = new SubTask("Name", "DESCRIPTION",
                inMemoryTaskManager.generateId(), TaskStatus.NEW, epicTask.getId());

        inMemoryTaskManager.updateSubTask(subTask2, epicTask.getId());
        int indexOfSub = inMemoryTaskManager.getEpicSubTask(epicTask).indexOf(subTask2);

        Assertions.assertEquals(subTask2, inMemoryTaskManager.getEpicSubTask(epicTask).get(indexOfSub));
    }

    @Test
    public void shouldUpdateEpicStatus() {
        inMemoryTaskManager.addTask(task);
        final Task task2 = new Task("Name", "DESCRIPTION", inMemoryTaskManager.generateId(), TaskStatus.NEW);
        inMemoryTaskManager.updateTask(task2);

        Assertions.assertEquals(task2, inMemoryTaskManager.getTaskById(task2.getId()));
    }

    // Тесты на получение списка всех задач

    @Test
    public void shouldReturnAllTasksMap() {
        inMemoryTaskManager.addTask(task);
        Map<Long, Task> taskMap = inMemoryTaskManager.getAllTasks();


        Assertions.assertNotNull(inMemoryTaskManager.getAllTasks());
        Assertions.assertEquals(taskMap, inMemoryTaskManager.getAllTasks());
        Assertions.assertEquals(task, inMemoryTaskManager.getAllTasks().get(task.getId()));
        Assertions.assertEquals(2, inMemoryTaskManager.getAllTasks().size());

    }

    @Test
    public void shouldReturnAllEpicsMap() {
        inMemoryTaskManager.addEpic(epicTask);
        Map<Long, EpicTask> epicTaskMap = inMemoryTaskManager.getAllEpics();


        Assertions.assertNotNull(inMemoryTaskManager.getAllEpics());
        Assertions.assertEquals(epicTaskMap, inMemoryTaskManager.getAllEpics());
        Assertions.assertEquals(epicTask, inMemoryTaskManager.getAllEpics().get(epicTask.getId()));
        Assertions.assertEquals(2, inMemoryTaskManager.getAllEpics().size());
    }

    @Test
    public void shouldReturnAllSubTaskEpicList() {
        inMemoryTaskManager.addEpic(epicTask);
        inMemoryTaskManager.addSubTask(epicTask, subTask);
        int indexInList = inMemoryTaskManager.getEpicSubTask(epicTask).indexOf(subTask);
        List<SubTask> subTaskList = inMemoryTaskManager.getEpicSubTask(epicTask);


        Assertions.assertNotNull(inMemoryTaskManager.getEpicSubTask(epicTask));
        Assertions.assertEquals(subTaskList, inMemoryTaskManager.getEpicSubTask(epicTask));
        Assertions.assertEquals(subTask, inMemoryTaskManager.getEpicSubTask(epicTask).get(indexInList));
        Assertions.assertEquals(1, inMemoryTaskManager.getEpicSubTask(epicTask).size());
    }


    // Тест на удаление всех задач

    @Test
    public void shouldDeleteAllTasks() {
        inMemoryTaskManager.addTask(task);
        inMemoryTaskManager.addEpic(epicTask);
        inMemoryTaskManager.addSubTask(epicTask, subTask);
        inMemoryTaskManager.clearAllTasks();

        Assertions.assertEquals(0, inMemoryTaskManager.getAllTasks().size());
        Assertions.assertEquals(0, inMemoryTaskManager.getAllTasks().size());

    }

}


