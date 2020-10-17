package tutorspet.testutil;

import static tutorspet.logic.commands.CommandTestUtil.VALID_PARTICIPATION_SCORE_33;
import static tutorspet.logic.commands.CommandTestUtil.VALID_PARTICIPATION_SCORE_51;
import static tutorspet.logic.commands.CommandTestUtil.VALID_PARTICIPATION_SCORE_80;
import static tutorspet.testutil.TypicalStudent.ALICE;
import static tutorspet.testutil.TypicalStudent.BENSON;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tutorspet.model.attendance.Attendance;
import tutorspet.model.attendance.AttendanceRecord;

public class TypicalAttendanceRecord {

    public static final AttendanceRecord RECORD_EMPTY = new AttendanceRecordBuilder().build();

    /**
     * A {@code AttendanceRecord} for testing with a single entry, Alice with a score of 80.
     */
    public static final AttendanceRecord RECORD_ALICE_80 = new AttendanceRecordBuilder()
            .withEntry(ALICE.getUuid(), new Attendance(VALID_PARTICIPATION_SCORE_80)).build();

    /**
     * A {@code AttendanceRecord} for testing with two entries:
     * - Alice with score of 51
     * - Benson with score of 33
     */
    public static final AttendanceRecord RECORD_ALICE_51_BENSON_33 = new AttendanceRecordBuilder()
            .withEntry(ALICE.getUuid(), new Attendance(VALID_PARTICIPATION_SCORE_51))
            .withEntry(BENSON.getUuid(), new Attendance(VALID_PARTICIPATION_SCORE_33)).build();

    private TypicalAttendanceRecord() {} // prevents instantiation

    public static List<AttendanceRecord> getTypicalAttendanceRecord() {
        return new ArrayList<>(Arrays.asList(RECORD_EMPTY, RECORD_ALICE_80, RECORD_ALICE_51_BENSON_33));
    }
}
