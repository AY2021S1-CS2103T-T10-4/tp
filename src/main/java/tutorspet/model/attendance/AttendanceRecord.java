package tutorspet.model.attendance;

import static java.util.Objects.requireNonNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import tutorspet.model.attendance.exceptions.AttendanceNotFoundException;

/**
 * Represents the attendance of students in a single lesson occurrence.
 * Guarantees: immutable.
 */
public class AttendanceRecord {

    private final Map<UUID, Attendance> attendances;

    public AttendanceRecord() {
        this.attendances = new HashMap<>();
    }

    /**
     * Overloaded constructor method.
     * Requires {@code attendances} to be non null.
     */
    public AttendanceRecord(Map<UUID, Attendance> attendances) {
        requireNonNull(attendances);

        this.attendances = attendances;
    }

    public Map<UUID, Attendance> getAttendanceRecord() {
        return Collections.unmodifiableMap(attendances);
    }

    /**
     * Returns the {@code Attendance} of the given {@code Student UUID}.
     *
     * @throws AttendanceNotFoundException if there is no attendance for the give {@code Student UUID}.
     */
    public Attendance getAttendance(UUID uuid) throws AttendanceNotFoundException {
        requireNonNull(uuid);

        if (!attendances.containsKey(uuid)) {
            throw new AttendanceNotFoundException();
        }

        return attendances.get(uuid);
    }

    /**
     * Returns true if the {@code AttendanceRecord} contains the given {@code Student UUID}.
     */
    public boolean hasAttendance(UUID uuid) {
        return attendances.containsKey(uuid);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AttendanceRecord // instanceof handles nulls
                && ((AttendanceRecord) other).attendances.equals(attendances));
    }

    @Override
    public int hashCode() {
        return Objects.hash(attendances);
    }
}
