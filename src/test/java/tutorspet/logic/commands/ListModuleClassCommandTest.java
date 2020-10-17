package tutorspet.logic.commands;

import static tutorspet.logic.commands.CommandTestUtil.assertCommandSuccess;
import static tutorspet.logic.commands.CommandTestUtil.showModuleClassAtIndex;
import static tutorspet.testutil.TypicalIndexes.INDEX_FIRST_ITEM;
import static tutorspet.testutil.TypicalTutorsPet.getTypicalTutorsPet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import tutorspet.model.Model;
import tutorspet.model.ModelManager;
import tutorspet.model.UserPrefs;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ListModuleClassCommand.
 */
public class ListModuleClassCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalTutorsPet(), new UserPrefs());
        expectedModel = new ModelManager(model.getTutorsPet(), new UserPrefs());
    }

    @Test
    public void execute_moduleClassListIsNotFiltered_showsSameList() {
        assertCommandSuccess(
                new ListModuleClassCommand(), model, ListModuleClassCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_moduleClassListIsFiltered_showsEverything() {
        showModuleClassAtIndex(model, INDEX_FIRST_ITEM);
        assertCommandSuccess(
                new ListModuleClassCommand(), model, ListModuleClassCommand.MESSAGE_SUCCESS, expectedModel);
    }
}
