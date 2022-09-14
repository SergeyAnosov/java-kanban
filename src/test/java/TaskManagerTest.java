import interfaces.TaskManager;
import constants.Status;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

abstract class TaskManagerTest<T extends TaskManager> {

    public T taskManager;
    public T getTaskManagerFailBacked;

      public void setUp() {
            Task task1 = new Task("Task0", Status.NEW, "extra0", 15, "11.09.2022 16:30");
            Task task2 = new Task("Task1", Status.IN_PROGRESS, "77777777", 25, "10.09.2022 15:00");
            taskManager.addTask(task1);
            taskManager.addTask(task2);

            Epic epic1 = new Epic("Epic2", Status.NEW, "extra2");
            Epic epic2 = new Epic("Epic3", Status.IN_PROGRESS, "extra3");
            taskManager.addEpic(epic1);
            taskManager.addEpic(epic2);

            SubTask subTask1 = new SubTask("SubTask7", Status.NEW, "extra1", 10, "13.09.2022 15:00", 2);
            SubTask subTask2 = new SubTask("SubTask8", Status.IN_PROGRESS, "extra2", 30, "15.09.2022 22:00", 2);
            taskManager.addSubTask(subTask1);
            taskManager.addSubTask(subTask2);
      }

      public void clearTaskManager() {
         taskManager.removeAll();
      }




}
