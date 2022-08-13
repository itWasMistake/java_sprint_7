package task;


public class SubTask extends Task {

    private TaskType type = TaskType.SUB_TASK;
    private Long epicId;

    public SubTask(String name, String description, long id, TaskStatus status, Long epicId) {
        super(name, description, id, status);
        this.epicId = epicId;
    }

    public Long getSubEpicId() {
        return epicId;
    }

    @Override
    public TaskType getType() {
        return type;
    }

}
