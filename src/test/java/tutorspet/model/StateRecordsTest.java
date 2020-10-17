package tutorspet.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tutorspet.model.VersionedTutorsPetTest.COMMIT_MESSAGE_1;
import static tutorspet.model.VersionedTutorsPetTest.COMMIT_MESSAGE_2;
import static tutorspet.model.VersionedTutorsPetTest.COMMIT_MESSAGE_3;
import static tutorspet.testutil.Assert.assertThrows;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

public class StateRecordsTest {

    @Test
    public void constructor_nullList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new StateRecords(0, null));
    }

    @Test
    public void equals() {
        StateRecords stateRecords = new StateRecords(1,
                Arrays.asList(COMMIT_MESSAGE_1, COMMIT_MESSAGE_2, COMMIT_MESSAGE_3));

        // same records and index -> return true
        StateRecords stateRecordsCopy = new StateRecords(1,
                Arrays.asList(COMMIT_MESSAGE_1, COMMIT_MESSAGE_2, COMMIT_MESSAGE_3));
        assertTrue(stateRecords.equals(stateRecordsCopy));

        // same object -> return true
        assertTrue(stateRecords.equals(stateRecords));

        // null -> return false
        assertFalse(stateRecords.equals(null));

        // different type -> return false
        assertFalse(stateRecords.equals(5));

        // different index -> return false
        stateRecordsCopy = new StateRecords(0,
                Arrays.asList(COMMIT_MESSAGE_1, COMMIT_MESSAGE_2, COMMIT_MESSAGE_3));
        assertFalse(stateRecords.equals(stateRecordsCopy));

        // different records -> return false
        stateRecordsCopy = new StateRecords(1,
                Arrays.asList(COMMIT_MESSAGE_1, COMMIT_MESSAGE_2, COMMIT_MESSAGE_1));
        assertFalse(stateRecords.equals(stateRecordsCopy));
    }
}
