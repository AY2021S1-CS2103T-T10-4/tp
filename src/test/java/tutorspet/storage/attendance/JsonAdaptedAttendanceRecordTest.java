package tutorspet.storage.attendance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static tutorspet.logic.commands.CommandTestUtil.EMPTY_ATTENDANCE_RECORD;
import static tutorspet.logic.commands.CommandTestUtil.VALID_ATTENDANCE_RECORD;
import static tutorspet.logic.commands.CommandTestUtil.VALID_PARTICIPATION_SCORE_33;
import static tutorspet.logic.commands.CommandTestUtil.VALID_PARTICIPATION_SCORE_51;
import static tutorspet.logic.commands.CommandTestUtil.VALID_UUID_AMY;
import static tutorspet.logic.commands.CommandTestUtil.VALID_UUID_BOB;
import static tutorspet.logic.commands.CommandTestUtil.VALID_WEEK_INDEX_5;
import static tutorspet.logic.commands.CommandTestUtil.VALID_WEEK_VALUE_5;
import static tutorspet.storage.attendance.JsonAdaptedStudentAttendanceTest.VALID_JSON_ADAPTED_STUDENT_ATTENDANCE_AMY;
import static tutorspet.storage.attendance.JsonAdaptedStudentAttendanceTest.VALID_JSON_ADAPTED_STUDENT_ATTENDANCE_BOB;
import static tutorspet.testutil.Assert.assertThrows;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import tutorspet.commons.core.index.Index;
import tutorspet.commons.exceptions.IllegalValueException;
import tutorspet.model.attendance.Attendance;
import tutorspet.model.attendance.AttendanceRecord;
import tutorspet.model.attendance.Week;
import tutorspet.model.lesson.NumberOfOccurrences;

public class JsonAdaptedAttendanceRecordTest {

    /** A {@code JsonAdaptedAttendanceRecord} for testing representing week 1 and is empty. */
    public static final JsonAdaptedAttendanceRecord EMPTY_JSON_ADAPTED_ATTENDANCE_RECORD_WEEK_ONE =
            new JsonAdaptedAttendanceRecord(new Week(Index.fromOneBased(1)), EMPTY_ATTENDANCE_RECORD);
    /**
     * A {@code JsonAdaptedAttendanceRecord} for testing representing week 2
     * and has two valid {@code JsonAdaptedStudentAttendance}s
     */
    public static final JsonAdaptedAttendanceRecord VALID_JSON_ADAPTED_ATTENDANCE_RECORD_WEEK_TWO =
            new JsonAdaptedAttendanceRecord(new Week(Index.fromOneBased(2)), VALID_ATTENDANCE_RECORD);

    private static final List<JsonAdaptedStudentAttendance> VALID_JSON_ADAPTED_STUDENT_ATTENDANCES =
            Arrays.asList(VALID_JSON_ADAPTED_STUDENT_ATTENDANCE_AMY, VALID_JSON_ADAPTED_STUDENT_ATTENDANCE_BOB);

    @Test
    public void toKeyValuePair_validAttendanceRecord_returnsAttendanceRecord() throws Exception {
        JsonAdaptedAttendanceRecord record =
                new JsonAdaptedAttendanceRecord(new Week(VALID_WEEK_INDEX_5), VALID_ATTENDANCE_RECORD);
        assertEquals(VALID_ATTENDANCE_RECORD, record.toKeyValuePair().getValue());
    }

    @Test
    public void toKeyValuePair_emptyAttendanceRecord_returnsAttendanceRecord() throws Exception {
        JsonAdaptedAttendanceRecord record =
                new JsonAdaptedAttendanceRecord(new Week(VALID_WEEK_INDEX_5), new AttendanceRecord());
        assertEquals(new AttendanceRecord(), record.toKeyValuePair().getValue());
    }

    @Test
    public void toKeyValuePair_validWeek_returnsWeek() throws Exception {
        JsonAdaptedAttendanceRecord record =
                new JsonAdaptedAttendanceRecord(new Week(VALID_WEEK_INDEX_5), new AttendanceRecord());
        assertEquals(new Week(VALID_WEEK_INDEX_5), record.toKeyValuePair().getKey());
    }

