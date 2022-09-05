package Service;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.List;

public class PrintService {

    public void printTasks(List<Task> tasks) {
        System.out.println("������ ������: ");
        for (Task task : tasks) {
            System.out.println(task);
        }
    }

    public void printEpics(List<Epic> epics) {
        System.out.println("������ ������: ");
        for (Epic epic : epics) {
            System.out.println(epic);
        }
    }

    public void printSubTasks(List<SubTask> subTasks) {
        System.out.println("������ SubTask��: ");
        for (SubTask subTask : subTasks) {
            System.out.println(subTask);
        }
    }
}
