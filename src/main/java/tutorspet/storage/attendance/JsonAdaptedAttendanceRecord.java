package tutorspet.storage.attendance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javafx.util.Pair;
import tutorspet.commons.core.index.Index;
import tutorspet.commons.exceptions.IllegalValueException;
import tutorspet.model.attendance.Attendance;
import tutorspet.model.attendance.AttendanceRecord;
import tutorspet.model.attendance.Week;

/**
 * Jackson-friendly version of {@link AttendanceRecord}.
 */
public class JsonAdaptedAttendanceRecord {

    public static final String MESSAGE_DUPLICATE_ATTENDANCE = "Attendance record contains duplicate student(s).";
    public static final String MESSAGE_INVALID_ATTENDANCE = "Attendance record contains invalid value(s).";

    private final int week;
    private final List<JsonAdaptedStudentAttendance> attendances = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedAttendanceRecord} with the given details.
     */
    @JsonCreator
    public JsonAdaptedAttendanceRecord(@JsonProperty("week") int week,
                                       @JsonProperty("records") List<JsonAdaptedStudentAttendance> attendances) {
        this.week = week;
        if (attendances != null) {
            this.attendances.addAll(attendances);
        }
    }

    /**
     * Converts a given {@code AttendanceRecord} and its corresponding {@code Week} into this class for Jackson use.
     */
    public JsonAdaptedAttendanceRecord(Week week, AttendanceRecord source) {
        this.week = week.getOneBasedWeekIndex();
        attendances.addAll(source.getAttendanceRecord().entrySet().stream().map(entry ->
                new JsonAdaptedStudentAttendance(entry.getKey(), entry.getValue()))
                .collect(Collectors.toUnmodifiableList()));
    }

    /**
     * Converts this Jackson-friendly adapted AttendanceRecord to a key value pair object.
     *
     * @throws IllegalValueException if there were any data constraints violated in either the adapted week
     *         or attendances.
     */
    public Pair<Week, AttendanceRecord> toKeyValuePair() throws IllegalValueException {
        final Map<UUID, Attendance> attendancesMap = new HashMap<>();

        if (!(week > 0) || !Week.isValidWeek(Index.fromOneBased(week))) {
            throw new IllegalValueException(Week.MESSAGE_CONSTRAINTS);
        }

        if (attendances.stream().anyMatch(Objects::isNull)) {
            throw new IllegalValueException(MESSAGE_INVALID_ATTENDANCE);
        }

        // Using a enhanced for-loop instead of streams since map cannot handle checked exceptions nicely.
        for (JsonAdaptedStudentAttendance attendance : attendances) {
            Pair<UUID, Attendance> pair = attendance.toKeyValuePair();
            if (attendancesMap.containsKey(pair.getKey())) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_ATTENDANCE);
            }
            attendancesMap.put(pair.getKey(), pair.getValue());
        }

        return new Pair<>(new Week(Index.fromOneBased(week)), new AttendanceRecord(attendancesMap));
    }
}
