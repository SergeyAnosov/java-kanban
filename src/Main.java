import constants.Status;
import service.*;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

public class Main {


    public static void main(String[] args) {

        //ТЗ 4

        // создаём разные задачи

        TaskManager taskManager = Managers.getDefault();

        taskManager.addTask(new Task("Task1", Status.NEW, "extra1"));
        taskManager.addTask(new Task("Task2", Status.IN_PROGRESS, "extra2"));
        taskManager.addTask(new Task("Task3", Status.DONE, "extra3"));

        taskManager.addEpic(new Epic("Epic1", Status.NEW, "extra1"));
        taskManager.addEpic(new Epic("Epic2", Status.IN_PROGRESS, "extra2"));
        taskManager.addEpic(new Epic("Epic3", Status.DONE, "extra3"));

        taskManager.addSubTask(new SubTask("SubTask1", Status.NEW, "extra1", 2));
        taskManager.addSubTask(new SubTask("SubTask2", Status.IN_PROGRESS, "extra2", 1));
        taskManager.addSubTask(new SubTask("SubTask3", Status.IN_PROGRESS, "extra2", 0));

        System.out.println("----------------------------------------------------------------------------------------");

        //заполняем историю

        taskManager.getTask(2);
        taskManager.getTask(1);
        taskManager.getTask(0);

        taskManager.getSubTask(2);
        taskManager.getSubTask(1);
        taskManager.getSubTask(0);

        taskManager.getEpic(2);
        taskManager.getEpic(1);
        taskManager.getEpic(0);



        System.out.println(taskManager.getHistory());






    }
}
