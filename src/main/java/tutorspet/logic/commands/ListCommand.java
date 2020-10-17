package tutorspet.logic.commands;

import static java.util.Objects.requireNonNull;
import static tutorspet.model.Model.PREDICATE_SHOW_ALL_MODULE_CLASS;
import static tutorspet.model.Model.PREDICATE_SHOW_ALL_STUDENTS;

import tutorspet.model.Model;

/**
 * Lists all students and classes to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_SUCCESS = "Listed all students and classes.";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        model.updateFilteredStudentList(PREDICATE_SHOW_ALL_STUDENTS);
        model.updateFilteredModuleClassList(PREDICATE_SHOW_ALL_MODULE_CLASS);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
