package main.java.managers;

import main.java.exceptionCustom.*;
import main.java.memory.InMemoryTaskManager;
import main.java.task.*;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private File file;


    private void save() {

        file = new File("resources/tasks.csv");
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


                }


            }
            if (!Objects.isNull(super.getHistory())) {
                bw.append(TaskToCSV.historyToString(super.getHistory()));


            } else {
                return;
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

    @Override
    public EpicTask getEpicById(long id) {
        super.getEpicById(id);
        save();
        return super.getEpicById(id);
    }

    @Override
    public Task getTaskById(long id) {
        super.getTaskById(id);
        save();
        return super.getTaskById(id);
    }

    @Override
    public SubTask getSubTaskByID(Long epicId, int subId) {
        super.getSubTaskByID(epicId, subId);
        save();
        return super.getSubTaskByID(epicId, subId);
    }

    public static FileBackedTasksManager loadFromFile(String path) {
        final FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (br.ready()) {
                    final Task task = TaskToCSV.fromString(line);
                    final Task task2 = TaskToCSV.fromString(line);
                    final Task task3 = TaskToCSV.fromString(line);
                    if (task.getType().equals(TaskType.TASK)) {
                        fileBackedTasksManager.addTask(task);
                    } else if (task3.getType().equals(TaskType.EPIC_TASK)) {
                        fileBackedTasksManager.addEpic((EpicTask) task3);
                    } else if (task2.getType().equals(TaskType.SUB_TASK)) {
                        fileBackedTasksManager.addSubTask((EpicTask) task, (SubTask) task2);

                    }

                    fileBackedTasksManager.getTaskById(task.getId());

                }

            }
        } catch (IOException exception) {
            throw new ManagerSaveException(exception.getMessage());
        }
        return fileBackedTasksManager;
    }
    public static void main(String[] args) {
        FileBackedTasksManager tasksManager = loadFromFile("resources/tasks.csv");
        System.out.println("Задачи по приоритету");
        for (Task task : tasksManager.getPrioritizedTasks()) {
            System.out.println(task);
        }
        System.out.println("Список задач");
        for (Task task : tasksManager.getAllTasks().values()) {
            System.out.println(task);
        }

    }
}
