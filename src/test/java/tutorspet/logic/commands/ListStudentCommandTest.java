package tutorspet.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tutorspet.logic.commands.CommandTestUtil.assertCommandSuccess;
import static tutorspet.logic.commands.CommandTestUtil.showStudentAtIndex;
import static tutorspet.testutil.TypicalIndexes.INDEX_FIRST_ITEM;
import static tutorspet.testutil.TypicalTutorsPet.getTypicalTutorsPet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import tutorspet.model.Model;
import tutorspet.model.ModelManager;
import tutorspet.model.UserPrefs;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ListStudentCommand.
 */
public class ListStudentCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalTutorsPet(), new UserPrefs());
        expectedModel = new ModelManager(model.getTutorsPet(), new UserPrefs());
    }

    @Test
    public void execute_noIndexStudentListIsNotFiltered_showsSameList() {
        assertCommandSuccess(new ListStudentCommand(), model,
                ListStudentCommand.MESSAGE_LIST_ALL_SUCCESS, expectedModel);
    }

    @Test
    public void execute_noIndexStudentListIsFiltered_showsEverything() {
        showStudentAtIndex(model, INDEX_FIRST_ITEM);
        assertCommandSuccess(new ListStudentCommand(), model,
                ListStudentCommand.MESSAGE_LIST_ALL_SUCCESS, expectedModel);
    }

    @Test
    public void equals() {
        ListStudentCommand listStudentCommand = new ListStudentCommand();

        // ListStudentCommand -> returns true
        ListStudentCommand listStudentCommandCopy = new ListStudentCommand();
        assertTrue(listStudentCommand.equals(listStudentCommandCopy));

        // same object -> returns true
        assertTrue(listStudentCommand.equals(listStudentCommand));

        // null -> returns false
        assertFalse(listStudentCommand.equals(null));

        // different type -> returns false
        assertFalse(listStudentCommand.equals(5));
    }
}
