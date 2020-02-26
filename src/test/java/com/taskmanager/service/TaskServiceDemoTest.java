package com.taskmanager.service;

import com.taskmanager.exception.TaskNotFoundException;
import com.taskmanager.model.Task;
import com.taskmanager.repository.TaskRepository;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class TaskServiceDemoTest {

    public static final String TEST_TITLE_1 = "TestTitle1";
    public static final String TEST_DESCRIPTION_1 = "TestDescription1";
    public static final String TEST_TITLE_2 = "TestTitle2";
    public static final String TEST_DESCRIPTION_2 = "TestDescription2";

    @Mock
    private TaskRepository repository;

    @InjectMocks
    private TaskServiceDemo instance;

    @Before
    public void before() {
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
        Task mockTask1 = new Task(TEST_TITLE_1, TEST_DESCRIPTION_1);
        Task mockTask2 = new Task(TEST_TITLE_2, TEST_DESCRIPTION_2);
        List<Task> mockTasks = Arrays.asList(mockTask1, mockTask2);
        PageImpl<Task> mockTasksPage = new PageImpl<>(mockTasks);

        Mockito.when(repository.findAll(Mockito.any(Pageable.class))).thenReturn(mockTasksPage);

        List<Task> actualTasksList = instance.getAll(Mockito.mock(Pageable.class)).get()
                .collect(Collectors.toList());

        Mockito.verify(repository).findAll(Mockito.any(Pageable.class));
        Assert.assertThat(actualTasksList, CoreMatchers.hasItems(mockTask1, mockTask2));
    }

    @Test
    void getAllUrgentFiltering() {
        Task mockTaskUrgent = new Task(TEST_TITLE_1, TEST_DESCRIPTION_1, TaskServiceDemo.URGENT_PRIORITY);
        Task mockTaskLessThanUrgent = new Task(TEST_TITLE_2, TEST_DESCRIPTION_2, TaskServiceDemo.URGENT_PRIORITY - 1);
        Task mockTaskMoreThanUrgent = new Task(TEST_TITLE_1, TEST_DESCRIPTION_2, TaskServiceDemo.URGENT_PRIORITY + 1);
        List<Task> mockTasks = Arrays.asList(mockTaskUrgent, mockTaskLessThanUrgent, mockTaskMoreThanUrgent);
        PageImpl<Task> mockTasksPage = new PageImpl<>(mockTasks);

        Mockito.when(repository.findAll(Mockito.any(Pageable.class))).thenReturn(mockTasksPage);

        List<Task> actualTasksList = instance.getAllUrgent(Mockito.mock(Pageable.class)).get()
                .collect(Collectors.toList());

        Mockito.verify(repository).findAll(Mockito.any(Pageable.class));
        Assert.assertThat(actualTasksList, CoreMatchers.hasItems(mockTaskUrgent, mockTaskMoreThanUrgent));
        Assert.assertThat(actualTasksList, CoreMatchers.not(CoreMatchers.hasItems(mockTaskLessThanUrgent)));
    }

    @Test
    void getOneHappyPath() {
        long testId = 1L;
        Task mockTask = new Task(testId, TEST_TITLE_1, TEST_DESCRIPTION_1, TaskServiceDemo.URGENT_PRIORITY);
        Mockito.when(repository.findById(testId)).thenReturn(Optional.of(mockTask));

        Task actualTask = instance.getOne(testId);

        Mockito.verify(repository).findById(testId);
        Assert.assertEquals(mockTask, actualTask);
    }

    @Test
    void getOneExceptionTest() {
        long testId = 1L;

        Mockito.when(repository.findById(testId)).thenReturn(Optional.empty());

        Assertions.assertThrows(TaskNotFoundException.class, () -> instance.getOne(testId));
    }

    @Test
    void updateTaskWithCreation() {
        long testId = 1L;
        Task mockTask = new Task(TEST_TITLE_1, TEST_DESCRIPTION_1);

        Mockito.when(repository.findById(testId)).thenReturn(Optional.empty());
        Mockito.when(repository.save(mockTask)).thenReturn(mockTask);

        Task actualTask = instance.updateTask(mockTask, testId);

        Mockito.verify(repository).findById(testId);
        Mockito.verify(repository).save(mockTask);

        Assert.assertEquals(mockTask, actualTask);
    }

    @Test
    void updateTaskWithReplacement() {
        long testId = 1L;
        Task mockOldTask = new Task(TEST_TITLE_1, TEST_DESCRIPTION_1);
        Task mockNewTask = new Task(TEST_TITLE_1, TEST_DESCRIPTION_2);

        Mockito.when(repository.findById(testId)).thenReturn(Optional.of(mockOldTask));
        Mockito.doNothing().when(repository).deleteById(testId);
        Mockito.when(repository.save(mockNewTask)).thenReturn(mockNewTask);

        Task actualTask = instance.updateTask(mockNewTask, testId);

        Mockito.verify(repository).findById(testId);
        Mockito.verify(repository).deleteById(testId);
        Mockito.verify(repository).save(mockNewTask);

        Assert.assertEquals(mockNewTask, actualTask);
    }

    @Test
    void deleteTask() {
        long testId = 1L;
        Mockito.doNothing().when(repository).deleteById(testId);

        instance.deleteTask(testId);

        Mockito.verify(repository).deleteById(testId);
    }
}