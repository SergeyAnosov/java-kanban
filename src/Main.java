import Interfaces.TaskManager;
import Utils.Managers;
import constants.Status;
import service.*;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

public class Main {


    public static void main(String[] args) {



        TaskManager taskManager = Managers.getDefault();

        taskManager.addTask(new Task("Task0", Status.NEW, "extra1"));
        taskManager.addTask(new Task("Task1", Status.IN_PROGRESS, "extra2"));
        taskManager.addTask(new Task("Task2", Status.NEW, "extra1"));
        taskManager.addTask(new Task("Task3", Status.IN_PROGRESS, "extra2"));
       taskManager.addTask(new Task("Task4", Status.NEW, "extra1"));

        taskManager.addEpic(new Epic("Epic5", Status.NEW, "extra1"));
        taskManager.addEpic(new Epic("Epic6", Status.IN_PROGRESS, "extra2"));

        taskManager.addSubTask(new SubTask("SubTask7", Status.NEW, "extra1", 5));
        taskManager.addSubTask(new SubTask("SubTask8", Status.IN_PROGRESS, "extra2", 5));
        taskManager.addSubTask(new SubTask("SubTask9", Status.IN_PROGRESS, "extra2", 5));




        System.out.println("----------------------------------------------------------------------------------------");

        taskManager.getTaskById(0);
        taskManager.getTaskById(2);
        taskManager.getTaskById(2);

        System.out.println(taskManager.getHistory());;

        taskManager.getEpicById(5);
        taskManager.getEpicById(6);

        System.out.println(taskManager.getHistory());;

        taskManager.getSubTaskById(7);
        taskManager.getSubTaskById(8);
        taskManager.getSubTaskById(9);

        System.out.println(taskManager.getHistory());;







    }
}
