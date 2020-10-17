package tutorspet.model.lesson;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static tutorspet.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import tutorspet.model.lesson.exceptions.InvalidDayException;

public class DayTest {

    @Test
    public void testCreateDay_validDayString_success() {
        assertTrue(Day.createDay("MONDAY") == Day.MONDAY);
        assertTrue(Day.createDay("TUESDAY") == Day.TUESDAY);
        assertTrue(Day.createDay("WEDNESDAY") == Day.WEDNESDAY);
        assertTrue(Day.createDay("THURSDAY") == Day.THURSDAY);
        assertTrue(Day.createDay("FRIDAY") == Day.FRIDAY);
        assertTrue(Day.createDay("SATURDAY") == Day.SATURDAY);
        assertTrue(Day.createDay("SUNDAY") == Day.SUNDAY);
    }

    @Test
    public void testCreateDay_invalidDayString_throwsInvalidDayException() {
        assertThrows(InvalidDayException.class, () -> Day.createDay("invalid"));
    }
}
