import Interfaces.TaskManager;
import Service.FileBackedTasksManager;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager>  {


    @Test
    void loadFromFile() {
        File file = new File("src/test/java/resources/tasks.csv");
        taskManager = new FileBackedTasksManager(file);
        setUp();

        taskManager.getTaskById(1);
        taskManager.getTaskById(0);
        taskManager.getSubTaskById(5);
        taskManager.getSubTaskById(4);

        List<Task> listT = taskManager.getTasks();
        List<SubTask> listS = taskManager.getSubTasks();
        List<Epic> listE = taskManager.getEpics();
        List<Task> history = taskManager.getHistory();

        TaskManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(file);

        if (fileBackedTasksManager != null) {
            List<Task> listTasks = fileBackedTasksManager.getTasks();
            assertTrue(listTasks.contains(taskManager.getTask(1)));
            assertTrue(listTasks.contains(taskManager.getTask(0)));
            assertEquals(listTasks, listT);

            List<SubTask> listSubTasks = fileBackedTasksManager.getSubTasks();
            assertTrue(listSubTasks.contains(taskManager.getSubTask(5)));
            assertTrue(listSubTasks.contains(taskManager.getSubTask(4)));
            assertEquals(listSubTasks, listS);

            List<Epic> listEpics = fileBackedTasksManager.getEpics();
            assertTrue(listEpics.contains(taskManager.getEpic(2)));
            assertTrue(listEpics.contains(taskManager.getEpic(3)));
            assertEquals(listEpics, listE);

            List<Task> historyNew = fileBackedTasksManager.getHistory();
            assertEquals(history, historyNew);
        }

    }
}
