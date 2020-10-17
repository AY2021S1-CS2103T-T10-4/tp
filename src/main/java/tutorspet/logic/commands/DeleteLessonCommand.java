package tutorspet.logic.commands;

import static java.util.Objects.requireNonNull;
import static tutorspet.commons.util.CollectionUtil.requireAllNonNull;
import static tutorspet.logic.parser.CliSyntax.PREFIX_CLASS_INDEX;
import static tutorspet.logic.parser.CliSyntax.PREFIX_LESSON_INDEX;

import java.util.ArrayList;
import java.util.HashSet;
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
 * Deletes a lesson identified using it's displayed index in the displayed module class list.
 */
public class DeleteLessonCommand extends Command {

    public static final String COMMAND_WORD = "delete-lesson";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the lesson in a specific class identified by the "
            + "by the index number used in the displayed class list "
            + "and index number in the lesson list.\n"
            + "Parameters: "
            + PREFIX_CLASS_INDEX + "CLASS_INDEX (must be a positive integer) "
            + PREFIX_LESSON_INDEX + "LESSON_INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_CLASS_INDEX + "1 "
            + PREFIX_LESSON_INDEX + "1";

    public static final String MESSAGE_DELETE_LESSON_SUCCESS = "Deleted lesson %1$s from %2$s";

    private final Index moduleClassIndex;
    private final Index lessonIndex;

    /**
     * @param moduleClassIndex in the filtered class list.
     * @param lessonIndex in the specified class list to be deleted.
     */
    public DeleteLessonCommand(Index moduleClassIndex, Index lessonIndex) {
        requireAllNonNull(moduleClassIndex, lessonIndex);

        this.moduleClassIndex = moduleClassIndex;
        this.lessonIndex = lessonIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<ModuleClass> lastShownModuleClassList = model.getFilteredModuleClassList();

        if (moduleClassIndex.getZeroBased() >= lastShownModuleClassList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_MODULE_CLASS_DISPLAYED_INDEX);
        }

        ModuleClass targetModuleClass = lastShownModuleClassList.get(moduleClassIndex.getZeroBased());

        if (lessonIndex.getZeroBased() >= targetModuleClass.getLessons().size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_LESSON_DISPLAYED_INDEX);
        }

        Lesson lessonToDelete = targetModuleClass.getLessons().get(lessonIndex.getZeroBased());
        ModuleClass modifiedModuleClass = createModifiedModuleClass(targetModuleClass, lessonToDelete);
        model.setModuleClass(targetModuleClass, modifiedModuleClass);

        String message = String.format(MESSAGE_DELETE_LESSON_SUCCESS, lessonToDelete, targetModuleClass);
        model.commit(message);
        return new CommandResult(message);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeleteLessonCommand // instance of handles nulls
                && moduleClassIndex.equals(((DeleteLessonCommand) other).moduleClassIndex)
                && lessonIndex.equals(((DeleteLessonCommand) other).lessonIndex));
    }

    private static ModuleClass createModifiedModuleClass(ModuleClass targetModuleClass, Lesson lessonToDelete) {
        assert targetModuleClass != null;
        assert lessonToDelete != null;
        assert targetModuleClass.hasLesson(lessonToDelete);

        Name moduleClassName = targetModuleClass.getName();
        Set<UUID> studentsIds = new HashSet<>(targetModuleClass.getStudentUuids());
        List<Lesson> listOfLessons = targetModuleClass.getLessons();
        List<Lesson> editedListOfLessons = new ArrayList<>();
        for (Lesson lesson : listOfLessons) {
            if (!lesson.equals(lessonToDelete)) {
                editedListOfLessons.add(lesson);
            }
        }
        assert listOfLessons.size() - 1 == editedListOfLessons.size();
        return new ModuleClass(moduleClassName, studentsIds, editedListOfLessons);
    }
}
