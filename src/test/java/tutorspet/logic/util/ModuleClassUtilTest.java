package tutorspet.logic.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static tutorspet.commons.core.Messages.MESSAGE_INVALID_LESSON_DISPLAYED_INDEX;
import static tutorspet.commons.core.Messages.MESSAGE_INVALID_STUDENT_IN_MODULE_CLASS;
import static tutorspet.logic.commands.CommandTestUtil.VALID_PARTICIPATION_SCORE_80;
import static tutorspet.logic.commands.CommandTestUtil.VALID_WEEK_1;
import static tutorspet.logic.commands.CommandTestUtil.VALID_WEEK_5;
import static tutorspet.logic.util.LessonUtil.addAttendanceToLesson;
import static tutorspet.logic.util.LessonUtil.deleteAttendanceFromLesson;
import static tutorspet.logic.util.LessonUtil.editAttendanceInLesson;
import static tutorspet.logic.util.ModuleClassUtil.addAttendanceToModuleClass;
import static tutorspet.logic.util.ModuleClassUtil.deleteAttendanceFromModuleClass;
import static tutorspet.logic.util.ModuleClassUtil.editAttendanceInModuleClass;
import static tutorspet.logic.util.ModuleClassUtil.getAttendanceFromModuleClass;
import static tutorspet.testutil.Assert.assertThrows;
import static tutorspet.testutil.TypicalModuleClass.CS2103T_TUTORIAL;
import static tutorspet.testutil.TypicalStudent.ALICE;
import static tutorspet.testutil.TypicalStudent.CARL;

import org.junit.jupiter.api.Test;

import tutorspet.commons.core.index.Index;
import tutorspet.logic.commands.exceptions.CommandException;
import tutorspet.model.attendance.Attendance;
import tutorspet.model.lesson.Lesson;
import tutorspet.model.moduleclass.ModuleClass;
import tutorspet.testutil.ModuleClassBuilder;

public class ModuleClassUtilTest {

    @Test
    public void addAttendanceToModuleClass_validParameters_success() throws CommandException {
        Attendance attendanceToAdd = new Attendance(VALID_PARTICIPATION_SCORE_80);
        Index lessonIndex = Index.fromZeroBased(0);

        Lesson lesson = CS2103T_TUTORIAL.getLessons().get(lessonIndex.getZeroBased());
        Lesson editedLesson = addAttendanceToLesson(lesson, ALICE, VALID_WEEK_5, attendanceToAdd);

        ModuleClass expectedModuleClass = new ModuleClassBuilder(CS2103T_TUTORIAL).withLessons(editedLesson).build();

        assertEquals(expectedModuleClass,
                addAttendanceToModuleClass(CS2103T_TUTORIAL, lessonIndex, VALID_WEEK_5, ALICE, attendanceToAdd));
    }

    @Test
    public void addAttendanceToModuleClass_invalidStudent_throwsCommandException() {
        Attendance attendanceToAdd = new Attendance(VALID_PARTICIPATION_SCORE_80);
        Index lessonIndex = Index.fromZeroBased(0);

        assertThrows(CommandException.class, MESSAGE_INVALID_STUDENT_IN_MODULE_CLASS, () ->
                addAttendanceToModuleClass(CS2103T_TUTORIAL, lessonIndex, VALID_WEEK_5, CARL, attendanceToAdd));
    }

    @Test
    public void addAttendanceToModuleClass_invalidLessonIndex_throwsCommandException() {
        Attendance attendanceToAdd = new Attendance(VALID_PARTICIPATION_SCORE_80);
        Index lessonIndex = Index.fromZeroBased(5);

        assertThrows(CommandException.class, MESSAGE_INVALID_LESSON_DISPLAYED_INDEX, () ->
                addAttendanceToModuleClass(CS2103T_TUTORIAL, lessonIndex, VALID_WEEK_5, ALICE, attendanceToAdd));
    }

    @Test
    public void editAttendanceInModuleClass_validParameters_success() throws CommandException {
        Attendance attendanceToSet = new Attendance(VALID_PARTICIPATION_SCORE_80);
        Index lessonIndex = Index.fromZeroBased(0);

        Lesson lesson = CS2103T_TUTORIAL.getLessons().get(lessonIndex.getZeroBased());
        Lesson editedLesson = editAttendanceInLesson(lesson, ALICE, VALID_WEEK_1, attendanceToSet);

        ModuleClass expectedModuleClass = new ModuleClassBuilder(CS2103T_TUTORIAL).withLessons(editedLesson).build();

        assertEquals(expectedModuleClass,
                editAttendanceInModuleClass(CS2103T_TUTORIAL, lessonIndex, VALID_WEEK_1, ALICE, attendanceToSet));
    }

