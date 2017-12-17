package space.funin.questBot.Exceptions;

public class RoleAlreadyPresentException  extends Exception {
    public RoleAlreadyPresentException() {
    }

    public RoleAlreadyPresentException(String message) {
        super(message);
    }

    public RoleAlreadyPresentException(String message, Throwable cause) {
        super(message, cause);
    }

    public RoleAlreadyPresentException(Throwable cause) {
        super(cause);
    }
}
