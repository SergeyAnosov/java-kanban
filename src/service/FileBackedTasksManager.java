package service;

import Interfaces.HistoryManager;
import Interfaces.TaskManager;
import Utils.Managers;
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
        /*final int id = task.getId();
        switch (task.getTaskType()) {
            case TASK -> tasks.put(id, task);
            case SUB_TASK -> subTasks.put(id, (SubTask) task);
            case EPIC -> epics.put(id, (Epic) task);
        }*/
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubTask(SubTask subTask) {
        super.addSubTask(subTask);
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
            bufferedWriter.write(historyToString(historyManager));

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка!");
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
   /* public Task fromString(String value) {
         Task task = new Task(0, null, null);
	        String[] str = value.split(",");	        
	        
	        task.taskId = Integer.parseInt(str[0]);
	        task.name = str[2];
	        task.extraInfo = str[4];
	        return task;        
    }*/
    
    // статический метод историю в строку
    public static String historyToString(HistoryManager historyManager) {
        StringBuilder sb = new StringBuilder();
        String s;
        List<Task> list = historyManager.getHistory();
        for (Task task : list) {
            sb.append((task.getId() + ","));
        }
        //sb.deleteCharAt(sb.length() - 1);
        s = sb.toString();
        return s;
    }
    
    /*// статический метод для восстановления менеджера истории из файла CSV
    public static List<Integer> historyFromString(String value) {
    }
    
    // метод который восстанавливает данные менеджера из файла при запуске программы
    public static FileBackedTaskManager loadFromFile(File file) {
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


}
