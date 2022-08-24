package service;

import Interfaces.HistoryManager;
import Interfaces.TaskManager;
import Utils.Managers;
import constants.Status;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.*;
import java.nio.file.Path;
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

                // запись в файл
                TaskManager taskManager = Managers.getDefault();

                taskManager.addTask(new Task("Task0", Status.NEW, "extra1"));
                taskManager.addTask(new Task("Task1", Status.IN_PROGRESS, "extra2"));
                taskManager.addTask(new Task("Task2", Status.NEW, "extra1"));
                taskManager.addTask(new Task("Task3", Status.IN_PROGRESS, "extra2"));
                taskManager.addTask(new Task("Task4", Status.NEW, "extra1"));

                System.out.print("Создали список Taskoв: ");
                System.out.println(taskManager.getTasks());

                taskManager.addEpic(new Epic("Epic5", Status.NEW, "extra1"));
                taskManager.addEpic(new Epic("Epic6", Status.IN_PROGRESS, "extra2"));

                System.out.print("Создали список Epicов: ");
                System.out.println(taskManager.getEpics());

                taskManager.addSubTask(new SubTask("SubTask7", Status.NEW, "extra1", 6));
                taskManager.addSubTask(new SubTask("SubTask8", Status.IN_PROGRESS, "extra2", 6));
                taskManager.addSubTask(new SubTask("SubTask9", Status.IN_PROGRESS, "extra2", 6));

                System.out.print("Создали список SubTaskов: ");
                System.out.println(taskManager.getSubTasks());

                taskManager.getTaskById(0);
                taskManager.getTaskById(2);
                taskManager.getTaskById(2);

                taskManager.getEpicById(5);
                taskManager.getEpicById(6);

                taskManager.getSubTaskById(7);
                taskManager.getSubTaskById(8);
                taskManager.getSubTaskById(9);

                System.out.println(taskManager.getHistory());

                System.out.println("_________________________________________________________________________________");

                System.out.println("Проверяем чтение из файла");

                // чтение из файла
                File file = new File("src/resources/tasks.csv");
                FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(file);
                 System.out.println("Читаем списки задач");
                if (fileBackedTasksManager != null) {
                    System.out.println(fileBackedTasksManager.getTasks());
                    System.out.println(fileBackedTasksManager.getEpics());
                    System.out.println(fileBackedTasksManager.getSubTasks());
                    System.out.println("Проверяем историю");
                    System.out.println(fileBackedTasksManager.getHistory());
                }
        }

    @Override
    public void addTask(Task task) {  // переопределить все методы с добавлением save();
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
    public SubTask getSubTaskById(int subTaskId) {
        save();
        return super.getSubTaskById(subTaskId);
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

            bufferedWriter.write("id,type,name,status,description,epic");
            bufferedWriter.newLine();

            addTasksToFile(bufferedWriter, tasks.values());
            addEpicsToFile(bufferedWriter, epics.values());
            addSubTasksToFile(bufferedWriter, subTasks.values());
            bufferedWriter.newLine();

            String s = historyToString(historyManager);
            bufferedWriter.write(s);

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи в файл");
        }
    }    
    
    // написать разные методы для Task, Epic, SubTask; addEpicToFile, addSubTaskToFile
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
    
    // метод создания задачи из строки
    public Task fromString(String value) {
	    if (!value.isEmpty()) {	   
	    
		 String[] content = value.split(",");
		    if (content[1].equals("TASK")) {
                if (content[3].equals("NEW")) {
                    Status status = Status.NEW;
                } else if (content[3].equals("IN_PROGRESS")) {
                    Status status = Status.IN_PROGRESS;
                } else if (content[3].equals("DONE")) {
                    Status status = Status.DONE;
                }
			Task task = new Task(content[2], switchStatus(content[3]), content[4]);
			task.setId(Integer.parseInt(content[0]));
			return task;
		    } else if (content[1].equals("EPIC")) {
			Task task = new Epic(content[2], switchStatus(content[3]), content[4]);
			task.setId(Integer.parseInt(content[0]));
			return task;
		    } else if (content[1].equals("SUB_TASK")) {
			Task task = new SubTask(content[2], switchStatus(content[3]), content[4], Integer.parseInt(content[5]));
			task.setId(Integer.parseInt(content[0]));
			return task;
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

    // статический метод историю в строку
    public static String historyToString(HistoryManager historyManager) {

            StringBuilder sb = new StringBuilder();
            String s;
            List<Task> list = historyManager.getHistory();
            for (Task task : list) {
                sb.append(task.getId()).append(",");
            }
            if (!sb.isEmpty()) {
                sb.deleteCharAt(sb.length() - 1);
            }
            s = sb.toString();
            return s;
    }
    
    // статический метод для восстановления менеджера истории из файла CSV
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
	
    
    // метод который восстанавливает данные менеджера из файла при запуске программы
    public static FileBackedTasksManager loadFromFile(File file) {
	    if (file.isFile()) {
	        return null;
	    }
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
	    
	   try (Reader fileReader = new FileReader(file);
            BufferedReader br = new BufferedReader(fileReader)) {

		   	    
		   while (br.ready()) {
				String line = br.readLine();
               if (!line.startsWith("id")) {
                   Task task = fileBackedTasksManager.fromString(line);
                   if (task != null) {
                       switch (task.getTaskType()) {
                           case TASK:
                               fileBackedTasksManager.addTask(task);
                               break;
                           case EPIC:
                               fileBackedTasksManager.addEpic((Epic) task);
                               break;
                           case SUB_TASK:
                               fileBackedTasksManager.addSubTask((SubTask) task);
                               break;
                       }
                   }
               } else if (line.isBlank()) {
                   br.readLine();
                   String history = br.readLine();
                   List<Integer> listHistory = historyFromString(history);
                   for (Integer integer : listHistory) {
                       Task task = fileBackedTasksManager.getTaskById(integer);
                       if (task != null) {
                           switch (task.getTaskType()) {
                               case TASK:
                                   fileBackedTasksManager.getTask(integer);
                                   break;
                               case EPIC:
                                   fileBackedTasksManager.getEpic(integer);
                                   break;
                               case SUB_TASK:
                                   fileBackedTasksManager.getSubTask(integer);
                                   break;
                           }
                       }
                   }
			}
           }
		   
	   } catch (IOException e) {
		   throw new ManagerSaveException("Ошибка чтения из файла");
        }
       return null;
    }

    public String taskToString(Task task) {
        return  task.getId() +  "," + task.getTaskType() + "," + task.getName() + "," + task.getStatus() + "," + task.getExtraInfo();
    }

    public String epicToString(Epic epic) {
        return  epic.getId() + "," + epic.getTaskType() + "," + epic.getName() + "," + epic.getStatus() + "," + epic.getExtraInfo();
    }

    public String subTaskToString(SubTask subTask) {
        return  subTask.getId() + "," + subTask.getTaskType() + "," + subTask.getName() +  "," + subTask.getStatus() + "," +
                subTask.getExtraInfo() + "," + subTask.getEpicId();
    }

    
    static class ManagerSaveException extends RuntimeException {

        public ManagerSaveException(final String message) {
            super(message);
        }
    }
}
