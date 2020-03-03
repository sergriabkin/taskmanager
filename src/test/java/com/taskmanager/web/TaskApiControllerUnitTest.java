package com.taskmanager.web;

import com.taskmanager.dto.TaskDto;
import com.taskmanager.model.Task;
import com.taskmanager.service.TaskService;
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
        TaskDto mockTaskDto = new TaskDto(TEST_TITLE_1, TEST_DESCRIPTION_1);
        HttpEntity<TaskDto> httpEntity = new HttpEntity<>(mockTaskDto, headersGenerator.withRole("ROLE_CSR"));

        BDDMockito.given(service.addTask(Mockito.any(Task.class))).willReturn(Mockito.mock(Task.class));

        ResponseEntity<String> responseEntity =
                restTemplate.exchange(TASKS_URL, HttpMethod.POST, httpEntity, String.class);

        Mockito.verify(service).addTask(Mockito.any(Task.class));

        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Mockito.verify(service).addTask(Mockito.any(Task.class));
    }

    @Test
    void getAll() {
        List<Task> mockTasksList = new ArrayList<>();
        mockTasksList.add(new Task(100L, TEST_TITLE_1, TEST_DESCRIPTION_1, 4));
        mockTasksList.add(new Task(100L, TEST_TITLE_2, TEST_DESCRIPTION_2, 3));
        Page<Task> mockPage = new PageImpl<>(mockTasksList, PageRequest.of(0, 10), 1);

        Mockito.when(service.getAll(Mockito.any(Pageable.class))).thenReturn(mockPage);

        ResponseEntity<String> responseEntity =
                restTemplate.getForEntity(TASKS_URL, String.class);

        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Mockito.verify(service).getAll(Mockito.any(Pageable.class));
    }


    @Test
    void getOne() {
        BDDMockito.given(service.getOne(100L)).willReturn(Mockito.mock(Task.class));

        ResponseEntity<String> responseEntity =
                restTemplate.getForEntity(TASKS_URL + "/100", String.class);

        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Mockito.verify(service).getOne(100L);
    }

    @Test
    void updateTask() {
        TaskDto taskDto = new TaskDto(TEST_TITLE_1, TEST_DESCRIPTION_1);
        HttpEntity<TaskDto> httpEntity = new HttpEntity<>(taskDto, headersGenerator.withRole("ROLE_CSR"));

        BDDMockito.given(service.updateTask(Mockito.any(Task.class), Mockito.anyLong()))
                .willReturn(Mockito.mock(Task.class));

        ResponseEntity<String> responseEntity =
                restTemplate.exchange(TASKS_URL + "/100", HttpMethod.PUT, httpEntity, String.class);

        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Mockito.verify(service).updateTask(Mockito.any(Task.class), Mockito.anyLong());
    }


    @Test
    void deleteTask() {
        TaskDto taskDto = new TaskDto(TEST_TITLE_1, TEST_DESCRIPTION_1);
        HttpEntity<TaskDto> httpEntity = new HttpEntity<>(taskDto, headersGenerator.withRole("ROLE_CSR"));

        ResponseEntity<Void> responseEntity =
                restTemplate.exchange(TASKS_URL + "/100", HttpMethod.DELETE, httpEntity, Void.class);

        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Mockito.verify(service).deleteTask(100L);
    }
}