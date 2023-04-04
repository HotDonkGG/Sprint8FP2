package test.java.server;

import static org.junit.jupiter.api.Assertions.*;

import main.java.managers.task.HttpTaskManager;
import main.java.model.Epic;
import main.java.model.SubTask;
import main.java.model.Task;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class HttpTaskManagerTest {

    private HttpTaskManager httpTaskManager;

    @BeforeEach
    void init() {
        httpTaskManager = new HttpTaskManager();
    }

    @Test
    void getTaskById() throws Exception {
        Task task = httpTaskManager.getTaskById(1);
        assertNotNull(task);
        assertEquals("Task1", task.getName());
        assertEquals("Description1", task.getDescription());
    }

    @Test
    void getEpicById() throws Exception {
        Epic epic = httpTaskManager.getEpicById(1);
        assertNotNull(epic);
        assertEquals("Epic1", epic.getName());
        assertEquals("Description1", epic.getDescription());
    }

    @Test
    void getSubTaskById() throws Exception {
        SubTask subTask = httpTaskManager.getSubTaskById(1);
        assertNotNull(subTask);
        assertEquals("SubTask1", subTask.getName());
        assertEquals("Description1", subTask.getDescription());
    }

    @Test
    void removeTaskById() throws Exception {
        Task task = httpTaskManager.removeTaskById(1);
        assertNotNull(task);
        assertEquals("Task1", task.getName());
        assertEquals("Description1", task.getDescription());
        assertNull(httpTaskManager.getTaskById(1));
    }

    @Test
    void removeEpicById() throws Exception {
        Epic epic = httpTaskManager.removeEpicById(1);
        assertNotNull(epic);
        assertEquals("Epic1", epic.getName());
        assertEquals("Description1", epic.getDescription());
        assertNull(httpTaskManager.getEpicById(1));
    }

    @Test
    void removeSubTaskById() throws Exception {
        SubTask subTask = httpTaskManager.removeSubTaskById(1);
        assertNotNull(subTask);
        assertEquals("SubTask1", subTask.getName());
        assertEquals("Description1", subTask.getDescription());
        assertNull(httpTaskManager.getSubTaskById(1));
    }

    @Test
    void save() throws Exception {
        httpTaskManager.save();
        assertNotNull(httpTaskManager.getTaskById(1));
        assertNotNull(httpTaskManager.getEpicById(1));
        assertNotNull(httpTaskManager.getSubTaskById(1));
    }
}
