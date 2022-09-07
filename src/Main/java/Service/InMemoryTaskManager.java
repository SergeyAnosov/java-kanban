package Service;

import Interfaces.HistoryManager;
import Interfaces.TaskManager;
import Utils.Managers;
import constants.Status;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;


import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

public class InMemoryTaskManager implements TaskManager {
    protected Map<Integer, Task> tasks = new HashMap<>();
    protected Map<Integer, SubTask> subTasks = new HashMap<>();
    protected Map<Integer, Epic> epics = new HashMap<>();
    public final HistoryManager historyManager = Managers.getDefaultHistory();
    protected DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

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
        updateEpic(epic, subTask.getEpicId());
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
    
    public LocalDateTime getEpicStartTime(Epic epic) {
        List<SubTask> list = getSubTasksFromEpic(epic.getId());       
        LocalDateTime startTime = epic.calculateEpicStartTime(list);
        epic.setStartTime(startTime);
        return startTime;
    }

    private Duration getDuration(Epic epic) {
            List<SubTask> list = getSubTasksFromEpic(epic.getId());
            Duration epicDuration = epic.calculateEpicDuration(list);
            epic.setDuration(epicDuration);
        return epicDuration;        
    }

    public LocalDateTime getEpicEndTime(Epic epic) {            
            List<SubTask> list = getSubTasksFromEpic(epic.getId());
            LocalDateTime endTime = epic.calculateEpicEndTime(list);
            epic.setEndTime(endTime);
        return endTime;
    }

    @Override
    public void updateEpicStatus(Epic epic) {

        List<Integer> subTaskIds = epic.getSubTaskIds();
        int countSubtasks = subTaskIds.size();
        int counterNEW = 0;
        int counterDONE = 0;

        if (subTaskIds.isEmpty()) {
            epic.setStatus(Status.NEW);
        }
        for (Integer i : subTaskIds) {
            if (getSubTask(i) != null) {
                if (getSubTask(i).getStatus().equals(Status.NEW)) {
                    counterNEW += 1;
                } else if (getSubTask(i).getStatus().equals(Status.DONE)) {
                    counterDONE += 1;
                }
            }
        }
        if (counterNEW == countSubtasks) {
            epic.setStatus(Status.NEW);
        } else if (counterDONE == countSubtasks) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    @Override
    public void removeAllTasks() {
        tasks.clear();
    }

    @Override
    public void removeAllSubTasks() {
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.setSubTaskIds(new ArrayList<>());
            updateEpic(epic, epic.getId());
        }
    }

    @Override
    public void removeAllEpics() {
        epics.clear();
    }   

    @Override
    public void deleteTask(int taskId) {
        tasks.remove(taskId);
        historyManager.remove(taskId);
    }

    @Override
    public void deleteEpic(int epicId) {
        Epic epic = getEpic(epicId);
        List<SubTask> subs = getSubTasksFromEpic(epicId);

        for (SubTask sub : subs) {
            historyManager.remove(sub.getId());
            deleteSubTask(sub.getId());
            updateEpic(epic, epicId);
        }
        historyManager.remove(epicId);
        epics.remove(epicId);
    }

    @Override
    public void deleteSubTask(int subTaskId) {
        SubTask subTask = getSubTask(subTaskId);
        subTasks.remove(subTaskId);
        if (subTask != null) {
            int epicId = subTask.getEpicId();
            Epic epic = getEpic(epicId);
            updateEpicStatus(epic);
            updateEpic(epic, epicId);
        }
        historyManager.remove(subTaskId);
    }

    @Override
    public void updateTask(Task task, int taskId) {
        tasks.put(taskId, task);
    }

    @Override
    public void updateEpic(Epic epic, int epicId) {
        LocalDateTime epicStartTime = getEpicStartTime(epic);
        LocalDateTime epicEndTime = getEpicEndTime(epic);
        Duration epicDuration = getDuration(epic);
        epic.setStartTime(epicStartTime);
        epic.setEndTime(epicEndTime);
        epic.setDuration(epicDuration);
        epics.put(epicId, epic);
        updateEpicStatus(epic);
    }

    @Override
    public void updateSubTask(SubTask subTask, int subTaskId) {
        subTasks.put(subTaskId, subTask);
        int epicId = subTask.getEpicId();
        Epic epic = getEpic(epicId);
        updateEpicStatus(epic);
        updateEpic(epic, epicId);
    }

    @Override
    public List<SubTask> getSubTasksFromEpic(int epicId) {
        List<SubTask> subTaskList = new ArrayList<>();
        Epic epic = epics.get(epicId);
        if (epic != null) {
            List<Integer> list = epic.getSubTaskIds();
            for (Integer integer : list) {
                SubTask subTask = getSubTask(integer);
                subTaskList.add(subTask);
            }
        }
        return subTaskList;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }


}
