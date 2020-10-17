package tutorspet.logic.commands;

import static java.util.Objects.requireNonNull;
import static tutorspet.commons.util.CollectionUtil.requireAllNonNull;
import static tutorspet.logic.parser.CliSyntax.PREFIX_CLASS_INDEX;
import static tutorspet.logic.parser.CliSyntax.PREFIX_DAY;
import static tutorspet.logic.parser.CliSyntax.PREFIX_END_TIME;
import static tutorspet.logic.parser.CliSyntax.PREFIX_LESSON_INDEX;
import static tutorspet.logic.parser.CliSyntax.PREFIX_START_TIME;
import static tutorspet.logic.parser.CliSyntax.PREFIX_VENUE;
import static tutorspet.model.Model.PREDICATE_SHOW_ALL_MODULE_CLASS;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import tutorspet.commons.core.Messages;
import tutorspet.commons.core.index.Index;
import tutorspet.commons.util.CollectionUtil;
import tutorspet.logic.commands.exceptions.CommandException;
import tutorspet.model.Model;
import tutorspet.model.attendance.AttendanceRecordList;
import tutorspet.model.components.name.Name;
import tutorspet.model.lesson.Day;
import tutorspet.model.lesson.Lesson;
import tutorspet.model.lesson.NumberOfOccurrences;
import tutorspet.model.lesson.Venue;
import tutorspet.model.moduleclass.ModuleClass;

/**
 * Edits the details of an existing lesson in a module class.
 */
public class EditLessonCommand extends Command {

    public static final String COMMAND_WORD = "edit-lesson";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Edits the details of the lesson, identified "
            + "by the index number used in the displayed class list "
            + "and index number in the lesson list.\n"
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: "
            + PREFIX_CLASS_INDEX + "CLASS_INDEX (must be a positive integer) "
            + PREFIX_LESSON_INDEX + "LESSON_INDEX (must be a positive integer) "
            + "[" + PREFIX_DAY + "DAY] "
            + "[" + PREFIX_START_TIME + "START_TIME] "
            + "[" + PREFIX_END_TIME + "END_TIME] "
            + "[" + PREFIX_VENUE + "VENUE]\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_CLASS_INDEX + "1 " + PREFIX_LESSON_INDEX + "1 "
            + PREFIX_DAY + "WEDNESDAY "
            + PREFIX_END_TIME + "18:00";

    public static final String MESSAGE_EDIT_LESSON_SUCCESS = "Edited Lesson: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_LESSON = "This lesson already exists.";

    private final Index moduleClassIndex;
    private final Index lessonIndex;
    private final EditLessonCommand.EditLessonDescriptor editLessonDescriptor;

    /**
     * @param moduleClassIndex in the filtered class list.
     * @param lessonIndex in the specified class list to be edited.
     * @param editLessonDescriptor details to edit the class with.
     */
    public EditLessonCommand(
            Index moduleClassIndex, Index lessonIndex, EditLessonCommand.EditLessonDescriptor editLessonDescriptor) {
        requireAllNonNull(moduleClassIndex, lessonIndex, editLessonDescriptor);

        this.moduleClassIndex = moduleClassIndex;
        this.lessonIndex = lessonIndex;
        this.editLessonDescriptor = new EditLessonCommand.EditLessonDescriptor(editLessonDescriptor);
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

        Lesson lessonToEdit = targetModuleClass.getLessons().get(lessonIndex.getZeroBased());
        Lesson editedLesson = createEditedLesson(lessonToEdit, editLessonDescriptor);
        ModuleClass modifiedModuleClass = createModifiedModuleClass(targetModuleClass, lessonToEdit, editedLesson);

        model.setModuleClass(targetModuleClass, modifiedModuleClass);
        model.updateFilteredModuleClassList(PREDICATE_SHOW_ALL_MODULE_CLASS);
        String message = String.format(MESSAGE_EDIT_LESSON_SUCCESS, editedLesson);
        model.commit(message);
        return new CommandResult(message);
    }

