package main.java.exceptionCustom;

public class ManagerSaveException extends RuntimeException {

    public  ManagerSaveException(final String message) {
        super(message);
    }
}
