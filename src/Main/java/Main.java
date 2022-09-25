import constants.Status;
import http.server.HttpTaskServer;
import http.server.KVServer;
import service.HttpTaskManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.time.LocalDateTime;

import static service.FileBackedTasksManager.formatter;

public class Main {

    public static void main(String[] args) throws IOException { // общий тест программы

        KVServer kvServer = new KVServer();
        kvServer.start();
        HttpTaskManager manager = new HttpTaskManager();
        HttpTaskServer httpTaskServer = new HttpTaskServer(manager);
        httpTaskServer.start();

        System.out.println("Создаём Taskи");
        Task task0 = new Task("Task0", Status.NEW, "extra0", 15, LocalDateTime.parse("10.09.2022 16:30",formatter));
        Task task1 = new Task("Task1", Status.IN_PROGRESS, "77777777", 25, LocalDateTime.parse("11.09.2022 15:00",formatter));

        manager.addTask(task0);
        manager.addTask(task1);

        Epic epic2 = new Epic("Epic2", Status.NEW, "extra2");
        Epic epic3 = new Epic("Epic3", Status.IN_PROGRESS, "extra3");
        manager.addEpic(epic2);
        manager.addEpic(epic3);

        SubTask subTask4 = new SubTask("SubTask4", Status.NEW, "extra1", 10, LocalDateTime.parse("13.09.2022 15:00",formatter), 2);
        SubTask subTask5 = new SubTask("SubTask5", Status.IN_PROGRESS, "extra2", 30, LocalDateTime.parse("15.09.2022 22:00",formatter), 2);
        manager.addSubTask(subTask4);
        manager.addSubTask(subTask5);

        manager.getTaskById(0);
        manager.getTaskById(1);
        manager.getTaskById(0);
        manager.getEpicById(3);
        manager.getEpicById(2);
        manager.getSubTaskById(5);
        manager.getSubTaskById(4);

        System.out.println("Печатаем поля manager перед сохранением на сервер");
        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubTasks());
        System.out.println("История");
        System.out.println(manager.getHistory());
        //manager.save();
        System.out.println("----------------------------------------------------");

        HttpTaskManager manager1 = new HttpTaskManager();
        manager1.loadFromKVServer();
        System.out.println("Печатаем поля manager1 после загрузки с сервера");
        System.out.println(manager1.getTasks());
        System.out.println(manager1.getEpics());
        System.out.println(manager1.getSubTasks());
        System.out.println("История");
        System.out.println(manager1.getHistory());
    }
}
