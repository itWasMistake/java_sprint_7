package managers;

import exceptionCustom.ManagerSaveException;
import memory.InMemoryHistoryManager;
import memory.InMemoryTaskManager;
import task.*;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private void save() {

        File file = new File("resources/tasks.csv");
        try (final FileWriter fw = new FileWriter(file); BufferedWriter bw = new BufferedWriter(fw)) {
            for (Map.Entry<Long, Task> taskEntry : super.getAllTasks().entrySet()) {
                bw.append(TaskToCSV.toString(taskEntry.getValue()));
                bw.newLine();

            }

            for (Map.Entry<Long, EpicTask> epicEntry : super.getAllEpics().entrySet()) {
                bw.append(TaskToCSV.toString(epicEntry.getValue()));
                bw.newLine();
                for (SubTask subTasks : epicEntry.getValue().getSubTaskEpic()) {
                    bw.append(TaskToCSV.toString(subTasks) + subTasks.getSubEpicId());
                    bw.newLine();
                }
            }
            if (!Objects.isNull(super.getHistory())) {
                bw.append(TaskToCSV.historyToString(super.getHistory()));
            }



        } catch (IOException exception) {
            throw new ManagerSaveException(exception.getMessage());
        }

    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(EpicTask epicTask) {
        super.addEpic(epicTask);
        save();
    }

    @Override
    public void addSubTask(EpicTask epicTask, SubTask subTask) {
        super.addSubTask(epicTask, subTask);
        save();
    }

    @Override
    public void delTask(Long id) {
        super.delTask(id);
        save();
    }

    @Override
    public void delEpic(Long id) {
        super.delEpic(id);
        save();
    }

    @Override
    public void delSubTask(Long id, EpicTask epicTask) {
        super.delSubTask(id, epicTask);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(EpicTask epicTask) {
        super.updateEpic(epicTask);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask, long subId) {
        super.updateSubTask(subTask, subId);
        save();
    }

    @Override
    public List<Task> getHistory() {
        super.getHistory();
        save();
        return super.getHistory();
    }

    public static FileBackedTasksManager loadFromFile(String path) {
        final FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.readLine();
            while (br.readLine() != null) {
                String read = br.readLine();
                String read2 = br.readLine();
                String read3 = br.readLine();
                if (br.ready()) {
                    final Task task = TaskToCSV.fromString(read);
                    final Task task2 = TaskToCSV.fromString(read2);
                    final Task task3 = TaskToCSV.fromString(read3);
                    if (task.getType().equals(TaskType.TASK)) {
                        fileBackedTasksManager.addTask(task);
                    } else if (task3.getType().equals(TaskType.EPIC_TASK)) {

                        fileBackedTasksManager.addEpic((EpicTask) task3);
                    } else if (task2.getType().equals(TaskType.SUB_TASK)) {

                        fileBackedTasksManager.addSubTask((EpicTask) task, (SubTask) task2);
                    }
                }
                TaskToCSV.historyFromString(read);
            }

        } catch (IOException exception) {
            throw new ManagerSaveException(exception.getMessage());
        }
        return fileBackedTasksManager;
    }

    public static void main(String[] args) {

        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager();
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Task task1 = new Task("TASK_1", "TASK_DESCRIPTION", fileBackedTasksManager.generateId(),
                TaskStatus.NEW);
        Task task2 = new Task("TASK_2", "TASK_DESCRIPTION", fileBackedTasksManager.generateId(),
                TaskStatus.NEW);
        Task task3 = new Task("TASK_3", "TASK_DESCRIPTION", fileBackedTasksManager.generateId(),
                TaskStatus.NEW);

        EpicTask epicTask1 = new EpicTask("EPICTASK1", "EPICTASK_DESCRIPRION",
                2,
                TaskStatus.NEW);
        EpicTask epicTask4 = new EpicTask("EPICTASK4", "EPICTASK_DESCRIPRION",
                1,
                TaskStatus.NEW);
        EpicTask epicTask2 = new EpicTask("EPICTASK2", "EPICTASK_DESCRIPRION", fileBackedTasksManager.generateId(),
                TaskStatus.NEW);
        EpicTask epicTask3 = new EpicTask("EPICTASK3", "EPICTASK_DESCRIPRION", fileBackedTasksManager.generateId(),
                TaskStatus.NEW);

        SubTask subTask1 = new SubTask("SUBTASK1", "SUBTASK_DESCRIPTION", fileBackedTasksManager.generateId(),
                TaskStatus.NEW, epicTask1.getId());
        SubTask subTask2 = new SubTask("SUBTASK2", "SUBTASK_DESCRIPTION",
                fileBackedTasksManager.generateId(),
                TaskStatus.NEW, epicTask1.getId());
        SubTask subTask3 = new SubTask("SUBTASK3", "SUBTASK_DESCRIPTION", fileBackedTasksManager.generateId(),
                TaskStatus.NEW, epicTask1.getId());


        fileBackedTasksManager.addTask(task1);
        fileBackedTasksManager.addTask(task2);
        fileBackedTasksManager.addTask(task3);

        fileBackedTasksManager.addEpic(epicTask2);
        fileBackedTasksManager.addEpic(epicTask3);
        fileBackedTasksManager.addEpic(epicTask1);
        fileBackedTasksManager.addEpic(epicTask4);
        fileBackedTasksManager.getTaskById(task1.getId());
        fileBackedTasksManager.getTaskById(task2.getId());
        fileBackedTasksManager.getTaskById(task3.getId());

    }
}
