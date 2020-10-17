package tutorspet.logic.commands;

import static java.util.Objects.requireNonNull;
import static tutorspet.model.Model.PREDICATE_SHOW_ALL_MODULE_CLASS;

import tutorspet.model.Model;

/**
 * Lists all classes to the user.
 */
public class ListModuleClassCommand extends Command {

    public static final String COMMAND_WORD = "list-class";

    public static final String MESSAGE_SUCCESS = "Listed all classes.";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        model.updateFilteredModuleClassList(PREDICATE_SHOW_ALL_MODULE_CLASS);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
