package lk.sliit.quick_task_manager.service.impl;

import lk.sliit.quick_task_manager.controller.dto.requestDto.UserLoginRequestDto;
import lk.sliit.quick_task_manager.controller.dto.requestDto.UserRegisterRequestDto;
import lk.sliit.quick_task_manager.controller.dto.respnseDto.UserResponseDto;
import lk.sliit.quick_task_manager.exception.ResourceNotFoundException;
import lk.sliit.quick_task_manager.exception.UserAlreadyExistsException;
import lk.sliit.quick_task_manager.model.User;
import lk.sliit.quick_task_manager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application.test.properties")
@ExtendWith(MockitoExtension.class)
@Transactional
public class UserServiceImplIntegrationTest {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    public void setUp() {
        testUser = new User();
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password123");

        // Save the user to DB for testing purposes
        userRepository.save(testUser);
    }

    @Test
    public void register_Success() throws UserAlreadyExistsException {
        UserRegisterRequestDto request = new UserRegisterRequestDto();
        request.setName("New User");
        request.setEmail("newuser@example.com");
        request.setPassword("newpassword");

        // Call the service layer
        UserResponseDto response = userService.register(request);

        assertNotNull(response);
        assertEquals("New User", response.getName());
        assertEquals("newuser@example.com", response.getEmail());
    }

    @Test
    public void register_UserAlreadyExists_ThrowsException() {
        UserRegisterRequestDto request = new UserRegisterRequestDto();
        request.setEmail("test@example.com");

        // Call the service layer
        assertThrows(UserAlreadyExistsException.class, () -> userService.register(request));
    }

    @Test
    public void login_Success() throws ResourceNotFoundException {
        UserLoginRequestDto request = new UserLoginRequestDto();
        request.setEmail("test@example.com");
        request.setPassword("password123");

        // Call the service layer
        UserResponseDto response = userService.login(request);

        assertNotNull(response);
        assertEquals("Test User", response.getName());
        assertEquals("test@example.com", response.getEmail());
    }

    @Test
    public void login_InvalidPassword_ThrowsException() {
        UserLoginRequestDto request = new UserLoginRequestDto();
        request.setEmail("test@example.com");
        request.setPassword("wrongpassword");

        // Call the service layer
        assertThrows(ResourceNotFoundException.class, () -> userService.login(request));
    }

    @Test
    public void login_UserNotFound_ThrowsException() {
        UserLoginRequestDto request = new UserLoginRequestDto();
        request.setEmail("nonexistent@example.com");
        request.setPassword("password123");

        // Call the service layer
        assertThrows(ResourceNotFoundException.class, () -> userService.login(request));
    }
}
