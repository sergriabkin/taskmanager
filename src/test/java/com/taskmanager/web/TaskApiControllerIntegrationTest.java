package com.taskmanager.web;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.taskmanager.dto.TaskDto;
import com.taskmanager.exception.TaskNotFoundException;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@DatabaseSetup("classpath:test-datasets.xml")
class TaskApiControllerIntegrationTest {

    public static final Long TEST_ID_1 = 1L;
    public static final String TEST_TITLE_1 = "TestTitle1";
    public static final String TEST_DESCRIPTION_1 = "TestDescription1";
    public static final Integer TEST_PRIORITY_1 = 5;

    public static final Long TEST_ID_2 = 2L;
    public static final String TEST_TITLE_2 = "TestTitle2";
    public static final String TEST_DESCRIPTION_2 = "TestDescription2";
    public static final Integer TEST_PRIORITY_2 = 4;

    public static final Long TEST_ID_3 = 3L;
    public static final String TEST_TITLE_3 = "TestTitle3";
    public static final String TEST_DESCRIPTION_3 = "TestDescription3";
    public static final Integer TEST_PRIORITY_3 = 3;

    public static final String TASKS_URL = "/api/tasks";

    @Autowired
    private TestJwtHeadersGenerator headersGenerator;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void addTask() {
        TaskDto inputTaskDto = new TaskDto(TEST_TITLE_3, TEST_DESCRIPTION_3, TEST_PRIORITY_3);
        HttpEntity<TaskDto> httpEntity = new HttpEntity<>(inputTaskDto, headersGenerator.withRole("ROLE_CSR"));

        ResponseEntity<String> responseEntity =
                restTemplate.exchange(TASKS_URL, HttpMethod.POST, httpEntity, String.class);

        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assert.assertNotNull(responseEntity.getBody());

        assertThirdTaskInResponse(responseEntity);
    }

    @Test
    void getAll() {
        ResponseEntity<String> responseEntity =
                restTemplate.getForEntity(TASKS_URL, String.class);

        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assert.assertNotNull(responseEntity.getBody());

        assertFirstTaskInResponse(responseEntity);
        assertSecondTaskInResponse(responseEntity);
    }

    @Test
    void getAllUrgent() {
        ResponseEntity<String> responseEntity =
                restTemplate.getForEntity(TASKS_URL + "/urgent", String.class);

        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assert.assertNotNull(responseEntity.getBody());

        assertFirstTaskInResponse(responseEntity);
    }

    @Test
    void getOne() {
        ResponseEntity<String> responseEntity =
                restTemplate.getForEntity(TASKS_URL + "/" + TEST_ID_1, String.class);

        System.out.println(responseEntity.getBody());

        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assert.assertNotNull(responseEntity.getBody());

        assertFirstTaskInResponse(responseEntity);
    }

    @Test
    void updateTask() {
        TaskDto inputTaskDto = new TaskDto(TEST_TITLE_3, TEST_DESCRIPTION_3, TEST_PRIORITY_3);
        HttpEntity<TaskDto> httpEntity = new HttpEntity<>(inputTaskDto, headersGenerator.withRole("ROLE_CSR"));

        ResponseEntity<String> responseEntity =
                restTemplate.exchange(TASKS_URL + "/" + TEST_ID_2, HttpMethod.PUT, httpEntity, String.class);

        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assert.assertNotNull(responseEntity.getBody());

        assertThirdTaskInResponse(responseEntity);

        assertTaskNotFound(TEST_ID_2);
    }

    @Test
    void deleteTask() {
        HttpEntity<TaskDto> httpEntity = new HttpEntity<>(headersGenerator.withRole("ROLE_CSR"));
        restTemplate.exchange(TASKS_URL + "/" + TEST_ID_1, HttpMethod.DELETE, httpEntity, Void.class);

        assertTaskNotFound(TEST_ID_1);
    }

    private void assertTaskNotFound(Long id) {
        ResponseEntity<String> responseEntity =
                restTemplate.getForEntity(TASKS_URL + "/" + id, String.class);
        Assert.assertThat(responseEntity.getBody(), CoreMatchers.containsString(TaskNotFoundException.TASK_NOT_FOUND_MESSAGE));
    }

    private void assertFirstTaskInResponse(ResponseEntity<String> responseEntity) {
        Assert.assertThat(responseEntity.getBody(), CoreMatchers.containsString(TEST_TITLE_1));
        Assert.assertThat(responseEntity.getBody(), CoreMatchers.containsString(TEST_DESCRIPTION_1));
        Assert.assertThat(responseEntity.getBody(), CoreMatchers.containsString(TEST_PRIORITY_1.toString()));
    }

    private void assertSecondTaskInResponse(ResponseEntity<String> responseEntity) {
        Assert.assertThat(responseEntity.getBody(), CoreMatchers.containsString(TEST_TITLE_2));
        Assert.assertThat(responseEntity.getBody(), CoreMatchers.containsString(TEST_DESCRIPTION_2));
        Assert.assertThat(responseEntity.getBody(), CoreMatchers.containsString(TEST_PRIORITY_2.toString()));
    }

    private void assertThirdTaskInResponse(ResponseEntity<String> responseEntity) {
        Assert.assertThat(responseEntity.getBody(), CoreMatchers.containsString(TEST_TITLE_3));
        Assert.assertThat(responseEntity.getBody(), CoreMatchers.containsString(TEST_DESCRIPTION_3));
        Assert.assertThat(responseEntity.getBody(), CoreMatchers.containsString(TEST_PRIORITY_3.toString()));
    }

}
