package main.java.client;

import main.java.exceptionCustom.ManagerLoadException;
import main.java.exceptionCustom.ManagerSaveException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final String url;

    private final String apiToken;

    public KVTaskClient(int port) {
        url = "http://localhost:" + port + "/";
        apiToken = register(url);
    }

    private String register(String url) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "register"))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new ManagerSaveException("Не удалось обработать запрос,  status code = " + response.statusCode());
            }
            return response.body();
        } catch (Exception exception) {
            throw new ManagerSaveException("Не удалость зарегестрировать пользователя");
        }

    }

    public String load(String key) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "load/" + key + "?" + apiToken))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new ManagerLoadException("Не удалось загрузить данные, status code = " + response.statusCode());
            }
            return response.body();


        } catch (Exception exception) {
            throw new ManagerLoadException("Не удалось загрузить состояние менеджера");
        }

    }

    public String put(String key, String json) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url+ "save/" + key + "?" + apiToken))
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new ManagerSaveException("Не удалось загрузить данные, status code = " + response.statusCode());
            }
            return response.body();
        } catch (Exception exception) {
            throw new ManagerSaveException("Не удалось сохранить состояние менеджера");
        }

    }

}
