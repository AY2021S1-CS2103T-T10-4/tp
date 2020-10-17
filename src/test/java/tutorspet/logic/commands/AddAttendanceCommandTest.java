package tutorspet.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tutorspet.logic.commands.CommandTestUtil.VALID_ATTENDANCE;
import static tutorspet.logic.commands.CommandTestUtil.VALID_WEEK_1;
import static tutorspet.logic.commands.CommandTestUtil.VALID_WEEK_5;
import static tutorspet.logic.commands.CommandTestUtil.assertCommandFailure;
import static tutorspet.logic.commands.CommandTestUtil.assertCommandSuccess;
import static tutorspet.testutil.Assert.assertThrows;
import static tutorspet.testutil.TypicalIndexes.INDEX_FIRST_ITEM;
import static tutorspet.testutil.TypicalIndexes.INDEX_THIRD_ITEM;
import static tutorspet.testutil.TypicalTutorsPet.getTypicalTutorsPet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import tutorspet.commons.core.Messages;
import tutorspet.commons.core.index.Index;
import tutorspet.model.Model;
import tutorspet.model.ModelManager;
import tutorspet.model.UserPrefs;
import tutorspet.model.attendance.Attendance;
import tutorspet.model.attendance.AttendanceRecord;
import tutorspet.model.attendance.AttendanceRecordList;
import tutorspet.model.attendance.Week;
import tutorspet.model.lesson.Lesson;
import tutorspet.model.moduleclass.ModuleClass;
import tutorspet.model.student.Student;
import tutorspet.testutil.LessonBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code AddAttendanceCommand}.
 */
public class AddAttendanceCommandTest {

    private final Model model = new ModelManager(getTypicalTutorsPet(), new UserPrefs());

