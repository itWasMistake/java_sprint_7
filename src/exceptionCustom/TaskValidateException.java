package exceptionCustom;

public class TaskValidateException extends RuntimeException {
    public  TaskValidateException(final String message) {
        super(message);
    }
}
