package main.java.task;

import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    protected String name;
    protected String description;
    protected TaskStatus status = TaskStatus.NEW;
    protected TaskType type = TaskType.TASK;
    protected long id;
    protected LocalDateTime endTime;
    protected LocalDateTime startTime;
    protected long duration;


    public Task(String name, String description, long id, TaskStatus status) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;

    }


    public Task(String name, String description, long id, TaskStatus status, LocalDateTime startTime,
                LocalDateTime endTime, long duration) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getEndTime() {
        return startTime.plusMinutes(duration);
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getDuration() {
        return duration;
    }

    public String getName() {
        return name;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public TaskType getType() {
        return type;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", type=" + type +
                ", id=" + id +
                ", endTime=" + endTime +
                ", startTime=" + startTime +
                ", duration=" + duration +
                '}';
    }
}