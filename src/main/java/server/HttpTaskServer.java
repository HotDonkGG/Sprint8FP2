package main.java.server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import main.java.managers.Managers;
import main.java.managers.history.HistoryManager;
import main.java.managers.task.TaskManager;
import main.java.model.exception.ManagerSaveException;
import main.java.model.Epic;
import main.java.model.SubTask;
import main.java.model.Task;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private Gson gson;
    private HttpServer server;
    private TaskManager taskManager;
    private HistoryManager history;

    public HttpTaskServer(String uri) throws IOException {
        this(Managers.getManagerDefault(), uri);
    }

    public HttpTaskServer(TaskManager taskManager, String uri) throws IOException {
        this.taskManager = taskManager;
        gson = Managers.getGson();
        server = HttpServer.create(new InetSocketAddress(("localhost"), PORT), 0);
        server.createContext(uri, this::handleAllTasks);
        server.createContext(uri + "task/", this::handleTasks);
        server.createContext(uri + "subtasks/", this::handleSubTasks);
        server.createContext(uri + "epic/", this::handleEpic);
        server.createContext(uri + "history/", this::handleHistory);
    }

    private void handleAllTasks(HttpExchange httpExchange) throws IOException {
        try {
            String path = httpExchange.getRequestURI().getPath();
            String requestMethod = httpExchange.getRequestMethod();
            switch (requestMethod) {
                case "GET": {
                    if (Pattern.matches("^/tasks/$", path)) {
                        String response = gson.toJson(taskManager.getAllTask());
                        sendText(httpExchange, response);
                        return;
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleTasks(HttpExchange httpExchange) throws IOException {
        try {
            String path = httpExchange.getRequestURI().getPath();
            String requestMethod = httpExchange.getRequestMethod();
            switch (requestMethod) {
                case "GET": {
                    if (Pattern.matches("^/tasks/task/\\d+$", path)) {
                        String pathId = path.replaceFirst("/tasks/task/", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                              String response = gson.toJson(taskManager.getTaskById(id));
                            sendText(httpExchange, response);
                            break;
                        } else {
                            System.out.println("получен некорректный id " + pathId);
                            httpExchange.sendResponseHeaders(405, 0);
                            break;
                        }
                    }
                    if (Pattern.matches("^/tasks/\\d+ /task$", path)) {
                        String pathId = path.replaceFirst("/task/", "")
                                .replaceFirst("task/", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            String responce = gson.toJson(taskManager.getTaskById(id));
                            sendText(httpExchange, responce);
                            break;
                        } else {
                            System.out.println("получен некорректный id " + pathId);
                            httpExchange.sendResponseHeaders(405, 0);
                            break;
                        }
                    }
                    break;
                }
                case "DELETE": {
                    if (Pattern.matches("^/tasks/task/\\d+$", path)) {
                        String pathId = path.replaceFirst("/tasks/task/", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            taskManager.removeTaskById(id);
                            System.out.println("удалили задачу по id " + id);
                            httpExchange.sendResponseHeaders(200, 0);
                        } else {
                            System.out.println("получен некорректный id " + pathId);
                            httpExchange.sendResponseHeaders(405, 0);
                        }
                    } else {
                        httpExchange.sendResponseHeaders(405, 0);
                    }
                    if (Pattern.matches("^/tasks$", path)) {
                        String pathId = path.replaceFirst("/task/", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            break;
                        }
                    }
                }
                case "POST": {
                    if (Pattern.matches("^/tasks/$", path)) {
                        String requestBody = readText(httpExchange);
                        Task task = gson.fromJson(requestBody, Task.class);
                        if (task == null || task.getTaskType() == null || task.getStatus() == null || task.getDescription() == null) {
                            httpExchange.sendResponseHeaders(400, 0);
                            break;
                        }
                        try {
                            taskManager.addTask(task);
                            System.out.println("Task сохранена с id " + task.getId());
                            String response = gson.toJson(task);
                            sendText(httpExchange, response);
                        } catch (ManagerSaveException e) {
                            System.out.println("Ошибка сохранения : " + e.getMessage());
                            httpExchange.sendResponseHeaders(500, 0);
                        }
                    }
                    break;
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            httpExchange.close();
        }
    }

    private void handleSubTasks(HttpExchange httpExchange) throws IOException {
        try {
            String path = httpExchange.getRequestURI().getPath();
            String requestMethod = httpExchange.getRequestMethod();
            switch (requestMethod) {
                case "GET": {
                    if (Pattern.matches("^/tasks/subtask$", path)) {
                        String response = gson.toJson(taskManager.getAllSubTask());
                        sendText(httpExchange, response);
                        return;
                    }
                    if (Pattern.matches("^/tasks/subtask/\\d+$", path)) {
                        String pathId = path.replaceFirst("/tasks/subtask/", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            //  String response = gson.toJson(taskManager.getTaskById(id));
                            sendText(httpExchange, String.valueOf(id));
                            break;
                        } else {
                            System.out.println("получен некорректный id " + pathId);
                            httpExchange.sendResponseHeaders(405, 0);
                            break;
                        }
                    }
                    if (Pattern.matches("^/tasks/\\d+ /subtask$", path)) {
                        String pathId = path.replaceFirst("/tasks/subtask/", "")
                                .replaceFirst("/subtask/", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            String responce = gson.toJson(taskManager.getSubTaskById(id));
                            sendText(httpExchange, responce);
                            break;
                        } else {
                            System.out.println("получен некорректный id " + pathId);
                            httpExchange.sendResponseHeaders(405, 0);
                            break;
                        }
                    }
                    break;
                }
                case "DELETE": {
                    if (Pattern.matches("^/tasks/subtask/\\d+$", path)) {
                        String pathId = path.replaceFirst("/tasks/subtask/", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            taskManager.removeSubTaskById(id);
                            System.out.println("удалили задачу по id " + id);
                            httpExchange.sendResponseHeaders(200, 0);
                        } else {
                            System.out.println("получен некорректный id " + pathId);
                            httpExchange.sendResponseHeaders(405, 0);
                        }
                    } else {
                        httpExchange.sendResponseHeaders(405, 0);
                    }
                    if (Pattern.matches("^/tasks/subtask$", path)) {
                        String pathId = path.replaceFirst("/tasks/subtask/", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            break;
                        }
                    }
                }
                case "POST": {
                    if (Pattern.matches("^/tasks/subtask$", path)) {
                        String requestBody = readText(httpExchange);
                        SubTask subTask = gson.fromJson(requestBody, SubTask.class);
                        if (subTask == null || subTask.getTaskType() == null || subTask.getStatus() == null || subTask.getDescription() == null) {
                            httpExchange.sendResponseHeaders(400, 0);
                            break;
                        }
                        try {
                            taskManager.addSubTask(subTask);
                            System.out.println("Task сохранена с id " + subTask.getId());
                            String response = gson.toJson(subTask);
                            sendText(httpExchange, response);
                        } catch (ManagerSaveException e) {
                            System.out.println("Ошибка сохранения : " + e.getMessage());
                            httpExchange.sendResponseHeaders(500, 0);
                        }
                    }
                    break;
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            httpExchange.close();
        }
    }

    private void handleEpic(HttpExchange httpExchange) throws IOException {
        try {
            String path = httpExchange.getRequestURI().getPath();
            String requestMethod = httpExchange.getRequestMethod();
            switch (requestMethod) {
                case "GET": {
                    if (Pattern.matches("^/tasks/epic$", path)) {
                        String response = gson.toJson(taskManager.getAllEpic());
                        sendText(httpExchange, response);
                        return;
                    }
                    if (Pattern.matches("^/tasks/epic/\\d+$", path)) {
                        String pathId = path.replaceFirst("/tasks/epic/", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            sendText(httpExchange, String.valueOf(id));
                            break;
                        } else {
                            System.out.println("получен некорректный id " + pathId);
                            httpExchange.sendResponseHeaders(405, 0);
                            break;
                        }
                    }
                    if (Pattern.matches("^/tasks/\\d+ /epic$", path)) {
                        String pathId = path.replaceFirst("/tasks/epic/", "")
                                .replaceFirst("/epic/", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            String responce = gson.toJson(taskManager.getEpicById(id));
                            sendText(httpExchange, responce);
                            break;
                        } else {
                            System.out.println("получен некорректный id " + pathId);
                            httpExchange.sendResponseHeaders(405, 0);
                            break;
                        }
                    }
                    break;
                }
                case "DELETE": {
                    if (Pattern.matches("^/tasks/epic/\\d+$", path)) {
                        String pathId = path.replaceFirst("/tasks/epic/", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            taskManager.removeEpicById(id);
                            System.out.println("удалили задачу по id " + id);
                            httpExchange.sendResponseHeaders(200, 0);
                        } else {
                            System.out.println("получен некорректный id " + pathId);
                            httpExchange.sendResponseHeaders(405, 0);
                        }
                    } else {
                        httpExchange.sendResponseHeaders(405, 0);
                    }
                    if (Pattern.matches("^/tasks/epic$", path)) {
                        String pathId = path.replaceFirst("/tasks/epic/", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            break;
                        }
                    }
                }
                case "POST": {
                    if (Pattern.matches("^/tasks/epic$", path)) {
                        String requestBody = readText(httpExchange);
                        Epic epic = gson.fromJson(requestBody, Epic.class);
                        if (epic == null || epic.getTaskType() == null || epic.getStatus() == null || epic.getDescription() == null) {
                            httpExchange.sendResponseHeaders(400, 0);
                            break;
                        }
                        try {
                            taskManager.addEpic(epic);
                            System.out.println("Epic сохранена с id " + epic.getId());
                            String response = gson.toJson(epic);
                            sendText(httpExchange, response);
                        } catch (ManagerSaveException e) {
                            System.out.println("Ошибка сохранения : " + e.getMessage());
                            httpExchange.sendResponseHeaders(500, 0);
                        }
                    }
                    break;
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            httpExchange.close();
        }
    }

    private void handleHistory(HttpExchange httpExchange) throws IOException {
        try {
            String path = httpExchange.getRequestURI().getPath();
            String requestMethod = httpExchange.getRequestMethod();
            switch (requestMethod) {
                case "GET": {
                    if (Pattern.matches("^/tasks/history$", path)) {
                        String response = gson.toJson(history.getHistory());
                        sendText(httpExchange, response);
                    } else {
                        System.out.println("история не получена ");
                        httpExchange.sendResponseHeaders(405, 0);
                        break;
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            httpExchange.close();
        }
    }

    private int parsePathId(String path) {
        try {
            return Integer.parseInt(path);
        } catch (NumberFormatException exception) {
            return -1;
        }
    }

    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        System.out.println("Открой в браузере http://localhost:" + PORT + "/");
        server.start();
    }

    public void stop() {
        server.stop(0);
        System.out.println("Остановили сервер на порту: " + PORT);
    }

    private void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }

    private String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), UTF_8);
    }
}