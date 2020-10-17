package tutorspet.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static tutorspet.storage.JsonAdaptedLesson.END_TIME_FIELD;
import static tutorspet.storage.JsonAdaptedLesson.INVALID_FIELD_MESSAGE_FORMAT;
import static tutorspet.storage.JsonAdaptedLesson.MISSING_FIELD_MESSAGE_FORMAT;
import static tutorspet.storage.JsonAdaptedLesson.START_TIME_FIELD;
import static tutorspet.testutil.Assert.assertThrows;
import static tutorspet.testutil.TypicalLesson.LESSON_WED_2_TO_4;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import tutorspet.commons.exceptions.IllegalValueException;
import tutorspet.model.attendance.AttendanceRecordList;
import tutorspet.model.lesson.Day;
import tutorspet.model.lesson.NumberOfOccurrences;
import tutorspet.model.lesson.Venue;
import tutorspet.storage.attendance.JsonAdaptedAttendanceRecordList;

public class JsonAdaptedLessonTest {

    private static final String INVALID_TIME = "1400";
    private static final String INVALID_DAY = "wed";
    private static final int INVALID_NUMBER_OF_OCCURRENCES = NumberOfOccurrences.UPPER_BOUND + 1;
    private static final String INVALID_VENUE = "!@#$%^&*";
    private static final JsonAdaptedAttendanceRecordList INVALID_JSON_ADAPTED_ATTENDANCE_RECORD_LIST =
            new JsonAdaptedAttendanceRecordList(Collections.singletonList(null));

    private static final String VALID_START_TIME = LESSON_WED_2_TO_4.getStartTime().toString();
    private static final String VALID_END_TIME = LESSON_WED_2_TO_4.getEndTime().toString();
    private static final String VALID_DAY = LESSON_WED_2_TO_4.getDay().toString();
    private static final int VALID_NUMBER_OF_OCCURRENCES = LESSON_WED_2_TO_4.getNumberOfOccurrences().value;
    private static final String VALID_VENUE = LESSON_WED_2_TO_4.getVenue().toString();
    private static final JsonAdaptedAttendanceRecordList VALID_JSON_ADAPTED_ATTENDANCE_RECORD_LIST =
            new JsonAdaptedAttendanceRecordList(LESSON_WED_2_TO_4.getAttendanceRecordList());

    @Test
    public void toModelType_validLessonDetails_returnsLesson() throws Exception {
        JsonAdaptedLesson lesson = new JsonAdaptedLesson(LESSON_WED_2_TO_4);
        assertEquals(LESSON_WED_2_TO_4, lesson.toModelType());
    }

    @Test
    public void toModelType_validIndividualLessonDetails_returnsLesson() throws Exception {
        JsonAdaptedLesson lesson = new JsonAdaptedLesson(
                VALID_START_TIME, VALID_END_TIME, VALID_DAY, VALID_NUMBER_OF_OCCURRENCES, VALID_VENUE,
                VALID_JSON_ADAPTED_ATTENDANCE_RECORD_LIST);
        assertEquals(LESSON_WED_2_TO_4, lesson.toModelType());
    }

    @Test
    public void toModelType_invalidStartTime_throwsIllegalValueException() {
        JsonAdaptedLesson lesson = new JsonAdaptedLesson(
                INVALID_TIME, VALID_END_TIME, VALID_DAY, VALID_NUMBER_OF_OCCURRENCES, VALID_VENUE,
                VALID_JSON_ADAPTED_ATTENDANCE_RECORD_LIST);
        String expectedMessage = String.format(INVALID_FIELD_MESSAGE_FORMAT, START_TIME_FIELD);
        assertThrows(IllegalValueException.class, expectedMessage, lesson::toModelType);
    }

