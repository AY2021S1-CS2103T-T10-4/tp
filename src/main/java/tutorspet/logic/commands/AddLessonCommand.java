package tutorspet.logic.commands;

import static java.util.Objects.requireNonNull;
import static tutorspet.commons.util.CollectionUtil.requireAllNonNull;
import static tutorspet.logic.parser.CliSyntax.PREFIX_CLASS_INDEX;
import static tutorspet.logic.parser.CliSyntax.PREFIX_DAY;
import static tutorspet.logic.parser.CliSyntax.PREFIX_END_TIME;
import static tutorspet.logic.parser.CliSyntax.PREFIX_NUMBER_OF_OCCURRENCES;
import static tutorspet.logic.parser.CliSyntax.PREFIX_START_TIME;
import static tutorspet.logic.parser.CliSyntax.PREFIX_VENUE;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import tutorspet.commons.core.Messages;
import tutorspet.commons.core.index.Index;
import tutorspet.logic.commands.exceptions.CommandException;
import tutorspet.model.Model;
import tutorspet.model.components.name.Name;
import tutorspet.model.lesson.Lesson;
import tutorspet.model.moduleclass.ModuleClass;

/**
 * Adds a lesson to the student manager.
 */
public class AddLessonCommand extends Command {

    public static final String COMMAND_WORD = "add-lesson";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a lesson to the student manager. "
            + "Parameters: "
            + PREFIX_CLASS_INDEX + "CLASS_INDEX (must be a positive integer) "
            + PREFIX_DAY + "DAY "
            + PREFIX_START_TIME + "START_TIME "
            + PREFIX_END_TIME + "END_TIME "
            + PREFIX_VENUE + "VENUE "
            + PREFIX_NUMBER_OF_OCCURRENCES + "NO_OF_OCCURRENCES\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_CLASS_INDEX + "1 "
            + PREFIX_DAY + "TUESDAY "
            + PREFIX_START_TIME + "08:00 "
            + PREFIX_END_TIME + "10:00 "
            + PREFIX_VENUE + "COM1-0211 "
            + PREFIX_NUMBER_OF_OCCURRENCES + "13";

    public static final String MESSAGE_SUCCESS = "New lesson added: %1$s";
    public static final String MESSAGE_EXISTING_LESSON = "This lesson already exists";

    private final Index moduleClassIndex;
    private final Lesson toAdd;

    /**
     * Creates an AddLessonCommand to add the specified {@code Lesson}.
     */
    public AddLessonCommand(Index moduleClassIndex, Lesson lesson) {
        requireAllNonNull(moduleClassIndex, lesson);

        this.toAdd = lesson;
        this.moduleClassIndex = moduleClassIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<ModuleClass> lastShownModuleClassList = model.getFilteredModuleClassList();

        if (moduleClassIndex.getZeroBased() >= lastShownModuleClassList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_MODULE_CLASS_DISPLAYED_INDEX);
        }

        // add lesson to moduleClass
        ModuleClass moduleClassToAddTo = lastShownModuleClassList.get(moduleClassIndex.getZeroBased());
        ModuleClass modifiedModuleClass = createModifiedModuleClass(moduleClassToAddTo, toAdd);
        model.setModuleClass(moduleClassToAddTo, modifiedModuleClass);

        String message = String.format(MESSAGE_SUCCESS, toAdd);
        model.commit(message);
        return new CommandResult(message);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddLessonCommand // instanceof handles nulls
                && moduleClassIndex.equals(((AddLessonCommand) other).moduleClassIndex))
                && toAdd.equals(((AddLessonCommand) other).toAdd);
    }

    private static ModuleClass createModifiedModuleClass(ModuleClass moduleClassToAddTo, Lesson lessonToAdd)
            throws CommandException {
        assert moduleClassToAddTo != null;
        assert lessonToAdd != null;

        if (moduleClassToAddTo.hasLesson(lessonToAdd)) {
            throw new CommandException(MESSAGE_EXISTING_LESSON);
        }

        Name moduleClassName = moduleClassToAddTo.getName();
        Set<UUID> studentsIds = moduleClassToAddTo.getStudentUuids();
        List<Lesson> lessons = new ArrayList<>(moduleClassToAddTo.getLessons());
        lessons.add(lessonToAdd);
        return new ModuleClass(moduleClassName, studentsIds, lessons);
    }
}
