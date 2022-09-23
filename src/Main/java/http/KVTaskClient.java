package http;
import com.google.gson.Gson;
import service.FileBackedTasksManager;
import service.ManagerSaveException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {

    private  String url;
    private  String apiToken;

    public KVTaskClient() {
        url = "http://localhost:8080/";
        apiToken = register(url);
    }

    private String register(String url) {
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest build = HttpRequest.newBuilder()
                    .uri(URI.create(url + "register"))
                    .GET()
                    .build();

            HttpResponse<String> send = httpClient.send(build, HttpResponse.BodyHandlers.ofString());
            return send.body();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void save(String key, String value) {
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest build = HttpRequest.newBuilder()
                    .uri(URI.create(url + "save/" + key + "?API_TOKEN=" + apiToken))
                    .POST(HttpRequest.BodyPublishers.ofString(value))
                    .build();
            HttpResponse<String> send = httpClient.send(build, HttpResponse.BodyHandlers.ofString());
            if(send.statusCode() != 200) {
                System.out.println(send.statusCode());
                throw new ManagerSaveException("Во время сохранения на сервер произошла ошибка");

            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

}
