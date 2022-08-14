package memory;

import managers.HistoryManager;
import managers.Managers;
import managers.TaskManager;
import task.EpicTask;
import task.SubTask;
import task.Task;
import task.TaskToCSV;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {

    private final Map<Long, Task> tasks = new HashMap<>();
    private final Map<Long, EpicTask> epic = new HashMap<>();

    private final InMemoryHistoryManager historyManager = Managers.getHistoryDefault();

    // Добавление задачи

    @Override
    public void addTask(Task task) {
        if (!tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void addEpic(EpicTask epicTask) {
        if (!epic.containsKey(epicTask.getId())) {
            epic.put(epicTask.getId(), epicTask);

        }
    }

    @Override
    public void addSubTask(EpicTask epicTask, SubTask subTask) {
        epicTask.addSubTask(subTask);
    }

    // Удаление задачи

    @Override
    public void delTask(Long id) {
        tasks.remove(id);

    }

    @Override
    public void delEpic(Long id) {
        epic.remove(id);
    }

    @Override
    public void delSubTask(Long id, EpicTask epicTask) {
        epicTask.deleteSubTask(id);
    }

    // Обновление  задачи

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateEpic(EpicTask epicTask) {
        for (Long hashIds : epic.keySet()) {
            if (!(hashIds.equals(epicTask.getId()))) {
                epic.put(epicTask.getId(), epicTask);
            }
        }
    }

    @Override
    public void updateSubTask(SubTask subTask, long epicId) {
        EpicTask epicTask = epic.get(epicId);
        epicTask.addSubTask(subTask);
    }

    // Получение задачи по ID

    @Override
    public EpicTask getEpicById(long id) {
        historyManager.add(epic.get(id));
        return epic.get(id);
    }

    @Override
    public Task getTaskById(long id) {
        Task task = tasks.get(id);
        historyManager.add(task);
        return task;

    }
    @Override
    public SubTask getSubTaskByID(Long epicId, int subId) {
        EpicTask epicTask = epic.get(epicId);
        List<SubTask> subTasks = epicTask.getSubTaskEpic();
        SubTask subTask = subTasks.get(subId);
        historyManager.add(subTask);
        return subTask;
    }

    // Получение списка задач

    @Override
    public Map<Long, Task> getAllTasks() {
        return tasks;
    }

    @Override
    public Map<Long, EpicTask> getAllEpics() {
        return epic;
    }

    @Override
    public List<SubTask> getEpicSubTask(EpicTask epic) {
        return epic.getSubTaskEpic();
    }

    // Генерация ID

    @Override
    public Long generateId() {
        Long id = 1L;
        for (Task task : tasks.values()) {
            for (EpicTask epicTask : epic.values()) {
                for (SubTask subTask : epicTask.getSubTaskEpic()) {
                        if (task.getId().equals(id)) {
                            return ++id;
                        } else if (epicTask.getId().equals(id)) {
                            return ++id;
                        } else if (subTask.getId().equals(id)) {
                            return ++id;
                        } else {
                            return ++id;
                        }
                    }
                }
            }

        return id;
    }

    // Удаление всех задач

    @Override
    public void clearAllTasks() {
        epic.clear();
        tasks.clear();
    }

    // Получение истории

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

}
