package Service;

import Interfaces.HistoryManager;
import Interfaces.TaskManager;
import Utils.Managers;
import constants.Status;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class FileBackedTasksManager extends InMemoryTaskManager {

    private final File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    public static void main(String[] args) {

        // этот блок нужен только для заполнения файла
        TaskManager taskManager = Managers.getDefaultBacked();

        taskManager.addTask(new Task("Task0", Status.NEW, "extra0", 15, "15.09.2022 16:30"));
        taskManager.addTask(new Task("Task1", Status.IN_PROGRESS, "77777777", 25, "10.09.2022 10:30"));

        Epic epic2 = new Epic("Epic2", Status.NEW, "extra2");
        Epic epic3 = new Epic("Epic3", Status.IN_PROGRESS, "extra3");

        taskManager.addEpic(epic2);
        taskManager.addEpic(epic3);

        taskManager.addSubTask(new SubTask("SubTask7", Status.NEW, "extra1", 10, "02.09.2022 15:00", 2));
        taskManager.addSubTask(new SubTask("SubTask8", Status.IN_PROGRESS, "extra2", 30, "01.09.2022 22:00", 2));

        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubTasks());

        taskManager.getTaskById(0);
        taskManager.getTaskById(1);
        taskManager.getTaskById(0);

        taskManager.getEpicById(3);
        taskManager.getEpicById(2);

        taskManager.getSubTaskById(5);
        taskManager.getSubTaskById(4);

        System.out.println("История: ");
        System.out.println(taskManager.getHistory());
        System.out.println("Файл создан и заполнен");
        System.out.println("_________________________________________________________________________________");

        // Этот блок зпускается для чтении истории из файла. Создаётся другой taskManager

        File file = new File("src/Main/resources/tasks.csv");
        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(file);

        System.out.println("Печатаем списки задач");
        fileBackedTasksManager.updateEpic(epic2, epic2.getId());
        fileBackedTasksManager.updateEpic(epic3, epic3.getId());

        System.out.println(fileBackedTasksManager.getTasks());
        System.out.println(fileBackedTasksManager.getEpics());
        System.out.println(fileBackedTasksManager.getSubTasks());
        System.out.println("Печатаем историю");
        System.out.println(fileBackedTasksManager.getHistory());

        System.out.println("Выводим список по приоритету");
        System.out.println(fileBackedTasksManager.getPrioritizedTasks());
        taskManager.addSubTask(null);
    }

    @Override
    public void addTask(Task task) {
        if (task != null) {
            super.addTask(task);
            save();
        }
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public List<Task> getHistory() {
        save();
        return super.getHistory();
    }

    @Override
    public Task getTask(int taskId) {
        Task task = super.getTask(taskId);
        save();
        return task;
    }

    @Override
    public Epic getEpic(int epicId) {
        Epic epic = super.getEpic(epicId);
        save();
        return epic;
    }

    @Override
    public SubTask getSubTask(int subTaskId) {
        SubTask subTask = super.getSubTask(subTaskId);
        save();
        return subTask;

    }

    @Override
    public void deleteTask(int taskId) {
        super.deleteTask(taskId);
        save();
    }

    @Override
    public void deleteEpic(int epicId) {
        super.deleteEpic(epicId);
        save();
    }

    @Override
    public void deleteSubTask(int subTaskId) {
        super.deleteSubTask(subTaskId);
        save();
    }

    private void save() {

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {

            bufferedWriter.write("id,type,name,status,description,duration,startTime,epicId");
            bufferedWriter.newLine();

            addTasksToFile(bufferedWriter, tasks.values());
            addEpicsToFile(bufferedWriter, epics.values());
            addSubTasksToFile(bufferedWriter, subTasks.values());
            bufferedWriter.newLine();

            String s = historyToString(historyManager);
            bufferedWriter.write(s);

        } catch (IOException e) {
            throw new ManagerSaveException("error");
        }
    }

    
    private void addTasksToFile(BufferedWriter bufferedWriter, Collection<Task> tasks) throws IOException {

        for (Task value : tasks) {
            bufferedWriter.write(taskToString(value));
            bufferedWriter.newLine();
        }
    }

    private void addEpicsToFile(BufferedWriter bufferedWriter, Collection<Epic> epics) throws IOException {

        for (Epic value : epics) {
            bufferedWriter.write(epicToString(value));
            bufferedWriter.newLine();
        }
    }

    private void addSubTasksToFile(BufferedWriter bufferedWriter, Collection<SubTask> subTasks) throws IOException {

        for (SubTask value : subTasks) {
            bufferedWriter.write(subTaskToString(value));
            bufferedWriter.newLine();
        }
    }

   
    public Task fromString(String value) { //"id,type,name,status,description,duration,startTime,epicId"
        if (!value.isEmpty()) {

            String[] content = value.split(",");
            switch (content[1]) {
                case "TASK" -> {
                    String name = content[2];
                    Status status = switchStatus(content[3]);
                    Strin description = content[4];
                    int duration = Integer.parseInt(content[5]);
                    String startTime = content[6];
                    int taskId = Integer.parseInt(content[0]);
                    
                    Task task = new Task(name, status, description, duration, startTime);
                    task.setId(taskId);
                    return task;
                }
                case "EPIC" -> {
                    String name = content[2];
                    Status status = switchStatus(content[3]);
                    Strin description = content[4];
                    int epicId = Integer.parseInt(content[0]);
                    
                    Epic epic = new Epic(name, status, description);                   
                    epic.setEpicId(epicId);                    
                    return epic;
                }
                case "SUB_TASK" -> {
                    String name = content[2];
                    Status status = switchStatus(content[3]);
                    Strin description = content[4];
                    int duration = Integer.parseInt(content[5]);
                    String startTime = content[6];
                    int subtaskId = Integer.parseInt(content[0]);
                    int epicId = Integer.parseInt(content[7]);
                    
                    SubTask subTask = new SubTask(name, status, description, duration, startTime, epicId);
                    subTask.setSubTaskId(subtaskId);
                    return subTask;
                }
            }
        }
        return null;
    }

    public static Status switchStatus(String s) {
        return switch (s) {
            case ("NEW") -> Status.NEW;
            case ("IN_PROGRESS") -> Status.IN_PROGRESS;
            case ("DONE") -> Status.DONE;
            default -> null;
        };
    }

    
    public static String historyToString(HistoryManager historyManager) {

        StringBuilder sb = new StringBuilder();
        String s;
        List<Task> list = historyManager.getHistory();
        for (Task task : list) {
            sb.append(task.getId()).append(",");
        }
        s = sb.toString();
        return s;
    }

    
    public static List<Integer> historyFromString(String value) {
        if (value.isEmpty()) {
            return null;
        }
        List<Integer> list = new ArrayList<>();
        String[] content = value.split(",");
        Integer[] array = new Integer[content.length];
        for (int i = 0; i < content.length; i++) {
            array[i] = Integer.parseInt(content[i]);
        }
        Collections.addAll(list, array);
        return list;
    }


    public static FileBackedTasksManager loadFromFile(File file) {

        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);

        try (Reader fileReader = new FileReader(file);
             BufferedReader br = new BufferedReader(fileReader)) {
            String s = br.readLine();
            while (br.ready()) {
                String line = br.readLine();

                if (line.isBlank()) {
                    String history = br.readLine();
                    List<Integer> listHistory = historyFromString(history);
                    if (listHistory != null) {
                        for (Integer integer : listHistory) {

                            // проверить в какой мапе находится ключ и исходя из этого создавать Task
                            if (fileBackedTasksManager.tasks.containsKey(integer)) {
                                Task task = fileBackedTasksManager.getTaskById(integer);

                            } else if (fileBackedTasksManager.epics.containsKey(integer)) {
                                Epic epic = fileBackedTasksManager.getEpicById(integer);

                            } else if (fileBackedTasksManager.subTasks.containsKey(integer)) {
                                SubTask subTask = fileBackedTasksManager.getSubTaskById(integer);
                            } // TODO неправильно считывает эпики
                        }
                    }
                }

                Task task = fileBackedTasksManager.fromString(line);
                if (task != null) {
                    switch (task.getTaskType()) {
                        //case TASK -> fileBackedTasksManager.tasks.put(task.getId(), task);
                        case TASK -> fileBackedTasksManager.addTask(task);
                        case EPIC -> fileBackedTasksManager.epics.put(task.getId(), (Epic) task);
                        case SUB_TASK -> fileBackedTasksManager.subTasks.put(task.getId(), (SubTask) task);
                    }
                }

            }


        } catch (IOException e) {
            throw new ManagerSaveException("error");
        }
        return fileBackedTasksManager;
    }

        public String taskToString (Task task){
            return task.getId() + "," + task.getTaskType() + "," + task.getName() + "," + task.getStatus() + "," + task.getExtraInfo() + "," + task.getDuration().toMinutes() + "," + task.getStartTime().format(formatter);
        }

        public String epicToString (Epic epic){
            return epic.getId() + "," + epic.getTaskType() + "," + epic.getName() + "," + epic.getStatus() + "," + epic.getExtraInfo()/* + "," + epic.getDuration().toMinutes() + "," + epic.getStartTime().format(formatter)*/;
        }

        public String subTaskToString (SubTask subTask){
            return subTask.getId() + "," + subTask.getTaskType() + "," + subTask.getName() + "," + subTask.getStatus() + "," +
                    subTask.getExtraInfo() + "," + subTask.getDuration().toMinutes() + "," + subTask.getStartTime().format(formatter) + "," + subTask.getEpicId();
        }


        public static class ManagerSaveException extends RuntimeException {

            public ManagerSaveException(final String message) {
                super(message);
            }
        }
    }
