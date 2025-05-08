package lk.sliit.quick_task_manager.controller.dto.respnseDto;

import jakarta.validation.constraints.FutureOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponseDto {
    private Long id;
    private String title;
    private String description;

    @FutureOrPresent(message = "Due date must be today or a future date")
    private LocalDate dueDate;

    private String status;
}
