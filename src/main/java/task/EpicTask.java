package main.java.task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EpicTask extends Task {

    private final TaskType type = TaskType.EPIC_TASK;

    private final List<SubTask> subTaskEpic = new ArrayList<>();

    public EpicTask(String name, String description, long id, TaskStatus status) {
        super(name, description, id, status);
    }

    public EpicTask(String name, String description, long id, TaskStatus status,
                    LocalDateTime startTime, LocalDateTime endTime, Long duration) {
        super(name, description, id, status, startTime, endTime, duration);
    }

    public void addSubTask(SubTask subTask) {
        subTaskEpic.add(subTask);
    }

    public List<SubTask> getSubTaskEpic() {
        return subTaskEpic;
    }

    public void deleteSubTask(long id) {
        subTaskEpic.remove((int) id);
    }

    @Override
    public void setStatus(TaskStatus status) {

    }

    @Override
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public TaskType getType() {
        return type;
    }

    @Override
    public TaskStatus getStatus() {
        int newSub = 0;
        int doneSub = 0;
        if (subTaskEpic.isEmpty()) {
            return TaskStatus.NEW;
        } else {
            for (SubTask subTask : subTaskEpic) {
                if (subTask.getStatus().equals(TaskStatus.NEW)) {
                    newSub++;
                    if (newSub == subTaskEpic.size()) {
                        return TaskStatus.NEW;
                    }
                } else if (subTask.getStatus().equals(TaskStatus.DONE)) {
                    doneSub++;
                    if (doneSub == subTaskEpic.size()) {
                        return TaskStatus.DONE;
                    }
                } else {
                    return TaskStatus.IN_PROGRESS;
                }
            }
        }
        return null;
    }
}

