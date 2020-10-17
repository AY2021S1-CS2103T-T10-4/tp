package tutorspet.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tutorspet.commons.core.Messages.MESSAGE_STUDENTS_LISTED_OVERVIEW;
import static tutorspet.logic.commands.CommandTestUtil.assertCommandSuccess;
import static tutorspet.testutil.TypicalStudent.CARL;
import static tutorspet.testutil.TypicalStudent.ELLE;
import static tutorspet.testutil.TypicalStudent.FIONA;
import static tutorspet.testutil.TypicalTutorsPet.getTypicalTutorsPet;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import tutorspet.model.Model;
import tutorspet.model.ModelManager;
import tutorspet.model.UserPrefs;
import tutorspet.model.components.name.NameContainsKeywordsPredicate;
import tutorspet.model.student.Student;

/**
 * Contains integration tests (interaction with the Model) for {@code FindStudentCommand}.
 */
public class FindStudentCommandTest {

    private Model model = new ModelManager(getTypicalTutorsPet(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalTutorsPet(), new UserPrefs());

    @Test
    public void equals() {
        NameContainsKeywordsPredicate<Student> firstPredicate =
                new NameContainsKeywordsPredicate<>(Collections.singletonList("first"));
        NameContainsKeywordsPredicate<Student> secondPredicate =
                new NameContainsKeywordsPredicate<>(Collections.singletonList("second"));

        FindStudentCommand findFirstCommand = new FindStudentCommand(firstPredicate);
        FindStudentCommand findSecondCommand = new FindStudentCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FindStudentCommand findFirstCommandCopy = new FindStudentCommand(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different student -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute_zeroKeywords_noStudentFound() {
        String expectedMessage = String.format(MESSAGE_STUDENTS_LISTED_OVERVIEW, 0);
        NameContainsKeywordsPredicate<Student> predicate = preparePredicate(" ");
        FindStudentCommand command = new FindStudentCommand(predicate);
        expectedModel.updateFilteredStudentList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredStudentList());
    }

    @Test
    public void execute_multipleKeywords_multipleStudentsFound() {
        String expectedMessage = String.format(MESSAGE_STUDENTS_LISTED_OVERVIEW, 3);
        NameContainsKeywordsPredicate<Student> predicate = preparePredicate("Kurz Elle Kunz");
        FindStudentCommand command = new FindStudentCommand(predicate);
        expectedModel.updateFilteredStudentList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(CARL, ELLE, FIONA), model.getFilteredStudentList());
    }

    /**
     * Parses {@code userInput} into a {@code NameContainsKeywordPredicate<Student>}.
     */
    private NameContainsKeywordsPredicate<Student> preparePredicate(String userInput) {
        return new NameContainsKeywordsPredicate<Student>(Arrays.asList(userInput.split("\\s+")));
    }
}
