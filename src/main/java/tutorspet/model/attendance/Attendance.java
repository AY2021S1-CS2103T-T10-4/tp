package tutorspet.model.attendance;

import static tutorspet.commons.util.AppUtil.checkArgument;

import java.util.Objects;

/**
 * Represents a student's attendance for a particular lesson.
 * Guarantees: immutable; is valid as declared in {@link #isValidParticipationScore(int)}
 */
public class Attendance {

    public static final int LOWER_BOUND = 0;
    public static final int UPPER_BOUND = 100;
    public static final String MESSAGE_CONSTRAINTS =
            String.format("The participation score must be a number between %d to %d.", LOWER_BOUND, UPPER_BOUND);
    private final int participationScore;

    /**
     * Constructor method.
     * Participation score is checked to be valid.
     */
    public Attendance(int participationScore) {
        checkArgument(isValidParticipationScore(participationScore), MESSAGE_CONSTRAINTS);

        this.participationScore = participationScore;
    }

    /**
     * Returns true if the given score is an integer between 0 and 100.
     */
    public static boolean isValidParticipationScore(int participationScore) {
        return LOWER_BOUND <= participationScore
                && participationScore <= UPPER_BOUND;
    }

    public int getParticipationScore() {
        return participationScore;
    }

    @Override
    public String toString() {
        return Integer.toString(participationScore);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Attendance // instanceof handles nulls
                && ((Attendance) other).participationScore == participationScore);
    }

    @Override
    public int hashCode() {
        return Objects.hash(participationScore);
    }
}
