package tutorspet.logic.commands;

import static java.util.Objects.requireNonNull;
import static tutorspet.commons.util.CollectionUtil.requireAllNonNull;
import static tutorspet.logic.parser.CliSyntax.PREFIX_CLASS_INDEX;
import static tutorspet.logic.parser.CliSyntax.PREFIX_STUDENT_INDEX;

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
import tutorspet.model.moduleclass.SameModuleClassPredicate;
import tutorspet.model.student.Student;
import tutorspet.model.student.StudentInUuidCollectionPredicate;

/**
 * Links an existing student to an existing class.
 */
public class LinkCommand extends Command {

    public static final String COMMAND_WORD = "link";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Links the student identified by the index number used in the displayed student list to "
            + "the class identified by the index number used in the displayed class list.\n"
            + "Parameters: "
            + PREFIX_STUDENT_INDEX + "STUDENT_INDEX (must be a positive integer) "
            + PREFIX_CLASS_INDEX + "CLASS_INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_STUDENT_INDEX + "1"
            + PREFIX_CLASS_INDEX + "1";

    public static final String MESSAGE_LINK_SUCCESS = "Linked %1$s to %2$s";
    public static final String MESSAGE_EXISTING_LINK = "This student is already linked to this class.";

    private final Index moduleClassIndex;
    private final Index studentIndex;

    /**
     * @param moduleClassIndex in the filtered class list to link.
     * @param studentIndex in the filtered student list to link.
     */
    public LinkCommand(Index moduleClassIndex, Index studentIndex) {
        requireAllNonNull(moduleClassIndex, studentIndex);

        this.moduleClassIndex = moduleClassIndex;
        this.studentIndex = studentIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Student> lastShownStudentList = model.getFilteredStudentList();
        List<ModuleClass> lastShownModuleClassList = model.getFilteredModuleClassList();

        if (studentIndex.getZeroBased() >= lastShownStudentList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);
        }

        if (moduleClassIndex.getZeroBased() >= lastShownModuleClassList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_MODULE_CLASS_DISPLAYED_INDEX);
        }

        Student studentToLink = lastShownStudentList.get(studentIndex.getZeroBased());
        ModuleClass moduleClassToLink = lastShownModuleClassList.get(moduleClassIndex.getZeroBased());
        ModuleClass modifiedModuleClass = createModifiedModuleClass(moduleClassToLink, studentToLink);
        model.setModuleClass(moduleClassToLink, modifiedModuleClass);

        model.updateFilteredModuleClassList(new SameModuleClassPredicate(modifiedModuleClass));
        model.updateFilteredStudentList(new StudentInUuidCollectionPredicate(modifiedModuleClass.getStudentUuids()));

        String message = String.format(MESSAGE_LINK_SUCCESS, studentToLink.getName(), moduleClassToLink);
        model.commit(message);
        return new CommandResult(message);
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof LinkCommand
                && studentIndex.equals(((LinkCommand) other).studentIndex)
                && moduleClassIndex.equals(((LinkCommand) other).moduleClassIndex));
    }

    private static ModuleClass createModifiedModuleClass(ModuleClass moduleClassToLink, Student studentToLink)
            throws CommandException {
        assert moduleClassToLink != null;
        assert studentToLink != null;

        UUID studentUuid = studentToLink.getUuid();

        if (moduleClassToLink.hasStudentUuid(studentUuid)) {
            throw new CommandException(MESSAGE_EXISTING_LINK);
        }

        Name moduleClassName = moduleClassToLink.getName();
        Set<UUID> studentsIds = new HashSet<>(moduleClassToLink.getStudentUuids());
        studentsIds.add(studentUuid);
        List<Lesson> lessons = moduleClassToLink.getLessons();
        return new ModuleClass(moduleClassName, studentsIds, lessons);
    }
}
