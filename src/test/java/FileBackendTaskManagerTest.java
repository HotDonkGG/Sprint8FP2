package test.java;

import main.java.managers.Managers;
import main.java.managers.task.FileBackedTasksManager;
import main.java.model.Epic;
import main.java.model.SubTask;
import main.java.model.Task;
import main.java.model.enums.TaskType;
import main.java.model.exception.ManagerSaveException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackendTaskManagerTest extends TaskManagerTest<FileBackedTasksManager>{
    private Path filePath = Path.of("src/java.java.main/resources/results.csv");
    @BeforeEach
    public void loadInitialConditions() {

        manager = Managers.getFileBackendManager();

    }

    @Test
    public void loadFromFileTest() {

        var task1 = manager.addTask(new Task("Task1", "Task1",
                Instant.EPOCH, Duration.ZERO));
        var epic1 = manager.addEpic(new Epic("Epic1", "Epic1", TaskType.EPIC));
        var subtask1 = manager.addSubTask(new SubTask("Subtask1", "Subtask1",
                Instant.EPOCH, Duration.ZERO, epic.getId()));

        manager.getTaskById(task1.getId());
        manager.getEpicById(epic1.getId());
        manager.getSubTaskById(subtask1.getId());
        manager = FileBackedTasksManager.loadFromFile(filePath);

        assertEquals(Map.of(task1.getId(), task1), manager.getAllTask());
        assertEquals(Map.of(epic1.getId(), epic1), manager.getAllEpic());
        assertEquals(Map.of(subtask1.getId(), subtask1), manager.getAllTask());
    }

    @Test
    public void throwManagerSaveExceptionTest() {

        filePath = Path.of("probablyShouldFinallyFall.exe");

        assertThrows(ManagerSaveException.class, () -> FileBackedTasksManager.loadFromFile(filePath));

    }
}
