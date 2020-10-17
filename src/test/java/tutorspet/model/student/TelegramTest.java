package tutorspet.model.student;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tutorspet.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class TelegramTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Telegram(null));
    }

    @Test
    public void constructor_invalidTelegram_throwsIllegalArgumentException() {
        String invalidTelegram = "";
        assertThrows(IllegalArgumentException.class, () -> new Telegram(invalidTelegram));
    }

    @Test
    public void isValidTelegram() {
        // null Telegram handle
        assertThrows(NullPointerException.class, () -> Telegram.isValidTelegram(null));

        // invalid Telegram handles
        assertFalse(Telegram.isValidTelegram("")); // empty string
        assertFalse(Telegram.isValidTelegram("tEle")); // less than 4 characters
        assertFalse(Telegram.isValidTelegram("te1e-gram")); // usage of special character
        assertFalse(Telegram.isValidTelegram("Tele Gram")); // usage of white space

        // valid Telegram handles
        assertTrue(Telegram.isValidTelegram("Tel3g")); // exactly 5 characters
        assertTrue(Telegram.isValidTelegram("gR4MYeLe_pK8")); // special and upper case alphanumeric characters
        assertTrue(Telegram.isValidTelegram("alex_merier3471")); // typical telegram handle
    }
}
