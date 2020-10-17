package tutorspet.model.lesson;

import static tutorspet.commons.util.AppUtil.checkArgument;

/**
 * Represents the number of times a lesson occurs.
 * Guarantees: immutable; is valid as declared in {@link #isValidNumberOfOccurrences(int)}
 */
public class NumberOfOccurrences {

    public static final int LOWER_BOUND = 1;
    public static final int UPPER_BOUND = 52;
    public static final String MESSAGE_CONSTRAINTS = String.format("Number of occurrences should be a whole number "
            + "between %d and %d.", LOWER_BOUND, UPPER_BOUND);

    public final int value;

    /**
     * Constructs a {@code NumberOfOccurrences}.
     *
     * @param value A valid number of occurrences.
     */
    public NumberOfOccurrences(int value) {
        checkArgument(isValidNumberOfOccurrences(value), MESSAGE_CONSTRAINTS);

        this.value = value;
    }

    /**
     * Returns true if the given value is between valid range.
     */
    public static boolean isValidNumberOfOccurrences(int value) {
        return value >= LOWER_BOUND && value <= UPPER_BOUND;
    }

    public int getNumberOfOccurrences() {
        return value;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof NumberOfOccurrences // instanceof handles nulls
                && value == ((NumberOfOccurrences) other).value); // state check
    }

    @Override
    public int hashCode() {
        return value;
    }
}
