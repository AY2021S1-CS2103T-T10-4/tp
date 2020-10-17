package tutorspet.testutil;

import java.util.HashMap;
import java.util.UUID;

import tutorspet.model.attendance.Attendance;
import tutorspet.model.attendance.AttendanceRecord;

/**
 * A utility class to help with building AttendanceRecord objects.
 */
public class AttendanceRecordBuilder {

    private HashMap<UUID, Attendance> record;

    /**
     * Creates a {@code AttendanceRecordBuilder} with no entries.
     */
    public AttendanceRecordBuilder() {
        record = new HashMap<>();
    }

    /**
     * Initializes the {@code AttendanceRecordBuilder} with the data of {@code recordToCopy}.
     */
    public AttendanceRecordBuilder(AttendanceRecord recordToCopy) {
        record = new HashMap<>(recordToCopy.getAttendanceRecord());
    }

    /**
     * Inserts a new {@code UUID} and {@code Attendance} into the {@code AttendanceRecord} we are building.
     */
    public AttendanceRecordBuilder withEntry(UUID uuid, Attendance attendance) {
        record.put(uuid, attendance);
        return this;
    }

    public AttendanceRecord build() {
        return new AttendanceRecord(record);
    }
}
