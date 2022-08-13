package managers;

import task.EpicTask;
import task.SubTask;
import task.Task;

import java.util.List;
import java.util.Map;

public interface TaskManager {

    // Добавление задачи

    void addTask(Task task);

    void addEpic(EpicTask epicTask);

    void addSubTask(EpicTask epicTask, SubTask subTask);

    // Удаление задачи
    void delTask(Long id);

    void delEpic(Long id);

    void delSubTask(Long id, EpicTask epicTask);

    // Обновление задачи
    void updateTask(Task task);

    void updateEpic(EpicTask epicTask);

    void updateSubTask(SubTask subTask, long subId);

    // Получение задачи по ID
    EpicTask getEpicById(long id);

    Task getTaskById(long id);

    SubTask getSubTaskByID(Long epicId, int subId);

    // Получение списка всех задач
    Map<Long, Task> getAllTasks();

    Map<Long, EpicTask> getAllEpics();

    List<SubTask> getEpicSubTask(EpicTask epic);

    // Генерация ID
    Long generateId();

    // Удаление всех задач
    void clearAllTasks();

    // Получение истории
    List<Task> getHistory();

}