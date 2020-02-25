package com.taskmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskmanager.model.Task;
import com.taskmanager.service.TaskService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(TaskApiController.class)
class TaskApiControllerTest {

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);
    public static final String TEST_TITLE_1 = "TestTitle1";
    public static final String TEST_DESCRIPTION_1 = "TestDescription1";
    public static final String TEST_TITLE_2 = "TestTitle2";
    public static final String TEST_DESCRIPTION_2 = "TestDescription2";
    public static final String ROOT_PATH = "/api";


    @MockBean
    private TaskService service;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void addTask() throws Exception {
        Task mockTask = new Task(TEST_TITLE_1, TEST_DESCRIPTION_1);
        String requestJson = new ObjectMapper().writer().writeValueAsString(mockTask);
        BDDMockito.given(service.addTask(mockTask)).willReturn(mockTask);

        mockMvc.perform(MockMvcRequestBuilders.post(ROOT_PATH + "/tasks")
                .contentType(APPLICATION_JSON_UTF8)
                .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(CoreMatchers.containsString(TEST_TITLE_1)))
                .andExpect(MockMvcResultMatchers.content().string(CoreMatchers.containsString(TEST_DESCRIPTION_1)));
    }

    @Test
    void getAll() throws Exception {
        List<Task> mockTasksList = new ArrayList<>();
        mockTasksList.add(new Task(TEST_TITLE_1, TEST_DESCRIPTION_1));
        mockTasksList.add(new Task(TEST_TITLE_2, TEST_DESCRIPTION_2));

        BDDMockito.given(service.getAll(Mockito.mock(Pageable.class)))
                .willReturn(new PageImpl<>(mockTasksList));

        mockMvc.perform(MockMvcRequestBuilders.get(ROOT_PATH + "/tasks"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getOne() throws Exception {
        Task mockTask = new Task(TEST_TITLE_1, TEST_DESCRIPTION_1);
        BDDMockito.given(service.getOne(100L)).willReturn(mockTask);

        mockMvc.perform(MockMvcRequestBuilders.get(ROOT_PATH + "/tasks/100"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(CoreMatchers.containsString(TEST_TITLE_1)))
                .andExpect(MockMvcResultMatchers.content().string(CoreMatchers.containsString(TEST_DESCRIPTION_1)));
    }

    @Test
    void updateTask() {
    }

    @Test
    void deleteTask() {
    }
}