    @Test
    public void toKeyValuePair_validJsonAdaptedStudentAttendances_returnsAttendanceRecord() throws Exception {
        JsonAdaptedAttendanceRecord record =
                new JsonAdaptedAttendanceRecord(VALID_WEEK_VALUE_5, VALID_JSON_ADAPTED_STUDENT_ATTENDANCES);

        Map<UUID, Attendance> attendanceMap = new HashMap<>();
        attendanceMap.put(UUID.fromString(VALID_UUID_AMY), new Attendance(VALID_PARTICIPATION_SCORE_33));
        attendanceMap.put(UUID.fromString(VALID_UUID_BOB), new Attendance(VALID_PARTICIPATION_SCORE_51));
        AttendanceRecord attendanceRecord = record.toKeyValuePair().getValue();
        AttendanceRecord expectedAttendanceRecord = new AttendanceRecord(attendanceMap);
        assertEquals(expectedAttendanceRecord, attendanceRecord);
    }

    @Test
    public void toKeyValuePair_emptyJsonAdaptedStudentAttendances_returnsAttendanceRecord() throws Exception {
        JsonAdaptedAttendanceRecord record =
                new JsonAdaptedAttendanceRecord(VALID_WEEK_VALUE_5, Collections.emptyList());
        assertEquals(new AttendanceRecord(), record.toKeyValuePair().getValue());
    }

    @Test
    public void toKeyValuePair_nullJsonAdaptedStudentAttendances_returnsNewAttendanceRecord() throws Exception {
        JsonAdaptedAttendanceRecord record =
                new JsonAdaptedAttendanceRecord(VALID_WEEK_VALUE_5, null);
        assertEquals(new AttendanceRecord(), record.toKeyValuePair().getValue());
    }

    @Test
    public void toKeyValuePair_validJsonWeekProperty_returnsWeek() throws Exception {
        JsonAdaptedAttendanceRecord records =
                new JsonAdaptedAttendanceRecord(VALID_WEEK_VALUE_5, Collections.emptyList());
        assertEquals(new Week(VALID_WEEK_INDEX_5), records.toKeyValuePair().getKey());
    }

    @Test
    public void toKeyValuePair_duplicateAttendanceRecord_throwsIllegalValueException() {
        List<JsonAdaptedStudentAttendance> duplicateList =
                Arrays.asList(VALID_JSON_ADAPTED_STUDENT_ATTENDANCE_AMY, VALID_JSON_ADAPTED_STUDENT_ATTENDANCE_AMY);
        JsonAdaptedAttendanceRecord record = new JsonAdaptedAttendanceRecord(VALID_WEEK_VALUE_5, duplicateList);
        assertThrows(IllegalValueException.class,
                JsonAdaptedAttendanceRecord.MESSAGE_DUPLICATE_ATTENDANCE,
                record::toKeyValuePair);
    }

    @Test
    public void toKeyValuePair_invalidWeek_throwsIllegalValueException() {
        JsonAdaptedAttendanceRecord record =
                new JsonAdaptedAttendanceRecord(NumberOfOccurrences.UPPER_BOUND + 1,
                        VALID_JSON_ADAPTED_STUDENT_ATTENDANCES);
        assertThrows(IllegalValueException.class,
                Week.MESSAGE_CONSTRAINTS,
                record::toKeyValuePair);

        JsonAdaptedAttendanceRecord recordWeekZero =
                new JsonAdaptedAttendanceRecord(NumberOfOccurrences.LOWER_BOUND - 1,
                        VALID_JSON_ADAPTED_STUDENT_ATTENDANCES);
        assertThrows(IllegalValueException.class,
                Week.MESSAGE_CONSTRAINTS,
                recordWeekZero::toKeyValuePair);
    }

    @Test
    public void toKeyValuePair_nullInJsonAdaptedStudentAttendances_throwsIllegalValueException() {
        List<JsonAdaptedStudentAttendance> nullContainingList = Collections.singletonList(null);
        JsonAdaptedAttendanceRecord record = new JsonAdaptedAttendanceRecord(VALID_WEEK_VALUE_5, nullContainingList);
        assertThrows(IllegalValueException.class,
                JsonAdaptedAttendanceRecord.MESSAGE_INVALID_ATTENDANCE,
                record::toKeyValuePair);
    }
}
