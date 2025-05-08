package lk.sliit.quick_task_manager.controller;

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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.test.properties")
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void clearData() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("register new user")
    public void testRegisterUser() throws Exception {

        var requestBody = """
                {
                  "name": "testuser",
                  "email": "test@example.com",
                  "password": "testpass"
                }
                """;

        var resultActions = mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("login with valid credentials")
    public void testLoginUser() throws Exception {

        // First register the user
        var registerBody = """
                {
                  "name": "testuser",
                  "email": "test@example.com",
                  "password": "testpass"
                }
                """;

        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerBody)
        ).andExpect(MockMvcResultMatchers.status().isOk());

        // Then login
        var loginBody = """
                {
                  "email": "test@example.com",
                  "password": "testpass"
                }
                """;

        var resultActions = mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginBody)
        );

        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Login with invalid credentials")
    public void testLoginInvalidUser() throws Exception {
        String loginBody = """
                {
                  "email": "invalid@example.com",
                  "password": "wrongpass"
                }
                """;

        mockMvc.perform(
                MockMvcRequestBuilders.post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginBody)
        ).andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
}
