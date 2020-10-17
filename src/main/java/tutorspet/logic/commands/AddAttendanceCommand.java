package tutorspet.logic.commands;

import static java.util.Objects.requireNonNull;
import static tutorspet.commons.util.CollectionUtil.requireAllNonNull;
import static tutorspet.logic.parser.CliSyntax.PREFIX_CLASS_INDEX;
import static tutorspet.logic.parser.CliSyntax.PREFIX_LESSON_INDEX;
import static tutorspet.logic.parser.CliSyntax.PREFIX_PARTICIPATION_SCORE;
import static tutorspet.logic.parser.CliSyntax.PREFIX_STUDENT_INDEX;
import static tutorspet.logic.parser.CliSyntax.PREFIX_WEEK;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import tutorspet.commons.core.Messages;
import tutorspet.commons.core.index.Index;
import tutorspet.logic.commands.exceptions.CommandException;
import tutorspet.model.Model;
import tutorspet.model.attendance.Attendance;
import tutorspet.model.attendance.AttendanceRecord;
import tutorspet.model.attendance.AttendanceRecordList;
import tutorspet.model.attendance.Week;
import tutorspet.model.components.name.Name;
import tutorspet.model.lesson.Day;
import tutorspet.model.lesson.Lesson;
import tutorspet.model.lesson.NumberOfOccurrences;
import tutorspet.model.lesson.Venue;
import tutorspet.model.moduleclass.ModuleClass;
import tutorspet.model.student.Student;

/**
 * Adds a student's attendance to the specified {@code Lesson} in the student manager.
 */
public class AddAttendanceCommand extends Command {

    public static final String COMMAND_WORD = "add-attendance";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a student's attendance to the specified "
            + "lesson in the student manager. "
            + "Note: All indexes must be positive integers.\n"
            + "Parameters: "
            + PREFIX_CLASS_INDEX + "CLASS_INDEX "
            + PREFIX_LESSON_INDEX + "LESSON_INDEX "
            + PREFIX_STUDENT_INDEX + "STUDENT_INDEX "
            + PREFIX_WEEK + "WEEK_NUMBER (must be a positive integer) "
            + PREFIX_PARTICIPATION_SCORE + "PARTICIPATION_SCORE (must be an integer between 0 and 100)";

    public static final String MESSAGE_SUCCESS = "New attendance added: %1$s attended week %2$s lesson with "
            + "participation score of %3$s";
    public static final String MESSAGE_DUPLICATE_ATTENDANCE = "Attendance have been recorded previously.";

    private final Index moduleCLassIndex;
    private final Index lessonIndex;
    private final Index studentIndex;
    private final Week week;
    private final Attendance toAdd;

    /**
     * Creates an AddAttendanceCommand to add the specified {@code Attendance}.
     */
    public AddAttendanceCommand(Index moduleCLassIndex, Index lessonIndex, Index studentIndex,
                                Week week, Attendance toAdd) {
        requireAllNonNull(moduleCLassIndex, lessonIndex, studentIndex, week, toAdd);

        this.moduleCLassIndex = moduleCLassIndex;
        this.lessonIndex = lessonIndex;
        this.studentIndex = studentIndex;
        this.week = week;
        this.toAdd = toAdd;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Student> lastShownStudentList = model.getFilteredStudentList();
        List<ModuleClass> lastShownModuleClassList = model.getFilteredModuleClassList();

        if (studentIndex.getZeroBased() >= lastShownStudentList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);
        }

