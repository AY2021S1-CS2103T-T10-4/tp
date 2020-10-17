package tutorspet.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tutorspet.model.VersionedTutorsPet.INITIAL_COMMIT_MESSAGE;
import static tutorspet.testutil.Assert.assertThrows;
import static tutorspet.testutil.TypicalStudent.AMY;
import static tutorspet.testutil.TypicalTutorsPet.getTypicalTutorsPet;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import tutorspet.model.exception.RedoStateException;
import tutorspet.model.exception.UndoStateException;

public class VersionedTutorsPetTest {

    public static final String COMMIT_MESSAGE_1 = "Commit Message 1";
    public static final String COMMIT_MESSAGE_2 = "Commit Message 2";
    public static final String COMMIT_MESSAGE_3 = "Commit Message 3";

    private VersionedTutorsPet versionedTutorsPet = new VersionedTutorsPet(getTypicalTutorsPet());

    @Test
    public void commit_nullCommitMessage_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> versionedTutorsPet.commit(null));
    }

    @Test
    public void commit_removesFutureStates() {
        versionedTutorsPet.commit(COMMIT_MESSAGE_1);
        versionedTutorsPet.undo();
        assertTrue(versionedTutorsPet.canRedo());
        versionedTutorsPet.commit(COMMIT_MESSAGE_2);
        assertFalse(versionedTutorsPet.canRedo());
    }

    @Test
    public void canUndo_hasPreviousState_returnsTrue() {
        versionedTutorsPet.commit(COMMIT_MESSAGE_1);
        assertTrue(versionedTutorsPet.canUndo());
    }

    @Test
    public void canUndo_newVersionedTutorsPet_returnsFalse() {
        assertFalse(versionedTutorsPet.canUndo());
    }

    @Test
    public void undo_hasPreviousState_returnsCommitMessage() {
        versionedTutorsPet.commit(COMMIT_MESSAGE_1);
        assertEquals(COMMIT_MESSAGE_1, versionedTutorsPet.undo());
    }

    @Test
    public void undo_preservesFutureState() {
        versionedTutorsPet.commit(COMMIT_MESSAGE_1);
        versionedTutorsPet.undo();
        assertTrue(versionedTutorsPet.canRedo());
    }

    @Test
    public void undo_noPreviousState_throwsUndoStateException() {
        assertThrows(UndoStateException.class, () -> versionedTutorsPet.undo());
    }

    @Test
    public void canRedo_hasNextState_returnsTrue() {
        versionedTutorsPet.commit(COMMIT_MESSAGE_1);
        versionedTutorsPet.undo();
        assertTrue(versionedTutorsPet.canRedo());
    }

    @Test
    public void canRedo_noNextState_returnsFalse() {
        assertFalse(versionedTutorsPet.canRedo());
    }

    @Test
    public void redo_hasNextState_returnsCommitMessage() {
        versionedTutorsPet.commit(COMMIT_MESSAGE_1);
        versionedTutorsPet.undo();
        assertEquals(COMMIT_MESSAGE_1, versionedTutorsPet.redo());
    }

    @Test
    public void redo_preservesRedoneState() {
        versionedTutorsPet.commit(COMMIT_MESSAGE_1);
        versionedTutorsPet.commit(COMMIT_MESSAGE_2);
        assertEquals(COMMIT_MESSAGE_2, versionedTutorsPet.undo());
        assertEquals(COMMIT_MESSAGE_2, versionedTutorsPet.redo());
        assertTrue(versionedTutorsPet.canUndo());
        assertEquals(COMMIT_MESSAGE_2, versionedTutorsPet.undo());
    }

    @Test
    public void redo_noNextState_throwsRedoStateException() {
        assertThrows(RedoStateException.class, () -> versionedTutorsPet.redo());
    }

    @Test
    public void viewStateRecords_initialState() {
        assertEquals(new StateRecords(0, Collections.singletonList(INITIAL_COMMIT_MESSAGE)),
                versionedTutorsPet.viewStateRecords());
    }

    @Test
    public void viewStateRecords_latestState() {
        versionedTutorsPet.commit(COMMIT_MESSAGE_1);
        assertEquals(new StateRecords(1, Arrays.asList(INITIAL_COMMIT_MESSAGE, COMMIT_MESSAGE_1)),
                versionedTutorsPet.viewStateRecords());
    }

    @Test
    public void viewStateRecords_earliestState() {
        versionedTutorsPet.commit(COMMIT_MESSAGE_1);
        versionedTutorsPet.undo();
        assertEquals(new StateRecords(0, Arrays.asList(INITIAL_COMMIT_MESSAGE, COMMIT_MESSAGE_1)),
                versionedTutorsPet.viewStateRecords());
    }

    @Test
    public void viewStateRecords_intermediateState() {
        versionedTutorsPet.commit(COMMIT_MESSAGE_1);
        versionedTutorsPet.commit(COMMIT_MESSAGE_2);
        versionedTutorsPet.undo();
        assertEquals(new StateRecords(1, Arrays.asList(INITIAL_COMMIT_MESSAGE, COMMIT_MESSAGE_1, COMMIT_MESSAGE_2)),
                versionedTutorsPet.viewStateRecords());
    }

    @Test
    public void equals() {
        // same state -> returns true
        VersionedTutorsPet versionedTutorsPetCopy = new VersionedTutorsPet(getTypicalTutorsPet());
        versionedTutorsPet.commit(COMMIT_MESSAGE_1);
        versionedTutorsPet.undo();
        versionedTutorsPetCopy.commit(COMMIT_MESSAGE_1);
        versionedTutorsPetCopy.undo();
        assertTrue(versionedTutorsPet.equals(versionedTutorsPetCopy));

        // same object -> returns true
        assertTrue(versionedTutorsPet.equals(versionedTutorsPet));

        // null -> returns false
        assertFalse(versionedTutorsPet.equals(null));

        // different type -> returns false
        assertFalse(versionedTutorsPet.equals(5));

        // different state pointer -> returns false
        versionedTutorsPetCopy.redo();
        assertFalse(versionedTutorsPet.equals(versionedTutorsPetCopy));

        // different state history -> returns false
        versionedTutorsPet = new VersionedTutorsPet(getTypicalTutorsPet());
        VersionedTutorsPet versionedTutorsPetDifferentState = new VersionedTutorsPet(getTypicalTutorsPet());
        versionedTutorsPet.commit(COMMIT_MESSAGE_1);
        versionedTutorsPetDifferentState.commit(COMMIT_MESSAGE_2);
        assertFalse(versionedTutorsPet.equals(versionedTutorsPetDifferentState));

        // different current state -> returns false
        versionedTutorsPet = new VersionedTutorsPet(getTypicalTutorsPet());
        VersionedTutorsPet versionedTutorsPetDifferentCurrentState = new VersionedTutorsPet(getTypicalTutorsPet());
        versionedTutorsPetDifferentCurrentState.addStudent(AMY);
        assertFalse(versionedTutorsPet.equals(versionedTutorsPetDifferentCurrentState));
    }
}
