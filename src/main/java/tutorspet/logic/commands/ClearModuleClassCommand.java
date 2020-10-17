package tutorspet.logic.commands;

import static java.util.Objects.requireNonNull;

import tutorspet.model.Model;

/**
 * Clears all classes in the student manager.
 */
public class ClearModuleClassCommand extends Command {

    public static final String COMMAND_WORD = "clear-class";
    public static final String MESSAGE_SUCCESS = "All classes in Tutor's Pet have been cleared!";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        model.deleteAllModuleClasses();
        model.commit(MESSAGE_SUCCESS);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
