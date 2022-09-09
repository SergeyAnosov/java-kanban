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

    protected Set<Task> sortedTasks = new TreeSet<>((o1, o2) -> {
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
        sortedTasks.add(task);
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
        }
        tasks.clear();
    }

    @Override
    public void removeAllSubTasks() {
        subTasks.clear();
        for (SubTask sub : subTasks.values()) {
            sortedTasks.remove(sub);
        }
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
        sortedTasks.remove(getTask(taskId));
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
        sortedTasks.remove(subTask);
        subTasks.remove(subTaskId);
        if (subTask != null) {
            int epicId = subTask.getEpicId();
            Epic epic = getEpic(epicId);
            updateEpicStatus(epic);
            epic.calculateEpicStartTime(getSubTasksFromEpic(epic.getId()));
            epic.calculateEpicEndTime(getSubTasksFromEpic(epic.getId()));
        }
        historyManager.remove(subTaskId);
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


   /* public class TaskComparator implements Comparator<Task> {
        @Override
        public Comparator<Task> comparator = (o1, o2) -> {
            if (o1.getStartTime() != null && o2.getStartTime() != null) {
                return o1.getStartTime().compareTo(o2.getStartTime());
            } else if (o1.getStartTime() == null) {
                return 1;
            } else if (o2.getStartTime() == null) {
                return -1;
            }
            return 0;
        };
    }*/

    public Set<Task> getPrioritizedTasks() {
        return sortedTasks;
    }


}
    
   
    


