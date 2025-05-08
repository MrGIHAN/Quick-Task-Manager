package lk.sliit.quick_task_manager.service.impl;

import lk.sliit.quick_task_manager.controller.dto.requestDto.TaskRequestDto;
import lk.sliit.quick_task_manager.controller.dto.respnseDto.TaskResponseDto;
import lk.sliit.quick_task_manager.exception.ResourceNotFoundException;
import lk.sliit.quick_task_manager.model.Task;
import lk.sliit.quick_task_manager.model.User;
import lk.sliit.quick_task_manager.model.options.TaskStatus;
import lk.sliit.quick_task_manager.repository.TaskRepository;
import lk.sliit.quick_task_manager.repository.UserRepository;
import lk.sliit.quick_task_manager.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Override
    public TaskResponseDto addTask(Long userId, TaskRequestDto requestDto) throws ResourceNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Task task = new Task();
        task.setTitle(requestDto.getTitle());
        task.setDescription(requestDto.getDescription());
        task.setDueDate(requestDto.getDueDate());
        task.setStatus(TaskStatus.PENDING);
        task.setUser(user);

        taskRepository.save(task);

        TaskResponseDto responseDto = new TaskResponseDto();
        responseDto.setId(task.getId());
        responseDto.setTitle(task.getTitle());
        responseDto.setDescription(task.getDescription());
        responseDto.setDueDate(task.getDueDate());
        responseDto.setStatus(task.getStatus().name());

        return responseDto;
    }

    @Override
    public List<TaskResponseDto> getUserTasks(Long userId) throws ResourceNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<Task> tasks = taskRepository.findByUser(user);
        List<TaskResponseDto> taskResponseDtos = new ArrayList<>();

        for (Task task : tasks) {
            TaskResponseDto responseDto = new TaskResponseDto();
            responseDto.setId(task.getId());
            responseDto.setTitle(task.getTitle());
            responseDto.setDescription(task.getDescription());
            responseDto.setDueDate(task.getDueDate());
            responseDto.setStatus(task.getStatus().name()); // ✅ ENUM to String

            taskResponseDtos.add(responseDto);
        }

        return taskResponseDtos;
    }

    //http://localhost:8080/tasks/1/status?status=COMPLETED
    @Override
    public void updateTaskStatus(Long taskId, String status) throws ResourceNotFoundException {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        try {
            TaskStatus taskStatus = TaskStatus.valueOf(status.toUpperCase());
            task.setStatus(taskStatus);
            taskRepository.save(task);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status value. Allowed values: PENDING, COMPLETED");
        }
    }

    @Override
    public void deleteByTaskId(Long taskId) throws ResourceNotFoundException {

        if(!taskRepository.existsById(taskId)) {
            throw new ResourceNotFoundException("Task not found");
        }
        taskRepository.deleteById(taskId);
    }
}

