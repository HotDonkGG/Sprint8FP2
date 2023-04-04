package test.java;

import main.java.managers.history.HistoryManager;
import main.java.managers.history.InMemoryHistoryManager;
import main.java.managers.task.TaskManager;
import main.java.model.Epic;
import main.java.model.SubTask;
import main.java.model.Task;
import main.java.model.enums.TaskStatus;
import main.java.model.enums.TaskType;
import main.java.model.exception.IntersectionsException;
import main.java.utility.UtilityManagers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest <T extends TaskManager>{

    private final Map<Integer, Task> emptyMap = new HashMap<>();
    private final List<Task> emptyList = new ArrayList<>();
    public T manager;

    protected Task task;

    protected Epic epic;

    protected SubTask subTask;

    @BeforeEach
    public void init(){
        task = new Task(1, "Task1", "Task1", TaskStatus.NEW, TaskType.TASK,
                Instant.EPOCH, Duration.ZERO);
        epic = new Epic(1, "Epic1", TaskStatus.NEW, "Epic1", Instant.EPOCH, Duration.ZERO);
        subTask = new SubTask("Subtask1", "Subtask1",
                Instant.EPOCH, Duration.ZERO, epic.getId());
    }
    @Test
    public void hashCodeTaskTest() {

        var task1 = manager.addTask(task);

        assertEquals(Map.of(task1.getId(), task1).hashCode(), manager.getAllTask().hashCode());

    }

    @Test
    public void hashCodeSubtaskTest() {

        var epic1 = manager.addEpic(epic);
        var subtask1 = manager.addSubTask(subTask);

        assertEquals(Map.of(subtask1.getId(), subtask1).hashCode(), manager.getAllSubTask().hashCode());

    }

    @Test
    public void hashCodeEpicTest() {

        var epic1 = manager.addEpic(epic);

        assertEquals(Map.of(epic1.getId(), epic1).hashCode(), manager.getAllEpic().hashCode());

    }

    @Test
    public void getEpicIDTest() {

        var epic1 = manager.addEpic(epic);
        var subtask1 = manager.addSubTask(subTask);

        assertEquals(epic1.getId(), subtask1.getEpicId());

    }

    @Test
    public void setEpicEndTimeTest() {

        var epic1 = manager.addEpic(epic);

        epic1.setEndTime(Instant.ofEpochSecond(42));

        assertEquals(Instant.ofEpochSecond(42), epic1.getEndTime());

    }

    @Test
    public void getTaskTypeTest() {

        var task1 = manager.addTask(task);

        assertEquals(TaskType.TASK, task1.getTaskType());

    }

    @Test
    public void getDurationTest() {

        var task1 = manager.addTask(task);

        assertEquals(0, task1.getDuration());

    }

    @Test
    public void setDurationTest() {

        var task1 = manager.addTask(task);

        task1.setDuration(Duration.ofSeconds(42));

        assertEquals(42, task1.getDuration());

    }

    @Test
    public void setStartTimeTest() {

        var task1 = manager.addTask(task);

        task1.setStartTime(Instant.ofEpochSecond(42));

        assertEquals(Instant.ofEpochSecond(42), task1.getStartTime());

    }

    @Test
    public void getTaskNameTest() {

        var task1 = manager.addTask(task);

        assertEquals("Task1", task1.getName());

    }

    @Test
    public void getTaskDescriptionTest() {

        var task1 = manager.addTask(task);

        assertEquals("Task1", task1.getDescription());

    }

    @Test
    public void createNewTaskTest() {

        var task1 = manager.addTask(task);
        var tasks = manager.getAllTask();

        assertEquals(Map.of(task1.getId(), task1), tasks);

    }

    @Test
    public void createNewEpicTest() {

        var epic1 = manager.addEpic(epic);
        var epics = manager.getAllEpic();

        assertEquals(Map.of(epic1.getId(), epic1), epics);

    }

    @Test
    public void createNewSubtaskTest() {

        var epic1 = manager.addEpic(epic);
        var subtask1 = manager.addSubTask(subTask);
        var subtasks = manager.getAllSubTask();

        assertEquals(manager.getAllSubTask().get(subtask1.getId()), subtask1);

    }

    @Test
    public void updateTaskStateTest() {

        var task1 = manager.addTask(task);

        task1.setStatus(TaskStatus.IN_PROGRESS);
        manager.updateTask(task1);

        var updatedTaskState = manager.getAllTask().get(task1.getId()).getStatus();

        assertEquals(TaskStatus.IN_PROGRESS, updatedTaskState);

    }

    @Test
    public void updateSubtaskStateDoneTest() {

        var epic1 = manager.addEpic(epic);
        var subtask1 = manager.addSubTask(subTask);
        subtask1.setStatus(TaskStatus.DONE);

        manager.updateSubTask(subtask1);

        var updatedEpicState = manager.getEpicById(epic1.getId()).getStatus();
        var updatedSubtaskState = manager.getSubTaskById(subtask1.getId()).getStatus();

        assertEquals(TaskStatus.DONE, updatedEpicState);
        assertEquals(TaskStatus.DONE, updatedSubtaskState);

    }

    @Test
    public void noSubtaskRemoveIfIncorrectIDTest() {

        var epic1 = manager.addEpic(epic);

        manager.removeSubTaskById(42);

        assertEquals(0, manager.getAllSubTask().size());

    }

    @Test
    public void updateEpicStateToInProgressTest() {

        var epic1 = manager.addEpic(epic);
        var subtask1 = manager.addSubTask(subTask);
        var subtask2 = manager.addSubTask(subTask);

        subtask1.setStatus(TaskStatus.IN_PROGRESS);
        manager.updateSubTask(subtask1);
        subtask2.setStatus(TaskStatus.DONE);
        manager.updateSubTask(subtask2);

        var updatedEpicState = manager.getEpicById(epic1.getId()).getStatus();

        assertEquals(TaskStatus.IN_PROGRESS, updatedEpicState);

    }

    @Test
    public void updateEpicTest() {

        var epic1 = manager.addEpic(epic);

        epic1.setStatus(TaskStatus.IN_PROGRESS);
        manager.updateEpic(epic1);

        var updatedEpicState = manager.getEpicById(epic1.getId()).getStatus();

        assertEquals(TaskStatus.IN_PROGRESS, updatedEpicState);

    }

    @Test
    public void removeTaskTest() {

        var task1 = manager.addTask(task);

        manager.removeTaskById(task1.getId());

        assertEquals(emptyList, manager.getAllTask());

    }

    @Test
    public void updateSubtaskStateInProgressTest() {

        var epic1 = manager.addEpic(epic);
        var subtask1 = manager.addSubTask(subTask);

        subtask1.setStatus(TaskStatus.IN_PROGRESS);
        manager.updateTask(subtask1);

        var updatedEpicState = manager.getEpicById(epic1.getId()).getStatus();
        var updatedSubtaskState = manager.getSubTaskById(subtask1.getId()).getStatus();

        assertEquals(TaskStatus.IN_PROGRESS, updatedEpicState);
        assertEquals(TaskStatus.IN_PROGRESS, updatedSubtaskState);

    }

    @Test
    public void noTaskRemoveIfIncorrectIDTest() {

        var task1 = manager.addTask(task);

        manager.removeTaskById(42);

        assertEquals(Map.of(task1.getId(), task1), manager.getAllTask());

    }

    @Test
    public void removeEpicTest() {

        var epic1 = manager.addEpic(epic);
        var subtask1 = manager.addSubTask(subTask);

        manager.removeEpicById(epic1.getId());

        assertEquals(emptyMap, manager.getAllEpic());

    }

    @Test
    public void calculateStartAndEndTimeOfEpicTest() {

        var epic1 = manager.addEpic(epic);
        var subtask1 = manager.addSubTask(subTask);
        var subtask2 = manager.addSubTask(subTask);

        assertEquals(subtask1.getStartTime(), epic1.getStartTime());
        assertEquals(subtask2.getEndTime(), epic1.getEndTime());

    }

    @Test
    public void noEpicRemoveIfIncorrectIDTest() {

        var epic1 = manager.addEpic(epic);
        var subtask1 = manager.addSubTask(subTask);

        manager.removeEpicById(42);

        assertEquals(Map.of(epic1.getId(), epic1), manager.getAllEpic());

    }

    @Test
    public void tasksToStringTest() {

        var epic1 = manager.addEpic(epic);
        var subtask1 = manager.addSubTask(subTask);

        assertEquals(epic1 + "\n" + subtask1 + "\n", UtilityManagers.tasksToString(manager));

    }

    @Test
    public void tasksFromStringTest() {

        var realTask = new Task(1, "Task1", TaskStatus.NEW,
                "Task1", Instant.EPOCH, Duration.ofSeconds(30));

        var taskFromString = UtilityManagers.tasksFromString(
                "1,TASK,Task1,NEW,Task1,1970-01-01T00:00:00Z,30,1970-01-01T00:00:00Z");

        assertEquals(realTask, taskFromString);

    }

    @Test
    public void throwIllegalArgumentExceptionTest() {

        assertThrows(IllegalArgumentException.class, () -> UtilityManagers.tasksFromString(
                "1,MASK,Task1,NEW,Task1,1970-01-01T00:00:00Z,30,1970-01-01T00:00:00Z"));

    }

    @Test
    public void throwIntersectionExceptionTest() {

        assertThrows(IntersectionsException.class, () -> {

            manager.addTask(new Task(
                    "Task1", "Task1",
                    Instant.ofEpochMilli(42), Duration.ofSeconds(42)));

            manager.addTask(new Task(
                    "Task2", "Task2",
                    Instant.ofEpochMilli(43), Duration.ofSeconds(43)));

        });

    }

    @Test
    public void historyToStringTest() {

        HistoryManager manager = new InMemoryHistoryManager();

        var task1 = new Task(1, "Task1", TaskStatus.NEW,
                "Task1", Instant.EPOCH, Duration.ZERO);
        var task2 = new Task(2, "Task2", TaskStatus.NEW,
                "Task2", Instant.EPOCH, Duration.ZERO);
        var task3 = new Task(3, "Task3", TaskStatus.NEW,
                "Task3", Instant.EPOCH, Duration.ZERO);

        manager.addHistory(task1);
        manager.addHistory(task2);
        manager.addHistory(task3);

        assertEquals(task1.getId() + ","
                        + task2.getId() + ","
                        + task3.getId() + ",",
                UtilityManagers.historyToString(manager));

    }

    @Test
    public void historyFromStringTest() {

        assertEquals(List.of(1, 2), UtilityManagers.historyFromString("1,2"));

    }
}