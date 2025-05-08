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
import org.mockito.*;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestPropertySource(locations = "classpath:application.test.properties")
class TaskServiceImplUnitTest {

    @InjectMocks
    private TaskServiceImpl taskService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password");
    }

    @Test
    void addTask_ShouldAddSuccessfully() throws ResourceNotFoundException {
        TaskRequestDto requestDto = new TaskRequestDto();
        requestDto.setTitle("Unit Test Task");
        requestDto.setDescription("Description");
        requestDto.setDueDate(LocalDate.of(2025, 12, 31));

        Task savedTask = new Task();
        savedTask.setId(10L);
        savedTask.setTitle(requestDto.getTitle());
        savedTask.setDescription(requestDto.getDescription());
        savedTask.setDueDate(requestDto.getDueDate());
        savedTask.setStatus(TaskStatus.PENDING);
        savedTask.setUser(testUser);

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

        TaskResponseDto result = taskService.addTask(1L, requestDto);

        assertNotNull(result);
        assertEquals("Unit Test Task", result.getTitle());
        assertEquals("PENDING", result.getStatus());
        assertEquals("Description", result.getDescription());
        assertEquals(LocalDate.of(2025, 12, 31), result.getDueDate());

    }

    @Test
    void getUserTasks_ShouldReturnTasks() throws ResourceNotFoundException {
        Task task = new Task();
        task.setId(5L);
        task.setTitle("Task Title");
        task.setDescription("Desc");
        task.setDueDate(LocalDate.now());
        task.setStatus(TaskStatus.PENDING);
        task.setUser(testUser);

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(taskRepository.findByUser(testUser)).thenReturn(List.of(task));

        List<TaskResponseDto> result = taskService.getUserTasks(1L);

        assertEquals(1, result.size());
        assertEquals("Task Title", result.get(0).getTitle());
    }

    @Test
    void updateTaskStatus_ShouldUpdateStatus() throws ResourceNotFoundException {
        Task task = new Task();
        task.setId(100L);
        task.setStatus(TaskStatus.PENDING);

        when(taskRepository.findById(100L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        taskService.updateTaskStatus(100L, "COMPLETED");

        assertEquals(TaskStatus.COMPLETED, task.getStatus());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void deleteByTaskId_ShouldDeleteIfExists() throws ResourceNotFoundException {
        when(taskRepository.existsById(200L)).thenReturn(true);

        taskService.deleteByTaskId(200L);

        verify(taskRepository, times(1)).deleteById(200L);
    }

    @Test
    void deleteByTaskId_ShouldThrowIfNotExists() {
        when(taskRepository.existsById(999L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            taskService.deleteByTaskId(999L);
        });
    }
}
