package main.java;

import main.java.client.KVTaskClient;
import main.java.managers.Managers;
import main.java.managers.TaskManager;
import main.java.server.HttpTaskServer;
import main.java.server.KVServer;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        TaskManager manager = Managers.getDefault();
        HttpTaskServer httpTaskServer = new HttpTaskServer(manager);
        KVServer server = new KVServer();
        KVTaskClient taskClient = new KVTaskClient(KVServer.PORT);
        httpTaskServer.startServer();
        server.start();
    }
}
