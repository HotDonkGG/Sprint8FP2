package test.java;

import main.java.managers.task.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager>{

    @BeforeEach
    public void init(){
        manager = new  InMemoryTaskManager();
    }
}

