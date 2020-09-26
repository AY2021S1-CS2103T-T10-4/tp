package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_CS2100_TUTORIAL;
import static seedu.address.logic.commands.CommandTestUtil.DESC_CS2103T_TUTORIAL;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_CS2030_TUTORIAL;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showModuleClassAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_MODULE_CLASS;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_MODULE_CLASS;
import static seedu.address.testutil.TypicalTutorsPet.getTypicalTutorsPet;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditModuleClassCommand.EditModuleClassDescriptor;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.TutorsPet;
import seedu.address.model.UserPrefs;
import seedu.address.model.moduleclass.ModuleClass;
import seedu.address.testutil.EditModuleClassDescriptorBuilder;
import seedu.address.testutil.ModuleClassBuilder;
import seedu.address.testutil.TypicalModuleClass;

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for
 * {@code EditModuleClassCommand}.
 */
public class EditModuleClassCommandTest {

    private Model model = new ModelManager(getTypicalTutorsPet(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        ModuleClass editedModuleClass = new ModuleClassBuilder(TypicalModuleClass.CS2103T_TUTORIAL).build();
        EditModuleClassDescriptor descriptor = new EditModuleClassDescriptorBuilder(editedModuleClass).build();
        EditModuleClassCommand editModuleClassCommand = new EditModuleClassCommand(INDEX_FIRST_MODULE_CLASS,
                descriptor);

        String expectedMessage = String.format(EditModuleClassCommand.MESSAGE_EDIT_MODULE_CLASS_SUCCESS,
                editedModuleClass);

        Model expectedModel = new ModelManager(new TutorsPet(model.getTutorsPet()), new UserPrefs());
        expectedModel.setModuleClass(model.getFilteredModuleClassList().get(0), editedModuleClass);

        assertCommandSuccess(editModuleClassCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastModuleClass = Index.fromOneBased(model.getFilteredModuleClassList().size());
        ModuleClass lastModuleClass = model.getFilteredModuleClassList().get(indexLastModuleClass.getZeroBased());

        ModuleClassBuilder studentInList = new ModuleClassBuilder(lastModuleClass);
        ModuleClass editedModuleClass = studentInList.withName(VALID_NAME_CS2030_TUTORIAL).build();

        EditModuleClassDescriptor descriptor = new EditModuleClassDescriptorBuilder()
                .withName(VALID_NAME_CS2030_TUTORIAL).build();
        EditModuleClassCommand editModuleClassCommand = new EditModuleClassCommand(indexLastModuleClass, descriptor);

        String expectedMessage = String.format(EditModuleClassCommand.MESSAGE_EDIT_MODULE_CLASS_SUCCESS,
                editedModuleClass);

        Model expectedModel = new ModelManager(new TutorsPet(model.getTutorsPet()), new UserPrefs());
        expectedModel.setModuleClass(lastModuleClass, editedModuleClass);

        assertCommandSuccess(editModuleClassCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showModuleClassAtIndex(model, INDEX_FIRST_MODULE_CLASS);

        ModuleClass moduleClassInFilteredList =
                model.getFilteredModuleClassList().get(INDEX_FIRST_MODULE_CLASS.getZeroBased());
        ModuleClass editedModuleClass =
                new ModuleClassBuilder(moduleClassInFilteredList).withName(VALID_NAME_CS2030_TUTORIAL).build();
        EditModuleClassCommand editModuleClassCommand = new EditModuleClassCommand(INDEX_FIRST_MODULE_CLASS,
                new EditModuleClassDescriptorBuilder().withName(VALID_NAME_CS2030_TUTORIAL).build());

        String expectedMessage = String.format(EditModuleClassCommand.MESSAGE_EDIT_MODULE_CLASS_SUCCESS,
                editedModuleClass);

        Model expectedModel = new ModelManager(new TutorsPet(model.getTutorsPet()), new UserPrefs());
        expectedModel.setModuleClass(model.getFilteredModuleClassList().get(0), editedModuleClass);

        assertCommandSuccess(editModuleClassCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateStudentUnfilteredList_failure() {
        ModuleClass firstModuleClass = model.getFilteredModuleClassList().get(INDEX_FIRST_MODULE_CLASS.getZeroBased());
        EditModuleClassDescriptor descriptor = new EditModuleClassDescriptorBuilder(firstModuleClass).build();
        EditModuleClassCommand editModuleClassCommand = new EditModuleClassCommand(INDEX_SECOND_MODULE_CLASS,
                descriptor);

        assertCommandFailure(editModuleClassCommand, model, EditModuleClassCommand.MESSAGE_DUPLICATE_MODULE_CLASS);
    }

    @Test
    public void execute_duplicateStudentFilteredList_failure() {
        showModuleClassAtIndex(model, INDEX_FIRST_MODULE_CLASS);

        // edit module class in filtered list into a duplicate in Tutor's Pet
        ModuleClass moduleClassInList = model.getTutorsPet().getModuleClassList()
                .get(INDEX_SECOND_MODULE_CLASS.getZeroBased());
        EditModuleClassCommand editModuleClassCommand = new EditModuleClassCommand(INDEX_FIRST_MODULE_CLASS,
                new EditModuleClassDescriptorBuilder(moduleClassInList).build());

        assertCommandFailure(editModuleClassCommand, model, EditModuleClassCommand.MESSAGE_DUPLICATE_MODULE_CLASS);
    }

    @Test
    public void execute_invalidStudentIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredModuleClassList().size() + 1);
        EditModuleClassDescriptor descriptor =
                new EditModuleClassDescriptorBuilder().withName(VALID_NAME_CS2030_TUTORIAL).build();
        EditModuleClassCommand editModuleClassCommand = new EditModuleClassCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editModuleClassCommand, model, Messages.MESSAGE_INVALID_MODULE_CLASS_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of module class list.
     */
    @Test
    public void execute_invalidStudentIndexFilteredList_failure() {
        showModuleClassAtIndex(model, INDEX_FIRST_MODULE_CLASS);
        Index outOfBoundIndex = INDEX_SECOND_MODULE_CLASS;

        // ensures that outOfBoundIndex is still in bounds of module class list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getTutorsPet().getModuleClassList().size());

        EditModuleClassCommand editModuleClassCommand = new EditModuleClassCommand(outOfBoundIndex,
                new EditModuleClassDescriptorBuilder().withName(VALID_NAME_CS2030_TUTORIAL).build());

        assertCommandFailure(editModuleClassCommand, model, Messages.MESSAGE_INVALID_MODULE_CLASS_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        final EditModuleClassCommand standardCommand = new EditModuleClassCommand(INDEX_FIRST_MODULE_CLASS,
                DESC_CS2100_TUTORIAL);

        // same value -> return true
        EditModuleClassDescriptor copyDescriptor = new EditModuleClassDescriptor(DESC_CS2100_TUTORIAL);
        EditModuleClassCommand commandWithSameValue = new EditModuleClassCommand(INDEX_FIRST_MODULE_CLASS,
                copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValue));

        // same object -> return true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> return false
        assertFalse(standardCommand.equals(null));

        // different index -> return false
        assertFalse(standardCommand
                .equals(new EditModuleClassCommand(INDEX_SECOND_MODULE_CLASS, DESC_CS2100_TUTORIAL)));

        // different descriptor -> return false
        assertFalse(standardCommand
                .equals(new EditModuleClassCommand(INDEX_FIRST_MODULE_CLASS, DESC_CS2103T_TUTORIAL)));
    }
}
