package service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import http.KVTaskClient;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import utils.Managers;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class HttpTaskManager extends FileBackedTasksManager {

    KVTaskClient kvTaskClient = new KVTaskClient();
    Gson gson = Managers.getGson();

    public HttpTaskManager() {
        super(new File("src/Main/resources/tasks.csv"));
    }

    public void loadFromKVServer() {
        try {
            String jsonTasks = kvTaskClient.load("tasks");
            String jsonEpics = kvTaskClient.load("epics");
            String jsonSubTasks = kvTaskClient.load("subTasks");

            tasks = gson.fromJson(jsonTasks, new TypeToken<HashMap<Integer, Task>>(){}.getType());
            for (Task task : tasks.values()) {
                super.getTaskById(task.getId()); // заполнение истории
            }
            epics = gson.fromJson(jsonEpics, new TypeToken<HashMap<Integer, Epic>>(){}.getType());
            for (Epic epic : epics.values()) { // заполнение истории
                super.getEpicById(epic.getId());
            }
            subTasks = gson.fromJson(jsonSubTasks, new TypeToken<HashMap<Integer, SubTask>>(){}.getType());
            for (SubTask sub: subTasks.values()) { // заполнение истории
                super.getSubTaskById(sub.getId());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save() {
        String jsonTasks = gson.toJson(tasks);
        kvTaskClient.save("tasks", jsonTasks);

        String jsonEpics = gson.toJson(epics);
        kvTaskClient.save("epics", jsonEpics);

        String jsonSubTasks = gson.toJson(subTasks);
        kvTaskClient.save("subTasks", jsonSubTasks);

        try {
            String jsonHistory = gson.toJson(getHistory());
            kvTaskClient.save("history", jsonHistory);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
