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
    public void addTask(Task task) {
        super.addTask(task);
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
            throw new RuntimeException(e);
        }
    }

    private <T extends Task> void addTasksToFile(BufferedWriter bufferedWriter, Collection<T> tasks) throws IOException {

        for (Task value : tasks) {
            bufferedWriter.write(value.getId() + "," + value.getTaskType() + ",");
            bufferedWriter.newLine();
        }
    }
}
