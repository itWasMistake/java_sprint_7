package memory;

import exceptionCustom.TaskValidateException;
import managers.Managers;
import managers.TaskManager;
import task.EpicTask;
import task.SubTask;
import task.Task;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {



    private final Map<Long, Task> tasks = new HashMap<>();
    private final Map<Long, EpicTask> epic = new HashMap<>();
    private final TreeSet<Task> prioritizedTasks = new TreeSet<Task>(Comparator.comparing(Task::getStartTime));

    private final InMemoryHistoryManager historyManager = Managers.getHistoryDefault();

    protected Long id = 0L;
    // Добавление задачи

    @Override
    public void addTask(Task task) {
        if (!tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
            prioritizedTasks.add(task);
        }

    }

    @Override
    public void addEpic(EpicTask epicTask) {
        if (!epic.containsKey(epicTask.getId())) {
            epic.put(epicTask.getId(), epicTask);
            prioritizedTasks.add(epicTask);
        }
    }

    @Override
    public void addSubTask(EpicTask epicTask, SubTask subTask) {
        epicTask.addSubTask(subTask);
        prioritizedTasks.add(subTask);

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
    private  void updateEpicDurationAndStartTime(Long epicId) {
        EpicTask epicTask = epic.get(epicId);
        List<SubTask> subTasks = epicTask.getSubTaskEpic();
        if (subTasks.isEmpty()) {
            epicTask.setDuration(0L);
            return;
        }
        LocalDateTime startEpic = LocalDateTime.MAX;
        LocalDateTime endEpic = LocalDateTime.MIN;
        long durationEpic = 0L;
        int subIndex;
        for (SubTask subTask : subTasks) {
            subIndex = subTasks.indexOf(subTask);
            SubTask currentSub = subTasks.get(subIndex);
            LocalDateTime startTime = currentSub.getStartTime();
            LocalDateTime endTime = currentSub.getEndTime();
            if (startTime.isBefore(startEpic)) {
                startEpic = startTime;
            }
            if (endTime.isBefore(endEpic)) {
                endEpic = endTime;
            }
            durationEpic += currentSub.getDuration();
        }
        epicTask.setStartTime(startEpic);
        epicTask.setEndTime(endEpic);
        epicTask.setDuration(durationEpic);
    }

    @Override
    public void updateSubTask(SubTask subTask, long epicId) {
        EpicTask epicTask = epic.get(epicId);
        epicTask.addSubTask(subTask);
        updateEpicDurationAndStartTime(epicId);
    }

    // Получение задачи по ID

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    public void validate(Task task) {
        LocalDateTime startTime = task.getStartTime();
        long duration = task.getDuration();
        LocalDateTime endTime = startTime.plusMinutes(duration);

        Integer count = prioritizedTasks.stream()
                .map(innerTask -> {
                    LocalDateTime thisStartTime = innerTask.getStartTime();
                    LocalDateTime thisEndTime = innerTask.getEndTime();
                    if (startTime.isBefore(thisStartTime) && endTime.isBefore(thisStartTime)) {
                        return 1;
                    }
                    if (startTime.isAfter(thisEndTime) && endTime.isAfter(thisEndTime)) {
                        return 1;
                    }
                    return 0;
                })
                .reduce(Integer::sum)
                .orElse(0);

        if (count == prioritizedTasks.size())
            return;

        if (prioritizedTasks.isEmpty())
            return;
        throw new TaskValidateException("Пересечение задачи");
    }


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
        return ++id;
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
