package tutorspet.storage.attendance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static tutorspet.logic.commands.CommandTestUtil.INVALID_PARTICIPATION_SCORE_101;
import static tutorspet.logic.commands.CommandTestUtil.INVALID_STUDENT_UUID;
import static tutorspet.logic.commands.CommandTestUtil.VALID_ATTENDANCE_33;
import static tutorspet.logic.commands.CommandTestUtil.VALID_ATTENDANCE_51;
import static tutorspet.logic.commands.CommandTestUtil.VALID_UUID_AMY;
import static tutorspet.logic.commands.CommandTestUtil.VALID_UUID_BOB;
import static tutorspet.storage.JsonAdaptedStudent.STUDENT_UUID_FIELD;
import static tutorspet.storage.JsonAdaptedUuidTest.VALID_JSON_ADAPTED_UUID;
import static tutorspet.storage.attendance.JsonAdaptedAttendanceTest.VALID_JSON_ADAPTED_ATTENDANCE_33;
import static tutorspet.testutil.Assert.assertThrows;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import tutorspet.commons.exceptions.IllegalValueException;
import tutorspet.model.attendance.Attendance;
import tutorspet.storage.JsonAdaptedUuid;

public class JsonAdaptedStudentAttendanceTest {

    /** A {@code JsonAdaptedStudentAttendance} for testing with AMY as student and participation score of 80. */
    public static final JsonAdaptedStudentAttendance VALID_JSON_ADAPTED_STUDENT_ATTENDANCE_AMY =
            new JsonAdaptedStudentAttendance(UUID.fromString(VALID_UUID_AMY), VALID_ATTENDANCE_33);
    /** A {@code JsonAdaptedStudentAttendance} for testing with BOB as student and participation score of 51. */
    public static final JsonAdaptedStudentAttendance VALID_JSON_ADAPTED_STUDENT_ATTENDANCE_BOB =
            new JsonAdaptedStudentAttendance(UUID.fromString(VALID_UUID_BOB), VALID_ATTENDANCE_51);

    @Test
    public void toKeyValuePair_validAttendance_returnsAttendance() throws Exception {
        JsonAdaptedStudentAttendance studentAttendance =
                new JsonAdaptedStudentAttendance(UUID.fromString(VALID_UUID_AMY), VALID_ATTENDANCE_33);
        assertEquals(VALID_ATTENDANCE_33, studentAttendance.toKeyValuePair().getValue());
    }

    @Test
    public void toKeyValuePair_validUuid_returnsUuid() throws Exception {
        JsonAdaptedStudentAttendance studentAttendance =
                new JsonAdaptedStudentAttendance(UUID.fromString(VALID_UUID_AMY), VALID_ATTENDANCE_33);
        assertEquals(UUID.fromString(VALID_UUID_AMY), studentAttendance.toKeyValuePair().getKey());
    }

    @Test
    public void toKeyValuePair_validJsonAdaptedAttendance_returnsAttendance() throws Exception {
        JsonAdaptedStudentAttendance studentAttendance =
                new JsonAdaptedStudentAttendance(VALID_JSON_ADAPTED_UUID, VALID_JSON_ADAPTED_ATTENDANCE_33);
        assertEquals(VALID_ATTENDANCE_33, studentAttendance.toKeyValuePair().getValue());
    }

    @Test
    public void toKeyValuePair_validJsonAdaptedUuid_returnsUuid() throws Exception {
        JsonAdaptedStudentAttendance studentAttendance =
                new JsonAdaptedStudentAttendance(VALID_JSON_ADAPTED_UUID, VALID_JSON_ADAPTED_ATTENDANCE_33);
        assertEquals(UUID.fromString(VALID_UUID_AMY), studentAttendance.toKeyValuePair().getKey());
    }

    @Test
    public void toKeyValuePair_invalidJsonAdaptedUuid_throwsIllegalValueException() {
        JsonAdaptedUuid invalidJsonAdaptedUuid = new JsonAdaptedUuid(INVALID_STUDENT_UUID);
        JsonAdaptedStudentAttendance studentAttendance =
                new JsonAdaptedStudentAttendance(invalidJsonAdaptedUuid, VALID_JSON_ADAPTED_ATTENDANCE_33);
        assertThrows(IllegalValueException.class, JsonAdaptedUuid.MESSAGE_INVALID_UUID,
                studentAttendance::toKeyValuePair);
    }

    @Test
    public void toKeyValuePair_invalidJsonAdaptedAttendance_throwsIllegalValueException() {
        JsonAdaptedAttendance invalidJsonAdaptedAttendance = new JsonAdaptedAttendance(INVALID_PARTICIPATION_SCORE_101);
        JsonAdaptedStudentAttendance studentAttendance =
                new JsonAdaptedStudentAttendance(VALID_JSON_ADAPTED_UUID, invalidJsonAdaptedAttendance);
        assertThrows(IllegalValueException.class, Attendance.MESSAGE_CONSTRAINTS, studentAttendance::toKeyValuePair);
    }

    @Test
    public void toKeyValuePair_nullJsonAdaptedStudentUuid_throwsIllegalValueException() {
        JsonAdaptedStudentAttendance studentAttendance =
                new JsonAdaptedStudentAttendance(null, VALID_JSON_ADAPTED_ATTENDANCE_33);
        String expectedMessage = String.format(JsonAdaptedStudentAttendance.MISSING_FIELD_MESSAGE_FORMAT,
                STUDENT_UUID_FIELD);
        assertThrows(IllegalValueException.class, expectedMessage, studentAttendance::toKeyValuePair);
    }

    @Test
    public void toKeyValuePair_nullJsonAdaptedAttendance_throwsIllegalValueException() {
        String expectedMessage = String.format(JsonAdaptedStudentAttendance.MISSING_FIELD_MESSAGE_FORMAT,
                Attendance.class.getSimpleName());
        JsonAdaptedStudentAttendance studentAttendance =
                new JsonAdaptedStudentAttendance(VALID_JSON_ADAPTED_UUID, null);
        assertThrows(IllegalValueException.class, expectedMessage, studentAttendance::toKeyValuePair);
    }
}
