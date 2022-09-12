package Utils;



import Interfaces.HistoryManager;
import Interfaces.TaskManager;
import Service.FileBackedTasksManager;
import Service.InMemoryHistoryManager;
import Service.InMemoryTaskManager;

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
