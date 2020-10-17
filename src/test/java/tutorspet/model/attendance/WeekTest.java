package tutorspet.model.attendance;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tutorspet.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import tutorspet.commons.core.index.Index;

class WeekTest {

    private static final Week WEEK_ONE = new Week(Index.fromOneBased(1));

    @Test
    public void constructor_invalidWeek_throwsIllegalArgumentException() {
        Index invalidWeek = Index.fromOneBased(Week.UPPER_BOUND + 1);
        assertThrows(IllegalArgumentException.class, () -> new Week(invalidWeek));
    }

    @Test
    public void isValidWeek() {
        // invalid weeks
        assertFalse(Week.isValidWeek(Index.fromOneBased(Week.UPPER_BOUND + 1)));

        // valid weeks
        assertTrue(Week.isValidWeek(Index.fromOneBased(Week.LOWER_BOUND)));
        assertTrue(Week.isValidWeek(Index.fromOneBased(Week.UPPER_BOUND)));
        assertTrue(Week.isValidWeek(Index.fromOneBased(10)));
    }

    @Test
    public void equals() {
        // same object -> returns true
        assertTrue(WEEK_ONE.equals(WEEK_ONE));

        // null -> returns false
        assertFalse(WEEK_ONE.equals(null));

        // different type -> returns false
        assertFalse(WEEK_ONE.equals(WEEK_ONE.getZeroBasedWeekIndex()));

        // same week -> returns true
        assertTrue(WEEK_ONE.equals(new Week(
                Index.fromZeroBased(WEEK_ONE.getZeroBasedWeekIndex()))));

        // different week -> returns false
        assertFalse(WEEK_ONE.equals(new Week(
                Index.fromZeroBased(WEEK_ONE.getZeroBasedWeekIndex() + 1))));
    }
}
