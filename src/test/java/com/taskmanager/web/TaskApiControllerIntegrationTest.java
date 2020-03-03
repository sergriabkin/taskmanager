package com.taskmanager.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskmanager.dto.TaskDto;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.HashMap;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class TaskApiControllerIntegrationTest {

    public static final String TEST_TITLE_1 = "TestTitle1";
    public static final String TEST_DESCRIPTION_1 = "TestDescription1";
    public static final String TEST_TITLE_2 = "TestTitle2";
    public static final String TEST_DESCRIPTION_2 = "TestDescription2";
    public static final String TASKS_URL = "/api/tasks";

    @Autowired
    private TestHeadersGenerator headersGenerator;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void addTask() throws JsonProcessingException {
        TaskDto inputTaskDto = new TaskDto(TEST_TITLE_1, TEST_DESCRIPTION_1, 4);
        HttpEntity<TaskDto> httpEntity = new HttpEntity<>(inputTaskDto, headersGenerator.withRole("ROLE_CSR"));

        ResponseEntity<String> responseEntity =
                restTemplate.exchange(TASKS_URL, HttpMethod.POST, httpEntity, String.class);

        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assert.assertNotNull(responseEntity.getBody());
        HashMap<String, Object> response = new ObjectMapper().readValue(responseEntity.getBody(), HashMap.class);
        Assert.assertThat(response.get("title").toString(), CoreMatchers.is(TEST_TITLE_1));
        Assert.assertThat(response.get("description").toString(), CoreMatchers.is(TEST_DESCRIPTION_1));
        Assert.assertThat(response.get("priority").toString(), CoreMatchers.is("4"));
        Assert.assertThat(response.get("_links").toString(), CoreMatchers.containsString(TASKS_URL));
    }

    @Test
    void getAll() {
        ResponseEntity<String> responseEntity =
                restTemplate.getForEntity(TASKS_URL, String.class);

        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assert.assertNotNull(responseEntity.getBody());
    }

//    @Test
//    void getOne() {
//        Task addedTask = instance.addTask(new Task(TEST_TITLE_1, TEST_DESCRIPTION_1));
//
//        Task actualTask = instance.getOne(addedTask.getTaskId());
//
//        Assert.assertNotNull(actualTask);
//        Assert.assertNotNull(actualTask.getTaskId());
//        Assert.assertEquals(TEST_TITLE_1, actualTask.getTitle());
//        Assert.assertEquals(TEST_DESCRIPTION_1, actualTask.getDescription());
//    }
//
//    @Test
//    void updateTask() {
//        Task oldTask = instance.addTask(new Task(TEST_TITLE_1, TEST_DESCRIPTION_1));
//        Long oldTaskId = oldTask.getTaskId();
//
//        Task actualTask = instance.updateTask(new Task(TEST_TITLE_1, TEST_DESCRIPTION_2), oldTaskId);
//
//        Assert.assertNotNull(actualTask);
//        Assert.assertNotNull(actualTask.getTaskId());
//        Assert.assertEquals(TEST_TITLE_1, actualTask.getTitle());
//        Assert.assertEquals(TEST_DESCRIPTION_2, actualTask.getDescription());
//        List<Task> allTasks = instance.getAll(Mockito.mock(Pageable.class)).getContent();
//        Assert.assertThat(allTasks, CoreMatchers.not(CoreMatchers.hasItem(oldTask)));
//    }
//
//    @Test
//    void deleteTask() {
//        Task task = instance.addTask(new Task(TEST_TITLE_1, TEST_DESCRIPTION_1));
//        Long taskId = task.getTaskId();
//
//        instance.deleteTask(taskId);
//
//        List<Task> allTasks = instance.getAll(Mockito.mock(Pageable.class)).getContent();
//        Assert.assertThat(allTasks, CoreMatchers.not(CoreMatchers.hasItem(task)));
//    }
}
