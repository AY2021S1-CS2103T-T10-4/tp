package tutorspet.storage.attendance;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import tutorspet.commons.exceptions.IllegalValueException;
import tutorspet.model.attendance.Attendance;

/**
 * Jackson-friendly version of {@link Attendance}.
 */
public class JsonAdaptedAttendance {

    private final int participationScore;

    @JsonCreator
    public JsonAdaptedAttendance(int participationScore) {
        this.participationScore = participationScore;
    }

    public JsonAdaptedAttendance(Attendance source) {
        participationScore = source.getParticipationScore();
    }

    @JsonValue
    public int getParticipationScore() {
        return participationScore;
    }

    /**
     * Converts this Jackson-friendly adapted attendance object into the model's {@code Attendance} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted attendance.
     */
    public Attendance toModelType() throws IllegalValueException {
        if (!Attendance.isValidParticipationScore(participationScore)) {
            throw new IllegalValueException(Attendance.MESSAGE_CONSTRAINTS);
        }
        return new Attendance(participationScore);
    }
}
