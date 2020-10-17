package tutorspet.logic.commands;

import static tutorspet.logic.commands.CommandTestUtil.assertCommandSuccess;
import static tutorspet.testutil.TypicalTutorsPet.getOnlyStudentsTutorsPet;
import static tutorspet.testutil.TypicalTutorsPet.getTypicalTutorsPet;

import org.junit.jupiter.api.Test;

import tutorspet.model.Model;
import tutorspet.model.ModelManager;
import tutorspet.model.UserPrefs;

public class ClearModuleClassCommandTest {

    @Test
    public void execute_emptyTutorsPet_success() {
        Model model = new ModelManager();
        Model expectedModel = new ModelManager();
        expectedModel.commit(ClearModuleClassCommand.MESSAGE_SUCCESS);

        assertCommandSuccess(new ClearModuleClassCommand(),
                model, ClearModuleClassCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_nonEmptyTutorsPet_success() {
        Model model = new ModelManager(getTypicalTutorsPet(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalTutorsPet(), new UserPrefs());
        expectedModel.setTutorsPet(getOnlyStudentsTutorsPet());
        expectedModel.commit(ClearModuleClassCommand.MESSAGE_SUCCESS);

        assertCommandSuccess(new ClearModuleClassCommand(),
                model, ClearModuleClassCommand.MESSAGE_SUCCESS, expectedModel);
    }
}
