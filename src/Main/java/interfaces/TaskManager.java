package interfaces;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.List;

public interface TaskManager {

    void addTask(Task task);

    void addSubTask(SubTask subTask);

    void addEpic(Epic epic);

    List<Task> getTasks();

    List<SubTask> getSubTasks();

    List<Epic> getEpics();

    void removeAllTasks();

    void removeAllSubTasks();

    void removeAllEpics();

    Task getTask(int taskId);

    Epic getEpic(int epicId);

    SubTask getSubTask(int subTaskId);

    void removeAll();

    void deleteTask(int taskId);

    void deleteEpic(int epicId);

    void deleteSubTask(int subTaskId);

    void updateTask(Task task, int taskId);

    void updateEpic(Epic epic, int epicId);

    void updateSubTask(SubTask subTask, int subTaskId);

    List<SubTask> getSubTasksFromEpic(int epicId);

    void updateEpicStatus(Epic epic);

    List<Task> getHistory();

    Task getTaskById(int taskId);

    Epic getEpicById(int epicId);

    SubTask getSubTaskById(int subTaskId);
}
