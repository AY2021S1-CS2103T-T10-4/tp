package tutorspet.logic.commands;

import static tutorspet.commons.core.Messages.MESSAGE_INVALID_LESSON_DISPLAYED_INDEX;
import static tutorspet.commons.core.Messages.MESSAGE_INVALID_MODULE_CLASS_DISPLAYED_INDEX;
import static tutorspet.commons.core.Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX;
import static tutorspet.commons.core.Messages.MESSAGE_INVALID_WEEK;
import static tutorspet.commons.core.Messages.MESSAGE_MISSING_STUDENT_ATTENDANCE;
import static tutorspet.logic.commands.CommandTestUtil.VALID_WEEK_1;
import static tutorspet.logic.commands.CommandTestUtil.VALID_WEEK_5;
import static tutorspet.logic.commands.CommandTestUtil.assertCommandFailure;
import static tutorspet.logic.commands.CommandTestUtil.assertCommandSuccess;
import static tutorspet.logic.commands.DeleteAttendanceCommand.MESSAGE_DELETE_ATTENDANCE_SUCCESS;
import static tutorspet.testutil.Assert.assertThrows;
import static tutorspet.testutil.ModuleClassTestUtil.manualReplaceLessonToModuleClass;
import static tutorspet.testutil.TypicalIndexes.INDEX_FIRST_ITEM;
import static tutorspet.testutil.TypicalTutorsPet.getTypicalTutorsPet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Test;

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
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code DeleteAttendanceCommand}
 */
public class DeleteAttendanceCommandTest {

    private Model model = new ModelManager(getTypicalTutorsPet(), new UserPrefs());

    @Test
    public void constructor_nullIndexes_throwsNullPointerException() {
        Index moduleClassIndex = INDEX_FIRST_ITEM;
        Index lessonIndex = INDEX_FIRST_ITEM;
        Index studentIndex = INDEX_FIRST_ITEM;
        Week targetWeek = VALID_WEEK_1;

        assertThrows(NullPointerException.class, () -> new DeleteAttendanceCommand(
                null, lessonIndex, studentIndex, targetWeek));
        assertThrows(NullPointerException.class, () -> new DeleteAttendanceCommand(
                moduleClassIndex, null, studentIndex, targetWeek));
        assertThrows(NullPointerException.class, () -> new DeleteAttendanceCommand(
                moduleClassIndex, lessonIndex, null, targetWeek));
    }

    @Test
    public void constructor_nullWeek_throwsNullPointerException() {
        Index moduleClassIndex = INDEX_FIRST_ITEM;
        Index lessonIndex = INDEX_FIRST_ITEM;
        Index studentIndex = INDEX_FIRST_ITEM;

        assertThrows(NullPointerException.class, () -> new DeleteAttendanceCommand(
                moduleClassIndex, lessonIndex, studentIndex, null));
    }

