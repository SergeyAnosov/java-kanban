;
import Service.InMemoryTaskManager;
import constants.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;

import java.util.List;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    void setUP() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    void getTaskTest() { //getTask 1.1
        taskManager.addTask(new Task("Task1", Status.NEW, "extra1"));
        Task testTask1 = taskManager.getTask(0); //кладём таск в список
        Task task = taskManager.getTask(0);
        Assertions.assertEquals(task, testTask1);
    }
    @Test
    void getTaskTestEmptyTask() { // не создаём таск и проверяем на null //getTask 1.2
        Task testTask1 = taskManager.getTask(2);
        Assertions.assertNull(testTask1);
    }
    @Test
    void getTaskTestWrongTask() { // не создаём таск и проверяем на null //getTask 1.3
        taskManager.addTask(new Task("Task1", Status.NEW, "extra1"));
        Task testTask1 = taskManager.getTask(2);
        Assertions.assertNull(testTask1);
    }

    @Test
    void getTaskByIdTest() { //getTaskById + history 2.1
        taskManager.addTask(new Task("Task1", Status.NEW, "extra1", 2022.9.21.10));
        Task testTask1 = taskManager.getTaskById(0); //кладём таск в список
        List<Task> list = taskManager.historyManager.getHistory();
        Task task = taskManager.getTasks().get(0);
        Assertions.assertNotNull(list);
        Assertions.assertEquals(task, testTask1);
        Assertions.assertEquals(list.get(0), testTask1);
    }

    @Test
    void getTaskByIdTestEmptyTask() { //getTaskById + history 2.2
        Task testTask1 = taskManager.getTaskById(2);
        List<Task> list = taskManager.historyManager.getHistory();
        Assertions.assertTrue(list.isEmpty());
    }


}