package task;

import managers.HistoryManager;
import memory.InMemoryHistoryManager;
import task.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TaskToCSV {
    public static InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

    public static String toString(Task task) {
        StringBuilder sb = new StringBuilder();
        if (task.getType().equals(TaskType.TASK)) {
            sb.append(task.getId() + "," + task.getType() + "," + task.getName() + "," +
                    task.getStatus() + "," + task.getDescription());
            return sb.toString();
        } else if (task.getType().equals(TaskType.EPIC_TASK)) {
            sb.append(task.getId() + "," + task.getType() + "," + task.getName() + "," +
                    task.getStatus() + "," + task.getDescription());
            return sb.toString();
        } else {

            sb.append(task.getId() + "," + task.getType() + "," + task.getName() + "," +
                    task.getStatus() + "," + task.getDescription() + ",");
            return sb.toString();
        }
    }

    public static Task fromString(String value) {
        if (!Objects.isNull(value)) {
            String[] split = value.split(",");
            int id = Integer.parseInt(split[0]);
            String taskType = split[1];
            TaskType type = TaskType.valueOf(taskType);
            String name = split[2];
            TaskStatus status = TaskStatus.valueOf(split[3]);
            String description = split[4];
            if (type.equals(TaskType.TASK)) {
                return new Task(name, description, id, status);
            } else if (type.equals(TaskType.EPIC_TASK)) {
                return new EpicTask(name, description, id, status);
            } else {

                Long epicId = Long.parseLong(split[5]);
                return new SubTask(name, description, id, status, epicId);
            }
        } else {
            return null;
        }

    }

    public static String historyToString(List<Task> history) {
       String historyNumbers;
        for (Task tasks : history) {
           historyNumbers = tasks.getId().toString() + ",";
           return historyNumbers;
       }
        return null;
    }

    public static List<Long> historyFromString(String history) {
        List<Long> ids = new ArrayList<>();
        for (Task tasks : historyManager.getHistory()) {
            ids.add(tasks.getId());
        }
        return ids;
    }

}
