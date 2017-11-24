package jterm.command;

public class CommandException extends RuntimeException {
<<<<<<< HEAD

    CommandException(String message) {
=======
    public CommandException(String message) {
>>>>>>> upstream/dev
        super(message);
    }

    public CommandException(String message, Throwable cause) {
        super(message, cause);
    }
}
