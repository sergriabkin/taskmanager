package com.taskmanager.controller;

import com.taskmanager.model.Task;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskApiControllerIntegrationTest {

    public static final String TEST_TITLE_1 = "TestTitle1";
    public static final String TEST_DESCRIPTION_1 = "TestDescription1";
    public static final String TEST_TITLE_2 = "TestTitle2";
    public static final String TEST_DESCRIPTION_2 = "TestDescription2";

    @Autowired
    private TaskApiController instance;

    @Test
    void addTask() {
        Task actualTask = instance.addTask(new Task(TEST_TITLE_1, TEST_DESCRIPTION_1));

        Assert.assertNotNull(actualTask);
        Assert.assertNotNull(actualTask.getTaskId());
        Assert.assertEquals(actualTask.getTitle(), TEST_TITLE_1);
        Assert.assertEquals(actualTask.getDescription(), TEST_DESCRIPTION_1);
    }

    @Test
    void getAll() {
        Task mockTask1 = new Task(TEST_TITLE_1, TEST_DESCRIPTION_1);
        Task mockTask2 = new Task(TEST_TITLE_2, TEST_DESCRIPTION_2);

        instance.addTask(mockTask1);
        instance.addTask(mockTask2);

        List<Task> actualTasksList = instance.getAll(Mockito.mock(Pageable.class)).get()
                .collect(Collectors.toList());

        Assert.assertThat(actualTasksList, CoreMatchers.hasItems(mockTask1, mockTask2));
        Assert.assertThat(actualTasksList, CoreMatchers.everyItem(CoreMatchers.notNullValue(Task.class)));
        List<Long> taskIds = actualTasksList.stream().map(Task::getTaskId).collect(Collectors.toList());
        Assert.assertThat(taskIds, CoreMatchers.everyItem(CoreMatchers.notNullValue()));
    }

    @Test
    void getOne() {
        Task addedTask = instance.addTask(new Task(TEST_TITLE_1, TEST_DESCRIPTION_1));

        Task actualTask = instance.getOne(addedTask.getTaskId());

        Assert.assertNotNull(actualTask);
        Assert.assertNotNull(actualTask.getTaskId());
        Assert.assertEquals(TEST_TITLE_1, actualTask.getTitle());
        Assert.assertEquals(TEST_DESCRIPTION_1, actualTask.getDescription());
    }

    @Test
    void updateTask() {
        Task oldTask = instance.addTask(new Task(TEST_TITLE_1, TEST_DESCRIPTION_1));
        Long oldTaskId = oldTask.getTaskId();

        Task actualTask = instance.updateTask(new Task(TEST_TITLE_1, TEST_DESCRIPTION_2), oldTaskId);

        Assert.assertNotNull(actualTask);
        Assert.assertNotNull(actualTask.getTaskId());
        Assert.assertEquals(TEST_TITLE_1, actualTask.getTitle());
        Assert.assertEquals(TEST_DESCRIPTION_2, actualTask.getDescription());
        List<Task> allTasks = instance.getAll(Mockito.mock(Pageable.class)).getContent();
        Assert.assertThat(allTasks, CoreMatchers.not(CoreMatchers.hasItem(oldTask)));
    }

    @Test
    void deleteTask() {
        Task task = instance.addTask(new Task(TEST_TITLE_1, TEST_DESCRIPTION_1));
        Long taskId = task.getTaskId();

        instance.deleteTask(taskId);

        List<Task> allTasks = instance.getAll(Mockito.mock(Pageable.class)).getContent();
        Assert.assertThat(allTasks, CoreMatchers.not(CoreMatchers.hasItem(task)));
    }
}
