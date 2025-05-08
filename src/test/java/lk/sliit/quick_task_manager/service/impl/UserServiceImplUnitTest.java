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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestPropertySource(locations = "classpath:application.test.properties")
class UserServiceImplUnitTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPassword("password123");
    }

    @Test
    void register_Success() throws UserAlreadyExistsException {
        UserRegisterRequestDto request = new UserRegisterRequestDto();
        request.setName("Test User");
        request.setEmail("test@example.com");
        request.setPassword("password123");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(i -> {
            User savedUser = i.getArgument(0);
            savedUser.setId(1L); // simulate DB generated ID
            return savedUser;
        });

        UserResponseDto response = userService.register(request);

        assertNotNull(response);
        assertEquals("Test User", response.getName());
        assertEquals("test@example.com", response.getEmail());
        assertEquals(1L, response.getId());
    }

    @Test
    void register_UserAlreadyExists_ThrowsException() {
        UserRegisterRequestDto request = new UserRegisterRequestDto();
        request.setEmail("test@example.com");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));

        assertThrows(UserAlreadyExistsException.class, () -> userService.register(request));
    }

    @Test
    void login_Success() throws ResourceNotFoundException {
        UserLoginRequestDto request = new UserLoginRequestDto();
        request.setEmail("test@example.com");
        request.setPassword("password123");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));

        UserResponseDto response = userService.login(request);

        assertNotNull(response);
        assertEquals("Test User", response.getName());
        assertEquals("test@example.com", response.getEmail());
        assertEquals(1L, response.getId());
    }

    @Test
    void login_InvalidPassword_ThrowsException() {
        UserLoginRequestDto request = new UserLoginRequestDto();
        request.setEmail("test@example.com");
        request.setPassword("wrongpassword");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));

        assertThrows(ResourceNotFoundException.class, () -> userService.login(request));
    }

    @Test
    void login_UserNotFound_ThrowsException() {
        UserLoginRequestDto request = new UserLoginRequestDto();
        request.setEmail("notfound@example.com");
        request.setPassword("password123");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.login(request));
    }
}
