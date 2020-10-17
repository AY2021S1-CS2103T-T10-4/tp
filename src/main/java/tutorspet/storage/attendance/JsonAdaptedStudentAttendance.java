package tutorspet.storage.attendance;

import static tutorspet.storage.JsonAdaptedStudent.STUDENT_UUID_FIELD;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javafx.util.Pair;
import tutorspet.commons.exceptions.IllegalValueException;
import tutorspet.model.attendance.Attendance;
import tutorspet.storage.JsonAdaptedUuid;

/**
 * Jackson-friendly version of a the {@link java.util.UUID} - {@link Attendance} key value pair.
 */
public class JsonAdaptedStudentAttendance {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Student attendance's %s is missing!";

    private final JsonAdaptedUuid studentUuid;
    private final JsonAdaptedAttendance attendance;

    /**
     * Constructs a {@code JsonAdaptedStudentAttendance} with the given student UUID and attendance details.
     */
    @JsonCreator
    public JsonAdaptedStudentAttendance(@JsonProperty("studentUuid") JsonAdaptedUuid studentUuid,
                                        @JsonProperty("attendance") JsonAdaptedAttendance attendance) {
        this.studentUuid = studentUuid;
        this.attendance = attendance;
    }

    /**
     * Converts the given {@code studentUuid} and {@code attendance} into this class for Jackson use.
     */
    public JsonAdaptedStudentAttendance(UUID studentUuid, Attendance attendance) {
        this.studentUuid = new JsonAdaptedUuid(studentUuid);
        this.attendance = new JsonAdaptedAttendance(attendance);
    }

    /**
     * Converts this Jackson-friendly adapted student-attendance information to a key value pair object.
     *
     * @throws IllegalValueException if there were any data constraints violated in either the adapted student UUID
     *         or the adapted attendance.
     */
    public Pair<UUID, Attendance> toKeyValuePair() throws IllegalValueException {
        if (studentUuid == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, STUDENT_UUID_FIELD));
        }
        if (attendance == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Attendance.class.getSimpleName()));
        }
        return new Pair<>(studentUuid.toModelType(), attendance.toModelType());
    }
}