    @Test
    public void editAttendanceInModuleClass_invalidStudent_throwsCommandException() {
        Attendance attendanceToSet = new Attendance(VALID_PARTICIPATION_SCORE_80);
        Index lessonIndex = Index.fromZeroBased(0);

        assertThrows(CommandException.class, MESSAGE_INVALID_STUDENT_IN_MODULE_CLASS, () ->
                editAttendanceInModuleClass(CS2103T_TUTORIAL, lessonIndex, VALID_WEEK_1, CARL, attendanceToSet));
    }

    @Test
    public void editAttendanceInModuleClass_invalidLessonIndex_throwsCommandException() {
        Attendance attendanceToSet = new Attendance(VALID_PARTICIPATION_SCORE_80);
        Index lessonIndex = Index.fromZeroBased(5);

        assertThrows(CommandException.class, MESSAGE_INVALID_LESSON_DISPLAYED_INDEX, () ->
                editAttendanceInModuleClass(CS2103T_TUTORIAL, lessonIndex, VALID_WEEK_1, ALICE, attendanceToSet));
    }

    @Test
    public void deleteAttendanceFromModuleClass_validParameters_success() throws CommandException {
        Index lessonIndex = Index.fromZeroBased(0);

        Lesson lesson = CS2103T_TUTORIAL.getLessons().get(lessonIndex.getZeroBased());
        Lesson editedLesson = deleteAttendanceFromLesson(lesson, ALICE, VALID_WEEK_1);

        ModuleClass expectedModuleClass = new ModuleClassBuilder(CS2103T_TUTORIAL).withLessons(editedLesson).build();

        assertEquals(expectedModuleClass,
                deleteAttendanceFromModuleClass(CS2103T_TUTORIAL, lessonIndex, VALID_WEEK_1, ALICE));
    }

    @Test
    public void deleteAttendanceFromModuleClass_invalidStudent_throwsCommandException() {
        Index lessonIndex = Index.fromZeroBased(0);

        assertThrows(CommandException.class, MESSAGE_INVALID_STUDENT_IN_MODULE_CLASS, () ->
                deleteAttendanceFromModuleClass(CS2103T_TUTORIAL, lessonIndex, VALID_WEEK_1, CARL));
    }

    @Test
    public void deleteAttendanceFromModuleClass_invalidLessonIndex_throwsCommandException() {
        Index lessonIndex = Index.fromZeroBased(5);

        assertThrows(CommandException.class, MESSAGE_INVALID_LESSON_DISPLAYED_INDEX, () ->
                deleteAttendanceFromModuleClass(CS2103T_TUTORIAL, lessonIndex, VALID_WEEK_1, ALICE));
    }

    @Test
    public void getAttendanceFromModuleClass_validParameters_success() throws CommandException {
        Index lessonIndex = Index.fromZeroBased(0);

        assertEquals(new Attendance(VALID_PARTICIPATION_SCORE_80),
                getAttendanceFromModuleClass(CS2103T_TUTORIAL, lessonIndex, VALID_WEEK_1, ALICE));
    }

    @Test
    public void getAttendanceFromModuleClass_invalidStudent_throwsCommandException() {
        Index lessonIndex = Index.fromZeroBased(0);

        assertThrows(CommandException.class, MESSAGE_INVALID_STUDENT_IN_MODULE_CLASS, () ->
                getAttendanceFromModuleClass(CS2103T_TUTORIAL, lessonIndex, VALID_WEEK_1, CARL));
    }

    @Test
    public void getAttendanceFromModuleClass_invalidLessonIndex_throwsCommandException() {
        Index lessonIndex = Index.fromZeroBased(5);

        assertThrows(CommandException.class, MESSAGE_INVALID_LESSON_DISPLAYED_INDEX, () ->
                getAttendanceFromModuleClass(CS2103T_TUTORIAL, lessonIndex, VALID_WEEK_1, ALICE));
    }
}
