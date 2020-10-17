package tutorspet.model.attendance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tutorspet.logic.commands.CommandTestUtil.VALID_PARTICIPATION_SCORE_33;
import static tutorspet.logic.commands.CommandTestUtil.VALID_PARTICIPATION_SCORE_80;
import static tutorspet.testutil.Assert.assertThrows;
import static tutorspet.testutil.TypicalAttendanceRecord.RECORD_ALICE_80;
import static tutorspet.testutil.TypicalAttendanceRecord.RECORD_EMPTY;
import static tutorspet.testutil.TypicalStudent.ALICE;
import static tutorspet.testutil.TypicalStudent.BENSON;

import java.util.HashMap;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import tutorspet.model.attendance.exceptions.AttendanceNotFoundException;
import tutorspet.testutil.AttendanceRecordBuilder;

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
        Attendance value = new Attendance(VALID_PARTICIPATION_SCORE_33);
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
    public void hasAttendance_attendanceInAttendanceRecord_returnsTrue() {
        HashMap<UUID, Attendance> records = new HashMap<>();
        UUID studentUuid = ALICE.getUuid();
        Attendance attendance = new Attendance(VALID_PARTICIPATION_SCORE_33);
        records.put(studentUuid, attendance);
        AttendanceRecord attendanceRecord = new AttendanceRecord(records);
        assertTrue(attendanceRecord.hasAttendance(ALICE.getUuid()));
    }

    @Test
    public void hasAttendance_attendanceNotInAttendanceRecord_returnsFalse() {
        HashMap<UUID, Attendance> records = new HashMap<>();
        UUID studentUuid = ALICE.getUuid();
        Attendance attendance = new Attendance(VALID_PARTICIPATION_SCORE_33);
        records.put(studentUuid, attendance);
        AttendanceRecord attendanceRecord = new AttendanceRecord(records);
        assertFalse(attendanceRecord.hasAttendance(BENSON.getUuid()));
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
                .withEntry(ALICE.getUuid(), new Attendance(VALID_PARTICIPATION_SCORE_80)).build();
        assertTrue(RECORD_ALICE_80.equals(attendanceRecord));

        // different attendance record -> returns false
        AttendanceRecord attendanceRecordWrongUuid = new AttendanceRecordBuilder()
                .withEntry(BENSON.getUuid(), new Attendance(VALID_PARTICIPATION_SCORE_80)).build();
        assertFalse(RECORD_ALICE_80.equals(attendanceRecordWrongUuid));

        AttendanceRecord attendanceRecordWrongAttendance = new AttendanceRecordBuilder()
                .withEntry(ALICE.getUuid(), new Attendance(VALID_PARTICIPATION_SCORE_33)).build();
        assertFalse(RECORD_ALICE_80.equals(attendanceRecordWrongAttendance));
    }
}
