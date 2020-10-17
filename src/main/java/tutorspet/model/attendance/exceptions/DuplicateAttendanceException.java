package tutorspet.model.attendance.exceptions;

public class DuplicateAttendanceException extends RuntimeException {

    public DuplicateAttendanceException() {
        super("Operation would result in duplicate student attendance");
    }
}
