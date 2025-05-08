package lk.sliit.quick_task_manager.service.impl;

import lk.sliit.quick_task_manager.controller.dto.requestDto.UserLoginRequestDto;
import lk.sliit.quick_task_manager.controller.dto.requestDto.UserRegisterRequestDto;
import lk.sliit.quick_task_manager.controller.dto.respnseDto.UserResponseDto;
import lk.sliit.quick_task_manager.exception.ResourceNotFoundException;
import lk.sliit.quick_task_manager.exception.UserAlreadyExistsException;
import lk.sliit.quick_task_manager.model.User;
import lk.sliit.quick_task_manager.repository.UserRepository;
import lk.sliit.quick_task_manager.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl  implements UserService {

    private UserRepository userRepository;

    @Override
    public UserResponseDto register(UserRegisterRequestDto requestDto) throws UserAlreadyExistsException {
        Optional<User> user = userRepository.findByEmail(requestDto.getEmail());
        if (user.isPresent()) {
            throw new UserAlreadyExistsException("User already exists");
        }

        User newUser = new User();
        newUser.setEmail(requestDto.getEmail());
        newUser.setPassword(requestDto.getPassword());
        newUser.setName(requestDto.getName());
        userRepository.save(newUser);

        return new UserResponseDto(
                newUser.getId(),
                newUser.getName(),
                newUser.getEmail());

    }

    @Override
    public UserResponseDto login(UserLoginRequestDto requestDto) throws ResourceNotFoundException {
        Optional<User> userEmail = userRepository.findByEmail(requestDto.getEmail());

        if (userEmail.isPresent()) {
            User user1 = userEmail.get();

            if (user1.getPassword().equals(requestDto.getPassword())) {
                System.out.println("Login Successful for user: " + user1.getName());
                return new UserResponseDto(user1.getId(), user1.getName(), user1.getEmail());
            } else {
                throw new ResourceNotFoundException("Invalid email or password.");
            }

        } else {
            throw new ResourceNotFoundException("Invalid email or password.");
        }

    }

}

