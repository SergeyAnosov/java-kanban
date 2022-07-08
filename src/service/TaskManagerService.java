package service;

import constants.Status;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

public class TaskManagerService {
    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, SubTask> subTasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();

    private static int taskGenerator = 0;
    private static int epicGenerator = 0;
    private static int subTaskGenerator = 0;


    public void addTask(Task task) {     // ����� 2.4 �� ��������
        int taskId = taskGenerator++;
        task.setId(taskId);
        tasks.put(taskId, task);
    }

    public void addEpic(Epic epic) {     // ����� 2.4 �� ��������
        int epicId = epicGenerator++;
        epic.setId(epicId);
        epics.put(epicId, epic);
        updateEpicStatus(epic);
    }

    public void addSubTask(SubTask subTask) {     // ����� 2.4 �� ��������
        int epicId = subTask.getEpicId();
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return;
        }

        int subTaskId = subTaskGenerator++;
        subTask.setId(subTaskId);
        subTasks.put(subTaskId, subTask);
        epic.addSubtask(subTaskId);
        updateEpicStatus(epic);
    }

    public List<Task> getTasks() {     // ����� �� 2.1
        Collection<Task> values = tasks.values();
        return new ArrayList<>(values);
    }

    public List<Epic> getEpics() {     // ����� �� 2.1
        Collection<Epic> values = epics.values();
        return new ArrayList<>(values);
    }

    public List<SubTask> getSubTasks() {     // ����� �� 2.1
        Collection<SubTask> values = subTasks.values();
        return new ArrayList<>(values);
    }

    private void updateEpicStatus(Epic epic) {
        List<Integer> subTaskIds = epic.getSubTaskIds();
        if (subTaskIds.isEmpty()) {
            epic.setStatus(Status.DONE);
        }
        for (Integer subTaskId : subTaskIds) {
            SubTask subTask = getSubTaskFromId(subTaskId);
            if (subTask.getStatus() == Status.NEW) {
                epic.setStatus(Status.NEW);
            } else if (subTask.getStatus() == Status.IN_PROGRESS) {
                epic.setStatus(Status.IN_PROGRESS);
            } else if (subTask.getStatus() == Status.DONE) {
                epic.setStatus(Status.DONE);
            }
        }

        // ����� �����, ����������� �����.
        /*public void updateEpicStatus(Epic epic) {
            if (epics.isEmpty()) {
                epic.setStatus(Status.NEW);
            } else {
                epic.setStatus(Status.IN_PROGRESS);
            }
            if (epics.containsValue(Status.NEW)) {
                epic.setStatus(Status.NEW);
            }
            if (epics.containsValue(Status.DONE)
                    && (!epics.containsValue(Status.NEW)
                    || !epics.containsValue(Status.IN_PROGRESS))) {
                epic.setStatus(Status.DONE);
            }
        }*/

    }

    public void removeAllTasks() {    // ����� �� 2.2 �������� ������
        tasks.clear();
    }

    public void removeAllSubTasks() {    // ����� �� 2.2 �������� ���������
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.setSubTaskIds(new ArrayList<Integer>());
        }
    }

    public void removeAllEpics() {    // ����� �� 2.2 �������� ������
        epics.clear();
    }

    public Task getTaskFromId(int taskId) {    // ����� �� 2.3 ��������� �� ��������������
        return tasks.get(taskId);
    }

    public Epic getEpicFromId(int epicId) {    // ����� �� 2.3 ��������� �� ��������������
        return epics.get(epicId);
    }

    public SubTask getSubTaskFromId(int subTaskId) {    // ����� �� 2.3 ��������� �� ��������������
        return subTasks.get(subTaskId);
    }

    public void deleteTaskFromId(int taskId) {    // ����� �� 2.6 �������� �� ��������������
        tasks.remove(taskId);
    }

    public void deleteEpicFromId(int epicId) {    // ����� �� 2.6 �������� �� ��������������
        epics.remove(epicId);
    }

    public void deleteSubTaskFromId(int subTaskId) {    // ����� �� 2.6 �������� �� ��������������
        subTasks.remove(subTaskId);
        SubTask subTask = getSubTaskFromId(subTaskId);
        int epicId = subTask.getEpicId();
        Epic epic = getEpicFromId(epicId);
        updateEpicStatus(epic);
    }

    public void updateTask(Task task, int taskId) {    // ����� �� 2.5 ����������. ����� ������ ������
        tasks.put(taskId, task);                       // � ������ ��������������� ��������� � ���� ���������.
    }

    public void updateEpic(Epic epic, int epicId) {    // ����� �� 2.5 ����������. ����� ������ ������
        epics.put(epicId, epic);                       // � ������ ��������������� ��������� � ���� ���������.
        updateEpicStatus(epic);
    }

    public void updateSubTask(SubTask subTask, int subTaskId) {    // ����� �� 2.5 ����������. ����� ������ ������
        subTasks.put(subTaskId, subTask);                                 // � ������ ��������������� ��������� � ���� ���������.
        int epicId = subTask.getEpicId();
        Epic epic = getEpicFromId(epicId);
        updateEpicStatus(epic);
    }

    public List<SubTask> getSubTasksFromEpic(int epicId) {   // �. 3.1 ��������� ������ ���� �������� ������������ �����.
        List<SubTask> subTaskList = new ArrayList<>();
        Epic epic = epics.get(epicId);
        List<Integer> list = epic.getSubTaskIds();
        for (Integer integer : list) {
            SubTask subTask = getSubTaskFromId(integer);
            subTaskList.add(subTask);
        }
        return subTaskList;
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
