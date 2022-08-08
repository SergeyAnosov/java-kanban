import constants.Status;
import service.*;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

public class Main {


    public static void main(String[] args) {

        //ТЗ 5

        TaskManager taskManager = Managers.getDefault();

        taskManager.addTask(new Task("Task1", Status.NEW, "extra1"));
        taskManager.addTask(new Task("Task2", Status.IN_PROGRESS, "extra2"));
        taskManager.addTask(new Task("Task1", Status.NEW, "extra1"));
        taskManager.addTask(new Task("Task2", Status.IN_PROGRESS, "extra2"));
        taskManager.addTask(new Task("Task1", Status.NEW, "extra1"));

        System.out.println(taskManager.getTasks());

        taskManager.addEpic(new Epic("Epic3", Status.NEW, "extra1"));
        taskManager.addEpic(new Epic("Epic2", Status.IN_PROGRESS, "extra2"));

        System.out.println(taskManager.getEpics());

        taskManager.addSubTask(new SubTask("SubTask4", Status.NEW, "extra1", 5));
        taskManager.addSubTask(new SubTask("SubTask5", Status.IN_PROGRESS, "extra2", 5));
        taskManager.addSubTask(new SubTask("SubTask6", Status.IN_PROGRESS, "extra2", 5));

        System.out.println(taskManager.getSubTasks());






        System.out.println("----------------------------------------------------------------------------------------");

        //заполняем историю

        taskManager.getTask(0);
        taskManager.getTask(1);
        taskManager.getTask(0);
        System.out.println(taskManager.getHistory());

        taskManager.getEpic(2);
        taskManager.getEpic(2);
        taskManager.getEpic(6);

        System.out.println(taskManager.getHistory());


        taskManager.getSubTask(4);
        taskManager.getSubTask(4);
        taskManager.getSubTask(3);



        System.out.println(taskManager.getHistory());


    }
}