    @Test
    public void execute_validIndexes_success() {
        Index moduleClassIndex = INDEX_FIRST_ITEM;
        Index lessonIndex = INDEX_FIRST_ITEM;
        Index studentIndex = INDEX_FIRST_ITEM;
        Week targetWeek = VALID_WEEK_1;

        ModuleClass moduleClass = model.getFilteredModuleClassList().get(moduleClassIndex.getZeroBased());
        Student student = model.getFilteredStudentList().get(studentIndex.getZeroBased());
        Lesson lesson = moduleClass.getLessons().get(lessonIndex.getZeroBased());

        // delete week 1 attendance record
        Map<UUID, Attendance> record = lesson.getAttendanceRecordList()
                .getAttendanceRecord(targetWeek).getAttendanceRecord();
        Map<UUID, Attendance> updatedRecord = new HashMap<>(record);
        updatedRecord.remove(student.getUuid());
        AttendanceRecord updatedAttendanceRecord = new AttendanceRecord(updatedRecord);
        List<AttendanceRecord> updatedAttendanceRecords =
                new ArrayList<>(lesson.getAttendanceRecordList().getAttendanceRecordList());
        updatedAttendanceRecords.set(targetWeek.getZeroBasedWeekIndex(), updatedAttendanceRecord);

        Lesson modifiedLesson = new LessonBuilder(lesson)
                .withAttendanceRecordList(new AttendanceRecordList(updatedAttendanceRecords)).build();
        ModuleClass modifiedModuleClass =
                manualReplaceLessonToModuleClass(moduleClass, lesson, modifiedLesson);

        String expectedMessage = String.format(
                MESSAGE_DELETE_ATTENDANCE_SUCCESS, targetWeek,
                student.getName(), modifiedLesson);
        Model expectedModel = new ModelManager(model.getTutorsPet(), new UserPrefs());
        expectedModel.setModuleClass(moduleClass, modifiedModuleClass);
        expectedModel.commit(expectedMessage);

        DeleteAttendanceCommand deleteAttendanceCommand =
                new DeleteAttendanceCommand(moduleClassIndex, lessonIndex, studentIndex, targetWeek);

        assertCommandSuccess(deleteAttendanceCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_missingAttendance_failure() {
        Index moduleClassIndex = INDEX_FIRST_ITEM;
        Index lessonIndex = INDEX_FIRST_ITEM;
        Index studentIndex = INDEX_FIRST_ITEM;
        Week targetWeek = VALID_WEEK_5;

        ModuleClass moduleClass = model.getFilteredModuleClassList().get(moduleClassIndex.getZeroBased());
        Student student = model.getFilteredStudentList().get(studentIndex.getZeroBased());
        Lesson lesson = moduleClass.getLessons().get(lessonIndex.getZeroBased());

        // delete non-existent week 5 attendance record
        Map<UUID, Attendance> record = lesson.getAttendanceRecordList()
                .getAttendanceRecord(targetWeek).getAttendanceRecord();
        Map<UUID, Attendance> updatedRecord = new HashMap<>(record);
        updatedRecord.remove(student.getUuid());
        AttendanceRecord updatedAttendanceRecord = new AttendanceRecord(updatedRecord);
        List<AttendanceRecord> updatedAttendanceRecords =
                new ArrayList<>(lesson.getAttendanceRecordList().getAttendanceRecordList());
        updatedAttendanceRecords.set(targetWeek.getZeroBasedWeekIndex(), updatedAttendanceRecord);

        Lesson modifiedLesson = new LessonBuilder(lesson)
                .withAttendanceRecordList(new AttendanceRecordList(updatedAttendanceRecords)).build();
        ModuleClass modifiedModuleClass =
                manualReplaceLessonToModuleClass(moduleClass, lesson, modifiedLesson);

        model.setModuleClass(moduleClass, modifiedModuleClass);

        DeleteAttendanceCommand deleteAttendanceCommand =
                new DeleteAttendanceCommand(moduleClassIndex, lessonIndex, studentIndex, targetWeek);

        assertCommandFailure(deleteAttendanceCommand, model, MESSAGE_MISSING_STUDENT_ATTENDANCE);
    }

    @Test
    public void execute_invalidModuleClassIndex_failure() {
        Index moduleClassIndex = Index.fromOneBased(model.getFilteredModuleClassList().size() + 1);
        Index lessonIndex = INDEX_FIRST_ITEM;
        Index studentIndex = INDEX_FIRST_ITEM;
        Week targetWeek = VALID_WEEK_1;

        DeleteAttendanceCommand deleteAttendanceCommand =
                new DeleteAttendanceCommand(moduleClassIndex, lessonIndex, studentIndex, targetWeek);

        assertCommandFailure(deleteAttendanceCommand, model, MESSAGE_INVALID_MODULE_CLASS_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidLessonIndex_failure() {
        Index moduleClassIndex = INDEX_FIRST_ITEM;
        ModuleClass moduleClass = model.getFilteredModuleClassList().get(moduleClassIndex.getZeroBased());
        Index lessonIndex = Index.fromOneBased(moduleClass.getLessons().size() + 1);
        Index studentIndex = INDEX_FIRST_ITEM;
        Week targetWeek = VALID_WEEK_1;

        DeleteAttendanceCommand deleteAttendanceCommand =
                new DeleteAttendanceCommand(moduleClassIndex, lessonIndex, studentIndex, targetWeek);

        assertCommandFailure(deleteAttendanceCommand, model, MESSAGE_INVALID_LESSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidStudentIndex_failure() {
        Index moduleClassIndex = INDEX_FIRST_ITEM;
        Index lessonIndex = INDEX_FIRST_ITEM;
        Index studentIndex = Index.fromOneBased(model.getFilteredStudentList().size() + 1);
        Week targetWeek = VALID_WEEK_1;

        DeleteAttendanceCommand deleteAttendanceCommand =
                new DeleteAttendanceCommand(moduleClassIndex, lessonIndex, studentIndex, targetWeek);

        assertCommandFailure(deleteAttendanceCommand, model, MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidWeek_failure() {
        Index moduleClassIndex = INDEX_FIRST_ITEM;
        Index lessonIndex = INDEX_FIRST_ITEM;
        Index studentIndex = INDEX_FIRST_ITEM;
        ModuleClass moduleClass = model.getFilteredModuleClassList().get(moduleClassIndex.getZeroBased());
        Lesson lesson = moduleClass.getLessons().get(lessonIndex.getZeroBased());
        Week targetWeek =
                new Week(Index.fromOneBased(lesson.getAttendanceRecordList().getAttendanceRecordList().size() + 1));

        DeleteAttendanceCommand deleteAttendanceCommand =
                new DeleteAttendanceCommand(moduleClassIndex, lessonIndex, studentIndex, targetWeek);

        assertCommandFailure(deleteAttendanceCommand, model, MESSAGE_INVALID_WEEK);
    }
}