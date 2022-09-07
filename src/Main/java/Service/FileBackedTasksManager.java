package Service;

import Interfaces.HistoryManager;
import Interfaces.TaskManager;
import Utils.Managers;
import constants.Status;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import java.io.*;
import java.time.LocalDateTime;
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

        taskManager.addTask(new Task("Task0", Status.NEW, "extra1", 15, LocalDateTime.of(2022, 09, 15, 10, 30)));
        taskManager.addTask(new Task("Task777", Status.IN_PROGRESS, "77777777", 25, LocalDateTime.of(2022, 10, 15, 10, 30)));

        taskManager.addEpic(new Epic("Epic5", Status.NEW, "extra1"));
        taskManager.addEpic(new Epic("Epic6", Status.IN_PROGRESS, "extra2"));

        taskManager.addSubTask(new SubTask("SubTask7", Status.NEW, "extra1", 2));
        taskManager.addSubTask(new SubTask("SubTask8", Status.IN_PROGRESS, "extra2", 2));

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

        System.out.print("История: ");
        System.out.println(taskManager.getHistory());
        System.out.println("Файл создан и заполнен");
        System.out.println("_________________________________________________________________________________");

        // Этот блок зпускается для чтении истории из файла. Создаётся другой taskManager

        File file = new File("src/resources/tasks.csv");
        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(file);

        System.out.println("Печатаем списки задач");

        System.out.println(fileBackedTasksManager.getTasks());
        System.out.println(fileBackedTasksManager.getEpics());
        System.out.println(fileBackedTasksManager.getSubTasks());
        System.out.println("Печатаем историю");
        System.out.println(fileBackedTasksManager.getHistory());
    }

    @Override
    public void addTask(Task task) {  // ïåðåîïðåäåëèòü âñå ìåòîäû ñ äîáàâëåíèåì save();
        super.addTask(task);
        save();
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

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {

            bufferedWriter.write("id,type,name,status,description,duration,startTime,epicId");
            bufferedWriter.newLine();

            addTasksToFile(bufferedWriter, tasks.values());
            addEpicsToFile(bufferedWriter, epics.values());
            addSubTasksToFile(bufferedWriter, subTasks.values());
            bufferedWriter.newLine();

            String s = historyToString(historyManager);
            bufferedWriter.write(s);

        } catch (IOException e) {
            throw new ManagerSaveException("Îøèáêà çàïèñè â ôàéë");
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

   
    public Task fromString(String value) {
        if (!value.isEmpty()) {

            String[] content = value.split(",");
            switch (content[1]) {
                case "TASK" -> {
                    Task task = new Task(content[2], switchStatus(content[3]), content[4]);
                    task.setId(Integer.parseInt(content[0]));
                    return task;
                }
                case "EPIC" -> {
                    Epic epic = new Epic(content[2], switchStatus(content[3]), content[4]);
                    epic.setEpicId(Integer.parseInt(content[0]));
                    return epic;
                }
                case "SUB_TASK" -> {
                    SubTask subTask = new SubTask(content[2], switchStatus(content[3]), content[4], Integer.parseInt(content[5]));
                    subTask.setSubTaskId(Integer.parseInt(content[0]));
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
        //sb.deleteCharAt(sb.length() - 1);

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
                    int maxId = 0;
                    if (listHistory != null) {
                        for (Integer integer : listHistory) {
                            //if (integer > maxId) {
                                //maxId = integer;
                            //}
                            // проверить в какой мапе находится ключ и исходя из этого создавать Task
                            if (fileBackedTasksManager.tasks.containsKey(integer)) {
                                Task task = fileBackedTasksManager.getTaskById(integer);

                            } else if (fileBackedTasksManager.epics.containsKey(integer)) {
                                Epic epic = fileBackedTasksManager.getEpicById(integer);

                            } else if (fileBackedTasksManager.subTasks.containsKey(integer)) {
                                SubTask subTask = fileBackedTasksManager.getSubTaskById(integer);
                            }
                           // Task.setTaskIdGenerator((maxId + 1));
                        }
                    }
                }

                    Task task = fileBackedTasksManager.fromString(line);
                    if (task != null) {
                        switch (task.getTaskType()) {
                            case TASK -> fileBackedTasksManager.tasks.put(task.getId(), task);
                            case EPIC -> fileBackedTasksManager.epics.put(task.getId(), (Epic) task);
                            case SUB_TASK -> fileBackedTasksManager.subTasks.put(task.getId(), (SubTask) task);
                        }
                    }

            }


        } catch (IOException e) {
            throw new ManagerSaveException("Îøèáêà ÷òåíèÿ èç ôàéëà");
        }
        return fileBackedTasksManager;
    }

        public String taskToString (Task task){
            return task.getId() + "," + task.getTaskType() + "," + task.getName() + "," + task.getStatus() + "," + task.getExtraInfo() + "," + task.getDuration() + "," + task.getStartTime().format(formatter);
        }

        public String epicToString (Epic epic){
            return epic.getId() + "," + epic.getTaskType() + "," + epic.getName() + "," + epic.getStatus() + "," + epic.getExtraInfo();
        }

        public String subTaskToString (SubTask subTask){
            return subTask.getId() + "," + subTask.getTaskType() + "," + subTask.getName() + "," + subTask.getStatus() + "," +
                    subTask.getExtraInfo() + "," + subTask.getDuration() + "," + subTask.getStartTime().format(formatter) + "," + subTask.getEpicId();
        }


        static class ManagerSaveException extends RuntimeException {

            public ManagerSaveException(final String message) {
                super(message);
            }
        }
    }
