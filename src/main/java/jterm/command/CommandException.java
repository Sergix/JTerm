package jterm.command;

public class CommandException extends RuntimeException {
    CommandException(Throwable cause) {
        super(cause);
    }

    CommandException(String message) {
        super(message);
    }

    CommandException(String message, Throwable cause) {
        super(message, cause);
    }
}
