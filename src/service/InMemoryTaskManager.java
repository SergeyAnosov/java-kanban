package service;

import constants.Status;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

public class InMemoryTaskManager implements TaskManager {
    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, SubTask> subTasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();


    @Override
    public Task getTaskById(int taskId) {
        Task task = tasks.get(taskId);
        historyManager.addToHistoryMap(task);
        return task;
    }

    @Override
    public Epic getEpicById(int epicId) {
        historyManager.addToHistoryMap(epics.get(epicId));
        return epics.get(epicId);
    }

    @Override
    public SubTask getSubTaskById(int subTaskId) {
        historyManager.addToHistoryMap(subTasks.get(subTaskId));
        return subTasks.get(subTaskId);
    }
    
    @Override
    public Task getTask(int taskId) {
            return tasks.get(taskId);
    }
    
    @Override
    public Epic getEpic(int epicId) {
        return epics.get(epicId);
    }

    @Override
    public SubTask getSubTask(int subTaskId) {
        return subTasks.get(subTaskId);
    }

    @Override
    public void addTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void addEpic(Epic epic) {
        int epicId = epic.getId();
        epics.put(epicId, epic);
        updateEpicStatus(epic);
    }

    @Override
    public void addSubTask(SubTask subTask) {
        Epic epic = epics.get(subTask.getEpicId());
        if (epic == null) {
            return;
        }

        subTasks.put(subTask.getId(), subTask);
        epic.addSubtaskId(subTask.getId());
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
    public void removeAllTasks() {    // ïóíêò ÒÇ 2.2 Óäàëåíèå Òàñêîâ
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
    public void removeAllEpics() {    // ïóíêò ÒÇ 2.2 Óäàëåíèå Ýïèêîâ
        epics.clear();
    }   

    @Override
    public void deleteTask(int taskId) {
        historyManager.remove(taskId);
        tasks.remove(taskId);

    }

    @Override
    public void deleteEpic(int epicId) {
        historyManager.remove(epicId);
        epics.remove(epicId);
        List<SubTask> subs = getSubTasksFromEpic(epicId);
        for (SubTask sub : subs) {
            historyManager.remove(sub.getId());
        }

    }

    @Override
    public void deleteSubTask(int subTaskId) {
        historyManager.remove(subTaskId);
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

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }


}
