package service;

import tasks.Task;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private final File file;

    @Override
    public void addTask(Task task) {  // переопределить все методы с добавлением save();
        super.addTask(task);
	    
	   /*  private void addTask(Task task) {
		final int id = task.getId();
		switch (task.getType()) {
			case TASK -> tasks.put(id, task);
			case SUBTASK -> subtasks.put(id, (Subtask) task);
			case EPIC -> epics.put(id, (Epic) task);
		}
	} */
	    
        save();
    }  

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    private void save() {

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {

            bufferedWriter.write("id,type,name,status,description,epic");
            bufferedWriter.newLine();

            addTasksToFile(bufferedWriter, tasks.values());
            addTasksToFile(bufferedWriter, epics.values());
            addTasksToFile(bufferedWriter, subTasks.values());


        } catch (IOException e) {
            throw new ManagerSaveException(e);
        }
    }    
    
    // написать разные методы для Task, Epic, SubTask; addEpicToFile, addSubTaskToFile
    private <T extends Task> void addTasksToFile(BufferedWriter bufferedWriter, Collection<T> tasks) throws IOException {

        for (Task value : tasks) {
            bufferedWriter.write(value.getId() + "," + value.getTaskType() + ",");
            bufferedWriter.newLine();
        }
    }
    
    // метод создания задачи из строки
    public Task fromString(String value) {
         Task task = new Task(0, null, null);
	        String[] str = value.split(",");	        
	        
	        task.taskId = Integer.parseInt(str[0]);
	        task.name = str[2];
	        task.extraInfo = str[4];
	        return task;        
    }
    
    // статический метод историю в строку
    public static String historyToString(HistoryManager manager) {        
    }
    
    // статический метод для восстановления менеджера истории из файла CSV
    public static List<Integer> historyFromString(String value) {
    }
    
    // метод который восстанавливает данные менеджера из файла при запуске программы
    public static FileBackedTaskManager loadFromFile(File file) {
    }
    
    class ManagerSaveException extends RuntimeException {
        final String message;
        public ManagerSaveException(final String message) {
            super(message);
        }
    }
}
