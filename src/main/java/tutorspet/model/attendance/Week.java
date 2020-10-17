package tutorspet.model.attendance;

import static tutorspet.commons.util.AppUtil.checkArgument;

import java.util.Objects;

import tutorspet.commons.core.index.Index;
import tutorspet.model.lesson.NumberOfOccurrences;

/**
 * Represents a week number.
 * Guarantees: immutable; is valid as declared in {@link #isValidWeek(Index)}
 * Note that {@code Index} can only accept non negative integers.
 */
public class Week {

    public static final int LOWER_BOUND = NumberOfOccurrences.LOWER_BOUND;
    public static final int UPPER_BOUND = NumberOfOccurrences.UPPER_BOUND;
    public static final String MESSAGE_CONSTRAINTS =
            String.format("The week number must be a number between %d to %d.", LOWER_BOUND, UPPER_BOUND);
    private final Index week;

    /**
     * Constructor method.
     * Checks that week is valid.
     */
    public Week(Index week) {
        checkArgument(isValidWeek(week), MESSAGE_CONSTRAINTS);

        this.week = week;
    }

    /**
     * Returns true if the given week is an integer between {@link NumberOfOccurrences#LOWER_BOUND}
     * and {@link NumberOfOccurrences#UPPER_BOUND}.
     */
    public static boolean isValidWeek(Index week) {
        int weekIndex = week.getOneBased();
        return LOWER_BOUND <= weekIndex && weekIndex <= UPPER_BOUND;
    }

    public int getZeroBasedWeekIndex() {
        return week.getZeroBased();
    }

    public int getOneBasedWeekIndex() {
        return week.getOneBased();
    }

    @Override
    public String toString() {
        return Integer.toString(getOneBasedWeekIndex());
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Week // instanceof handles nulls
                && ((Week) other).week.equals(week));
    }

    @Override
    public int hashCode() {
        return Objects.hash(week);
    }
}
