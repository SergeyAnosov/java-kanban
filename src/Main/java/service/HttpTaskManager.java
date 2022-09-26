package service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import constants.TaskType;
import http.KVTaskClient;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import utils.Managers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
            String jsonSubTasks = kvTaskClient.load("subtasks");
            String jsonHistory = kvTaskClient.load("history");

            tasks = gson.fromJson(jsonTasks, new TypeToken<HashMap<Integer, Task>>(){}.getType());

            epics = gson.fromJson(jsonEpics, new TypeToken<HashMap<Integer, Epic>>(){}.getType());

            subTasks = gson.fromJson(jsonSubTasks, new TypeToken<HashMap<Integer, SubTask>>(){}.getType());

            List<Integer> history = gson.fromJson(jsonHistory, new TypeToken<List<Integer>>(){}.getType());
            System.out.println(history);

            for (Integer id : history) {
                if (getAllTasksTreeMap().get(id).getTaskType() == TaskType.TASK) {
                    getTaskById(id);
                }
                if (getAllTasksTreeMap().get(id).getTaskType() == TaskType.EPIC) {
                    getEpicById(id);
                }
                if (getAllTasksTreeMap().get(id).getTaskType() == TaskType.SUB_TASK) {
                    getSubTaskById(id);
                }
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
        kvTaskClient.save("subtasks", jsonSubTasks);

        List<Integer> historyId = new ArrayList<>();
        for (Task task : getHistory()) {
            historyId.add(task.getId());
        }
        String jsonHistory = gson.toJson(historyId);
        kvTaskClient.save("history", jsonHistory);
    }
}
