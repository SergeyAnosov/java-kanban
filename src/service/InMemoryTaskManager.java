package service;

import constants.Status;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, SubTask> subTasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();


    @Override
    public void addTask(Task task) {
        int taskId = Task.getTaskGenerator();
        task.setId(taskId);
        tasks.put(taskId, task);
    }

    @Override
    public void addEpic(Epic epic) {
        int epicId = Epic.getEpicGenerator();
        epic.setId(epicId);
        epics.put(epicId, epic);
        updateEpicStatus(epic);
    }

    @Override
    public void addSubTask(SubTask subTask) {
        int epicId = subTask.getEpicId();
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return;
        }

        int subTaskId = SubTask.getSubTaskGenerator();
        subTask.setId(subTaskId);
        subTasks.put(subTaskId, subTask);
        epic.addSubtask(subTaskId);
        updateEpicStatus(epic);
    }

    @Override
    public List<Task> getTasks() {
        Collection<Task> values = tasks.values();
        return new ArrayList<>(values);
    }

    @Override
    public List<Epic> getEpics() {
        Collection<Epic> values = epics.values();
        return new ArrayList<>(values);
    }

    @Override
    public List<SubTask> getSubTasks() {
        Collection<SubTask> values = subTasks.values();
        return new ArrayList<>(values);
    }

    @Override
    public void updateEpicStatus(Epic epic) {
        List<Integer> subTaskIds = epic.getSubTaskIds();
        if (subTaskIds.isEmpty()) {
            epic.setStatus(Status.DONE);
        }
        for (Integer subTaskId : subTaskIds) {
            SubTask subTask = getSubTask(subTaskId);
            if (subTask.getStatus() == Status.NEW) {
                epic.setStatus(Status.NEW);
            } else if (subTask.getStatus() == Status.IN_PROGRESS) {
                epic.setStatus(Status.IN_PROGRESS);
            } else if (subTask.getStatus() == Status.DONE) {
                epic.setStatus(Status.DONE);
            }
        }

    }

    @Override
    public void removeAllTasks() {    // пункт ТЗ 2.2 Удаление Тасков
        tasks.clear();
    }

    @Override
    public void removeAllSubTasks() {
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.setSubTaskIds(new ArrayList<>());
        }
    }

    @Override
    public void removeAllEpics() {    // пункт ТЗ 2.2 Удаление Эпиков
        epics.clear();
    }

    @Override
    public Task getTask(int taskId) {
        historyManager.add(tasks.get(taskId));    //добавление в историю
        return tasks.get(taskId);
    }

    @Override
    public Epic getEpic(int epicId) {
        historyManager.add(epics.get(epicId));    //добавление в историю
        return epics.get(epicId);
    }

    @Override
    public SubTask getSubTask(int subTaskId) {
        historyManager.add(subTasks.get(subTaskId));   //добавление в историю
        return subTasks.get(subTaskId);
    }

    @Override
    public void deleteTask(int taskId) {    // пункт ТЗ 2.6 Удаление по идентификатору
        tasks.remove(taskId);
    }

    @Override
    public void deleteEpic(int epicId) {    // пункт ТЗ 2.6 Удаление по идентификатору
        epics.remove(epicId);
    }

    @Override
    public void deleteSubTask(int subTaskId) {
        subTasks.remove(subTaskId);
        SubTask subTask = getSubTask(subTaskId);
        int epicId = subTask.getEpicId();
        Epic epic = getEpic(epicId);
        updateEpicStatus(epic);
    }

    @Override
    public void updateTask(Task task, int taskId) {
        tasks.put(taskId, task);
    }

    @Override
    public void updateEpic(Epic epic, int epicId) {
        epics.put(epicId, epic);
        updateEpicStatus(epic);
    }

    @Override
    public void updateSubTask(SubTask subTask, int subTaskId) {
        subTasks.put(subTaskId, subTask);
        int epicId = subTask.getEpicId();
        Epic epic = getEpic(epicId);
        updateEpicStatus(epic);
    }

    @Override
    public List<SubTask> getSubTasksFromEpic(int epicId) {
        List<SubTask> subTaskList = new ArrayList<>();
        Epic epic = epics.get(epicId);
        List<Integer> list = epic.getSubTaskIds();
        for (Integer integer : list) {
            SubTask subTask = getSubTask(integer);
            subTaskList.add(subTask);
        }
        return subTaskList;
    }

    @Override                           //  ТЗ 4. Список с историей
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public String toString() {
        return "TaskManagerService{" +
                "tasks=" + tasks +
                ", subTasks=" + subTasks +
                ", epics=" + epics +
                ", generator=" + taskGenerator +
                '}';
    }
}