    @Test
    public void constructor_nullAttendance_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new AddAttendanceCommand(INDEX_FIRST_ITEM, INDEX_FIRST_ITEM, INDEX_FIRST_ITEM,
                        VALID_WEEK_1, null));
    }

    @Test
    public void constructor_nullIndexes_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new AddAttendanceCommand(null, INDEX_FIRST_ITEM, INDEX_FIRST_ITEM,
                        VALID_WEEK_1, VALID_ATTENDANCE));
        assertThrows(NullPointerException.class, () ->
                new AddAttendanceCommand(INDEX_FIRST_ITEM, null, INDEX_FIRST_ITEM,
                        VALID_WEEK_1, VALID_ATTENDANCE));
        assertThrows(NullPointerException.class, () ->
                new AddAttendanceCommand(INDEX_FIRST_ITEM, INDEX_FIRST_ITEM, null,
                        VALID_WEEK_1, VALID_ATTENDANCE));
    }

    @Test
    public void constructor_nullWeek_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new AddAttendanceCommand(INDEX_FIRST_ITEM, INDEX_FIRST_ITEM, INDEX_FIRST_ITEM,
                        null, VALID_ATTENDANCE));
    }

    @Test
    public void execute_success() {
        Index moduleClassIndex = INDEX_FIRST_ITEM;
        Index lessonIndex = INDEX_FIRST_ITEM;
        Index studentIndex = INDEX_FIRST_ITEM;
        Week targetWeek = VALID_WEEK_5;

        ModuleClass moduleClass = model.getFilteredModuleClassList().get(moduleClassIndex.getZeroBased());
        Student student = model.getFilteredStudentList().get(studentIndex.getZeroBased());
        Lesson lesson = moduleClass.getLessons().get(lessonIndex.getZeroBased());
        AttendanceRecord attendanceRecord = new AttendanceRecord(Map.of(student.getUuid(), VALID_ATTENDANCE));
        List<AttendanceRecord> attendanceRecords =
                new ArrayList<>(lesson.getAttendanceRecordList().getAttendanceRecordList());
        attendanceRecords.set(targetWeek.getZeroBasedWeekIndex(), attendanceRecord);
        Lesson modifiedLesson = new LessonBuilder()
                .withStartTime(lesson.getStartTime())
                .withEndTime(lesson.getEndTime())
                .withDay(lesson.getDay())
                .withVenue(lesson.getVenue().venue)
                .withAttendanceRecordList(new AttendanceRecordList(attendanceRecords))
                .build();
        ModuleClass modifiedModuleClass = manualReplaceLessonToModuleClass(moduleClass, lesson, modifiedLesson);

        String expectedMessage =
                String.format(AddAttendanceCommand.MESSAGE_SUCCESS, student.getName(), targetWeek, VALID_ATTENDANCE);
        Model expectedModel = new ModelManager(model.getTutorsPet(), new UserPrefs());
        expectedModel.setModuleClass(moduleClass, modifiedModuleClass);
        expectedModel.commit(expectedMessage);

        AddAttendanceCommand addAttendanceCommand =
                new AddAttendanceCommand(moduleClassIndex, lessonIndex, studentIndex, targetWeek, VALID_ATTENDANCE);

        assertCommandSuccess(addAttendanceCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_existingAttendance_failure() {
        Index moduleClassIndex = INDEX_FIRST_ITEM;
        Index lessonIndex = INDEX_FIRST_ITEM;
        Index studentIndex = INDEX_FIRST_ITEM;

        ModuleClass moduleClass = model.getFilteredModuleClassList().get(moduleClassIndex.getZeroBased());
        Student student = model.getFilteredStudentList().get(studentIndex.getZeroBased());
        Lesson lesson = moduleClass.getLessons().get(lessonIndex.getZeroBased());
        AttendanceRecord attendanceRecord = new AttendanceRecord(Map.of(student.getUuid(), VALID_ATTENDANCE));
        List<AttendanceRecord> attendanceRecords =
                new ArrayList<>(lesson.getAttendanceRecordList().getAttendanceRecordList());
        attendanceRecords.set(VALID_WEEK_1.getZeroBasedWeekIndex(), attendanceRecord);
        Lesson modifiedLesson = new LessonBuilder()
                .withStartTime(lesson.getStartTime())
                .withEndTime(lesson.getEndTime())
                .withDay(lesson.getDay())
                .withVenue(lesson.getVenue().venue)
                .withAttendanceRecordList(new AttendanceRecordList(attendanceRecords))
                .build();
        ModuleClass modifiedModuleClass = manualReplaceLessonToModuleClass(moduleClass, lesson, modifiedLesson);

        model.setModuleClass(moduleClass, modifiedModuleClass);

        AddAttendanceCommand addAttendanceCommand =
                new AddAttendanceCommand(moduleClassIndex, lessonIndex, studentIndex, VALID_WEEK_1, VALID_ATTENDANCE);

        assertCommandFailure(addAttendanceCommand, model, AddAttendanceCommand.MESSAGE_DUPLICATE_ATTENDANCE);
    }

    @Test
    public void execute_studentNotInClass_failure() {
        Index moduleClassIndex = INDEX_FIRST_ITEM;
        Index lessonIndex = INDEX_FIRST_ITEM;

        AddAttendanceCommand addAttendanceCommand = new AddAttendanceCommand(moduleClassIndex, lessonIndex,
                INDEX_THIRD_ITEM, VALID_WEEK_1, VALID_ATTENDANCE);

        assertCommandFailure(addAttendanceCommand, model, Messages.MESSAGE_INVALID_STUDENT_IN_MODULE_CLASS);
    }

    @Test
    public void execute_invalidClassIndex_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredModuleClassList().size() + 1);
        Index lessonIndex = INDEX_FIRST_ITEM;
        Index studentIndex = INDEX_FIRST_ITEM;

        AddAttendanceCommand addAttendanceCommand =
                new AddAttendanceCommand(outOfBoundIndex, lessonIndex, studentIndex, VALID_WEEK_1, VALID_ATTENDANCE);

        assertCommandFailure(addAttendanceCommand, model, Messages.MESSAGE_INVALID_MODULE_CLASS_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidLessonIndex_failure() {
        Index moduleClassIndex = INDEX_FIRST_ITEM;
        ModuleClass moduleClass = model.getFilteredModuleClassList().get(moduleClassIndex.getZeroBased());
        Index outOfBoundIndex = Index.fromOneBased(moduleClass.getLessons().size() + 1);
        Index studentIndex = INDEX_FIRST_ITEM;

        AddAttendanceCommand addAttendanceCommand = new AddAttendanceCommand(moduleClassIndex,
                outOfBoundIndex, studentIndex, VALID_WEEK_1, VALID_ATTENDANCE);

        assertCommandFailure(addAttendanceCommand, model, Messages.MESSAGE_INVALID_LESSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidStudentIndex_failure() {
        Index moduleClassIndex = INDEX_FIRST_ITEM;
        Index lessonIndex = INDEX_FIRST_ITEM;
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredStudentList().size() + 1);

        AddAttendanceCommand addAttendanceCommand = new AddAttendanceCommand(moduleClassIndex,
                lessonIndex, outOfBoundIndex, VALID_WEEK_1, VALID_ATTENDANCE);

        assertCommandFailure(addAttendanceCommand, model, Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidWeek_failure() {
        Index moduleClassIndex = INDEX_FIRST_ITEM;
        Index lessonIndex = INDEX_FIRST_ITEM;
        Index studentIndex = INDEX_FIRST_ITEM;
        Lesson lesson = model.getFilteredModuleClassList().get(moduleClassIndex.getZeroBased())
            .getLessons().get(lessonIndex.getZeroBased());
        Week invalidWeek =
                new Week(Index.fromOneBased(lesson.getAttendanceRecordList().getAttendanceRecordList().size() + 1));

        AddAttendanceCommand addAttendanceCommand =
                new AddAttendanceCommand(moduleClassIndex, lessonIndex, studentIndex, invalidWeek, VALID_ATTENDANCE);

        assertCommandFailure(addAttendanceCommand, model, Messages.MESSAGE_INVALID_WEEK);
    }

    @Test
    public void equals() {
        Attendance attendanceScore90 = new Attendance(90);
        Attendance attendanceScore80 = new Attendance(80);
        Week week1 = new Week(Index.fromOneBased(1));
        Week week2 = new Week(Index.fromOneBased(2));
        AddAttendanceCommand addAttendanceCommand = new AddAttendanceCommand(INDEX_FIRST_ITEM, INDEX_FIRST_ITEM,
                INDEX_FIRST_ITEM, week1, attendanceScore90);

        // same object -> returns true
        assertTrue(addAttendanceCommand.equals(addAttendanceCommand));

        // same value -> returns true
        AddAttendanceCommand duplicateAddAttendanceCommand = new AddAttendanceCommand(INDEX_FIRST_ITEM,
                INDEX_FIRST_ITEM, INDEX_FIRST_ITEM, week1, attendanceScore90);
        assertTrue(addAttendanceCommand.equals(duplicateAddAttendanceCommand));

        // different type -> returns false
        assertFalse(addAttendanceCommand.equals(5));

        // null -> returns false
        assertFalse(addAttendanceCommand.equals(null));

        // different attendances -> returns false
        AddAttendanceCommand addAttendanceCommandDifferentAttendance =
                new AddAttendanceCommand(INDEX_FIRST_ITEM, INDEX_FIRST_ITEM, INDEX_FIRST_ITEM, week1,
                        attendanceScore80);
        assertFalse(addAttendanceCommand.equals(addAttendanceCommandDifferentAttendance));

        // different weeks -> return false
        AddAttendanceCommand addAttendanceCommandDifferentWeek =
                new AddAttendanceCommand(INDEX_FIRST_ITEM, INDEX_FIRST_ITEM, INDEX_FIRST_ITEM, week2,
                        attendanceScore90);
        assertFalse(addAttendanceCommand.equals(addAttendanceCommandDifferentWeek));
    }

    /**
     * Returns a new {@code ModuleClass} based on the given {@code moduleClass} but with the specified
     * {@code targetLesson} replaced.
     */
    private static ModuleClass manualReplaceLessonToModuleClass(ModuleClass moduleClass,
                                                                Lesson targetLesson, Lesson modifiedLesson) {
        List<Lesson> lessons = new ArrayList<>(moduleClass.getLessons());

        for (Lesson lesson : lessons) {
            if (lesson.isSameLesson(targetLesson)) {
                lessons.set(lessons.indexOf(lesson), modifiedLesson);
            }
        }

        return new ModuleClass(moduleClass.getName(), moduleClass.getStudentUuids(), lessons);
    }
}
