package tutorspet.model.lesson;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tutorspet.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class VenueTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Venue(null));
    }

    @Test
    public void constructor_invalidVenue_throwsIllegalArgumentException() {
        String invalidVenue = "";
        assertThrows(IllegalArgumentException.class, () -> new Venue(invalidVenue));
    }

    @Test
    public void isValidVenue() {
        // null venue
        assertThrows(NullPointerException.class, () -> Venue.isValidVenue(null));

        // blank venue
        assertFalse(Venue.isValidVenue("")); // empty string
        assertFalse(Venue.isValidVenue(" ")); // spaces only

        // invalid venues
        assertFalse(Venue.isValidVenue("COM1^")); // special characters
        assertFalse(Venue.isValidVenue(" COM1")); // leading space

        // valid venues
        // mixture of alphanumeric and special characters
        assertTrue(Venue.isValidVenue("https://zoom/j/95317249?)pwd=Ulld2tWY3MwMkRibjQyUkdZZz09"));
        assertTrue(Venue.isValidVenue("Com2")); // alphabets only
        assertTrue(Venue.isValidVenue("0209")); // numbers only
        assertTrue(Venue.isValidVenue("COM2 0101")); // alphanumeric with spaces
    }

    @Test
    public void equals() {
        Venue venue1 = new Venue("com1");
        Venue venue2 = new Venue("com1");
        Venue venue3 = new Venue("com2");

        // same object -> returns true
        assertTrue(venue1.equals(venue1));

        // same values -> returns true
        assertTrue(venue1.equals(venue2));

        // null -> returns false
        assertFalse(venue1.equals(null));

        // different type -> returns false
        assertFalse(venue1.equals(3));

        // different venue -> returns false
        assertFalse(venue1.equals(venue3));
    }
}
