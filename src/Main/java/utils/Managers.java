package utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import interfaces.HistoryManager;
import interfaces.TaskManager;
import service.FileBackedTasksManager;
import service.InMemoryHistoryManager;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;


public class Managers {

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefaultBacked(String pathname) {
        return new FileBackedTasksManager(new File(pathname));
    }

    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        return gsonBuilder.create();
    }

}
