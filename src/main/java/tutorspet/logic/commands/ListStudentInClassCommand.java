package tutorspet.logic.commands;

import static java.util.Objects.requireNonNull;
import static tutorspet.logic.parser.CliSyntax.PREFIX_CLASS_INDEX;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import tutorspet.commons.core.Messages;
import tutorspet.commons.core.index.Index;
import tutorspet.logic.commands.exceptions.CommandException;
import tutorspet.model.Model;
import tutorspet.model.moduleclass.ModuleClass;
import tutorspet.model.student.StudentInUuidCollectionPredicate;

/**
 * Lists all students of a class to the user.
 */
public class ListStudentInClassCommand extends ListStudentCommand {

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Lists students in a class or "
            + "all students if no class is specified.\n"
            + "Parameters: " + "[" + PREFIX_CLASS_INDEX + "CLASS_INDEX]\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_CLASS_INDEX + "1";

    public static final String MESSAGE_LIST_CLASS_SPECIFIC_SUCCESS = "Listed all students in %1$s.";

    private final Index moduleClassIndex;

    /**
     * Creates a ListStudentInClassCommand to list students in the class displayed at the specified index.
     */
    public ListStudentInClassCommand(Index moduleClassIndex) {
        requireNonNull(moduleClassIndex);

        this.moduleClassIndex = moduleClassIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        List<ModuleClass> lastShownModuleClassList = model.getFilteredModuleClassList();

        if (moduleClassIndex.getZeroBased() >= lastShownModuleClassList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_MODULE_CLASS_DISPLAYED_INDEX);
        }

        ModuleClass moduleClass = lastShownModuleClassList.get(moduleClassIndex.getZeroBased());
        Collection<UUID> studentUuids = moduleClass.getStudentUuids();
        model.updateFilteredStudentList(new StudentInUuidCollectionPredicate(studentUuids));
        return new CommandResult(String.format(MESSAGE_LIST_CLASS_SPECIFIC_SUCCESS, moduleClass));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ListStudentInClassCommand // instanceof handles nulls
                && moduleClassIndex.equals(((ListStudentInClassCommand) other).moduleClassIndex));
    }
}
