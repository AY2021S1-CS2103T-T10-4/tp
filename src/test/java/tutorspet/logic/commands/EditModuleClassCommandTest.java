package tutorspet.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tutorspet.logic.commands.CommandTestUtil.DESC_CS2100_LAB;
import static tutorspet.logic.commands.CommandTestUtil.DESC_CS2103T_TUTORIAL;
import static tutorspet.logic.commands.CommandTestUtil.VALID_NAME_CS2030_TUTORIAL;
import static tutorspet.logic.commands.CommandTestUtil.assertCommandFailure;
import static tutorspet.logic.commands.CommandTestUtil.assertCommandSuccess;
import static tutorspet.logic.commands.CommandTestUtil.showModuleClassAtIndex;
import static tutorspet.testutil.TypicalIndexes.INDEX_FIRST_ITEM;
import static tutorspet.testutil.TypicalIndexes.INDEX_SECOND_ITEM;
import static tutorspet.testutil.TypicalTutorsPet.getTypicalTutorsPet;

import org.junit.jupiter.api.Test;

import tutorspet.commons.core.Messages;
import tutorspet.commons.core.index.Index;
import tutorspet.logic.commands.EditModuleClassCommand.EditModuleClassDescriptor;
import tutorspet.model.Model;
import tutorspet.model.ModelManager;
import tutorspet.model.TutorsPet;
import tutorspet.model.UserPrefs;
import tutorspet.model.moduleclass.ModuleClass;
import tutorspet.testutil.EditModuleClassDescriptorBuilder;
import tutorspet.testutil.ModuleClassBuilder;
import tutorspet.testutil.TypicalModuleClass;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code EditModuleClassCommand}.
 */
public class EditModuleClassCommandTest {

