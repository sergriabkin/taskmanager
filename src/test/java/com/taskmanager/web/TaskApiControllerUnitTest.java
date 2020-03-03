package com.taskmanager.web;

import com.taskmanager.model.Task;
import com.taskmanager.service.TaskService;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
class TaskApiControllerUnitTest {

    public static final String TEST_TITLE_1 = "TestTitle1";
    public static final String TEST_DESCRIPTION_1 = "TestDescription1";
    public static final String TEST_TITLE_2 = "TestTitle2";
    public static final String TEST_DESCRIPTION_2 = "TestDescription2";
    public static final String TASKS_URL = "/api/tasks";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TestHeadersGenerator headersGenerator;

    @MockBean
    private TaskService service;

    @Test
    void addTask() {
        Task mockTask = new Task(TEST_TITLE_1, TEST_DESCRIPTION_1);
        HttpEntity<Task> httpEntity = new HttpEntity<>(mockTask, headersGenerator.withRole("ROLE_CSR"));

        BDDMockito.given(service.addTask(mockTask))
                .willReturn(mockTask);

        ResponseEntity<String> responseEntity =
                restTemplate.exchange(TASKS_URL, HttpMethod.POST, httpEntity, String.class);

        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Mockito.verify(service).addTask(Mockito.any(Task.class));

        Assert.assertThat(responseEntity.getBody(), CoreMatchers.containsString(TEST_TITLE_1));
        Assert.assertThat(responseEntity.getBody(), CoreMatchers.containsString(TEST_DESCRIPTION_1));
    }

    @Test
    void getAll() {
        List<Task> mockTasksList = new ArrayList<>();
        mockTasksList.add(new Task(TEST_TITLE_1, TEST_DESCRIPTION_1));
        mockTasksList.add(new Task(TEST_TITLE_2, TEST_DESCRIPTION_2));
        Page<Task> mockPage = new PageImpl<>(mockTasksList, PageRequest.of(0, 10), 1);

        BDDMockito.given(service.getAll(Mockito.any(Pageable.class)))
                .willReturn(mockPage);

        ResponseEntity<String> responseEntity =
                restTemplate.getForEntity(TASKS_URL, String.class);

        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Mockito.verify(service).getAll(Mockito.any(Pageable.class));
        Assert.assertThat(responseEntity.getBody(), CoreMatchers.containsString(TEST_TITLE_1));
        Assert.assertThat(responseEntity.getBody(), CoreMatchers.containsString(TEST_DESCRIPTION_1));
        Assert.assertThat(responseEntity.getBody(), CoreMatchers.containsString(TEST_TITLE_2));
        Assert.assertThat(responseEntity.getBody(), CoreMatchers.containsString(TEST_DESCRIPTION_2));
    }


    @Test
    void getOne() {
        Task mockTask = new Task(TEST_TITLE_1, TEST_DESCRIPTION_1);
        BDDMockito.given(service.getOne(100L)).willReturn(mockTask);

        ResponseEntity<String> responseEntity =
                restTemplate.getForEntity(TASKS_URL + "/100", String.class);

        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Mockito.verify(service).getOne(100L);
        Assert.assertThat(responseEntity.getBody(), CoreMatchers.containsString(TEST_TITLE_1));
        Assert.assertThat(responseEntity.getBody(), CoreMatchers.containsString(TEST_DESCRIPTION_1));
    }

    @Test
    void updateTask() {
        Task mockTask = new Task(TEST_TITLE_1, TEST_DESCRIPTION_1);
        HttpEntity<Task> httpEntity = new HttpEntity<>(mockTask, headersGenerator.withRole("ROLE_CSR"));

        BDDMockito.given(service.updateTask(mockTask, 100L))
                .willReturn(mockTask);

        ResponseEntity<String> responseEntity =
                restTemplate.exchange(TASKS_URL + "/100", HttpMethod.PUT, httpEntity, String.class);

        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Mockito.verify(service).updateTask(mockTask, 100L);
        Assert.assertThat(responseEntity.getBody(), CoreMatchers.containsString(TEST_TITLE_1));
        Assert.assertThat(responseEntity.getBody(), CoreMatchers.containsString(TEST_DESCRIPTION_1));
    }


    @Test
    void deleteTask() {
        Task mockTask = new Task(100L, TEST_TITLE_1, TEST_DESCRIPTION_1, 4);
        HttpEntity<Task> httpEntity = new HttpEntity<>(mockTask, headersGenerator.withRole("ROLE_CSR"));

        ResponseEntity<Void> responseEntity =
                restTemplate.exchange(TASKS_URL + "/100", HttpMethod.DELETE, httpEntity, Void.class);

        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Mockito.verify(service).deleteTask(100L);
    }
}