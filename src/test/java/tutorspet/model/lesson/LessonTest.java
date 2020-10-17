package tutorspet.model.lesson;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tutorspet.logic.commands.CommandTestUtil.VALID_DAY_WED_LESSON_WED_2_TO_4;
import static tutorspet.logic.commands.CommandTestUtil.VALID_END_TIME_1600_LESSON_WED_2_TO_4;
import static tutorspet.logic.commands.CommandTestUtil.VALID_NUMBER_OF_OCCURRENCES_7_LESSON_WED_2_TO_4;
import static tutorspet.logic.commands.CommandTestUtil.VALID_PARTICIPATION_SCORE_33;
import static tutorspet.logic.commands.CommandTestUtil.VALID_START_TIME_1400_LESSON_WED_2_TO_4;
import static tutorspet.logic.commands.CommandTestUtil.VALID_UUID_AMY;
import static tutorspet.logic.commands.CommandTestUtil.VALID_VENUE_COM1_B111_LESSON_WED_2_TO_4;
import static tutorspet.model.lesson.Lesson.TIME_FORMATTER;
import static tutorspet.testutil.Assert.assertThrows;
import static tutorspet.testutil.TypicalLesson.LESSON_FRI_8_TO_10;
import static tutorspet.testutil.TypicalLesson.LESSON_WED_2_TO_4;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import tutorspet.model.attendance.Attendance;
import tutorspet.model.attendance.AttendanceRecord;
import tutorspet.model.attendance.AttendanceRecordList;
import tutorspet.testutil.LessonBuilder;

public class LessonTest {

    private static final LocalTime VALID_START_TIME = LocalTime.parse(VALID_START_TIME_1400_LESSON_WED_2_TO_4);
    private static final LocalTime VALID_END_TIME = LocalTime.parse(VALID_END_TIME_1600_LESSON_WED_2_TO_4);
    private static final Venue VALID_VENUE = new Venue(VALID_VENUE_COM1_B111_LESSON_WED_2_TO_4);
    private static final NumberOfOccurrences VALID_NUMBER_OF_OCCURRENCES =
            new NumberOfOccurrences(1);
    private static final Attendance VALID_ATTENDANCE = new Attendance(VALID_PARTICIPATION_SCORE_33);
    private static final AttendanceRecord VALID_ATTENDANCE_RECORD =
            new AttendanceRecord(Map.of(UUID.fromString(VALID_UUID_AMY), VALID_ATTENDANCE));
    private static final AttendanceRecordList VALID_ATTENDANCE_RECORD_LIST =
            new AttendanceRecordList(Collections.singletonList(VALID_ATTENDANCE_RECORD));

    @Test
    public void constructor_newLesson_hasNewAttendanceRecordList() {
        Lesson lesson = new Lesson(VALID_START_TIME, VALID_END_TIME,
                VALID_DAY_WED_LESSON_WED_2_TO_4, VALID_NUMBER_OF_OCCURRENCES, VALID_VENUE);
        assertEquals(VALID_START_TIME, lesson.getStartTime());
        assertEquals(VALID_END_TIME, lesson.getEndTime());
        assertEquals(VALID_DAY_WED_LESSON_WED_2_TO_4, lesson.getDay());
        assertEquals(VALID_NUMBER_OF_OCCURRENCES, lesson.getNumberOfOccurrences());
        assertEquals(VALID_VENUE, lesson.getVenue());
        assertEquals(new AttendanceRecordList(VALID_NUMBER_OF_OCCURRENCES), lesson.getAttendanceRecordList());
    }

    @Test
    public void constructor_existingAttendanceRecordList_hasCorrectAttendanceRecordList() {
        Lesson lesson = new Lesson(VALID_START_TIME, VALID_END_TIME, VALID_DAY_WED_LESSON_WED_2_TO_4,
                VALID_NUMBER_OF_OCCURRENCES, VALID_VENUE, VALID_ATTENDANCE_RECORD_LIST);
        assertEquals(VALID_START_TIME, lesson.getStartTime());
        assertEquals(VALID_END_TIME, lesson.getEndTime());
        assertEquals(VALID_DAY_WED_LESSON_WED_2_TO_4, lesson.getDay());
        assertEquals(VALID_NUMBER_OF_OCCURRENCES, lesson.getNumberOfOccurrences());
        assertEquals(VALID_VENUE, lesson.getVenue());
        assertEquals(VALID_ATTENDANCE_RECORD_LIST, lesson.getAttendanceRecordList());
    }

    @Test
    public void constructor_attendanceRecordListMismatch_throwsAssertionError() {
        assertThrows(AssertionError.class, () ->
                new Lesson(VALID_START_TIME, VALID_END_TIME, VALID_DAY_WED_LESSON_WED_2_TO_4,
                        VALID_NUMBER_OF_OCCURRENCES, VALID_VENUE,
                        new AttendanceRecordList(new NumberOfOccurrences(VALID_NUMBER_OF_OCCURRENCES.value + 1))));
    }

