package lk.sliit.quick_task_manager.repository;

import lk.sliit.quick_task_manager.model.Task;
import lk.sliit.quick_task_manager.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestPropertySource(locations = "classpath:application.test.properties")
public class TaskRepositoryTest {

    @Mock
    private TaskRepository taskRepository;

    private User testUser;
    private Task task1;
    private Task task2;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setName("Test User");
        testUser.setPassword("password123");

        task1 = new Task();
        task1.setId(1L);
        task1.setTitle("Task 1");
        task1.setUser(testUser);

        task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Task 2");
        task2.setUser(testUser);
    }

    @Test
    void testFindByUser() {
        List<Task> expectedTasks = Arrays.asList(task1, task2);

        when(taskRepository.findByUser(testUser)).thenReturn(expectedTasks);

        List<Task> foundTasks = taskRepository.findByUser(testUser);

        assertEquals(2, foundTasks.size());
        assertEquals("Task 1", foundTasks.get(0).getTitle());
        assertEquals("Task 2", foundTasks.get(1).getTitle());
    }
}