    @Test
    public void toModelType_nullStartTime_throwsIllegalValueException() {
        JsonAdaptedLesson lesson = new JsonAdaptedLesson(
                null, VALID_END_TIME, VALID_DAY, VALID_NUMBER_OF_OCCURRENCES, VALID_VENUE,
                VALID_JSON_ADAPTED_ATTENDANCE_RECORD_LIST);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, START_TIME_FIELD);
        assertThrows(IllegalValueException.class, expectedMessage, lesson::toModelType);
    }

    @Test
    public void toModelType_invalidEndTime_throwsIllegalValueException() {
        JsonAdaptedLesson lesson = new JsonAdaptedLesson(
                VALID_START_TIME, INVALID_TIME, VALID_DAY, VALID_NUMBER_OF_OCCURRENCES, VALID_VENUE,
                VALID_JSON_ADAPTED_ATTENDANCE_RECORD_LIST);
        String expectedMessage = String.format(INVALID_FIELD_MESSAGE_FORMAT, END_TIME_FIELD);
        assertThrows(IllegalValueException.class, expectedMessage, lesson::toModelType);
    }

    @Test
    public void toModelType_nullEndTime_throwsIllegalValueException() {
        JsonAdaptedLesson lesson = new JsonAdaptedLesson(
                VALID_START_TIME, null, VALID_DAY, VALID_NUMBER_OF_OCCURRENCES, VALID_VENUE,
                VALID_JSON_ADAPTED_ATTENDANCE_RECORD_LIST);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, END_TIME_FIELD);
        assertThrows(IllegalValueException.class, expectedMessage, lesson::toModelType);
    }

    @Test
    public void toModelType_invalidDay_throwsIllegalValueException() {
        JsonAdaptedLesson lesson = new JsonAdaptedLesson(
                VALID_START_TIME, VALID_END_TIME, INVALID_DAY, VALID_NUMBER_OF_OCCURRENCES, VALID_VENUE,
                VALID_JSON_ADAPTED_ATTENDANCE_RECORD_LIST);
        String expectedMessage = Day.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, lesson::toModelType);
    }

    @Test
    public void toModelType_nullDay_throwsIllegalValueException() {
        JsonAdaptedLesson lesson = new JsonAdaptedLesson(
                VALID_START_TIME, VALID_END_TIME, null, VALID_NUMBER_OF_OCCURRENCES, VALID_VENUE,
                VALID_JSON_ADAPTED_ATTENDANCE_RECORD_LIST);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Day.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, lesson::toModelType);
    }

    @Test
    public void toModelType_invalidNumberOfOccurrences_throwsIllegalValueException() {
        JsonAdaptedLesson lesson = new JsonAdaptedLesson(
                VALID_START_TIME, VALID_END_TIME, VALID_DAY, INVALID_NUMBER_OF_OCCURRENCES, VALID_VENUE,
                VALID_JSON_ADAPTED_ATTENDANCE_RECORD_LIST);
        String expectedMessage = NumberOfOccurrences.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, lesson::toModelType);
    }

    @Test
    public void toModelType_invalidVenue_throwsIllegalValueException() {
        JsonAdaptedLesson lesson = new JsonAdaptedLesson(
                VALID_START_TIME, VALID_END_TIME, VALID_DAY, VALID_NUMBER_OF_OCCURRENCES, INVALID_VENUE,
                VALID_JSON_ADAPTED_ATTENDANCE_RECORD_LIST);
        String expectedMessage = Venue.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, lesson::toModelType);
    }

    @Test
    public void toModelType_nullVenue_throwsIllegalValueException() {
        JsonAdaptedLesson lesson = new JsonAdaptedLesson(
                VALID_START_TIME, VALID_END_TIME, VALID_DAY, VALID_NUMBER_OF_OCCURRENCES, null,
                VALID_JSON_ADAPTED_ATTENDANCE_RECORD_LIST);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Venue.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, lesson::toModelType);
    }

    @Test
    public void toModelType_invalidJsonAdaptedAttendanceRecordList_throwsIllegalValueException() {
        JsonAdaptedLesson lesson = new JsonAdaptedLesson(VALID_START_TIME, VALID_END_TIME, VALID_DAY,
                VALID_NUMBER_OF_OCCURRENCES, VALID_VENUE, INVALID_JSON_ADAPTED_ATTENDANCE_RECORD_LIST);
        assertThrows(IllegalValueException.class,
                JsonAdaptedAttendanceRecordList.MESSAGE_INVALID_RECORD,
                lesson::toModelType);
    }

    @Test
    public void toModelType_nullJsonAdaptedAttendanceRecordList_throwsIllegalValueException() {
        JsonAdaptedLesson lesson = new JsonAdaptedLesson(
                VALID_START_TIME, VALID_END_TIME, VALID_DAY, VALID_NUMBER_OF_OCCURRENCES, VALID_VENUE, null);
        String expectedMessage =
                String.format(MISSING_FIELD_MESSAGE_FORMAT, AttendanceRecordList.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, lesson::toModelType);
    }

    @Test
    public void toModelType_mismatchedJsonAdaptedAttendanceRecordList_throwsIllegalValueException() {
        JsonAdaptedLesson lesson = new JsonAdaptedLesson(
                VALID_START_TIME, VALID_END_TIME, VALID_DAY, VALID_NUMBER_OF_OCCURRENCES, VALID_VENUE,
                new JsonAdaptedAttendanceRecordList(new AttendanceRecordList(
                        new NumberOfOccurrences(VALID_NUMBER_OF_OCCURRENCES - 1))));
        String expectedMessage =
                String.format(INVALID_FIELD_MESSAGE_FORMAT, AttendanceRecordList.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, lesson::toModelType);
    }
}