    @Test
    public void isSameLesson() {
        // same object -> returns true
        assertTrue(LESSON_FRI_8_TO_10.isSameLesson(LESSON_FRI_8_TO_10));

        // same values -> returns true
        Lesson lessonCopy = new LessonBuilder(LESSON_FRI_8_TO_10).build();
        assertTrue(LESSON_FRI_8_TO_10.isSameLesson(lessonCopy));

        // null -> returns false
        assertFalse(LESSON_FRI_8_TO_10.isSameLesson(null));

        // different start time -> returns false
        Lesson editedLesson = new LessonBuilder(LESSON_FRI_8_TO_10)
                .withStartTime(LocalTime.parse(VALID_START_TIME_1400_LESSON_WED_2_TO_4, TIME_FORMATTER)).build();
        assertFalse(LESSON_FRI_8_TO_10.isSameLesson(editedLesson));

        // different end time -> returns false
        editedLesson = new LessonBuilder(LESSON_FRI_8_TO_10)
                .withEndTime(LocalTime.parse(VALID_END_TIME_1600_LESSON_WED_2_TO_4, TIME_FORMATTER)).build();
        assertFalse(LESSON_FRI_8_TO_10.isSameLesson(editedLesson));

        // different day -> returns false
        editedLesson = new LessonBuilder(LESSON_FRI_8_TO_10).withDay(VALID_DAY_WED_LESSON_WED_2_TO_4).build();
        assertFalse(LESSON_FRI_8_TO_10.isSameLesson(editedLesson));

        // different venue -> returns false
        editedLesson = new LessonBuilder(LESSON_FRI_8_TO_10).withVenue(VALID_VENUE_COM1_B111_LESSON_WED_2_TO_4).build();
        assertFalse(LESSON_FRI_8_TO_10.isSameLesson(editedLesson));

        // different number of occurrences -> returns true
        editedLesson = new LessonBuilder(LESSON_FRI_8_TO_10)
                .withNumberOfOccurrences(VALID_NUMBER_OF_OCCURRENCES_7_LESSON_WED_2_TO_4).build();
        assertTrue(LESSON_FRI_8_TO_10.isSameLesson(editedLesson));

        // different attendance record list and same number of occurrences -> return true
        List<AttendanceRecord> records = new ArrayList<>(Collections.nCopies(13, new AttendanceRecord()));
        records.set(0, VALID_ATTENDANCE_RECORD);
        editedLesson = new LessonBuilder(LESSON_FRI_8_TO_10)
                .withAttendanceRecordList(new AttendanceRecordList(records))
                .build();
        assertTrue(LESSON_FRI_8_TO_10.isSameLesson(editedLesson));
    }

    @Test
    public void toString_returnsCorrectFormat() {
        Lesson lesson = new Lesson(VALID_START_TIME, VALID_END_TIME, VALID_DAY_WED_LESSON_WED_2_TO_4,
                VALID_NUMBER_OF_OCCURRENCES, VALID_VENUE, VALID_ATTENDANCE_RECORD_LIST);
        String expectedString = (new StringBuilder()).append(VALID_DAY_WED_LESSON_WED_2_TO_4)
                .append(" ")
                .append(TIME_FORMATTER.format(VALID_START_TIME))
                .append(" to ")
                .append(TIME_FORMATTER.format(VALID_END_TIME))
                .append(" Venue: ")
                .append(VALID_VENUE.venue)
                .append(" Number of occurrences: ")
                .append(VALID_NUMBER_OF_OCCURRENCES.value).toString();
        assertEquals(expectedString, lesson.toString());
    }

    @Test
    public void equals() {
        // same object -> returns true
        assertTrue(LESSON_FRI_8_TO_10.equals(LESSON_FRI_8_TO_10));

        // same values -> returns true
        Lesson lessonCopy = new LessonBuilder(LESSON_FRI_8_TO_10).build();
        assertTrue(LESSON_FRI_8_TO_10.equals(lessonCopy));

        // null -> returns false
        assertFalse(LESSON_FRI_8_TO_10.equals(null));

        // different type -> returns false
        assertFalse(LESSON_FRI_8_TO_10.equals(1));

        // different lesson -> returns false
        assertFalse(LESSON_FRI_8_TO_10.equals(LESSON_WED_2_TO_4));

        // different start time -> returns false
        Lesson editedLesson = new LessonBuilder(LESSON_FRI_8_TO_10)
                .withStartTime(LocalTime.parse(VALID_START_TIME_1400_LESSON_WED_2_TO_4, TIME_FORMATTER)).build();
        assertFalse(LESSON_FRI_8_TO_10.equals(editedLesson));

        // different end time -> returns false
        editedLesson = new LessonBuilder(LESSON_FRI_8_TO_10)
                .withEndTime(LocalTime.parse(VALID_END_TIME_1600_LESSON_WED_2_TO_4, TIME_FORMATTER)).build();
        assertFalse(LESSON_FRI_8_TO_10.equals(editedLesson));

        // different day -> returns false
        editedLesson = new LessonBuilder(LESSON_FRI_8_TO_10).withDay(VALID_DAY_WED_LESSON_WED_2_TO_4).build();
        assertFalse(LESSON_FRI_8_TO_10.equals(editedLesson));

        // different venue -> returns false
        editedLesson = new LessonBuilder(LESSON_FRI_8_TO_10).withVenue(VALID_VENUE_COM1_B111_LESSON_WED_2_TO_4).build();
        assertFalse(LESSON_FRI_8_TO_10.equals(editedLesson));

        // different number of occurrences -> returns false
        editedLesson = new LessonBuilder(LESSON_FRI_8_TO_10)
                .withNumberOfOccurrences(VALID_NUMBER_OF_OCCURRENCES_7_LESSON_WED_2_TO_4).build();
        assertFalse(LESSON_FRI_8_TO_10.equals(editedLesson));

        // different attendance record list and same number of occurrences -> return false
        List<AttendanceRecord> records = new ArrayList<>(Collections.nCopies(13, new AttendanceRecord()));
        records.set(0, VALID_ATTENDANCE_RECORD);
        editedLesson = new LessonBuilder(LESSON_FRI_8_TO_10)
                .withAttendanceRecordList(new AttendanceRecordList(records))
                .build();
        assertFalse(LESSON_FRI_8_TO_10.equals(editedLesson));
    }
}
