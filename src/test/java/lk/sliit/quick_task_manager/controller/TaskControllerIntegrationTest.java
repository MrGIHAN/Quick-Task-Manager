package lk.sliit.quick_task_manager.controller;

import com.jayway.jsonpath.JsonPath;
import lk.sliit.quick_task_manager.model.User;
import lk.sliit.quick_task_manager.repository.TaskRepository;
import lk.sliit.quick_task_manager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.test.properties")
class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    private Long userId;

    @BeforeEach
    public void setUp() {
        taskRepository.deleteAll();
        userRepository.deleteAll();

        User user = new User();
        user.setName("taskuser");
        user.setEmail("task@example.com");
        user.setPassword("taskpass");
        userRepository.save(user);
        userId = user.getId();
    }

    @Test
    @DisplayName("Add a new task with all fields")
    public void testAddTask() throws Exception {
        String taskBody = String.format("""
                {
                  "title": "Integration Task",
                  "description": "Integration test description",
                  "dueDate": "%s"
                }
                """, LocalDate.now().plusDays(1));

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/users/{userId}/tasks", userId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(taskBody)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Integration Task"))
                .andExpect(jsonPath("$.description").value("Integration test description"))
                .andExpect(jsonPath("$.dueDate").value(LocalDate.now().plusDays(1).toString()))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    @DisplayName("Get all tasks for a user")
    public void testGetTasksForUser() throws Exception {
        String taskBody = String.format("""
                {
                  "title": "Read tasks",
                  "description": "Read test tasks",
                  "dueDate": "%s"
                }
                """, LocalDate.now().plusDays(2));

        mockMvc.perform(
                MockMvcRequestBuilders.post("/users/{userId}/tasks", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskBody)
        ).andExpect(status().isOk());

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/users/{userId}/tasks", userId)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Read tasks"));
    }

    @Test
    @DisplayName("Update task status to Completed")
    public void testUpdateTaskStatus() throws Exception {
        String taskBody = String.format("""
                {
                  "title": "Status Update Task",
                  "description": "To be completed",
                  "dueDate": "%s"
                }
                """, LocalDate.now().plusDays(3));

        var result = mockMvc.perform(
                MockMvcRequestBuilders.post("/users/{userId}/tasks", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskBody)
        ).andExpect(status().isOk()).andReturn();

        Integer taskIdInt = JsonPath.read(result.getResponse().getContentAsString(), "$.id");
        Long taskId = taskIdInt.longValue();

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/tasks/{taskId}/status", taskId)
                                .param("status", "COMPLETED")
                ).andExpect(status().isOk())
                .andExpect(content().string("Task status updated successfully."));
    }

    @Test
    @DisplayName("Delete a task by ID")
    public void testDeleteTaskById() throws Exception {
        String taskBody = String.format("""
                {
                  "title": "Delete Task",
                  "description": "Task to delete",
                  "dueDate": "%s"
                }
                """, LocalDate.now().plusDays(5));

        var result = mockMvc.perform(
                MockMvcRequestBuilders.post("/users/{userId}/tasks", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskBody)
        ).andExpect(status().isOk()).andReturn();

        Integer taskIdInt = JsonPath.read(result.getResponse().getContentAsString(), "$.id");
        Long taskId = taskIdInt.longValue();

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/tasks/{task-id}", taskId)
        ).andExpect(status().isOk()); // or isNoContent() based on your implementation
    }

    @Test
    @DisplayName("Add task with past due date should fail validation")
    public void testAddTaskWithPastDueDate() throws Exception {
        String taskBody = String.format("""
                {
                  "title": "Invalid Task",
                  "description": "Should fail",
                  "dueDate": "%s"
                }
                """, LocalDate.now().minusDays(1));

        mockMvc.perform(
                MockMvcRequestBuilders.post("/users/{userId}/tasks", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskBody)
        ).andExpect(status().isBadRequest());
    }
}