    /**
     * Creates and returns a {@code Lesson} with the details of {@code lessonToEdit}
     * edited with {@code editLessonDescriptor}.
     * {@code NumberOfOccurrences} and {@code AttendanceRecordList} remain unchanged.
     */
    private static Lesson createEditedLesson(
            Lesson lessonToEdit, EditLessonDescriptor editLessonDescriptor) {
        assert lessonToEdit != null;
        LocalTime updatedStartTime = editLessonDescriptor.getStartTime().orElse(lessonToEdit.getStartTime());
        LocalTime updatedEndTime = editLessonDescriptor.getEndTime().orElse(lessonToEdit.getEndTime());
        Day updatedDay = editLessonDescriptor.getDay().orElse(lessonToEdit.getDay());
        Venue updatedVenue = editLessonDescriptor.getVenue().orElse(lessonToEdit.getVenue());

        NumberOfOccurrences originalNumberOfOccurrences = lessonToEdit.getNumberOfOccurrences();
        AttendanceRecordList attendanceRecordList = lessonToEdit.getAttendanceRecordList();
        return new Lesson(updatedStartTime, updatedEndTime, updatedDay, originalNumberOfOccurrences,
                updatedVenue, attendanceRecordList);
    }

    /**
     * Adds all lessons in the target module class to the new module class.
     * The {@code editedLesson} is added in place of the {@code lessonToEdit}.
     *
     * @throws CommandException if the {@code editedLesson} already exists.
     */
    private static ModuleClass createModifiedModuleClass(
            ModuleClass targetModuleClass, Lesson lessonToEdit, Lesson editedLesson) throws CommandException {
        assert targetModuleClass != null;
        assert lessonToEdit != null;
        assert editedLesson != null;
        assert targetModuleClass.hasLesson(lessonToEdit);

        if (!lessonToEdit.isSameLesson(editedLesson) && targetModuleClass.hasLesson(editedLesson)) {
            throw new CommandException(MESSAGE_DUPLICATE_LESSON);
        }

        Name moduleClassName = targetModuleClass.getName();
        Set<UUID> studentsIds = new HashSet<>(targetModuleClass.getStudentUuids());
        List<Lesson> listOfLessons = targetModuleClass.getLessons();
        List<Lesson> editedListOfLessons = new ArrayList<>();
        for (Lesson lesson : listOfLessons) {
            if (lesson.equals(lessonToEdit)) {
                editedListOfLessons.add(editedLesson);
            } else {
                editedListOfLessons.add(lesson);
            }
        }
        assert listOfLessons.size() == editedListOfLessons.size();
        return new ModuleClass(moduleClassName, studentsIds, editedListOfLessons);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditLessonCommand)) {
            return false;
        }

        // state check
        EditLessonCommand e = (EditLessonCommand) other;
        return moduleClassIndex.equals(e.moduleClassIndex)
                && lessonIndex.equals(e.lessonIndex)
                && editLessonDescriptor.equals(e.editLessonDescriptor);
    }

    /**
     * Stores the details to edit the lesson with. Each non-empty field value will replace the
     * corresponding field value of the lesson.
     */
    public static class EditLessonDescriptor {

        private LocalTime startTime;
        private LocalTime endTime;
        private Day day;
        private Venue venue;

        public EditLessonDescriptor() {}

        /**
         * Copy constructor.
         */
        public EditLessonDescriptor(EditLessonCommand.EditLessonDescriptor toCopy) {
            setStartTime(toCopy.startTime);
            setEndTime(toCopy.endTime);
            setDay(toCopy.day);
            setVenue(toCopy.venue);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(startTime, endTime, day, venue);
        }

        public void setStartTime(LocalTime startTime) {
            this.startTime = startTime;
        }

        public Optional<LocalTime> getStartTime() {
            return Optional.ofNullable(startTime);
        }

        public void setEndTime(LocalTime endTime) {
            this.endTime = endTime;
        }

        public Optional<LocalTime> getEndTime() {
            return Optional.ofNullable(endTime);
        }

        public void setDay(Day day) {
            this.day = day;
        }

        public Optional<Day> getDay() {
            return Optional.ofNullable(day);
        }

        public void setVenue(Venue venue) {
            this.venue = venue;
        }

        public Optional<Venue> getVenue() {
            return Optional.ofNullable(venue);
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditLessonCommand.EditLessonDescriptor)) {
                return false;
            }

            // state check
            EditLessonCommand.EditLessonDescriptor e = (EditLessonCommand.EditLessonDescriptor) other;

            return getStartTime().equals(e.getStartTime())
                    && getEndTime().equals(e.getEndTime())
                    && getDay().equals(e.getDay())
                    && getVenue().equals(e.getVenue());
        }
    }
}
