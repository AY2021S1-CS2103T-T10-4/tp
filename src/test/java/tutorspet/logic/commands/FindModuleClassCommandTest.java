package tutorspet.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tutorspet.commons.core.Messages.MESSAGE_MODULE_CLASS_LISTED_OVERVIEW;
import static tutorspet.logic.commands.CommandTestUtil.assertCommandSuccess;
import static tutorspet.testutil.TypicalModuleClass.CS2100_LAB;
import static tutorspet.testutil.TypicalModuleClass.CS2100_TUTORIAL;
import static tutorspet.testutil.TypicalModuleClass.CS2103T_TUTORIAL;
import static tutorspet.testutil.TypicalTutorsPet.getTypicalTutorsPet;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import tutorspet.model.Model;
import tutorspet.model.ModelManager;
import tutorspet.model.UserPrefs;
import tutorspet.model.components.name.NameContainsKeywordsPredicate;
import tutorspet.model.moduleclass.ModuleClass;

/**
 * Contains integration tests (interaction with the Model) for {@code FindModuleClassCommand}.
 */
public class FindModuleClassCommandTest {

    private Model model = new ModelManager(getTypicalTutorsPet(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalTutorsPet(), new UserPrefs());

    @Test
    public void equals() {
        NameContainsKeywordsPredicate<ModuleClass> firstPredicate =
                new NameContainsKeywordsPredicate<>(Collections.singletonList("first"));
        NameContainsKeywordsPredicate<ModuleClass> secondPredicate =
                new NameContainsKeywordsPredicate<>(Collections.singletonList("second"));

        FindModuleClassCommand findFirstCommand = new FindModuleClassCommand(firstPredicate);
        FindModuleClassCommand findSecondCommand = new FindModuleClassCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FindModuleClassCommand findFirstCommandCopy = new FindModuleClassCommand(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different keyword -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute_zeroKeywords_noModuleClassFound() {
        String expectedMessage = String.format(MESSAGE_MODULE_CLASS_LISTED_OVERVIEW, 0);
        NameContainsKeywordsPredicate<ModuleClass> predicate = preparePredicate(" ");
        FindModuleClassCommand command = new FindModuleClassCommand(predicate);
        expectedModel.updateFilteredModuleClassList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredModuleClassList());
    }

    @Test
    public void execute_singleKeyword_multipleModuleClassesFound() {
        String expectedMessage = String.format(MESSAGE_MODULE_CLASS_LISTED_OVERVIEW, 2);
        NameContainsKeywordsPredicate<ModuleClass> predicate = preparePredicate("Tutorial");
        FindModuleClassCommand command = new FindModuleClassCommand(predicate);
        expectedModel.updateFilteredModuleClassList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(CS2103T_TUTORIAL, CS2100_TUTORIAL), model.getFilteredModuleClassList());
    }

    @Test
    public void execute_multipleKeywords_multipleModuleClassesFound() {
        String expectedMessage = String.format(MESSAGE_MODULE_CLASS_LISTED_OVERVIEW, 2);
        NameContainsKeywordsPredicate<ModuleClass> predicate = preparePredicate("Lab CS2103T");
        FindModuleClassCommand command = new FindModuleClassCommand(predicate);
        expectedModel.updateFilteredModuleClassList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(CS2103T_TUTORIAL, CS2100_LAB), model.getFilteredModuleClassList());
    }

    /**
     * Parses {@code userInput} into a {@code NameContainsKeywordPredicate<ModuleClass>}.
     */
    private NameContainsKeywordsPredicate<ModuleClass> preparePredicate(String userInput) {
        return new NameContainsKeywordsPredicate<>(Arrays.asList(userInput.split("\\s+")));
    }
}
