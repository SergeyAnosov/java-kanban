import com.google.gson.Gson;
import constants.Status;
import http.KVTaskClient;
import tasks.Task;
import utils.Managers;

import java.time.LocalDateTime;

import static service.FileBackedTasksManager.formatter;

public class Main {

    public static void main(String[] args) {
        KVTaskClient kvTaskClient = new KVTaskClient();

        Task task = new Task("Task0", Status.NEW, "extra0", 15, LocalDateTime.parse("10.09.2022 16:30",formatter));

        Gson gson = Managers.getGson();

        String json = gson.toJson(task);

        kvTaskClient.save("tasks", json);
        System.out.println(kvTaskClient);
    }
}
