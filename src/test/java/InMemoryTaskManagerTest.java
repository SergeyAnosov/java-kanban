import service.InMemoryTaskManager;
import constants.Status;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    protected DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");


    @BeforeEach
    void createManager() {
        taskManager = new InMemoryTaskManager();
        setUp();
    }

    @AfterEach
    void clear() {
        clearTaskManager();
    }

    @Test
    void updateEpicStatus() {
        Epic epic0 = new Epic("ep", Status.IN_PROGRESS, "ksjflksnf");// эпик без подзадач.
        taskManager.updateEpicStatus(epic0);
        Status status = epic0.getStatus();
        assertEquals(Status.NEW, status);

        Epic epic1 = taskManager.getEpicById(2); // эпик с 2 подзадачами
        Status status1 = epic1.getStatus();
        assertEquals(status1, Status.IN_PROGRESS);

        Epic epic2 = new Epic(10,"ep2", Status.IN_PROGRESS, "nwfsf"); //
        taskManager.addSubTask( new SubTask(12, "sub1", Status.NEW, 10));
        taskManager.addSubTask( new SubTask(13, "sub13", Status.NEW, 10));
        taskManager.updateEpicStatus(epic2);
        assertEquals(epic2.getStatus(), Status.NEW);

        Epic epic3 = new Epic(15,"ep2", Status.DONE, "nwfsf"); //
        taskManager.addEpic(epic3);
        taskManager.addSubTask( new SubTask(16, "sub1", Status.DONE,15, "10.09.2022 10:30", 15));
        taskManager.addSubTask( new SubTask(17, "sub13", Status.DONE,20, "11.09.2022 10:30",15));
        taskManager.updateEpicStatus(epic3);
        assertEquals(Status.DONE, epic3.getStatus());

        Epic epic4 = new Epic(20,"ep2", Status.IN_PROGRESS, "nwfsf"); //
        taskManager.addEpic(epic4);
        taskManager.addSubTask( new SubTask(16, "sub1", Status.DONE,15, "10.09.2022 10:30", 20));
        taskManager.addSubTask( new SubTask(17, "sub13", Status.NEW,20, "11.09.2022 10:30",20));
        taskManager.updateEpicStatus(epic4);
        assertEquals(Status.IN_PROGRESS, epic4.getStatus());

        SubTask sub16 = taskManager.getSubTask(16);
        assertEquals(20, sub16.getEpicId());
    }

    @Test
    void getTaskTest() { //getTask 1.1
        taskManager.addTask(new Task("Task0", Status.NEW, "extra0", 15, "10.09.2022 16:30"));
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

        Task testTask1 = taskManager.getTask(2);
        Assertions.assertNull(testTask1);
    }

    @Test
    void getTaskByIdTest() { //getTaskById + history 2.1
        Task task  = taskManager.getTaskById(0); // стандартное поведение
        assertNotNull(task);
        assertEquals(task.getId(), 0);
        List<Task> list = taskManager.getHistory();
        assertNotNull(list);
        assertEquals(task, list.get(0));
        assertTrue(list.contains(task));

        clearTaskManager();                             // с пустыми задачами
        Task task1  = taskManager.getTaskById(0);
        assertNull(task1);
        List<Task> list1 = taskManager.getHistory();
        assertTrue(list1.isEmpty());

        Task task3  = taskManager.getTaskById(10); // несуществующий таск
        assertNull(task3);
        List<Task> list3 = taskManager.getHistory();
        assertTrue(list3.isEmpty());
    }

    @Test
    void getEpicByIdTest() {
        Epic epic  = taskManager.getEpicById(2); // стандартное поведение
        assertNotNull(epic);
        assertEquals(epic.getId(), 2);
        List<Task> list = taskManager.getHistory();
        assertNotNull(list);
        assertEquals(epic, list.get(0));
        assertTrue(list.contains(epic));

        clearTaskManager();                             // с пустыми задачами
        Epic epic1 = taskManager.getEpicById(2);
        assertNull(epic1);
        List<Task> list1 = taskManager.getHistory();
        assertTrue(list1.isEmpty());

        Epic epic3  = taskManager.getEpicById(10); // несуществующий таск
        assertNull(epic3);
        List<Task> list3 = taskManager.getHistory();
        assertTrue(list3.isEmpty());
    }

    @Test
    void getSubTaskByIdTest() {
        SubTask sub  = taskManager.getSubTaskById(4); // стандартное поведение
        assertNotNull(sub);
        assertEquals(sub.getId(), 4);
        List<Task> list = taskManager.getHistory();
        assertNotNull(list);
        assertEquals(sub, list.get(0));
        assertTrue(list.contains(sub));

        clearTaskManager();                             // с пустыми задачами
        SubTask sub1 = taskManager.getSubTaskById(4);
        assertNull(sub1);
        List<Task> list1 = taskManager.getHistory();
        assertTrue(list1.isEmpty());

        SubTask sub3  = taskManager.getSubTaskById(10); // несуществующий таск
        assertNull(sub3);
        List<Task> list3 = taskManager.getHistory();
        assertTrue(list3.isEmpty());
    }

    @Test
    void addTaskTest() {
        Task taskNew = new Task(10, Status.NEW,  20, "10.09.2022 16:20"); // неверное время
        taskManager.addTask(taskNew);
        taskManager.getHistory();
        assertEquals(taskManager.getTaskById(10), taskNew);
    }

    @Test
    void addEpicTest() {
        Epic epic = new Epic("10", Status.NEW,  "asdfsdf");
        taskManager.addEpic(epic);
        taskManager.getHistory();
        assertEquals(epic, taskManager.getEpicById(6));

        Epic taskNull = null;
        taskManager.addEpic(taskNull);
        assertFalse(taskManager.getHistory().isEmpty());
    }

    @Test
    void addSubTaskTest() {
        SubTask subTaskNew = new SubTask(10, "sfaF",  Status.NEW,  20, "10.09.2022 16:20", 2); // неверное время
        taskManager.addTask(subTaskNew);
        taskManager.getHistory();
        assertEquals(taskManager.getTaskById(10), subTaskNew);

    }

    @Test
    void getTasksTest() {
        List<Task> list = taskManager.getTasks();
        assertFalse(list.isEmpty());
        assertTrue(list.contains(taskManager.getTask(0)));
        assertTrue(list.contains(taskManager.getTask(1)));

        clearTaskManager();
        List<Task> list1 = taskManager.getTasks();
        assertTrue(list1.isEmpty());
        assertFalse(list1.contains(taskManager.getTask(0)));
        assertFalse(list1.contains(taskManager.getTask(1)));
    }

    @Test
    void getEpicsTest() {
        List<Epic> list = taskManager.getEpics();
        assertFalse(list.isEmpty());
        assertTrue(list.contains(taskManager.getEpic(2)));
        assertTrue(list.contains(taskManager.getEpic(3)));

        clearTaskManager();
        List<Epic> list1 = taskManager.getEpics();
        assertTrue(list1.isEmpty());
        assertFalse(list1.contains(taskManager.getEpic(2)));
        assertFalse(list1.contains(taskManager.getEpic(3)));
    }

    @Test
    void getSubTasksTest() {
        List<SubTask> list = taskManager.getSubTasks();
        assertFalse(list.isEmpty());
        assertTrue(list.contains(taskManager.getSubTask(4)));
        assertTrue(list.contains(taskManager.getSubTask(5)));

        clearTaskManager();
        List<SubTask> list1 = taskManager.getSubTasks();
        assertTrue(list1.isEmpty());
        assertFalse(list1.contains(taskManager.getSubTask(4)));
        assertFalse(list1.contains(taskManager.getSubTask(5)));
    }

    @Test
    void deleteTaskTest() {
        taskManager.deleteTask(0);
        Task task = taskManager.getTaskById(0);
        assertNull(task);
        List<Task> list = taskManager.getTasks();
        assertFalse(list.contains(task));
    }


    @Test
    void  deleteEpicTest() {
        taskManager.deleteEpic(2);
        Epic epic = taskManager.getEpicById(2);
        assertNull(epic);
        List<Epic> list = taskManager.getEpics();
        assertFalse(list.contains(epic));
    }

    @Test
    void  deleteSubTaskTest() {
        taskManager.deleteSubTask(4);
        SubTask subTask = taskManager.getSubTaskById(4);
        assertNull(subTask);
        List<SubTask> list = taskManager.getSubTasks();
        assertFalse(list.contains(subTask));
        Epic epic = taskManager.getEpicById(2);
        List<Integer> listId = epic.getSubTaskIds();
        assertEquals(listId.size(), 1);
    }

    @Test
    void updateTaskTest() {
        Task task = taskManager.getTaskById(0);
        Task newTask = new Task(0, Status.NEW, 40, "22.10.2022 10:45");
        taskManager.updateTask(newTask, 0);

        Task newtask1 = null;
        taskManager.updateTask(newtask1, 0);
        assertNotEquals(task, newtask1);
    }

    @Test
    void updateEpicTest() {
        Epic epic = taskManager.getEpicById(2);
        Epic newEpic = new Epic("name", Status.NEW, "sadgdfsf:45");
        taskManager.updateEpic(newEpic, 0);

        Epic newEpic1 = null;
        taskManager.updateTask(newEpic1, 2);
        assertNotEquals(epic, newEpic1);
    }

    @Test
    void updateSubTaskTest() {
        SubTask subTask = taskManager.getSubTaskById(4);
        SubTask newsubTask = new SubTask(4, "sub", Status.NEW, 2);
        taskManager.updateSubTask(newsubTask, 0);

        SubTask newSubTask1 = null;
        taskManager.updateSubTask(newsubTask, 2);
        assertNotEquals(subTask, newSubTask1);
    }

    @Test
    void getSubTasksFromEpicTest() {
        List<SubTask> list = taskManager.getSubTasksFromEpic(2);
        assertTrue(list.contains(taskManager.getSubTask(4)));
        assertTrue(list.contains(taskManager.getSubTask(5)));

        List<SubTask> list1 = taskManager.getSubTasksFromEpic(3);
        assertTrue(list1.isEmpty());
    }

    @Test
    void getHistoryTest() {
        taskManager.getTaskById(1);
        taskManager.getSubTaskById(4);
        taskManager.getEpicById(2);
        List<Task> history = taskManager.getHistory();
        assertEquals(3, history.size());

        clearTaskManager();
        List<Task> history1 = taskManager.getHistory();
        assertTrue(history1.isEmpty());
    }

    @Test
    void getPrioritizedTasksTest() {
        Set<Task> set = taskManager.getPrioritizedTasks();
        assertEquals(4, set.size());
        Optional<Task> first = set.stream().findFirst();
        first.ifPresent(task -> assertEquals(task.getStartTime(), LocalDateTime.parse("10.09.2022 15:00", formatter)));
        clearTaskManager();
        assertTrue(taskManager.getPrioritizedTasks().isEmpty());
    }
}