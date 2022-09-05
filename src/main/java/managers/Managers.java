package main.java.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import main.java.adapters.LocalDateTimeAdapter;
import main.java.http.HttpTaskManager;
import main.java.memory.InMemoryHistoryManager;
import main.java.server.KVServer;

import java.io.IOException;
import java.time.LocalDateTime;

public class Managers {


    public static TaskManager getDefault() {
        return new HttpTaskManager(KVServer.PORT);
    }

    public static KVServer getDefaultKvServer() throws IOException {
        return new KVServer();
    }

    public static Gson getGson() {
        return new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .setPrettyPrinting()
                .create();
    }

    public static InMemoryHistoryManager getHistoryDefault() {
        return new InMemoryHistoryManager();
    }
}
