package tutorspet.storage.attendance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static tutorspet.logic.commands.CommandTestUtil.VALID_ATTENDANCE_RECORD;
import static tutorspet.logic.commands.CommandTestUtil.VALID_ATTENDANCE_RECORD_LIST;
import static tutorspet.logic.commands.CommandTestUtil.VALID_WEEK_VALUE_5;
import static tutorspet.storage.attendance.JsonAdaptedAttendanceRecordTest.EMPTY_JSON_ADAPTED_ATTENDANCE_RECORD_WEEK_ONE;
import static tutorspet.storage.attendance.JsonAdaptedAttendanceRecordTest.VALID_JSON_ADAPTED_ATTENDANCE_RECORD_WEEK_TWO;
import static tutorspet.storage.attendance.JsonAdaptedStudentAttendanceTest.VALID_JSON_ADAPTED_STUDENT_ATTENDANCE_AMY;
import static tutorspet.testutil.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import tutorspet.commons.core.index.Index;
import tutorspet.commons.exceptions.IllegalValueException;
import tutorspet.model.attendance.Week;

public class JsonAdaptedAttendanceRecordListTest {

    /**
     * A {@code JsonAdaptedAttendanceRecordList} for testing with contents as specified in
     * {@link tutorspet.logic.commands.CommandTestUtil#VALID_ATTENDANCE_RECORD_LIST}.
     */
    public static final JsonAdaptedAttendanceRecordList VALID_JSON_ADAPTED_ATTENDANCE_RECORD_LIST =
            new JsonAdaptedAttendanceRecordList(VALID_ATTENDANCE_RECORD_LIST);

    @Test
    public void toModelType_validAttendanceRecordList_returnsAttendanceRecordList() throws Exception {
        JsonAdaptedAttendanceRecordList attendanceRecordList =
                new JsonAdaptedAttendanceRecordList(VALID_ATTENDANCE_RECORD_LIST);
        assertEquals(VALID_ATTENDANCE_RECORD_LIST, attendanceRecordList.toModelType());
    }

    @Test
    public void toModelType_validJsonAdaptedAttendanceRecords_returnsAttendanceRecordList() throws Exception {
        List<JsonAdaptedAttendanceRecord> records = Arrays.asList(EMPTY_JSON_ADAPTED_ATTENDANCE_RECORD_WEEK_ONE,
                VALID_JSON_ADAPTED_ATTENDANCE_RECORD_WEEK_TWO);
        JsonAdaptedAttendanceRecordList attendanceRecordList =
                new JsonAdaptedAttendanceRecordList(records);
        assertEquals(VALID_ATTENDANCE_RECORD_LIST, attendanceRecordList.toModelType());
    }

    @Test
    public void toModelType_outOfOrderJsonAdaptedAttendanceRecords_returnsAttendanceRecordList() throws Exception {
        List<JsonAdaptedAttendanceRecord> records = Arrays.asList(VALID_JSON_ADAPTED_ATTENDANCE_RECORD_WEEK_TWO,
                EMPTY_JSON_ADAPTED_ATTENDANCE_RECORD_WEEK_ONE);
        JsonAdaptedAttendanceRecordList attendanceRecordList =
                new JsonAdaptedAttendanceRecordList(records);
        assertEquals(VALID_ATTENDANCE_RECORD_LIST, attendanceRecordList.toModelType());
    }

    @Test
    public void toModelType_emptyJsonAdaptedAttendanceRecords_throwsIllegalValueException() {
        List<JsonAdaptedAttendanceRecord> emptyRecords = new ArrayList<>();
        JsonAdaptedAttendanceRecordList attendanceRecordList = new JsonAdaptedAttendanceRecordList(emptyRecords);
        assertThrows(IllegalValueException.class,
                JsonAdaptedAttendanceRecordList.MESSAGE_MISSING_ATTENDANCE_RECORD_LIST,
                attendanceRecordList::toModelType);
    }

    @Test
    public void toModelType_nullJsonAdaptedAttendanceRecords_throwsIllegalValueException() {
        List<JsonAdaptedAttendanceRecord> nullRecords = null;
        JsonAdaptedAttendanceRecordList attendanceRecordList = new JsonAdaptedAttendanceRecordList(nullRecords);
        assertThrows(IllegalValueException.class,
                JsonAdaptedAttendanceRecordList.MESSAGE_MISSING_ATTENDANCE_RECORD_LIST,
                attendanceRecordList::toModelType);
    }

    @Test
    public void toModelType_invalidJsonAdaptedAttendanceRecordInList_throwsIllegalValueException() {
        List<JsonAdaptedStudentAttendance> studentAttendances =
                Arrays.asList(VALID_JSON_ADAPTED_STUDENT_ATTENDANCE_AMY, VALID_JSON_ADAPTED_STUDENT_ATTENDANCE_AMY);
        List<JsonAdaptedAttendanceRecord> invalidAttendanceRecord =
                Collections.singletonList(new JsonAdaptedAttendanceRecord(VALID_WEEK_VALUE_5, studentAttendances));
        JsonAdaptedAttendanceRecordList attendanceRecordList =
                new JsonAdaptedAttendanceRecordList(invalidAttendanceRecord);
        assertThrows(IllegalValueException.class,
                JsonAdaptedAttendanceRecord.MESSAGE_DUPLICATE_ATTENDANCE,
                attendanceRecordList::toModelType);
    }

    @Test
    public void toModelType_nonconsecutiveWeeksInJsonAdaptedAttendanceRecords_throwsIllegalValueException() {
        JsonAdaptedAttendanceRecord recordWeekThree =
                new JsonAdaptedAttendanceRecord(new Week(Index.fromOneBased(3)), VALID_ATTENDANCE_RECORD);
        List<JsonAdaptedAttendanceRecord> records =
                Arrays.asList(VALID_JSON_ADAPTED_ATTENDANCE_RECORD_WEEK_TWO, recordWeekThree);
        JsonAdaptedAttendanceRecordList attendanceRecordList =
                new JsonAdaptedAttendanceRecordList(records);
        assertThrows(IllegalValueException.class,
                JsonAdaptedAttendanceRecordList.MESSAGE_INVALID_RECORD,
                attendanceRecordList::toModelType);
    }

    @Test
    public void toModelType_nullInJsonAdaptedAttendanceRecords_throwsIllegalValueException() {
        List<JsonAdaptedAttendanceRecord> nullContainingList = Collections.singletonList(null);
        JsonAdaptedAttendanceRecordList attendanceRecordList =
                new JsonAdaptedAttendanceRecordList(nullContainingList);
        assertThrows(IllegalValueException.class,
                JsonAdaptedAttendanceRecordList.MESSAGE_INVALID_RECORD,
                attendanceRecordList::toModelType);
    }

    @Test
    public void toModelType_duplicateWeekInJsonAdaptedAttendanceRecords_throwsIllegalValueException() {
        JsonAdaptedAttendanceRecord recordWeekOne =
                new JsonAdaptedAttendanceRecord(new Week(Index.fromOneBased(1)), VALID_ATTENDANCE_RECORD);
        List<JsonAdaptedAttendanceRecord> records = Arrays.asList(EMPTY_JSON_ADAPTED_ATTENDANCE_RECORD_WEEK_ONE,
                recordWeekOne);
        JsonAdaptedAttendanceRecordList attendanceRecordList =
                new JsonAdaptedAttendanceRecordList(records);
        assertThrows(IllegalValueException.class,
                JsonAdaptedAttendanceRecordList.MESSAGE_DUPLICATE_ATTENDANCE_RECORD,
                attendanceRecordList::toModelType);
    }
}
