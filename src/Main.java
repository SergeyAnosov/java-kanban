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

        // ������ ��� ������
        Task task = new Task("task", Status.NEW, "�������� ����");
        Task task1 = new Task("task1", Status.IN_PROGRESS, "�������� ���� 1");
        taskManagerService.addTask(task);
        taskManagerService.addTask(task1);

        // ������ ��� �����
        // epic1 �� �������� IN_PROGRESS � ����� ����������� NEW, ����� ��������� ��������� ������� ����� �� NEW
        Epic epic1 = new Epic("epic1", Status.IN_PROGRESS, "�������� epic1");
        taskManagerService.addEpic(epic1);
        SubTask subTask = new SubTask("subTask", Status.NEW,
                "�������� subTask �� epic1", epic1.getId());
        SubTask subTask1 = new SubTask("subTask1", Status.NEW,
                "�������� subTask1 �� epic1", epic1.getId());
        taskManagerService.addSubTask(subTask);
        taskManagerService.addSubTask(subTask1);

        // epic2 � ����� ����������. ����� ����� NEW, ��������� IN_PROGRESS. ���� ������ �� IN_PROGRESS.
        Epic epic2 = new Epic("epic2", Status.NEW, "�������� epic2");
        taskManagerService.addEpic(epic2);
        SubTask subTask3 = new SubTask("subTask3", Status.IN_PROGRESS, "�������� subTask �� epic2", epic2.getId());
        taskManagerService.addSubTask(subTask3);

        //������ ������ ���� �� �������� NEW, ��� ��� ������ ������ ������ ������ �� DONE
        Epic emptyEpic = new Epic("epmtyEpic", Status.NEW, "description");
        taskManagerService.addEpic(emptyEpic);

        System.out.println("----------------------------------------------------------------------------------------");
        System.out.println("������ ��������");
        PrintService printService = new PrintService();
        List<Task> tasks = taskManagerService.getTasks();
        List<Epic> epics = taskManagerService.getEpics();
        List<SubTask> subTasks = taskManagerService.getSubTasks();

        System.out.println("������� ����� � ����� � �����������");
        printService.printTasks(tasks);
        printService.printEpics(epics);
        printService.printSubTasks(subTasks);

        System.out.println("----------------------------------------------------------------------------------------");
        System.out.println("�������� �������");
        System.out.println("��������� �� ����������������:");
        System.out.println(taskManagerService.getTaskFromId(1));
        System.out.println(taskManagerService.getEpicFromId(2));
        System.out.println(taskManagerService.getSubTaskFromId(3));
        System.out.println("��������� �������� �� ���� id");
        System.out.println(taskManagerService.getSubTasksFromEpic(2));
        System.out.println("----------------------------------------------------------------------------------------");

        System.out.println("��������");
        taskManagerService.removeAllTasks();
        System.out.println("������� �����");
        printService.printTasks(taskManagerService.getTasks());
        taskManagerService.removeAllSubTasks();
        System.out.println("������� SubTask�");
        printService.printEpics(taskManagerService.getEpics());
        printService.printSubTasks(taskManagerService.getSubTasks());
        System.out.println("������� �����: ");
        taskManagerService.removeAllEpics();
        printService.printEpics(taskManagerService.getEpics());
    }
}
