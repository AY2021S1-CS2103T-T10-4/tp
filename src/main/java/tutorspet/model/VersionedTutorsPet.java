package tutorspet.model;

import static java.util.Objects.requireNonNull;
import static tutorspet.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import tutorspet.model.exception.RedoStateException;
import tutorspet.model.exception.UndoStateException;

public class VersionedTutorsPet extends TutorsPet {

    public static final String INITIAL_COMMIT_MESSAGE = "Loaded save data!";

    private List<TutorsPetState> tutorsPetStateList;
    private int statePointer;

    /**
     * Creates a {@code VersionedTutorsPet} using the Students in the {@code toBeCopied}.
     */
    public VersionedTutorsPet(ReadOnlyTutorsPet toBeCopied) {
        super(toBeCopied);

        tutorsPetStateList = new ArrayList<>();
        TutorsPetState initialState = new TutorsPetState(INITIAL_COMMIT_MESSAGE, this);
        tutorsPetStateList.add(initialState);
        statePointer = 0;
    }

    /**
     * Saves the current state of the {@code TutorsPet} with the associated {@code commitMessage}.
     *
     * @throws NullPointerException if the {@code commitMessage} is null.
     */
    public void commit(String commitMessage) throws NullPointerException {
        requireNonNull(commitMessage);

        pruneStates();
        TutorsPetState state = new TutorsPetState(commitMessage, this);
        tutorsPetStateList.add(state);
        statePointer++;
    }

    /**
     * Undoes the most recent undoable command.
     *
     * @return the commit message of the undone command.
     * @throws UndoStateException if there are no commands to undo.
     */
    public String undo() throws UndoStateException {
        if (!canUndo()) {
            throw new UndoStateException();
        }

        TutorsPetState currentState = tutorsPetStateList.get(statePointer);
        TutorsPetState targetState = tutorsPetStateList.get(statePointer - 1);
        resetData(targetState.stateSnapshot);
        statePointer--;
        return currentState.commitMessage;
    }

    /**
     * Redoes the most recent undone command.
     *
     * @return the commit message of the redone command.
     * @throws RedoStateException if there are no commands to redo.
     */
    public String redo() throws RedoStateException {
        if (!canRedo()) {
            throw new RedoStateException();
        }

        TutorsPetState targetState = tutorsPetStateList.get(statePointer + 1);
        resetData(targetState.stateSnapshot);
        statePointer++;
        return targetState.commitMessage;
    }

    private void pruneStates() {
        tutorsPetStateList.subList(statePointer + 1, tutorsPetStateList.size()).clear();
    }

    /**
     * Returns true if there is a {@code Command} that can be undone.
     */
    public boolean canUndo() {
        return statePointer > 0;
    }

    /**
     * Returns true if there is an undone {@code Command} that can be redone.
     */
    public boolean canRedo() {
        return statePointer < tutorsPetStateList.size() - 1;
    }

    /**
     * Returns a summary of all {@code Command}s currently recorded by this {@code VersionedTutorsPet}.
     */
    public StateRecords viewStateRecords() {
        return new StateRecords(statePointer, tutorsPetStateList.stream().map(state ->
                state.commitMessage).collect(Collectors.toUnmodifiableList()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof VersionedTutorsPet // instanceof handles nulls
                && ((VersionedTutorsPet) other).statePointer == statePointer
                && ((VersionedTutorsPet) other).tutorsPetStateList.equals(tutorsPetStateList)
                && super.equals(other));
    }

    private class TutorsPetState {

        private String commitMessage;
        private ReadOnlyTutorsPet stateSnapshot;

        TutorsPetState(String commitMessage, ReadOnlyTutorsPet stateSnapshot) {
            requireAllNonNull(commitMessage, stateSnapshot);

            this.commitMessage = commitMessage;
            this.stateSnapshot = new TutorsPet(stateSnapshot);
        }

        @Override
        public boolean equals(Object other) {
            return other == this // short circuit if same object
                    || (other instanceof TutorsPetState // instanceof handles nulls
                    && ((TutorsPetState) other).stateSnapshot.equals(stateSnapshot)
                    && ((TutorsPetState) other).commitMessage.equals(commitMessage));
        }
    }
}
