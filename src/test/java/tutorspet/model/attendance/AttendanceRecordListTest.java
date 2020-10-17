package tutorspet.model.attendance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tutorspet.logic.commands.CommandTestUtil.VALID_NUMBER_OF_OCCURRENCES_7_LESSON_WED_2_TO_4;
import static tutorspet.logic.commands.CommandTestUtil.VALID_PARTICIPATION_SCORE_80;
import static tutorspet.testutil.Assert.assertThrows;
import static tutorspet.testutil.TypicalAttendanceRecord.RECORD_ALICE_51_BENSON_33;
import static tutorspet.testutil.TypicalAttendanceRecord.RECORD_ALICE_80;
import static tutorspet.testutil.TypicalAttendanceRecord.RECORD_EMPTY;
import static tutorspet.testutil.TypicalAttendanceRecord.getTypicalAttendanceRecord;
import static tutorspet.testutil.TypicalStudent.ALICE;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import tutorspet.commons.core.index.Index;
import tutorspet.model.attendance.exceptions.AttendanceNotFoundException;
import tutorspet.model.attendance.exceptions.InvalidWeekException;
import tutorspet.model.lesson.NumberOfOccurrences;

public class AttendanceRecordListTest {

    private static final AttendanceRecordList recordList =
            new AttendanceRecordList(new NumberOfOccurrences(VALID_NUMBER_OF_OCCURRENCES_7_LESSON_WED_2_TO_4));

    private static final Week VALID_WEEK = new Week(Index.fromOneBased(1));
    private static final Week INVALID_WEEK =
            new Week(Index.fromOneBased(VALID_NUMBER_OF_OCCURRENCES_7_LESSON_WED_2_TO_4 + 1));

    @Test
    public void constructor_emptyAttendanceRecordMap() {
        assertTrue(recordList.getAttendanceRecordList().size() == 7);
        for (AttendanceRecord record: recordList.getAttendanceRecordList()) {
            assertTrue(record.equals(RECORD_EMPTY));
        }
    }

    @Test
    public void getAttendance_nullStudent_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> recordList.getAttendance(null, VALID_WEEK));
    }

    @Test
    public void getAttendance_invalidWeek_throwsInvalidWeekException() {
        assertThrows(InvalidWeekException.class, () -> recordList.getAttendance(
                ALICE, INVALID_WEEK));
    }

    @Test
    public void getAttendance_nonExistingStudent_throwsAttendanceNotFoundException() {
        assertThrows(AttendanceNotFoundException.class, () -> recordList.getAttendance(ALICE, VALID_WEEK));
    }

    @Test
    public void getAttendance_existingStudent_success() {
        AttendanceRecordList attendanceRecordList = createAliceRecordList();
        Attendance attendance = attendanceRecordList.getAttendance(ALICE, VALID_WEEK);
        assertEquals(new Attendance(VALID_PARTICIPATION_SCORE_80), attendance);
    }

    @Test
    public void getAttendanceRecord_invalidWeek_throwsInvalidWeekException() {
        assertThrows(InvalidWeekException.class, () -> recordList.getAttendanceRecord(INVALID_WEEK));
    }

    @Test
    public void getAttendanceRecord_validWeek_success() {
        AttendanceRecordList attendanceRecordList = createAliceRecordList();
        AttendanceRecord attendanceRecord = attendanceRecordList.getAttendanceRecord(VALID_WEEK);
        assertEquals(RECORD_ALICE_80, attendanceRecord);
    }

    @Test
    public void hasAttendance_nullStudent_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> recordList.hasAttendance(null, VALID_WEEK));
    }

    @Test
    public void hasAttendance_nullWeek_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> recordList.hasAttendance(ALICE, null));
    }

    @Test
    public void hasAttendance_invalidWeek_throwsInvalidWeekException() {
        assertThrows(InvalidWeekException.class, () -> recordList.hasAttendance(ALICE, INVALID_WEEK));
    }

    @Test
    public void hasAttendance_existingStudent_returnsTrue() {
        AttendanceRecordList attendanceRecordList = createAliceRecordList();
        assertTrue(attendanceRecordList.hasAttendance(ALICE, VALID_WEEK));
    }

    @Test
    public void hasAttendance_nonExistingStudent_returnsFalse() {
        assertFalse(recordList.hasAttendance(ALICE, VALID_WEEK));
    }

    @Test
    public void equals() {
        AttendanceRecordList attendanceRecordList = new AttendanceRecordList(getTypicalAttendanceRecord());

        // same object -> returns true
        assertTrue(attendanceRecordList.equals(attendanceRecordList));
        assertTrue(recordList.equals(recordList));

        // null -> returns false
        assertFalse(attendanceRecordList.equals(null));
        assertFalse(recordList.equals(null));

        // different types -> return false
        assertFalse(attendanceRecordList.equals(attendanceRecordList.getAttendanceRecordList()));

        // same size, same attendance records -> return true
        List<AttendanceRecord> list = new ArrayList<>();
        list.add(RECORD_EMPTY);
        list.add(RECORD_ALICE_80);
        list.add(RECORD_ALICE_51_BENSON_33);
        AttendanceRecordList sameSizeAndRecord = new AttendanceRecordList(list);
        assertTrue(attendanceRecordList.equals(sameSizeAndRecord));

        // different size, same attendance records -> return false
        list.add(RECORD_EMPTY);
        AttendanceRecordList differentSizeSameRecord = new AttendanceRecordList(list);
        assertFalse(attendanceRecordList.equals(differentSizeSameRecord));

        // same size, different attendance records -> return false
        list.remove(list.size() - 1);
        list.remove(list.size() - 1);
        list.add(RECORD_EMPTY);
        AttendanceRecordList sameSizeDifferentRecord = new AttendanceRecordList(list);
        assertFalse(attendanceRecordList.equals(sameSizeDifferentRecord));

        //different size, different attendance records -> return false
        list.add(RECORD_ALICE_51_BENSON_33);
        AttendanceRecordList differentAttendanceRecordList = new AttendanceRecordList(list);
        assertFalse(attendanceRecordList.equals(differentAttendanceRecordList));
    }

    /**
     * Creates an {@code AttendanceRecordList} with {@code RECORD_ALICE_80} at the first, valid week.
     * The rest of the 6 elements are empty {@code AttendanceRecords}.
     */
    private AttendanceRecordList createAliceRecordList() {
        List<AttendanceRecord> list = new ArrayList<>();
        list.add(RECORD_ALICE_80);
        for (int i = 0; i < 6; i++) {
            list.add(RECORD_EMPTY);
        }
        return new AttendanceRecordList(list);
    }
}
