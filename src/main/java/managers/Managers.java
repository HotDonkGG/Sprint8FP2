package main.java.managers;

import com.google.gson.*;
import main.java.managers.history.HistoryManager;
import main.java.managers.history.InMemoryHistoryManager;
import main.java.managers.task.FileBackedTasksManager;
import main.java.managers.task.TaskManager;
import main.java.managers.task.HttpTaskManager;

import java.lang.reflect.Type;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public class Managers {

    public static HistoryManager getHistoryManager(){
        return  new InMemoryHistoryManager();
    }

    public static FileBackedTasksManager getFileBackendManager(){
        return new FileBackedTasksManager();
    }

    public static TaskManager getManagerDefault(){
        return new HttpTaskManager();
    }

    public static Gson getGson(){
        GsonBuilder gsonBuilder = new GsonBuilder();

        gsonBuilder.registerTypeAdapter(Instant.class, new JsonDeserializer<Instant>() {
            @Override
            public Instant deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                return ZonedDateTime.parse(json.getAsJsonPrimitive().getAsString()).toInstant();
            }
        });
        gsonBuilder.registerTypeAdapter(Duration.class, new JsonDeserializer<Duration>() {
            @Override
            public Duration deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                return Duration.parse(json.getAsJsonPrimitive().getAsString());
            }
        });

        return gsonBuilder.create();
    }
}