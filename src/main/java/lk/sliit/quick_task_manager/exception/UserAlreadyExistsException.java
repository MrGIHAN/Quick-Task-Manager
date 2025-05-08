package lk.sliit.quick_task_manager.exception;

public class UserAlreadyExistsException extends NotFoundException{
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
