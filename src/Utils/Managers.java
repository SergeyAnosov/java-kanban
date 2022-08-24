package Utils;

import Interfaces.HistoryManager;
import Interfaces.TaskManager;
import service.FileBackedTasksManager;
import service.InMemoryHistoryManager;
import service.InMemoryTaskManager;

import java.io.File;

public class Managers {
    
    public static TaskManager getDefault() {
        //return new InMemoryTaskManager();
        return new FileBackedTasksManager(new File("src/resources/tasks.csv"));
    }
    
    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }    
}
