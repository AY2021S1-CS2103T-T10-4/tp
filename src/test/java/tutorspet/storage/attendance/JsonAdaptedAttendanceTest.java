package tutorspet.storage.attendance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static tutorspet.logic.commands.CommandTestUtil.INVALID_PARTICIPATION_SCORE_101;
import static tutorspet.logic.commands.CommandTestUtil.INVALID_PARTICIPATION_SCORE_MINUS_1;
import static tutorspet.logic.commands.CommandTestUtil.VALID_ATTENDANCE_33;
import static tutorspet.logic.commands.CommandTestUtil.VALID_PARTICIPATION_SCORE_33;
import static tutorspet.logic.commands.CommandTestUtil.VALID_PARTICIPATION_SCORE_80;
import static tutorspet.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import tutorspet.commons.exceptions.IllegalValueException;
import tutorspet.model.attendance.Attendance;

public class JsonAdaptedAttendanceTest {

    /** A {@code JsonAdaptedAttendance} for testing with participation score of 33. */
    public static final JsonAdaptedAttendance VALID_JSON_ADAPTED_ATTENDANCE_33 =
            new JsonAdaptedAttendance(VALID_PARTICIPATION_SCORE_33);
    /** A {@code JsonAdaptedAttendance} for testing with participation score of 80. */
    public static final JsonAdaptedAttendance VALID_JSON_ADAPTED_ATTENDANCE_80 =
            new JsonAdaptedAttendance(VALID_PARTICIPATION_SCORE_80);

    @Test
    public void getParticipationScore_returnsParticipationScore() {
        JsonAdaptedAttendance attendance = new JsonAdaptedAttendance(VALID_PARTICIPATION_SCORE_80);
        assertEquals(VALID_PARTICIPATION_SCORE_80, attendance.getParticipationScore());
    }

    @Test
    public void toModelType_validAttendance_returnsAttendance() throws Exception {
        JsonAdaptedAttendance attendance = new JsonAdaptedAttendance(VALID_ATTENDANCE_33);
        assertEquals(VALID_ATTENDANCE_33, attendance.toModelType());
    }

    @Test
    public void toModelType_validParticipationScore_returnsAttendance() throws Exception {
        JsonAdaptedAttendance attendance = new JsonAdaptedAttendance(VALID_PARTICIPATION_SCORE_80);

        Attendance expectedAttendance = new Attendance(VALID_PARTICIPATION_SCORE_80);
        assertEquals(expectedAttendance, attendance.toModelType());
    }

    @Test
    public void toModelType_invalidParticipationScore_throwsIllegalValueException() {
        String expectedMessage = Attendance.MESSAGE_CONSTRAINTS;
        JsonAdaptedAttendance attendanceNegative = new JsonAdaptedAttendance(INVALID_PARTICIPATION_SCORE_MINUS_1);
        assertThrows(IllegalValueException.class, expectedMessage, attendanceNegative::toModelType);

        JsonAdaptedAttendance attendanceExceed = new JsonAdaptedAttendance(INVALID_PARTICIPATION_SCORE_101);
        assertThrows(IllegalValueException.class, expectedMessage, attendanceExceed::toModelType);
    }
}
