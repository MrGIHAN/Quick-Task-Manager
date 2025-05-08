package lk.sliit.quick_task_manager.service;

import lk.sliit.quick_task_manager.controller.dto.requestDto.UserLoginRequestDto;
import lk.sliit.quick_task_manager.controller.dto.requestDto.UserRegisterRequestDto;
import lk.sliit.quick_task_manager.controller.dto.respnseDto.UserResponseDto;
import lk.sliit.quick_task_manager.exception.ResourceNotFoundException;
import lk.sliit.quick_task_manager.exception.UserAlreadyExistsException;

public interface UserService {
    UserResponseDto register(UserRegisterRequestDto requestDto) throws UserAlreadyExistsException;
    UserResponseDto login(UserLoginRequestDto requestDto) throws ResourceNotFoundException;

}
