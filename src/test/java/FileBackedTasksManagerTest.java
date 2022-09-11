import Service.FileBackedTasksManager;
import Service.InMemoryTaskManager;
import Utils.Managers;
import constants.Status;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;

import java.io.File;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager>  {


    @BeforeEach
    void createFileBackedManager() {
        File file = new File("resources/tasks.csv");
        taskManager = new FileBackedTasksManager(file);
        setUp();
    }

    @AfterEach
    void clear() {
        clearTaskManager();
    }

    @Test
    void addTaskTest() {
        clearTaskManager();
        taskManager.addTask(new Task(2, Status.IN_PROGRESS, 15, "22.10.2022 15:00"));
        assertEquals();
    }
}
