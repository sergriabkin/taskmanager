package com.taskmanager.controller;

import com.taskmanager.model.Task;
import com.taskmanager.service.TaskService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @MockBean
    private TaskService service;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void addTask() {

    }

    @Test
    void getAll() throws Exception {
        List<Task> mockTasksList = new ArrayList<>();
        mockTasksList.add(new Task("TestTitle1", "TestDescription1"));
        mockTasksList.add(new Task("TestTitle2", "TestDescription2"));
        BDDMockito.given(service.getAll()).willReturn(mockTasksList);

        mockMvc.perform(MockMvcRequestBuilders.get("/tasks"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(CoreMatchers.containsString("TestTitle1")))
                .andExpect(MockMvcResultMatchers.content().string(CoreMatchers.containsString("TestTitle2")))
                .andExpect(MockMvcResultMatchers.content().string(CoreMatchers.containsString("TestDescription1")))
                .andExpect(MockMvcResultMatchers.content().string(CoreMatchers.containsString("TestDescription2")));
    }

    @Test
    void getOne() throws Exception {
        Task mockTask = new Task("TestTitle1", "TestDescription1");
        BDDMockito.given(service.getOne(100L)).willReturn(mockTask);

        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/100"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(CoreMatchers.containsString("TestTitle1")))
                .andExpect(MockMvcResultMatchers.content().string(CoreMatchers.containsString("TestDescription1")));
    }

    @Test
    void updateTask() {
    }

    @Test
    void deleteTask() {
    }
}