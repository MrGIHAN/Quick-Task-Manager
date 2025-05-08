package lk.sliit.quick_task_manager.service;

import lk.sliit.quick_task_manager.controller.dto.requestDto.TaskRequestDto;
import lk.sliit.quick_task_manager.controller.dto.respnseDto.TaskResponseDto;
import lk.sliit.quick_task_manager.exception.ResourceNotFoundException;

import java.util.List;

public interface TaskService {
    TaskResponseDto addTask(Long userId, TaskRequestDto requestDto) throws ResourceNotFoundException;
    List<TaskResponseDto> getUserTasks(Long userId) throws ResourceNotFoundException;
    void updateTaskStatus(Long taskId, String status) throws ResourceNotFoundException;
    void deleteByTaskId(Long taskId) throws ResourceNotFoundException;
}
