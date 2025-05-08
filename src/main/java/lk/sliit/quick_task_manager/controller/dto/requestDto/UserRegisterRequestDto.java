package lk.sliit.quick_task_manager.controller.dto.requestDto;

import lombok.Data;

@Data
public class UserRegisterRequestDto {

    private String name;

    private String email;

    private String password;
}
