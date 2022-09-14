package utils;



import interfaces.HistoryManager;
import interfaces.TaskManager;
import service.FileBackedTasksManager;
import service.InMemoryHistoryManager;
import service.InMemoryTaskManager;

import java.io.File;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }
    
    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefaultBacked(String pathname) {
        return new FileBackedTasksManager(new File(pathname));
    }
}
