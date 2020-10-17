package tutorspet.model.attendance;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tutorspet.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

class AttendanceTest {

    private static final Attendance ATTENDANCE_ONE = new Attendance(3);

    @Test
    public void constructor_invalidAttendance_throwsIllegalArgumentException() {
        int invalidParticipationScore = 1000;
        assertThrows(IllegalArgumentException.class, () -> new Attendance(invalidParticipationScore));
    }

    @Test
    public void isValidParticipationScore() {
        // invalid score
        assertFalse(Attendance.isValidParticipationScore(Attendance.LOWER_BOUND - 1));
        assertFalse(Attendance.isValidParticipationScore(Attendance.UPPER_BOUND + 1));
        assertFalse(Attendance.isValidParticipationScore(-1));

        // valid score
        assertTrue(Attendance.isValidParticipationScore(Attendance.LOWER_BOUND));
        assertTrue(Attendance.isValidParticipationScore(Attendance.UPPER_BOUND));
        assertTrue(Attendance.isValidParticipationScore(10));
    }

    @Test
    public void equals() {
        // same object -> returns true
        assertTrue(ATTENDANCE_ONE.equals(ATTENDANCE_ONE));

        // null -> returns false
        assertFalse(ATTENDANCE_ONE.equals(null));

        // different types -> returns false
        assertFalse(ATTENDANCE_ONE.equals(ATTENDANCE_ONE.getParticipationScore()));

        // same participationScore -> returns true
        assertTrue(ATTENDANCE_ONE.equals(new Attendance(ATTENDANCE_ONE.getParticipationScore())));

        // different participationScore -> returns false
        assertFalse(ATTENDANCE_ONE.equals(new Attendance(ATTENDANCE_ONE.getParticipationScore() + 1)));
    }
}
