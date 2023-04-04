package main.java.model;

import main.java.model.enums.TaskStatus;
import main.java.model.enums.TaskType;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private List<SubTask> subTasks;
    protected Epic EpicType;
    private Instant endTime = Instant.ofEpochSecond(0);

    public Epic(String name,
                String description,
                TaskType type) {

        super(name, description, Instant.ofEpochSecond(0), Duration.ZERO);
        this.subTasks = new ArrayList<>();
        this.taskType = type;
    }

    public Epic(int id,
                String name,
                TaskStatus status,
                String description,
                Instant startTime,
                Duration duration) {

        super(name, description, startTime, duration);
        this.endTime = super.getEndTime();
        this.subTasks = new ArrayList<>();
        this.taskType = TaskType.EPIC;
        this.status = status;
        this.id = id;
    }

    public List<SubTask> getSubTaskList() {
        return subTasks;
    }

    public void setSubTaskList(List<SubTask> subTaskList) {
        this.subTasks = subTaskList;
    }

    @Override
    public String toString() {
        return id + ","
                + taskType + ","
                + name + ","
                + status + ','
                + description + "+"
                + EpicType;
    }

    public void setEndTime(Instant endTime) {

        this.endTime = endTime;
    }

    @Override
    public Instant getEndTime() {

        return endTime;
    }
}