    private Model model = new ModelManager(getTypicalTutorsPet(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        ModuleClass editedModuleClass = new ModuleClassBuilder(TypicalModuleClass.CS2103T_TUTORIAL).build();
        EditModuleClassDescriptor descriptor = new EditModuleClassDescriptorBuilder(editedModuleClass).build();
        EditModuleClassCommand editModuleClassCommand = new EditModuleClassCommand(INDEX_FIRST_ITEM,
                descriptor);

        String expectedMessage = String.format(EditModuleClassCommand.MESSAGE_EDIT_MODULE_CLASS_SUCCESS,
                editedModuleClass);

        Model expectedModel = new ModelManager(new TutorsPet(model.getTutorsPet()), new UserPrefs());
        expectedModel.setModuleClass(model.getFilteredModuleClassList().get(0), editedModuleClass);
        expectedModel.commit(expectedMessage);

        assertCommandSuccess(editModuleClassCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastModuleClass = Index.fromOneBased(model.getFilteredModuleClassList().size());
        ModuleClass lastModuleClass = model.getFilteredModuleClassList().get(indexLastModuleClass.getZeroBased());

        ModuleClassBuilder moduleClassInList = new ModuleClassBuilder(lastModuleClass);
        ModuleClass editedModuleClass = moduleClassInList.withName(VALID_NAME_CS2030_TUTORIAL).build();

        EditModuleClassDescriptor descriptor = new EditModuleClassDescriptorBuilder()
                .withName(VALID_NAME_CS2030_TUTORIAL).build();
        EditModuleClassCommand editModuleClassCommand = new EditModuleClassCommand(indexLastModuleClass, descriptor);

        String expectedMessage = String.format(EditModuleClassCommand.MESSAGE_EDIT_MODULE_CLASS_SUCCESS,
                editedModuleClass);

        Model expectedModel = new ModelManager(new TutorsPet(model.getTutorsPet()), new UserPrefs());
        expectedModel.setModuleClass(lastModuleClass, editedModuleClass);
        expectedModel.commit(expectedMessage);

        assertCommandSuccess(editModuleClassCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showModuleClassAtIndex(model, INDEX_FIRST_ITEM);

        ModuleClass moduleClassInFilteredList =
                model.getFilteredModuleClassList().get(INDEX_FIRST_ITEM.getZeroBased());
        ModuleClass editedModuleClass =
                new ModuleClassBuilder(moduleClassInFilteredList).withName(VALID_NAME_CS2030_TUTORIAL).build();
        EditModuleClassCommand editModuleClassCommand = new EditModuleClassCommand(INDEX_FIRST_ITEM,
                new EditModuleClassDescriptorBuilder().withName(VALID_NAME_CS2030_TUTORIAL).build());

        String expectedMessage = String.format(EditModuleClassCommand.MESSAGE_EDIT_MODULE_CLASS_SUCCESS,
                editedModuleClass);

        Model expectedModel = new ModelManager(new TutorsPet(model.getTutorsPet()), new UserPrefs());
        expectedModel.setModuleClass(model.getFilteredModuleClassList().get(0), editedModuleClass);
        expectedModel.commit(expectedMessage);

        assertCommandSuccess(editModuleClassCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateModuleClassUnfilteredList_failure() {
        ModuleClass firstModuleClass = model.getFilteredModuleClassList().get(INDEX_FIRST_ITEM.getZeroBased());
        EditModuleClassDescriptor descriptor = new EditModuleClassDescriptorBuilder(firstModuleClass).build();
        EditModuleClassCommand editModuleClassCommand = new EditModuleClassCommand(INDEX_SECOND_ITEM,
                descriptor);

        assertCommandFailure(editModuleClassCommand, model, EditModuleClassCommand.MESSAGE_DUPLICATE_MODULE_CLASS);
    }

    @Test
    public void execute_duplicateModuleClassFilteredList_failure() {
        showModuleClassAtIndex(model, INDEX_FIRST_ITEM);

        // edit ModuleClass in filtered list into a duplicate in Tutor's Pet
        ModuleClass moduleClassInList = model.getTutorsPet().getModuleClassList()
                .get(INDEX_SECOND_ITEM.getZeroBased());
        EditModuleClassCommand editModuleClassCommand = new EditModuleClassCommand(INDEX_FIRST_ITEM,
                new EditModuleClassDescriptorBuilder(moduleClassInList).build());

        assertCommandFailure(editModuleClassCommand, model, EditModuleClassCommand.MESSAGE_DUPLICATE_MODULE_CLASS);
    }

    @Test
    public void execute_invalidModuleClassIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredModuleClassList().size() + 1);
        EditModuleClassDescriptor descriptor =
                new EditModuleClassDescriptorBuilder().withName(VALID_NAME_CS2030_TUTORIAL).build();
        EditModuleClassCommand editModuleClassCommand = new EditModuleClassCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editModuleClassCommand, model, Messages.MESSAGE_INVALID_MODULE_CLASS_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of the class list.
     */
    @Test
    public void execute_invalidModuleClassIndexFilteredList_failure() {
        showModuleClassAtIndex(model, INDEX_FIRST_ITEM);
        Index outOfBoundIndex = INDEX_SECOND_ITEM;

        // ensures that outOfBoundIndex is still in bounds of the class list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getTutorsPet().getModuleClassList().size());

        EditModuleClassCommand editModuleClassCommand = new EditModuleClassCommand(outOfBoundIndex,
                new EditModuleClassDescriptorBuilder().withName(VALID_NAME_CS2030_TUTORIAL).build());

        assertCommandFailure(editModuleClassCommand, model, Messages.MESSAGE_INVALID_MODULE_CLASS_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        final EditModuleClassCommand standardCommand = new EditModuleClassCommand(INDEX_FIRST_ITEM,
                DESC_CS2100_LAB);

        // same value -> return true
        EditModuleClassDescriptor copyDescriptor = new EditModuleClassDescriptor(DESC_CS2100_LAB);
        EditModuleClassCommand commandWithSameValue = new EditModuleClassCommand(INDEX_FIRST_ITEM,
                copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValue));

        // same object -> return true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> return false
        assertFalse(standardCommand.equals(null));

        // different types -> return false
        assertFalse(standardCommand.equals(1));

        // different index -> return false
        assertFalse(standardCommand
                .equals(new EditModuleClassCommand(INDEX_SECOND_ITEM, DESC_CS2100_LAB)));

        // different descriptor -> return false
        assertFalse(standardCommand
                .equals(new EditModuleClassCommand(INDEX_FIRST_ITEM, DESC_CS2103T_TUTORIAL)));
    }
}
