package main.java.server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import main.java.managers.Managers;
import main.java.managers.TaskManager;
import main.java.task.EpicTask;
import main.java.task.SubTask;
import main.java.task.Task;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Objects;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final Gson gson;
    private final HttpServer server;
    private final TaskManager manager;


    public HttpTaskServer() throws IOException {
        this(Managers.getDefault());
    }

    public HttpTaskServer(TaskManager manager) throws IOException {
        gson = Managers.getGson();
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks", this::handler);
        this.manager = Managers.getDefault();
        startServer();
    }

    public void startServer() throws RuntimeException {
        System.out.println("Сервер запущен на " + PORT + " - порту");
        server.start();

    }

    public void stop() throws RuntimeException {
        System.out.println("Сервер будт закрыт через 3 секунды");
        server.stop(3);
    }

    private void handler(HttpExchange exchange) {

        System.out.println("Обработка запроса /tasks");
        try {
            System.out.println("/tasks" + exchange.getRequestURI());
            final String path = exchange.getRequestURI().getPath().replaceFirst("/tasks", "");
            switch (path) {
                case "task":
                    handleTasks(exchange);
                    break;
                case "epic":
                    handleEpics(exchange);
                    break;
                case "subtask":
                    handleSubtasks(exchange);
                    break;
                case "history":
                    handleHistory(exchange);
                    break;
                default:
                    break;
            }
        } catch (Exception exception) {
            System.out.println("Произошла ошибка при обработке запроса");
        } finally {
            exchange.close();
        }

    }

    private void handleHistory(HttpExchange exchange) throws IOException {
        String response;
        String method = exchange.getRequestMethod();
        switch (method) {
            case "GET":
                response = gson.toJson(manager.getHistory());
                sendText(exchange, response);
                break;
            default:
                response = gson.toJson("Method Not Allowed");
                byte[] resp = response.getBytes(DEFAULT_CHARSET);
                exchange.sendResponseHeaders(405, 0);
                exchange.getResponseBody().write(resp);
                break;
        }
    }

    private void handleSubtasks(HttpExchange exchange) throws IOException {
        String response;
        String method = exchange.getRequestMethod();
        String param = exchange.getRequestURI().getQuery();
        String[] split = param.split("&");
        switch (method) {
            case "GET":
                if (split.length == 2) {
                    String epicIdString = param.substring(2);
                    String subIdString = param.substring(6);
                    long epicId = Long.parseLong(epicIdString);
                    int subId = Integer.parseInt(subIdString);
                    SubTask subTask = manager.getSubTaskByID(epicId, subId);
                    response = gson.toJson(subTask);
                    sendText(exchange, response);
                    break;
                } else if (split.length == 1) {
                    String epicId = param.substring(2);
                    long id = Long.parseLong(epicId);
                    EpicTask epic = manager.getEpicById(id);
                    response = gson.toJson(epic.getSubTaskEpic());
                    sendText(exchange, response);
                    break;
                } else {
                    response = gson.toJson("Запрашиваемый ресурс не был найден");
                    byte[] resp = response.getBytes(DEFAULT_CHARSET);
                    exchange.getResponseHeaders().add("Content-Type", "application/json");
                    exchange.sendResponseHeaders(404, 0);
                    exchange.getResponseBody().write(resp);
                    break;
                }

            case "POST":
                InputStream inputStream = exchange.getRequestBody();
                String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                String jsonString = gson.toJson(body);
                SubTask subTask = gson.fromJson(jsonString, SubTask.class);
                manager.addSubTask(manager.getEpicById(subTask.getSubEpicId()), subTask);
                response = gson.toJson("Подзадача была создана у эпика: " +
                        manager.getEpicById(subTask.getSubEpicId()));
                sendText(exchange, response);
                break;
            case "DELETE":
                if (split.length == 2) {
                    String epicIdString = param.substring(2);
                    String subIdString = param.substring(6);
                    long idEpic = Long.parseLong(epicIdString);
                    long idSub = Long.parseLong(subIdString);
                    manager.delSubTask(idSub, manager.getEpicById(idEpic));
                    response = gson.toJson("Задача по указанному id была удалена");
                    sendText(exchange, response);
                    break;
                } else {
                    manager.clearAllTasks();
                    response = gson.toJson("Все задачи в менеджере задач были удалены");
                    sendText(exchange, response);
                    break;
                }

            default:
                response = gson.toJson("Method Not Allowed");
                byte[] resp = response.getBytes(DEFAULT_CHARSET);
                exchange.getResponseHeaders().add("Content-Type", "application/json");
                exchange.sendResponseHeaders(405, 0);
                exchange.getResponseBody().write(resp);
                break;
        }

    }

    private void handleEpics(HttpExchange exchange) throws IOException {
        String response;
        String method = exchange.getRequestMethod();
        String param = exchange.getRequestURI().getQuery();
        switch (method) {
            case "GET":
                if (Objects.nonNull(param)) {
                    String idString = param.substring(2);
                    long id = Long.parseLong(idString);
                    EpicTask epic = manager.getEpicById(id);
                    response = gson.toJson(epic);
                    sendText(exchange, response);
                    break;
                } else {
                    response = gson.toJson(manager.getAllEpics());
                    sendText(exchange, response);
                    break;
                }

            case "POST":
                InputStream inputStream = exchange.getRequestBody();
                String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                String jsonString = gson.toJson(body);
                EpicTask epic = gson.fromJson(jsonString, EpicTask.class);
                if (manager.getAllEpics().containsKey(epic.getId()) || Objects.nonNull(epic.getId())) {
                    response = gson.toJson("Эпик с таким id уже существует и был обновлён");
                    manager.addEpic(epic);
                    sendText(exchange, response);
                    break;
                } else {
                    epic.setId(manager.generateId());
                    manager.addEpic(epic);
                    response = gson.toJson("Создан новый эпик");
                    sendText(exchange, response);
                    break;
                }
            case "DELETE":
                if (Objects.nonNull(param)) {
                    String idString = param.substring(2);
                    long id = Long.parseLong(idString);
                    manager.delEpic(id);
                    response = gson.toJson("Задача по указанному id была удалена");
                    sendText(exchange, response);
                    break;
                } else {
                    manager.clearAllTasks();
                    response = gson.toJson("Все задачи в менеджере задач были удалены");
                    sendText(exchange, response);
                    break;
                }

            default:
                response = gson.toJson("Method Not Allowed");
                byte[] resp = response.getBytes(DEFAULT_CHARSET);
                exchange.sendResponseHeaders(405, 0);
                exchange.getResponseBody().write(resp);
                break;
        }
    }

    private void handleTasks(HttpExchange exchange) throws IOException {
        String response;
        String method = exchange.getRequestMethod();
        String param = exchange.getRequestURI().getQuery();
        switch (method) {
            case "GET":
                if (Objects.nonNull(param)) {
                    String idString = param.substring(2);
                    long id = Long.parseLong(idString);
                    Task task = manager.getTaskById(id);
                    response = gson.toJson(task);
                    sendText(exchange, response);
                    break;
                } else {
                    response = gson.toJson(manager.getAllTasks());
                    sendText(exchange, response);
                    break;
                }
            case "POST":
                InputStream inputStream = exchange.getRequestBody();
                String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                String jsonString = gson.toJson(body);
                Task task = gson.fromJson(jsonString, Task.class);
                if (manager.getAllTasks().containsKey(task.getId())) {
                    manager.addTask(task);
                    response = gson.toJson("Задача с таким id уже существует и была обновлена");
                    sendText(exchange, response);
                }

                task.setId(manager.generateId());
                manager.addTask(task);
                response = gson.toJson("Создана новая задача");
                sendText(exchange, response);
                break;
            case "DELETE":
                if (Objects.nonNull(param)) {
                    String idString = param.substring(2);
                    long id = Long.parseLong(idString);
                    manager.delTask(id);
                    response = gson.toJson("Задача по указанному id была удалена");
                    sendText(exchange, response);
                    break;
                } else {
                    manager.clearAllTasks();
                    response = gson.toJson("Все задачи в менеджере задач были удалены");
                    sendText(exchange, response);
                    break;
                }

            default:
                response = gson.toJson("Method Not Allowed");
                byte[] resp = response.getBytes(DEFAULT_CHARSET);
                exchange.sendResponseHeaders(405, 0);
                exchange.getResponseBody().write(resp);
                break;
        }
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(DEFAULT_CHARSET);
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }
}
