package Service;

import Interfaces.HistoryManager;
import Interfaces.TaskManager;
import Utils.Managers;
import constants.Status;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.time.format.DateTimeFormatter;
import java.util.*;


public class InMemoryTaskManager implements TaskManager {
    protected Map<Integer, Task> tasks = new HashMap<>();
    protected Map<Integer, SubTask> subTasks = new HashMap<>();
    protected Map<Integer, Epic> epics = new HashMap<>();
    protected final HistoryManager historyManager = Managers.getDefaultHistory();
    protected DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    protected TreeSet<Task> sortedTasks = new TreeSet<>((o1, o2) -> {
        if ((o1.getStartTime() == null) && (o2.getStartTime() != null)) {
            return 1;
        } else if ((o1.getStartTime() != null) && (o2.getStartTime() == null))  {
            return -1;
        } else if ((o1.getStartTime() == null) && (o2.getStartTime() == null))  {
            return 0;
        } else {return o1.getStartTime().compareTo(o2.getStartTime());}
    });

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
        if (validate(task, sortedTasks)) {
            sortedTasks.add(task);
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void addEpic(Epic epic) {
        if (epic != null) {
            int epicId = epic.getId();
            epics.put(epicId, epic);
            updateEpicStatus(epic);
        } else {
            return;
        }
    }

    @Override
    public void addSubTask(SubTask subTask) {
        if (validate(subTask, sortedTasks)) {
            if (subTask != null) {
                Epic epic = epics.get(subTask.getEpicId());
                if (epic == null) {
                    return;
                }

                sortedTasks.add(subTask);
                subTasks.put(subTask.getId(), subTask);
                epic.addSubtaskId(subTask.getId());
                updateEpicStatus(epic);
                epic.calculateEpicStartTime(getSubTasksFromEpic(epic.getId()));
                epic.calculateEpicEndTime(getSubTasksFromEpic(epic.getId()));
            }
        }
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
        for (Task task : tasks.values()) {
            sortedTasks.remove(task);
            historyManager.remove(task.getId());
        }

        tasks.clear();
    }

    @Override
    public void removeAllSubTasks() {
        for (SubTask sub : subTasks.values()) {
            sortedTasks.remove(sub);
            historyManager.remove(sub.getId());
        }
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.setSubTaskIds(new ArrayList<>());
            updateEpic(epic, epic.getId());
        }
    }

    @Override
    public void removeAllEpics() {
        for (Epic epic : epics.values()) {
            historyManager.remove(epic.getId());
        }
        epics.clear();
    }

    @Override
    public void removeAll() {
        removeAllTasks();
        removeAllEpics();
        removeAllSubTasks();
        Task.setTaskIdGenerator(0);
    }

    @Override
    public void deleteTask(int taskId) {
        historyManager.remove(taskId);
        sortedTasks.remove(getTask(taskId));
        tasks.remove(taskId);
        Task.setTaskIdGenerator(Task.getTaskIdGenerator() - 1);
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
        Task.setTaskIdGenerator(Task.getTaskIdGenerator() - 1);
    }

    @Override
    public void deleteSubTask(int subTaskId) {
        SubTask subTask = getSubTask(subTaskId);
        if (subTask == null) {
            return;
        } else {
        sortedTasks.remove(subTask);
        subTasks.remove(subTaskId);

            int epicId = subTask.getEpicId();
            Epic epic = getEpic(epicId);
            List<Integer> subTaskIds = epic.getSubTaskIds();
            subTaskIds.remove(subTaskIds.indexOf(subTaskId));
            epic.setSubTaskIds(subTaskIds);
            updateEpicStatus(epic);
            epic.calculateEpicStartTime(getSubTasksFromEpic(epic.getId()));
            epic.calculateEpicEndTime(getSubTasksFromEpic(epic.getId()));
        }
        historyManager.remove(subTaskId);
        Task.setTaskIdGenerator(Task.getTaskIdGenerator() - 1);
    }

    @Override
    public void updateTask(Task task, int taskId) {
        if (task == null) {
            return;
        }
        tasks.put(taskId, task);
    }

    @Override
    public void updateEpic(Epic epic, int epicId) {
        if (epic == null) {
            return;
        }
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
        } else {
            return null;
        }
        return subTaskList;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    public Set<Task> getPrioritizedTasks() {
        return sortedTasks;
    }

    public <T extends Task> boolean validate(T task, TreeSet<T> sortedTasks) {
        if (task == null) {
            return false;
        }
        if (task.getStartTime() == null && task.getEndTime() == null) {
            return true;
        }
         if (sortedTasks.isEmpty()) { // если задач ещё нет, то можно любое время
            return true;
        } else {
             for (Task t : sortedTasks) {
                 if (task.getStartTime().isBefore(t.getStartTime())) { // если начало раньше
                     return task.getEndTime().isBefore(t.getStartTime()); // то закончится должен до начала нового
                 } else if (task.getStartTime().isAfter(t.getStartTime())) { // если начинается после
                     return  task.getStartTime().isAfter(t.getEndTime()); // то должен начинаться после окончания предыдущего
                 }
             }
         }
        return false;
    }
}
    
   
    


