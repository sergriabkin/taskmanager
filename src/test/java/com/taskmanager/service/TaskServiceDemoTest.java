package com.taskmanager.service;

import com.taskmanager.model.Task;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.utils.TaskFilterUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;


@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class TaskServiceDemoTest {

    public static final String TEST_TITLE_1 = "TestTitle1";
    public static final String TEST_DESCRIPTION_1 = "TestDescription1";
    public static final String TEST_TITLE_2 = "TestTitle2";
    public static final String TEST_DESCRIPTION_2 = "TestDescription2";

    @Mock
    private TaskRepository repository;

    @Mock
    private TaskFilterUtil taskFilter;

    @InjectMocks
    private TaskServiceDemo instance;

    @Before
    void before(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void addTask() {
        Task mockTask = new Task(TEST_TITLE_1, TEST_DESCRIPTION_1);
        Mockito.when(repository.save(Mockito.any(Task.class))).thenReturn(mockTask);

        Task actualTask = instance.addTask(mockTask);

        Mockito.verify(repository).save(mockTask);
        Assert.assertEquals(TEST_TITLE_1, actualTask.getTitle());
        Assert.assertEquals(TEST_DESCRIPTION_1, actualTask.getDescription());
    }

    @Test
    void getAll() {
    }

    @Test
    void getAllUrgent() {
    }

    @Test
    void getOne() {
    }

    @Test
    void updateTask() {
    }

    @Test
    void deleteTask() {
    }
}