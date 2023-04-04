package main.java.model;

import main.java.model.enums.TaskStatus;
import main.java.model.enums.TaskType;

import java.time.Duration;
import java.time.Instant;

public class SubTask extends Task {

    private int epicId;

    public SubTask(String name,
                   String description,
                   Instant startTime,
                   Duration duration,
                   int epicID) {

        super(name, description, startTime, duration);
        this.taskType = TaskType.SUBTASK;

    }

    public SubTask(int id,
                   String name,
                   TaskStatus taskStatus,
                   String description,
                   Instant startTime,
                   Duration duration,
                   int epicId) {

        super(name, description, startTime, duration);
        this.taskType = TaskType.SUBTASK;
        this.status = taskStatus;
        this.epicId = epicId;
        this.id = id;

    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return id + ","
                + taskType + ","
                + name + ","
                + status + ','
                + description;
    }
}