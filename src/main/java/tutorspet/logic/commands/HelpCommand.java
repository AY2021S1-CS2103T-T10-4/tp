package tutorspet.logic.commands;

import tutorspet.model.Model;

/**
 * Format full help instructions for every command for display.
 */
public class HelpCommand extends Command {

    public static final String COMMAND_WORD = "help";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Shows program usage instructions.\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SHOWING_HELP = "Opened help window.";

    @Override
    public CommandResult execute(Model model) {
        return new CommandResult(MESSAGE_SHOWING_HELP, true, false);
    }
}
