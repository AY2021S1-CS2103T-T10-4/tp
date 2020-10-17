package tutorspet.model.lesson;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tutorspet.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class NumberOfOccurrencesTest {

    @Test
    public void constructor_invalidNumberOfOccurrences_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                new NumberOfOccurrences(NumberOfOccurrences.LOWER_BOUND - 1));
        assertThrows(IllegalArgumentException.class, () ->
                new NumberOfOccurrences(NumberOfOccurrences.UPPER_BOUND + 1));
    }

    @Test
    public void isValidNumberOfOccurrences() {
        // invalid number of occurrences
        assertFalse(NumberOfOccurrences.isValidNumberOfOccurrences(-1));
        assertFalse(NumberOfOccurrences.isValidNumberOfOccurrences(100));
        assertFalse(NumberOfOccurrences.isValidNumberOfOccurrences(NumberOfOccurrences.LOWER_BOUND - 1));
        assertFalse(NumberOfOccurrences.isValidNumberOfOccurrences(NumberOfOccurrences.UPPER_BOUND + 1));

        // valid number of occurrences
        assertTrue(NumberOfOccurrences.isValidNumberOfOccurrences(NumberOfOccurrences.LOWER_BOUND));
        assertTrue(NumberOfOccurrences.isValidNumberOfOccurrences(14));
        assertTrue(NumberOfOccurrences.isValidNumberOfOccurrences(NumberOfOccurrences.UPPER_BOUND));
    }

    @Test
    public void equals() {
        NumberOfOccurrences num1 = new NumberOfOccurrences(1);
        NumberOfOccurrences num2 = new NumberOfOccurrences(1);
        NumberOfOccurrences num3 = new NumberOfOccurrences(10);

        // same object -> returns true
        assertTrue(num1.equals(num1));

        // same values -> returns true
        assertTrue(num1.equals(num2));

        // null -> returns false
        assertFalse(num1.equals(null));

        // different type -> returns false
        assertFalse(num1.equals(3));

        // different venue -> returns false
        assertFalse(num1.equals(num3));
    }
}
