package tutorspet.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tutorspet.logic.commands.CommandTestUtil.assertCommandFailure;
import static tutorspet.logic.commands.CommandTestUtil.assertCommandSuccess;
import static tutorspet.testutil.TypicalTutorsPet.getTypicalTutorsPet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import tutorspet.model.Model;
import tutorspet.model.ModelManager;
import tutorspet.model.UserPrefs;
import tutorspet.model.moduleclass.ModuleClass;
import tutorspet.testutil.ModuleClassBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code AddModuleClassCommand}.
 */
public class AddModuleClassCommandIntegrationTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalTutorsPet(), new UserPrefs());
    }

    @Test
    public void execute_newModuleClass_success() {
        ModuleClass validModuleClass = new ModuleClassBuilder().build();

        String expectedMessage = String.format(AddModuleClassCommand.MESSAGE_SUCCESS, validModuleClass);
        Model expectedModel = new ModelManager(model.getTutorsPet(), new UserPrefs());
        expectedModel.addModuleClass(validModuleClass);
        expectedModel.commit(expectedMessage);

        assertCommandSuccess(new AddModuleClassCommand(validModuleClass), model, expectedMessage, expectedModel);
        assertTrue(model.canUndo());
    }

    @Test
    public void execute_duplicateModuleClass_throwsCommandException() {
        ModuleClass moduleClassInList = model.getTutorsPet().getModuleClassList().get(0);
        assertCommandFailure(new AddModuleClassCommand(moduleClassInList), model,
                AddModuleClassCommand.MESSAGE_DUPLICATE_MODULE_CLASS);
        assertFalse(model.canUndo());
    }
}
