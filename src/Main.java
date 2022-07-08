import constants.Status;
import service.PrintService;
import service.TaskManagerService;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.List;
import java.util.Arrays;

public class Main {


    public static void main(String[] args) {

        TaskManagerService taskManagerService = new TaskManagerService();

        // создаём две задачи
        Task task = new Task("task", Status.NEW, "описание таск");
        Task task1 = new Task("task1", Status.IN_PROGRESS, "описание таск 1");
        taskManagerService.addTask(task);
        taskManagerService.addTask(task1);

        // создаём три Эпика
        // epic1 со статусом IN_PROGRESS и двумя подзадачами NEW, чтобы проверить изменение статуса Эпика на NEW
        Epic epic1 = new Epic("epic1", Status.IN_PROGRESS, "описание epic1");
        taskManagerService.addEpic(epic1);
        SubTask subTask = new SubTask("subTask", Status.NEW,
                "описание subTask из epic1", epic1.getId());
        SubTask subTask1 = new SubTask("subTask1", Status.NEW,
                "описание subTask1 из epic1", epic1.getId());
        taskManagerService.addSubTask(subTask);
        taskManagerService.addSubTask(subTask1);

        // epic2 с одной подзадачей. Стату Эпика NEW, подзадачи IN_PROGRESS. Эпик меняет на IN_PROGRESS.
        Epic epic2 = new Epic("epic2", Status.NEW, "описание epic2");
        taskManagerService.addEpic(epic2);
        SubTask subTask3 = new SubTask("subTask3", Status.IN_PROGRESS, "описание subTask из epic2", epic2.getId());
        taskManagerService.addSubTask(subTask3);

        //Создаю пустой ЭПИК со статусом NEW, так как пустой должен менять статус на DONE
        Epic emptyEpic = new Epic("epmtyEpic", Status.NEW, "description");
        taskManagerService.addEpic(emptyEpic);

        System.out.println("----------------------------------------------------------------------------------------");
        System.out.println("Печать Объектов");
        PrintService printService = new PrintService();
        List<Task> tasks = taskManagerService.getTasks();
        List<Epic> epics = taskManagerService.getEpics();
        List<SubTask> subTasks = taskManagerService.getSubTasks();

        System.out.println("Создали Таски и Эпики с подзадачами");
        printService.printTasks(tasks);
        printService.printEpics(epics);
        printService.printSubTasks(subTasks);

        System.out.println("----------------------------------------------------------------------------------------");
        System.out.println("Проверка методов");
        System.out.println("Получение по индентификаторам:");
        System.out.println(taskManagerService.getTaskFromId(1));
        System.out.println(taskManagerService.getEpicFromId(2));
        System.out.println(taskManagerService.getSubTaskFromId(3));
        System.out.println("Получение подзадач по Эпик id");
        System.out.println(taskManagerService.getSubTasksFromEpic(2));
        System.out.println("----------------------------------------------------------------------------------------");

        System.out.println("Удаление");
        taskManagerService.removeAllTasks();
        System.out.println("Удалили Таски");
        printService.printTasks(taskManagerService.getTasks());
        taskManagerService.removeAllSubTasks();
        System.out.println("Удалили SubTaskи");
        printService.printEpics(taskManagerService.getEpics());
        printService.printSubTasks(taskManagerService.getSubTasks());
        System.out.println("Удалили Эпики: ");
        taskManagerService.removeAllEpics();
        printService.printEpics(taskManagerService.getEpics());
    }
}
