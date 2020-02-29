package com.taskmanager.repository;

import com.taskmanager.model.Task;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TaskRepositoryIntegrationTest {

    public static final String TEST_TITLE_1 = "TestTitle1";
    public static final String TEST_DESCRIPTION_1 = "TestDescription1";

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TaskRepository instance;

    @Test
    public void findByTitle() {
        Task testTask = new Task(TEST_TITLE_1, TEST_DESCRIPTION_1);
        entityManager.persist(testTask);

        Task actualTask = instance.findByTitle(TEST_TITLE_1).get();

        assertNotNull(actualTask);
        assertNotNull(actualTask.getTaskId());
        assertThat(actualTask.getTitle(), CoreMatchers.is(TEST_TITLE_1));
    }
}