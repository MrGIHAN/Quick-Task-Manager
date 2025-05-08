package lk.sliit.quick_task_manager.controller.dto.requestDto;

import jakarta.validation.constraints.FutureOrPresent;
import lk.sliit.quick_task_manager.model.options.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequestDto {
    
    private String title;

    private String description;

    @FutureOrPresent(message = "Due date must be today or a future date")
    private LocalDate dueDate;

    private TaskStatus status;
    
}
