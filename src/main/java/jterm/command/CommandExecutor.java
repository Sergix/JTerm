package jterm.command;

import jterm.JTerm;
import jterm.io.output.TextColor;

import java.util.List;
import java.util.function.Consumer;

public class CommandExecutor {

    private Consumer<List<String>> command;
    private String commandName;
    private int minOptions;
    private String syntax;

    public CommandExecutor setCommand(Consumer<List<String>> command) {
        this.command = command;

        return this;
    }

    public CommandExecutor setCommandName(String commandName) {
        this.commandName = commandName;

        return this;
    }

    public CommandExecutor setMinOptions(int minOptions) {
        this.minOptions = minOptions;

        return this;
    }

    public CommandExecutor setSyntax(String syntax) {
        this.syntax = syntax;

        return this;
    }

    public void execute(List<String> options) {
        if (options.contains("-h")) {
            if (syntax == null || !syntax.isEmpty()) {
                JTerm.out.println(TextColor.INFO, syntax);
            }
        } else {
            if (options.size() < minOptions) {
                throw new CommandException(String.format("To few arguments for '%s'", commandName));
            }

            command.accept(options);
        }
    }
}