        if (moduleCLassIndex.getZeroBased() >= lastShownModuleClassList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_MODULE_CLASS_DISPLAYED_INDEX);
        }

        Student targetStudent = lastShownStudentList.get(studentIndex.getZeroBased());
        ModuleClass targetModuleClass = lastShownModuleClassList.get(moduleCLassIndex.getZeroBased());

        if (!targetModuleClass.hasStudentUuid(targetStudent.getUuid())) {
            throw new CommandException(Messages.MESSAGE_INVALID_STUDENT_IN_MODULE_CLASS);
        }

        if (lessonIndex.getZeroBased() >= targetModuleClass.getLessons().size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_LESSON_DISPLAYED_INDEX);
        }

        Lesson targetLesson = targetModuleClass.getLessons().get(lessonIndex.getZeroBased());

        if (!targetLesson.getAttendanceRecordList().isWeekContained(week)) {
            throw new CommandException(Messages.MESSAGE_INVALID_WEEK);
        }

        Lesson modifiedLesson = createModifiedLesson(targetLesson, targetStudent, week, toAdd);
        ModuleClass modifiedModuleClass = createModifiedModuleClass(targetModuleClass, lessonIndex, modifiedLesson);
        model.setModuleClass(targetModuleClass, modifiedModuleClass);

        String message = String.format(MESSAGE_SUCCESS, targetStudent.getName(), week, toAdd);
        model.commit(message);
        return new CommandResult(message);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddAttendanceCommand // instanceof handles nulls
                && moduleCLassIndex.equals(((AddAttendanceCommand) other).moduleCLassIndex)
                && lessonIndex.equals(((AddAttendanceCommand) other).lessonIndex)
                && studentIndex.equals(((AddAttendanceCommand) other).studentIndex)
                && week.equals(((AddAttendanceCommand) other).week)
                && toAdd.equals(((AddAttendanceCommand) other).toAdd));
    }

    private static ModuleClass createModifiedModuleClass(ModuleClass targetModuleClass,
                                                         Index lessonToEditIndex, Lesson lessonToUpdate) {
        assert targetModuleClass != null;
        assert lessonToEditIndex != null;
        assert lessonToUpdate != null;

        Name moduleClassName = targetModuleClass.getName();
        Set<UUID> studentsIds = targetModuleClass.getStudentUuids();
        List<Lesson> lessons = new ArrayList<>(targetModuleClass.getLessons());
        lessons.set(lessonToEditIndex.getZeroBased(), lessonToUpdate);

        return new ModuleClass(moduleClassName, studentsIds, lessons);
    }

    private static Lesson createModifiedLesson (Lesson targetLesson, Student targetStudent,
                                                Week targetWeek, Attendance attendanceToAdd) throws CommandException {
        assert targetLesson != null;
        assert targetStudent != null;
        assert targetWeek != null;
        assert attendanceToAdd != null;

        if (targetLesson.getAttendanceRecordList().hasAttendance(targetStudent, targetWeek)) {
            throw new CommandException(MESSAGE_DUPLICATE_ATTENDANCE);
        }

        Map<UUID, Attendance> record = targetLesson.getAttendanceRecordList()
                .getAttendanceRecord(targetWeek).getAttendanceRecord();
        Map<UUID, Attendance> updatedRecord = new HashMap<>(record);
        updatedRecord.put(targetStudent.getUuid(), attendanceToAdd);
        AttendanceRecord updatedAttendanceRecord = new AttendanceRecord(updatedRecord);
        List<AttendanceRecord> updatedAttendanceRecords =
                new ArrayList<>(targetLesson.getAttendanceRecordList().getAttendanceRecordList());
        updatedAttendanceRecords.set(targetWeek.getZeroBasedWeekIndex(), updatedAttendanceRecord);
        AttendanceRecordList updatedAttendanceRecordList = new AttendanceRecordList(updatedAttendanceRecords);

        // unchanged lesson fields
        LocalTime startTime = targetLesson.getStartTime();
        LocalTime endTime = targetLesson.getEndTime();
        Day day = targetLesson.getDay();
        NumberOfOccurrences numberOfOccurrences = targetLesson.getNumberOfOccurrences();
        Venue venue = targetLesson.getVenue();

        return new Lesson(startTime, endTime, day, numberOfOccurrences, venue, updatedAttendanceRecordList);
    }
}
