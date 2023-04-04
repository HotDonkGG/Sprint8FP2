package main.java.managers.task;

import com.google.gson.*;
import main.java.managers.Managers;
import main.java.model.Epic;
import main.java.model.Task;
import main.java.model.SubTask;
import main.java.server.KVTaskClient;

import java.io.IOException;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager {

    private static KVTaskClient taskClient;
    private static String uri = "http://localhost:8078";
    private static Gson gson = Managers.getGson();

    public HttpTaskManager() {
        try {
            taskClient = new KVTaskClient(uri);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Task getTaskById(Integer id) {
        try {
            return gson.fromJson(taskClient.load(id.toString()),Task.class);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public Epic getEpicById(Integer id){
        try {
            return gson.fromJson(taskClient.load(id.toString()),Epic.class);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    @Override
    public SubTask getSubTaskById(Integer id){
        try {
            return gson.fromJson(taskClient.load(id.toString()),SubTask.class);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    @Override
    public Task removeTaskById(Integer id){
        try {
            return gson.fromJson( taskClient.delete(String.valueOf(id)), Task.class);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    @Override
    public Epic removeEpicById(Integer id){
        try {
            return gson.fromJson( taskClient.delete(String.valueOf(id)), Epic.class);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    @Override
    public SubTask removeSubTaskById(Integer id){
        try {
            return gson.fromJson( taskClient.delete(String.valueOf(id)), SubTask.class);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public Task addTask(Task task) {
        Task newAddTask = super.addTask(task);
        try{
            String taskJson = gson.toJson(newAddTask);
            taskClient.save(newAddTask.getId().toString(),taskJson);
        } catch (IOException e){
            System.out.println(e.getMessage());
        } return newAddTask;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        try{
            String taskJson = gson.toJson(task);
            taskClient.save(task.getId().toString(), taskJson);
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Task> getAllTask() {
        List<Task> tasks = super.getAllTask();
        try {
            String tasksJson = gson.toJson(tasks);
            taskClient.save("tasks", tasksJson);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return tasks;
    }

    @Override
    public void save() {
        try {
            String tasks = gson.toJson(getAllTask());
            taskClient.save("task", tasks);

            String epics = gson.toJson(getAllEpic());
            taskClient.save("epic", epics);

            String subtasks = gson.toJson(getAllSubTask());
            taskClient.save("subtask", subtasks);

            String history = gson.toJson(historyManager);
            taskClient.save("history", history);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
