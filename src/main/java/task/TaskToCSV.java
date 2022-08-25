package main.java.task;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TaskToCSV {

    public static String toString(Task task) {
        StringBuilder sb = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd/HH:mm:ss");
        if (task.getType().equals(TaskType.TASK)) {
            sb
                    .append(task.getId())
                    .append(",")
                    .append(task.getType())
                    .append(",")
                    .append(task.getName())
                    .append(",")
                    .append(task.getStatus())
                    .append(",").append(task.getDescription())
                    .append(",")
                    .append(task.getStartTime().format(formatter))
                    .append(",")
                    .append(task.getEndTime().format(formatter))
                    .append(",")
                    .append(task.getDuration())
            ;
            return sb.toString();
        } else if (task.getType().equals(TaskType.EPIC_TASK)) {
            sb
                    .append(task.getId())
                    .append(",")
                    .append(task.getType())
                    .append(",")
                    .append(task.getName())
                    .append(",")
                    .append(task.getStatus())
                    .append(",").append(task.getDescription())
                    .append(",")
                    .append(task.getStartTime().format(formatter))
                    .append(",")
                    .append(task.getEndTime().format(formatter))
                    .append(",")
                    .append(task.getDuration())
            ;
            return sb.toString();
        } else {
            sb
                    .append(task.getId())
                    .append(",")
                    .append(task.getType())
                    .append(",")
                    .append(task.getName())
                    .append(",")
                    .append(task.getStatus())
                    .append(",").append(task.getDescription())
                    .append(",")
                    .append(task.getStartTime().format(formatter))
                    .append(",")
                    .append(task.getEndTime().format(formatter))
                    .append(",")
                    .append(task.getDuration())
            ;
            return sb.toString();
        }
    }

    public static Task fromString(String value) {
        if (!Objects.isNull(value)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd/HH:mm:ss");
            String[] split = value.split(",");
            int id = Integer.parseInt(split[0]);
            String taskType = split[1];
            TaskType type = TaskType.valueOf(taskType);
            String name = split[2];
            TaskStatus status = TaskStatus.valueOf(split[3]);
            String description = split[4];
            LocalDateTime startTime = LocalDateTime.parse(split[5], formatter);
            LocalDateTime endTime = LocalDateTime.parse(split[6], formatter);
            Long duration = Long.parseLong(split[7]);
            if (type.equals(TaskType.TASK)) {
                return new Task(name, description, id, status, startTime, endTime, duration);
            } else if (type.equals(TaskType.EPIC_TASK)) {
                return new EpicTask(name, description, id, status, startTime, endTime, duration);
            } else {

                Long epicId = Long.parseLong(split[5]);
                return new SubTask(name, description, id, status, epicId, startTime, endTime, duration);
            }
        } else {
            return null;
        }

    }

    public static String historyToString(List<Task> history) {
        StringBuilder stringBuilder = new StringBuilder();
        int count = 0;
        for (Task tasks : history) {
            count++;
            if (count == history.size()) {
                stringBuilder.append(tasks.getId());
            } else {
                stringBuilder.append(tasks.getId()).append(",");
            }


        }
        return stringBuilder.toString();
    }


    public static List<Long> historyFromString(String history) {
        List<Long> ids = new ArrayList<>();
        if (!Objects.isNull(history)) {
            for (int i = 0; i < ids.size(); i++) {
                String[] split = history.split(",");
                long id = Long.parseLong(split[0]);
                ids.add(id);
            }

        } else {
            return ids;
        }
        return ids;
    }

}
