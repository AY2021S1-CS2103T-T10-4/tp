package tutorspet.storage.attendance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javafx.util.Pair;
import tutorspet.commons.core.index.Index;
import tutorspet.commons.exceptions.IllegalValueException;
import tutorspet.model.attendance.AttendanceRecord;
import tutorspet.model.attendance.AttendanceRecordList;
import tutorspet.model.attendance.Week;

/**
 * Jackson-friendly version of {@link AttendanceRecordList}.
 */
public class JsonAdaptedAttendanceRecordList {

    public static final String MESSAGE_DUPLICATE_ATTENDANCE_RECORD = "Attendance list contains duplicate record(s).";
    public static final String MESSAGE_INVALID_RECORD = "Attendance list contains invalid record(s).";
    public static final String MESSAGE_MISSING_ATTENDANCE_RECORD_LIST = "Attendance list data is corrupted.";

    private final List<JsonAdaptedAttendanceRecord> recordList = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedAttendanceRecordList} with the given attendance records.
     */
    @JsonCreator
    public JsonAdaptedAttendanceRecordList(@JsonProperty("recordList") List<JsonAdaptedAttendanceRecord> recordList) {
        if (recordList != null) {
            this.recordList.addAll(recordList);
        }
    }

    /**
     * Converts a given {@code AttendanceRecordList} into this class for Jackson use.
     */
    public JsonAdaptedAttendanceRecordList(AttendanceRecordList source) {
        List<AttendanceRecord> attendanceRecordList = source.getAttendanceRecordList();
        for (int i = 0; i < attendanceRecordList.size(); i++) {
            JsonAdaptedAttendanceRecord record =
                    new JsonAdaptedAttendanceRecord(new Week(Index.fromZeroBased(i)), attendanceRecordList.get(i));
            recordList.add(record);
        }
    }

    /**
     * Converts this Jackson-friendly adapted attendance record list object into the model's
     * {@code AttendanceRecordList} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted attendance records.
     */
    public AttendanceRecordList toModelType() throws IllegalValueException {
        if (recordList.size() == 0) {
            throw new IllegalValueException(MESSAGE_MISSING_ATTENDANCE_RECORD_LIST);
        }

        if (recordList.stream().anyMatch(Objects::isNull)) {
            throw new IllegalValueException(MESSAGE_INVALID_RECORD);
        }

        List<AttendanceRecord> attendanceRecords = new ArrayList<>(Collections.nCopies(recordList.size(), null));
        for (JsonAdaptedAttendanceRecord record : recordList) {
            Pair<Week, AttendanceRecord> pair = record.toKeyValuePair();

            if (pair.getKey().getOneBasedWeekIndex() > attendanceRecords.size()) {
                throw new IllegalValueException(MESSAGE_INVALID_RECORD);
            }
            if (attendanceRecords.get(pair.getKey().getZeroBasedWeekIndex()) != null) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_ATTENDANCE_RECORD);
            }

            attendanceRecords.set(pair.getKey().getZeroBasedWeekIndex(), pair.getValue());
        }

        assert attendanceRecords.stream().noneMatch(Objects::isNull);

        return new AttendanceRecordList(attendanceRecords);
    }
}
