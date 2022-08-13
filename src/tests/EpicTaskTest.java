package tests;

import memory.InMemoryTaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.EpicTask;
import task.SubTask;
import task.TaskStatus;


class EpicTaskStatusTest {

    // Создание окружения
    static EpicTask epicWithoutSubTasks;
    static EpicTask epicTask;

    static InMemoryTaskManager taskManager;

    static SubTask subTask0;
    static SubTask subTask2;
    static SubTask subTask3;
    static SubTask subTask4;

    @BeforeAll
    static void beforeAll() {
        taskManager = new InMemoryTaskManager();
    }

    @BeforeEach
    public void beforeEach() {
        epicTask = new EpicTask("NAME", "DESCRIPTION", taskManager.generateId(),
                TaskStatus.NEW);

        epicWithoutSubTasks = new EpicTask("NAME1", "DESCRIPTION", taskManager.generateId(),
                TaskStatus.NEW);

        subTask0 = new SubTask("NAME1", "DESCRIPTION1", taskManager.generateId(),
                TaskStatus.NEW, epicTask.getId());
        subTask2 = new SubTask("NAME2", "DESCRIPTION2", taskManager.generateId(),
                TaskStatus.NEW, epicTask.getId());
        subTask3 = new SubTask("NAME3", "DESCRIPTION3", taskManager.generateId(),
                TaskStatus.NEW, epicTask.getId());
        subTask4 = new SubTask("NAME4", "DESCRIPTION4", taskManager.generateId(),
                TaskStatus.NEW, epicTask.getId());

        epicTask.addSubTask(subTask0);
        epicTask.addSubTask(subTask2);
        epicTask.addSubTask(subTask3);
        epicTask.addSubTask(subTask4);
    }

    // Проверка статуса эпика при пустом списке подзадач

    @Test
    public void listOfSubTasksAreEmpty() {
        boolean mustBeTrue = epicWithoutSubTasks.getSubTaskEpic().isEmpty();
        Assertions.assertTrue(mustBeTrue);
        Assertions.assertEquals(epicWithoutSubTasks.getStatus(), TaskStatus.NEW);
    }

    // Проверка статуса эпика когда все позадачи со статусом NEW

    @Test
    public void allSubTasksAreNew() {

        subTask0.setStatus(TaskStatus.NEW);
        subTask2.setStatus(TaskStatus.NEW);
        subTask3.setStatus(TaskStatus.NEW);
        subTask4.setStatus(TaskStatus.NEW);

        Assertions.assertEquals(TaskStatus.NEW, epicTask.getStatus());
    }

    // Проверка статуса эпика когда все подзадачи со статусом DONE

    @Test
    public void allSubTasksAreDone() {


        subTask0.setStatus(TaskStatus.DONE);
        subTask2.setStatus(TaskStatus.DONE);
        subTask3.setStatus(TaskStatus.DONE);
        subTask4.setStatus(TaskStatus.DONE);


        Assertions.assertEquals(TaskStatus.DONE, epicTask.getStatus());
    }

    // Проверка статуса эпика когда все задачи со статусом IN_PROGRESS

    @Test
    public void allSubTasksAreInProgress() {

        subTask0.setStatus(TaskStatus.IN_PROGRESS);
        subTask2.setStatus(TaskStatus.IN_PROGRESS);
        subTask3.setStatus(TaskStatus.IN_PROGRESS);
        subTask4.setStatus(TaskStatus.IN_PROGRESS);

        Assertions.assertEquals(TaskStatus.IN_PROGRESS, epicTask.getStatus());
    }


    @Test
    public void nonSuitable() {
        subTask0.setStatus(TaskStatus.IN_PROGRESS);
        subTask2.setStatus(TaskStatus.DONE);
        subTask3.setStatus(TaskStatus.IN_PROGRESS);
        subTask4.setStatus(TaskStatus.IN_PROGRESS);

        Assertions.assertEquals(TaskStatus.IN_PROGRESS, epicTask.getStatus());
    }
}