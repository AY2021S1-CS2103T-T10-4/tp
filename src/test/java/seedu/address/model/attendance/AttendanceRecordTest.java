package seedu.address.model.attendance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ATTENDANCE_33;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ATTENDANCE_80;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalAttendanceRecord.RECORD_ALICE_80;
import static seedu.address.testutil.TypicalAttendanceRecord.RECORD_EMPTY;
import static seedu.address.testutil.TypicalStudent.ALICE;
import static seedu.address.testutil.TypicalStudent.BENSON;

import java.util.HashMap;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import seedu.address.model.attendance.exceptions.AttendanceNotFoundException;
import seedu.address.testutil.AttendanceRecordBuilder;

public class AttendanceRecordTest {

    private final AttendanceRecord attendanceRecord = new AttendanceRecord();

    @Test
    public void constructor_emptyAttendanceRecord() {
        assertTrue(attendanceRecord.getAttendanceRecord().isEmpty());
    }

    @Test
    public void constructor_nonEmptyAttendanceRecord() {
        HashMap<UUID, Attendance> hashMap = new HashMap<>();
        UUID key = ALICE.getUuid();
        Attendance value = new Attendance(VALID_ATTENDANCE_33);
        hashMap.put(key, value);
        AttendanceRecord record = new AttendanceRecord(hashMap);

        assertTrue(record.getAttendanceRecord().containsKey(key));
        assertEquals(value, record.getAttendanceRecord().get(key));
    }

    @Test
    public void getAttendance_nullUuid_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> attendanceRecord.getAttendance(null));
    }

    @Test
    public void getAttendance_emptyAttendanceRecord_throwsAttendanceNotFoundException() {
        assertThrows(AttendanceNotFoundException.class, () -> attendanceRecord.getAttendance(ALICE.getUuid()));
    }

    @Test
    public void getAttendance_nonExistingUuid_throwsAttendanceNotFoundException() {
        assertThrows(AttendanceNotFoundException.class, () -> attendanceRecord.getAttendance(ALICE.getUuid()));
    }

    @Test
    public void equals() {
        // same object -> returns true
        assertTrue(RECORD_ALICE_80.equals(RECORD_ALICE_80));
        assertTrue(RECORD_EMPTY.equals(RECORD_EMPTY));

        // null -> returns false
        assertFalse(RECORD_ALICE_80.equals(null));
        assertFalse(RECORD_EMPTY.equals(null));

        // different types -> returns false
        assertFalse(RECORD_ALICE_80.equals(RECORD_ALICE_80.getAttendanceRecord()));

        // same attendance record -> returns true
        AttendanceRecord attendanceRecord = new AttendanceRecordBuilder()
                .withEntry(ALICE.getUuid(), new Attendance(VALID_ATTENDANCE_80)).build();
        assertTrue(RECORD_ALICE_80.equals(attendanceRecord));

        // different attendance record -> returns false
        AttendanceRecord attendanceRecordWrongUuid = new AttendanceRecordBuilder()
                .withEntry(BENSON.getUuid(), new Attendance(VALID_ATTENDANCE_80)).build();
        assertFalse(RECORD_ALICE_80.equals(attendanceRecordWrongUuid));

        AttendanceRecord attendanceRecordWrongAttendance = new AttendanceRecordBuilder()
                .withEntry(ALICE.getUuid(), new Attendance(VALID_ATTENDANCE_33)).build();
        assertFalse(RECORD_ALICE_80.equals(attendanceRecordWrongAttendance));
    }
}