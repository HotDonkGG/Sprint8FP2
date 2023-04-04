package test.java.server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import main.java.managers.task.TaskManager;
import main.java.model.Task;
import main.java.model.enums.TaskStatus;

import main.java.server.HttpTaskServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {
    private HttpTaskServer httpTaskServer;
    private HttpClient httpClient;
    private Gson gson;
    private TaskManager taskManager;

    @BeforeEach
    void init() {
        try {
            httpTaskServer = new HttpTaskServer(taskManager, "/tasks/");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        httpTaskServer.start();
        httpClient = HttpClient.newHttpClient();
        gson = new Gson();
    }

    @AfterEach
    void tearDown() {
        httpTaskServer.stop();
    }

    @Test
    void getTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Type taskType = new TypeToken<ArrayList<Type>>() {
        }.getType();
    }
    @Test
    void getAllTasks() throws Exception {
        Task task1 = new Task(1,"Task1",TaskStatus.NEW,"taskdescription1",Instant.now(), Duration.ofMinutes(12));
        Task task2 = new Task(2,"Task2",TaskStatus.NEW,"taskdescription2",Instant.now(), Duration.ofMinutes(15));
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/"))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<Task> tasks = gson.fromJson(response.body(), List.class);
        assertEquals(2, tasks.size());
    }
    @Test
    void getTaskById() throws Exception {
        Task task = new Task(1,"Task1",TaskStatus.NEW,"taskdescription1",Instant.now(), Duration.ofMinutes(12));
        taskManager.addTask(task);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/task/" + task.getId()))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Task returnedTask = gson.fromJson(response.body(), Task.class);
        assertEquals(task, returnedTask);
    }
    @Test
    void deleteTaskById() throws Exception {
        Task task = new Task(1,"Task1",TaskStatus.NEW,"taskdescription1",Instant.now(), Duration.ofMinutes(12));
        taskManager.addTask(task);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/task/" + task.getId()))
                .DELETE()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertNull(taskManager.getTaskById(task.getId()));
    }
    @Test
    void testAddTask() throws Exception {
        Task task = new Task(1,"Task1",TaskStatus.NEW,"taskdescription1",Instant.now(), Duration.ofMinutes(12));
        String requestBody = gson.toJson(task);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertNull(taskManager.getAllTask());
    }
}