package tutorspet.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tutorspet.testutil.Assert.assertThrows;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

public class CommandHistoryTest {

    private static final String CACHE = "Cache";
    private static final String PREVIOUS = "Previous";
    private static final String COMMAND_1 = "Command 1";
    private static final String COMMAND_2 = "Command 2";

    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void constructor_cleanState() {
        CommandHistory commandHistoryTest = new CommandHistory();
        assertFalse(commandHistoryTest.hasPrevious());
        assertFalse(commandHistoryTest.hasNext());
        assertFalse(commandHistoryTest.hasCached());
    }

    @Test
    public void addHistory_null_throwNullPointerException() {
        assertThrows(NullPointerException.class, () -> commandHistory.addHistory(null));
    }

    @Test
    public void addHistory_validString_success() {
        commandHistory.addHistory(PREVIOUS);
        assertTrue(commandHistory.hasPrevious());
        assertEquals(PREVIOUS, commandHistory.getPrevious(CACHE));
    }

    @Test
    public void addHistory_validString_resetsCache() {
        // setup previous history and cache
        commandHistory.addHistory(PREVIOUS);
        commandHistory.getPrevious(CACHE);
        assertEquals(CACHE, commandHistory.getCached());

        commandHistory.addHistory(COMMAND_1);
        assertFalse(commandHistory.hasCached());
    }

    @Test
    public void getPrevious_nullString_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> commandHistory.getPrevious(null));
    }

    @Test
    public void getPrevious_noPrevious_throwsIndexOutOfBoundsException() {
        assertThrows(IndexOutOfBoundsException.class, () -> commandHistory.getPrevious(CACHE));
    }

    @Test
    public void getPrevious_hasPrevious_returnsPreviousAndDecrementsPointer() {
        commandHistory.addHistory(PREVIOUS);
        assertEquals(PREVIOUS, commandHistory.getPrevious(CACHE));
        assertFalse(commandHistory.hasPrevious());
    }

    @Test
    public void getPrevious_outsideStack_storesCache() {
        // setup previous history
        commandHistory.addHistory(PREVIOUS);
        assertFalse(commandHistory.hasNext());

        commandHistory.getPrevious(CACHE);
        assertEquals(CACHE, commandHistory.getCached());
    }

    @Test
    public void getNext_noNext_throwsIndexOutOfBoundsException() {
        assertThrows(IndexOutOfBoundsException.class, () -> commandHistory.getNext());
    }

    @Test
    public void getNext_hasNext_returnsNextAndIncrementsPointer() {
        // setup previous history
        commandHistory.addHistory(COMMAND_1);
        commandHistory.addHistory(COMMAND_2);
        commandHistory.getPrevious(CACHE);
        commandHistory.getPrevious(CACHE);
        assertTrue(commandHistory.hasNext(), "Test precondition error: no next command was found.");

        assertEquals(COMMAND_2, commandHistory.getNext());
        assertFalse(commandHistory.hasNext());
    }

    @Test
    public void getNext_retainsCache() {
        // setup previous history
        commandHistory.addHistory(COMMAND_1);
        commandHistory.addHistory(COMMAND_2);
        commandHistory.getPrevious(CACHE);
        commandHistory.getPrevious(CACHE);

        commandHistory.getNext();
        assertEquals(CACHE, commandHistory.getCached());
    }

    @Test
    public void getCache_noCache_throwsNoSuchElementException() {
        assertThrows(NoSuchElementException.class, () -> commandHistory.getCached());
    }

    @Test
    public void getCache_hasCache_restoresPointerToAboveStack() {
        // setup previous history
        commandHistory.addHistory(COMMAND_1);
        commandHistory.getPrevious(CACHE);

        commandHistory.getCached();
        assertFalse(commandHistory.hasNext());
    }
}
