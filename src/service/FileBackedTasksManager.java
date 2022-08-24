package service;

import Interfaces.HistoryManager;
import constants.TaskType;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private final File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
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
            throw new ManagerSaveException("Ошибка записи");
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
	    if (value.isEmpty()) {
		    return;
	    }
	 String[] content = value.split(",");
	    if (content[1].equals(TaskType.TASK)) {
		Task task = new Task(content[2], content[3], content[4]);
		return task;
	    } else if (content[1].equals(TaskType.EPIC)) {
		Task task = new Epic(content[2], content[3], content[4]);
		return task;
	    } else if (content[1].equals(TaskType.SUB_TASK)) {
		Task task = new SubTask(content[2], content[3], content[4], Integer.parseInt.(content[5]));
		return task;
	    }
	    return null;
    }      
		
        /*    //разобраться
		 String[] lines = contentOfFile.split("\n");
        for (int j = 1; j < lines.length; j++) {
            String[] content = lines[j].split(",");

            String itemName = content[0];
            boolean isExpense = Boolean.parseBoolean(content[1]);
            int quantity = Integer.parseInt(content[2]);
            int sumOfOne = Integer.parseInt(content[3]);

            MonthlyRecord monthlyRecord = new MonthlyRecord(itemName, isExpense, quantity, sumOfOne);

            monthlyReport.addRecord(monthlyRecord);
    }  */
	
    
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
    
    /*// статический метод для восстановления менеджера истории из файла CSV
    public static List<Integer> historyFromString(String value) {
    }
    
    // метод который восстанавливает данные менеджера из файла при запуске программы
    public static FileBackedTaskManager loadFromFile(File file) {
    }*/
	
	// пишем один общий метод
	/*public <T extends Task> String toString(Task task) {
		if (task.getTaskType().equals(TaskType.TASK)) {
            return  task.getId() +  "," + task.getTaskType() + "," + task.getName() + "," + task.getStatus() + "," + task.getExtraInfo();
        } else if (task.getTaskType().equals(TaskType.EPIC)) {
            Task epic = new Epic(task.getName(), task.getStatus(), task.getExtraInfo());
            epic.setId(task.getId());
            return  epic.getId() +  "," + epic.getTaskType() + "," + epic.getName() + "," + epic.getStatus() + "," + epic.getExtraInfo();
		} else if (task.getTaskType().equals(TaskType.SUB_TASK)) {
           SubTask subTask = (SubTask) task;
		    return subTask.getId() +  "," + subTask.getTaskType() + "," + subTask.getName() + ","
			    + subTask.getStatus() + "," + subTask.getExtraInfo() + subTask.getEpicId();
		}
        return null;
	}*/

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

     /*final int id = task.getId();
        switch (task.getTaskType()) {
            case TASK -> tasks.put(id, task);
            case SUB_TASK -> subTasks.put(id, (SubTask) task);
            case EPIC -> epics.put(id, (Epic) task);
        }*/


}
