package main.java.utility;

import main.java.managers.history.HistoryManager;
import main.java.model.Epic;
import main.java.model.SubTask;
import main.java.model.Task;
import main.java.model.enums.TaskType;
import main.java.model.enums.TaskStatus;


import main.java.managers.task.TaskManager;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public final class UtilityManagers {

    public static Task tasksFromString(String value) {

        var epicID = 0;
        var values = value.split(",");
        var id = Integer.parseInt(values[0]);
        var type = values[1];
        var name = values[2];
        var status = TaskStatus.valueOf(values[3]);
        var description = values[4];
        var startTime = Instant.parse(values[5]);
        var duration = Duration.parse(values[6]);

        if (TaskType.valueOf(type).equals(TaskType.SUBTASK))
            epicID = Integer.parseInt(values[8]);

        if (TaskType.valueOf(type).equals(TaskType.TASK))
            return new Task(id, name, status, description, startTime, duration);

        if (TaskType.valueOf(type).equals(TaskType.EPIC))
            return new Epic(id, name, status, description, startTime, duration);

        if (TaskType.valueOf(type).equals(TaskType.SUBTASK))
            return new SubTask(id, name, status, description, startTime, duration, epicID);

        else
            throw new IllegalArgumentException("Данный формат таска не поддерживается");

    }
    public static String historyToString(HistoryManager manager) {
        StringBuilder sb = new StringBuilder();
        for (Task task : manager.getHistory()) {
            sb.append(task.getId()).append(",");
        }
        return sb.toString();
    }

    public static  String tasksToString(TaskManager taskManager){
        List<Task> tasks = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        tasks.addAll(taskManager.getAllTask());
        tasks.addAll(taskManager.getAllEpic());
        tasks.addAll(taskManager.getAllSubTask());
        for(Task task: tasks){
            sb.append(task.toString()).append("\n");
        }
        return sb.toString();
    }

    public static List<Integer> historyFromString(String value) {
        List<Integer> history = new ArrayList<>();
        for (String a : value.split(",")) {
            history.add(Integer.parseInt(a));
        }
        return history;
    }
}