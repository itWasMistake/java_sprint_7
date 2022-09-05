package main.java.http;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import main.java.client.KVTaskClient;
import main.java.managers.FileBackedTasksManager;
import main.java.managers.Managers;
import main.java.task.EpicTask;
import main.java.task.SubTask;
import main.java.task.Task;
import main.java.task.TaskStatus;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class HttpTaskManager extends FileBackedTasksManager {

    private static final String TASKS_KEY = "tasks";
    private static final String EPICS_KEY = "epics";
    private static final String HISTORY_KEY = "history";
    private KVTaskClient client;
    private Gson gson;

    public HttpTaskManager(int port) {
        gson = Managers.getGson();
        client = new KVTaskClient(port);
    }

    protected void save() {
        String tasks = gson.toJson(getAllTasks());
        String epics = gson.toJson(getAllEpics());
        List<Long> history = getHistory().stream().map(Task::getId).collect(toList());
        String historyJson = gson.toJson(history);
        client.put(HISTORY_KEY, historyJson);
        client.put(TASKS_KEY, tasks);
        client.put(EPICS_KEY, epics);
    }

    protected void load() {
        Type tasksType = new TypeToken<ArrayList<Task>>() {

        }.getType();
        Type epicsType = new TypeToken<ArrayList<EpicTask>>() {

        }.getType();


        ArrayList<Task> tasks = gson.fromJson(client.load(EPICS_KEY), epicsType);
        ArrayList<EpicTask> epicTasks = gson.fromJson(client.load(TASKS_KEY), tasksType);


        tasks.forEach(task -> {
            Long id = task.getId();
            String name = task.getName();
            String description = task.getDescription();
            TaskStatus status = task.getStatus();
            LocalDateTime startTime = task.getStartTime();
            LocalDateTime endTime = task.getEndTime();
            long duration = task.getDuration();
            this.getPrioritizedTasks().add(task);
            this.addTask(new Task(name, description, id, status, startTime, endTime, duration));
        });
        epicTasks.forEach(epicTask -> {
            Long id = epicTask.getId();
            String name = epicTask.getName();
            String description = epicTask.getDescription();
            TaskStatus status = epicTask.getStatus();
            LocalDateTime startTime = epicTask.getStartTime();
            LocalDateTime endTime = epicTask.getEndTime();
            long duration = epicTask.getDuration();
            List<SubTask> subTaskArrayList = epicTask.getSubTaskEpic();
            this.getPrioritizedTasks().add(epicTask);
            EpicTask epic = new EpicTask(name, description, id, status, startTime, endTime, duration);
            this.addEpic(epicTask);
        });
        Type historyType = new TypeToken<ArrayList<Long>>() {

        }.getType();
        ArrayList<Long> history = gson.fromJson(client.load(HISTORY_KEY), historyType);
        for (Long id : history) {
            this.getHistory().add(this.getTaskById(id));
        }
    }
}
