package lk.sliit.quick_task_manager.controller;

import lk.sliit.quick_task_manager.controller.dto.requestDto.UserLoginRequestDto;
import lk.sliit.quick_task_manager.controller.dto.requestDto.UserRegisterRequestDto;
import lk.sliit.quick_task_manager.controller.dto.respnseDto.UserResponseDto;
import lk.sliit.quick_task_manager.exception.ResourceNotFoundException;
import lk.sliit.quick_task_manager.exception.UserAlreadyExistsException;
import lk.sliit.quick_task_manager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 1. User Registration
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@RequestBody UserRegisterRequestDto requestDto) throws UserAlreadyExistsException {
        UserResponseDto responseDto = userService.register(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    // 2. User Login
    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> login(@RequestBody UserLoginRequestDto requestDto) throws ResourceNotFoundException {
        UserResponseDto responseDto = userService.login(requestDto);
        if (responseDto != null) {
            return ResponseEntity.ok(responseDto);
        } else {
            return ResponseEntity.status(401).body(null);
        }
    }
}