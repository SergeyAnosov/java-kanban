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


    public void addTask(Task task) {     // пункт 2.4 ТЗ Создание
        int taskId = taskGenerator++;
        task.setId(taskId);
        tasks.put(taskId, task);
    }

    public void addEpic(Epic epic) {     // пункт 2.4 ТЗ Создание
        int epicId = epicGenerator++;
        epic.setId(epicId);
        epics.put(epicId, epic);
        updateEpicStatus(epic);
    }

    public void addSubTask(SubTask subTask) {     // пункт 2.4 ТЗ Создание
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

    public List<Task> getTasks() {     // пункт ТЗ 2.1
        Collection<Task> values = tasks.values();
        return new ArrayList<>(values);
    }

    public List<Epic> getEpics() {     // пункт ТЗ 2.1
        Collection<Epic> values = epics.values();
        return new ArrayList<>(values);
    }

    public List<SubTask> getSubTasks() {     // пункт ТЗ 2.1
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

        // чужой метод, разобраться потом.
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

    public void removeAllTasks() {    // пункт ТЗ 2.2 Удаление Тасков
        tasks.clear();
    }

    public void removeAllSubTasks() {    // пункт ТЗ 2.2 Удаление СабТасков
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.setSubTaskIds(new ArrayList<Integer>());
        }
    }

    public void removeAllEpics() {    // пункт ТЗ 2.2 Удаление Эпиков
        epics.clear();
    }

    public Task getTaskFromId(int taskId) {    // пукнт ТЗ 2.3 Получение по идентификатору
        return tasks.get(taskId);
    }

    public Epic getEpicFromId(int epicId) {    // пукнт ТЗ 2.3 Получение по идентификатору
        return epics.get(epicId);
    }

    public SubTask getSubTaskFromId(int subTaskId) {    // пукнт ТЗ 2.3 Получение по идентификатору
        return subTasks.get(subTaskId);
    }

    public void deleteTaskFromId(int taskId) {    // пункт ТЗ 2.6 Удаление по идентификатору
        tasks.remove(taskId);
    }

    public void deleteEpicFromId(int epicId) {    // пункт ТЗ 2.6 Удаление по идентификатору
        epics.remove(epicId);
    }

    public void deleteSubTaskFromId(int subTaskId) {    // пункт ТЗ 2.6 Удаление по идентификатору
        subTasks.remove(subTaskId);
        SubTask subTask = getSubTaskFromId(subTaskId);
        int epicId = subTask.getEpicId();
        Epic epic = getEpicFromId(epicId);
        updateEpicStatus(epic);
    }

    public void updateTask(Task task, int taskId) {    // пункт ТЗ 2.5 Обновление. Новая версия объект
        tasks.put(taskId, task);                       // с верным идентификатором передаётся в виде параметра.
    }

    public void updateEpic(Epic epic, int epicId) {    // пункт ТЗ 2.5 Обновление. Новая версия объект
        epics.put(epicId, epic);                       // с верным идентификатором передаётся в виде параметра.
        updateEpicStatus(epic);
    }

    public void updateSubTask(SubTask subTask, int subTaskId) {    // пункт ТЗ 2.5 Обновление. Новая версия объект
        subTasks.put(subTaskId, subTask);                                 // с верным идентификатором передаётся в виде параметра.
        int epicId = subTask.getEpicId();
        Epic epic = getEpicFromId(epicId);
        updateEpicStatus(epic);
    }

    public List<SubTask> getSubTasksFromEpic(int epicId) {   // п. 3.1 Получение списка всех подзадач определённого эпика.
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
