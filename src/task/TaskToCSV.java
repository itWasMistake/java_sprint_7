package task;

import memory.InMemoryHistoryManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TaskToCSV {
    public static InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

    public static String toString(Task task) {
        StringBuilder sb = new StringBuilder();
        if (task.getType().equals(TaskType.TASK)) {
            sb.append(task.getId()).append(",").append(task.getType()).append(",").append(task.getName()).append(",")
                    .append(task.getStatus()).append(",").append(task.getDescription());
            return sb.toString();
        } else if (task.getType().equals(TaskType.EPIC_TASK)) {
            sb.append(task.getId()).append(",").append(task.getType()).append(",").append(task.getName()).append(",")
                    .append(task.getStatus()).append(",").append(task.getDescription());
            return sb.toString();
        } else {
            sb.append(task.getId()).append(",").append(task.getType()).append(",").append(task.getName()).append(",")
                    .append(task.getStatus()).append(",").append(task.getDescription()).append(",");
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
        StringBuilder stringBuilder = new StringBuilder();

        for (Task tasks : history) {
            stringBuilder.append(tasks.getId()).append(",");
        }
        return stringBuilder.toString();
    }


    public static List<Long> historyFromString(String history) {
        List<Long> ids = new ArrayList<>();
        if (!Objects.isNull(history)) {
            String[] split = history.split(",");
            long id = Long.parseLong(split[0]);
            ids.add(id);
        } else {
            return null;
        }
        return ids;
    }

}
