package tutorspet.logic.commands;

import static java.util.Objects.requireNonNull;
import static tutorspet.model.Model.PREDICATE_SHOW_ALL_MODULE_CLASS;
import static tutorspet.model.Model.PREDICATE_SHOW_ALL_STUDENTS;

import tutorspet.logic.commands.exceptions.CommandException;
import tutorspet.model.Model;

/**
 * Undoes the most recent undoable {@code Command}.
 */
public class UndoCommand extends Command {

    public static final String COMMAND_WORD = "undo";

    public static final String MESSAGE_SUCCESS = "Successfully undone: %s";
    public static final String MESSAGE_NO_PREVIOUS_COMMAND = "There are no commands to undo";

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (!model.canUndo()) {
            throw new CommandException(MESSAGE_NO_PREVIOUS_COMMAND);
        }

        String commandMessage = model.undo();
        model.updateFilteredStudentList(PREDICATE_SHOW_ALL_STUDENTS);
        model.updateFilteredModuleClassList(PREDICATE_SHOW_ALL_MODULE_CLASS);
        return new CommandResult(String.format(MESSAGE_SUCCESS, commandMessage));
    }
}
