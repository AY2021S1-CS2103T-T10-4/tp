package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalTutorsPet.getOnlyModuleClassTutorsPet;
import static seedu.address.testutil.TypicalTutorsPet.getTypicalTutorsPet;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

public class ClearStudentCommandTest {

    @Test
    public void execute_emptyTutorsPet_success() {
        Model model = new ModelManager();
        Model expectedModel = new ModelManager();

        assertCommandSuccess(new ClearStudentCommand(), model, ClearStudentCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_nonEmptyTutorsPet_success() {
        Model model = new ModelManager(getTypicalTutorsPet(), new UserPrefs());
        Model expectedModel = new ModelManager(getOnlyModuleClassTutorsPet(), new UserPrefs());

        assertCommandSuccess(new ClearStudentCommand(), model, ClearStudentCommand.MESSAGE_SUCCESS, expectedModel);
    }
}
