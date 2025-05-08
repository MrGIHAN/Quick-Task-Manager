package lk.sliit.quick_task_manager.service.impl;

import lk.sliit.quick_task_manager.controller.dto.requestDto.TaskRequestDto;
import lk.sliit.quick_task_manager.controller.dto.respnseDto.TaskResponseDto;
import lk.sliit.quick_task_manager.exception.ResourceNotFoundException;
import lk.sliit.quick_task_manager.model.Task;
import lk.sliit.quick_task_manager.model.User;
import lk.sliit.quick_task_manager.model.options.TaskStatus;
import lk.sliit.quick_task_manager.repository.TaskRepository;
import lk.sliit.quick_task_manager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application.test.properties")
@ExtendWith(MockitoExtension.class)
@Transactional
public class TaskServiceImplIntegrationTest {

    @Autowired
    private TaskServiceImpl taskService;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    public void setUp() {
        // Create and save a test user to the repository
        testUser = new User();
        testUser.setName("Test User");
        testUser.setEmail("testuser@example.com");
        testUser.setPassword("password123");
        userRepository.save(testUser);
    }

    @Test
    public void addTask_Success() throws ResourceNotFoundException {
        // Create TaskRequestDto
        TaskRequestDto taskRequestDto = new TaskRequestDto();
        taskRequestDto.setTitle("Test Task");
        taskRequestDto.setDescription("This is a test task.");
        taskRequestDto.setDueDate(LocalDate.parse("2025-12-31"));

        // Call addTask method
        TaskResponseDto taskResponseDto = taskService.addTask(testUser.getId(), taskRequestDto);

        // Assert that the task was added
        assertNotNull(taskResponseDto);
        assertEquals("Test Task", taskResponseDto.getTitle());
        assertEquals("This is a test task.", taskResponseDto.getDescription());
        assertEquals(LocalDate.parse("2025-12-31"), taskResponseDto.getDueDate());
        assertEquals("PENDING", taskResponseDto.getStatus());
    }

    @Test
    public void getUserTasks_Success() throws ResourceNotFoundException {
        // Given a task added for the test user
        Task task = new Task();
        task.setTitle("Task for User");
        task.setDescription("Description");
        task.setDueDate(LocalDate.now().plusDays(2));
        task.setStatus(TaskStatus.PENDING);
        task.setUser(testUser);
        taskRepository.save(task);

        // When retrieving tasks for the user
        List<TaskResponseDto> tasks = taskService.getUserTasks(testUser.getId());

        assertNotNull(tasks);
        assertEquals(1, tasks.size());
        assertEquals("Task for User", tasks.get(0).getTitle());
    }

    @Test
    public void getUserTasks_UserNotFound_ThrowsException() {
        // When attempting to retrieve tasks for a non-existent user
        assertThrows(ResourceNotFoundException.class, () -> taskService.getUserTasks(999L));
    }

    @Test
    public void updateTaskStatus_Success() throws ResourceNotFoundException {
        // Given a task for the test user
        Task task = new Task();
        task.setTitle("Task for Update");
        task.setDescription("Description");
        task.setDueDate(LocalDate.now().plusDays(2));
        task.setStatus(TaskStatus.PENDING);
        task.setUser(testUser);
        taskRepository.save(task);

        // When updating the task status
        taskService.updateTaskStatus(task.getId(), "COMPLETED");

        // Verify that the status was updated
        Task updatedTask = taskRepository.findById(task.getId()).orElseThrow();
        assertEquals(TaskStatus.COMPLETED, updatedTask.getStatus());
    }

    @Test
    public void updateTaskStatus_TaskNotFound_ThrowsException() {
        // When trying to update a task that doesn't exist
        assertThrows(ResourceNotFoundException.class, () -> taskService.updateTaskStatus(999L, "COMPLETED"));
    }

    @Test
    public void updateTaskStatus_InvalidStatus_ThrowsException() {
        // Given a task for the test user
        Task task = new Task();
        task.setTitle("Task for Invalid Status");
        task.setDescription("Description");
        task.setDueDate(LocalDate.now().plusDays(2));
        task.setStatus(TaskStatus.PENDING);
        task.setUser(testUser);
        taskRepository.save(task);

        // When attempting to update with an invalid status
        assertThrows(IllegalArgumentException.class, () -> taskService.updateTaskStatus(task.getId(), "INVALID"));
    }

    @Test
    public void deleteByTaskId_Success() throws ResourceNotFoundException {
        // Given a task for the test user
        Task task = new Task();
        task.setTitle("Task to Delete");
        task.setDescription("Description");
        task.setDueDate(LocalDate.now().plusDays(2));
        task.setStatus(TaskStatus.PENDING);
        task.setUser(testUser);
        taskRepository.save(task);

        // When deleting the task
        taskService.deleteByTaskId(task.getId());

        // Verify that the task no longer exists
        assertFalse(taskRepository.existsById(task.getId()));
    }

    @Test
    public void deleteByTaskId_TaskNotFound_ThrowsException() {
        // When trying to delete a task that doesn't exist
        assertThrows(ResourceNotFoundException.class, () -> taskService.deleteByTaskId(999L));
    }
}
