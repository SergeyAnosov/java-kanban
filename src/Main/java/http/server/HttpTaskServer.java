package http.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import constants.Status;
import interfaces.TaskManager;
import service.HttpTaskManager;
import service.InMemoryTaskManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import utils.DurationAdapter;
import utils.LocalDateTimeAdapter;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

import static java.nio.charset.StandardCharsets.UTF_8;
import static service.FileBackedTasksManager.formatter;

public class HttpTaskServer {

    HttpTaskManager manager;

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .serializeNulls()
            .create();

    private final HttpServer httpServer;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final int PORT = 8080;

    public HttpTaskServer(HttpTaskManager httpTaskManager) throws IOException {
        manager = httpTaskManager;
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TaskHandler());
        System.out.println("HTTP-сервер запущен на порту: " + PORT);
    }

    public void start() {
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(1);
    }

    class TaskHandler implements HttpHandler {
        @Override
        public void handle(final HttpExchange httpExchange) throws IOException {
            try {
                int id;
                final String path = httpExchange.getRequestURI().getPath();
                final String requestMethod = httpExchange.getRequestMethod();
                String query = httpExchange.getRequestURI().getRawQuery();
                String[] splitStrings = path.split("/");
                final int pathLength = path.split("/").length;
                String[] splitId = new String[]{""};
                if (query != null) {
                    splitId = query.split("=");
                }

                switch (requestMethod) {
                    case "GET": {
                        if (path.endsWith("tasks") && pathLength == 2) { // список по приоритетам
                            String prioritizedTasksJson = gson.toJson(manager.getPrioritizedTasks());
                            sendText(httpExchange, prioritizedTasksJson);
                        }
                        if (path.endsWith("tasks/task") && pathLength == 3) { // список Тасков
                            String tasksJson = gson.toJson(manager.getTasks());
                            sendText(httpExchange, tasksJson);
                        }
                        if (path.endsWith("epic") && pathLength == 3) { // список Эпиков
                            String epicsJson = gson.toJson(manager.getEpics());
                            sendText(httpExchange, epicsJson);
                        }
                        if (path.endsWith("subtask") && pathLength == 3) { // список сабТасков
                            String subtasksJson = gson.toJson(manager.getSubTasks());
                            sendText(httpExchange, subtasksJson);
                        }
                        if (splitStrings[splitStrings.length - 1].equals("history")) { // список Истории
                            String historyJson = gson.toJson(manager.getHistory());
                            sendText(httpExchange, historyJson);
                        }
                        if (splitStrings[splitStrings.length - 1].equals("task") && (query != null)) { // Task по ID
                            id = Integer.parseInt(splitId[splitId.length - 1]);
                            String taskJson = gson.toJson(manager.getTask(id));
                            sendText(httpExchange, taskJson);
                        }
                        if (splitStrings[splitStrings.length - 1].equals("epic") && (query != null)) { // Epic по ID
                            id = Integer.parseInt(splitId[splitId.length - 1]);
                            String epicJson = gson.toJson(manager.getEpic(id));
                            sendText(httpExchange, epicJson);
                        }
                        if (splitStrings[splitStrings.length - 1].equals("subtask") && (query != null)) { // SubTask по ID
                            id = Integer.parseInt(splitId[splitId.length - 1]);
                            String subTaskJson = gson.toJson(manager.getSubTask(id));
                            sendText(httpExchange, subTaskJson);
                        }
                        break;
                    }

                    case "POST": {
                        if (splitStrings[splitStrings.length - 1].equals("task")) { // Добавление TASK
                            InputStream inputStream = httpExchange.getRequestBody();
                            String newTask = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                            inputStream.close();

                            Task task = gson.fromJson(newTask, Task.class);
                            if (manager.getTasks().contains(task)) {
                                manager.updateTask(task, task.getId());
                                httpExchange.sendResponseHeaders(200, 0);
                            } else {
                                manager.addTask(task);
                                httpExchange.sendResponseHeaders(201, 0);
                            }

                        }
                        if (splitStrings[splitStrings.length - 1].equals("epic")) { // Добавление EPIC
                            InputStream inputStream = httpExchange.getRequestBody();
                            String newEpic = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                            inputStream.close();

                            Epic epic = gson.fromJson(newEpic, Epic.class);
                            if (manager.getEpics().contains(epic)) {
                                manager.updateEpic(epic, epic.getId());
                                httpExchange.sendResponseHeaders(200, 0);
                            } else {
                                manager.addEpic(epic);
                                httpExchange.sendResponseHeaders(201, 0);
                            }

                        }

                        if (splitStrings[splitStrings.length - 1].equals("subtask") && (query == null)) { // Добавление SubTask
                            InputStream inputStream = httpExchange.getRequestBody();
                            String newSubTask = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                            inputStream.close();

                            SubTask subTask = gson.fromJson(newSubTask, SubTask.class);
                            if (manager.getSubTasks().contains(subTask)) {
                                manager.updateSubTask(subTask, subTask.getId());
                                httpExchange.sendResponseHeaders(200, 0);
                            } else {
                                manager.addSubTask(subTask);
                                httpExchange.sendResponseHeaders(201, 0);
                            }
                        }
                        break;
                    }
                    case "DELETE": {
                        if (splitStrings[splitStrings.length - 1].equals("tasks") && (query == null)) { // Очистка TASKов
                            manager.removeAllTasks();
                            httpExchange.sendResponseHeaders(200, 0);
                            break;
                        }
                        if (splitStrings[splitStrings.length - 1].equals("epics") && (query == null)) { // Очистка Epicов
                            manager.removeAllEpics();
                            httpExchange.sendResponseHeaders(200, 0);
                            break;
                        }
                        if (splitStrings[splitStrings.length - 1].equals("subtasks") && (query == null)) { // Очистка SubTaskов
                            manager.removeAllEpics();
                            httpExchange.sendResponseHeaders(200, 0);
                            break;
                        }
                        if (splitStrings[splitStrings.length - 1].equals("task") && (query != null)) { // Удаление TASK по id
                            id = Integer.parseInt(splitId[splitId.length - 1]);
                            manager.deleteTask(id);
                            httpExchange.sendResponseHeaders(200, 0);
                            break;
                        }
                        if (splitStrings[splitStrings.length - 1].equals("epic") && (query != null)) { // Удаление Epic по id
                            id = Integer.parseInt(splitId[splitId.length - 1]);
                            manager.deleteEpic(id);
                            httpExchange.sendResponseHeaders(200, 0);
                            break;
                        }
                        if (splitStrings[splitStrings.length - 1].equals("subtask") && (query != null)) { // Удаление SubTask по id
                            id = Integer.parseInt(splitId[splitId.length - 1]);
                            manager.deleteSubTask(id);
                            httpExchange.sendResponseHeaders(200, 0);
                            break;
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                httpExchange.close();
            }
        }
    }
    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }

    public void init() { // заполнение TaskManagera
        manager.addTask(new Task("Task0", Status.NEW, "extra0", 15, LocalDateTime.parse("10.09.2022 16:30",formatter)));
        manager.addTask(new Task("Task1", Status.IN_PROGRESS, "77777777", 25, LocalDateTime.parse("11.09.2022 15:00",formatter)));
        Epic epic2 = new Epic("Epic2", Status.NEW, "extra2");
        Epic epic3 = new Epic("Epic3", Status.IN_PROGRESS, "extra3");
        manager.addEpic(epic2);
        manager.addEpic(epic3);
        manager.addSubTask(new SubTask("SubTask4", Status.NEW, "extra1", 10, LocalDateTime.parse("13.09.2022 15:00",formatter), 2));
        manager.addSubTask(new SubTask("SubTask5", Status.IN_PROGRESS, "extra2", 30, LocalDateTime.parse("15.09.2022 22:00",formatter), 2));
        manager.getTaskById(0);
        manager.getTaskById(1);
        manager.getTaskById(0);
        manager.getEpicById(3);
        manager.getEpicById(2);
        manager.getSubTaskById(5);
        manager.getSubTaskById(4);
    }

}
