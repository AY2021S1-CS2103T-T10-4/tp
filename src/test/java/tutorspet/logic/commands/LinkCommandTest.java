package tutorspet.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tutorspet.logic.commands.CommandTestUtil.assertCommandFailure;
import static tutorspet.logic.commands.CommandTestUtil.assertCommandSuccess;
import static tutorspet.logic.commands.CommandTestUtil.showModuleClassAtIndex;
import static tutorspet.logic.commands.CommandTestUtil.showStudentAtIndex;
import static tutorspet.logic.commands.UnlinkCommandTest.copyModelWithModuleClassAndShowStudents;
import static tutorspet.model.Model.PREDICATE_SHOW_ALL_MODULE_CLASS;
import static tutorspet.model.Model.PREDICATE_SHOW_ALL_STUDENTS;
import static tutorspet.testutil.Assert.assertThrows;
import static tutorspet.testutil.TypicalIndexes.INDEX_FIRST_ITEM;
import static tutorspet.testutil.TypicalIndexes.INDEX_SECOND_ITEM;
import static tutorspet.testutil.TypicalTutorsPet.getNoLinkTutorsPet;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import tutorspet.commons.core.Messages;
import tutorspet.commons.core.index.Index;
import tutorspet.model.Model;
import tutorspet.model.ModelManager;
import tutorspet.model.UserPrefs;
import tutorspet.model.moduleclass.ModuleClass;
import tutorspet.model.student.Student;

/**
 * Contains integration tests (interaction with the Model) for {@code LinkCommand}.
 */
public class LinkCommandTest {

    private Model model = new ModelManager(getNoLinkTutorsPet(), new UserPrefs());

    @Test
    public void constructor_nullStudentIndex_throwNullPointerException() {
        assertThrows(NullPointerException.class, () -> new LinkCommand(INDEX_FIRST_ITEM, null));
    }

    @Test
    public void constructor_nullModuleClassIndex_throwNullPointerException() {
        assertThrows(NullPointerException.class, () -> new LinkCommand(null, INDEX_FIRST_ITEM));
    }

    @Test
    public void execute_unfilteredList_success() {
        Index moduleClassIndex = INDEX_FIRST_ITEM;
        Index studentIndex = INDEX_FIRST_ITEM;

        // manually link first class to first student
        ModuleClass moduleClass = model.getFilteredModuleClassList().get(moduleClassIndex.getZeroBased());
        Student student = model.getFilteredStudentList().get(studentIndex.getZeroBased());
        ModuleClass modifiedModuleClass = manualLinkStudentToModuleClass(moduleClass, student);

        String expectedMessage =
                String.format(LinkCommand.MESSAGE_LINK_SUCCESS, student.getName(), modifiedModuleClass);
        Model expectedModel = copyModelWithModuleClassAndShowStudents(model, moduleClass, modifiedModuleClass);
        expectedModel.commit(expectedMessage);

        assertCommandSuccess(new LinkCommand(moduleClassIndex, studentIndex), model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showStudentAtIndex(model, INDEX_FIRST_ITEM);
        showModuleClassAtIndex(model, INDEX_FIRST_ITEM);

        Index moduleClassIndex = INDEX_FIRST_ITEM;
        Index studentIndex = INDEX_FIRST_ITEM;

        // manually link first class to first student
        ModuleClass moduleClass = model.getFilteredModuleClassList().get(moduleClassIndex.getZeroBased());
        Student student = model.getFilteredStudentList().get(studentIndex.getZeroBased());
        ModuleClass modifiedModuleClass = manualLinkStudentToModuleClass(moduleClass, student);

        String expectedMessage =
                String.format(LinkCommand.MESSAGE_LINK_SUCCESS, student.getName(), modifiedModuleClass);
        Model expectedModel = copyModelWithModuleClassAndShowStudents(model, moduleClass, modifiedModuleClass);
        expectedModel.commit(expectedMessage);

        assertCommandSuccess(new LinkCommand(moduleClassIndex, studentIndex), model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_existingStudent_failure() {
        Index moduleClassIndex = INDEX_FIRST_ITEM;
        Index studentIndex = INDEX_FIRST_ITEM;

        // manually link first class to first student
        ModuleClass moduleClass = model.getFilteredModuleClassList().get(moduleClassIndex.getZeroBased());
        Student student = model.getFilteredStudentList().get(studentIndex.getZeroBased());
        ModuleClass modifiedModuleClass = manualLinkStudentToModuleClass(moduleClass, student);

        // update model with modified class
        model.setModuleClass(moduleClass, modifiedModuleClass);
        model.updateFilteredStudentList(PREDICATE_SHOW_ALL_STUDENTS);
        model.updateFilteredModuleClassList(PREDICATE_SHOW_ALL_MODULE_CLASS);

        LinkCommand linkCommand = new LinkCommand(INDEX_FIRST_ITEM, INDEX_FIRST_ITEM);

        assertCommandFailure(linkCommand, model, LinkCommand.MESSAGE_EXISTING_LINK);
    }

    @Test
    public void execute_invalidStudentIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredStudentList().size() + 1);
        LinkCommand linkCommand = new LinkCommand(INDEX_FIRST_ITEM, outOfBoundIndex);

        assertCommandFailure(linkCommand, model, Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidStudentIndexFilteredList_failure() {
        showStudentAtIndex(model, INDEX_FIRST_ITEM);
        Index outOfBoundIndex = INDEX_SECOND_ITEM;

        // ensures that outOfBoundIndex is still in bounds of student list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getTutorsPet().getStudentList().size());

        LinkCommand linkCommand = new LinkCommand(INDEX_FIRST_ITEM, outOfBoundIndex);

        assertCommandFailure(linkCommand, model, Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidModuleClassIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredModuleClassList().size() + 1);
        LinkCommand linkCommand = new LinkCommand(outOfBoundIndex, INDEX_FIRST_ITEM);

        assertCommandFailure(linkCommand, model, Messages.MESSAGE_INVALID_MODULE_CLASS_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidModuleClassIndexFilteredList_failure() {
        showModuleClassAtIndex(model, INDEX_FIRST_ITEM);
        Index outOfBoundIndex = INDEX_SECOND_ITEM;

        // ensures that outOfBoundIndex is still in bounds of class list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getTutorsPet().getModuleClassList().size());

        LinkCommand linkCommand = new LinkCommand(outOfBoundIndex, INDEX_FIRST_ITEM);

        assertCommandFailure(linkCommand, model, Messages.MESSAGE_INVALID_MODULE_CLASS_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        LinkCommand linkCommand = new LinkCommand(INDEX_FIRST_ITEM, INDEX_FIRST_ITEM);

        // same indexes -> return true
        LinkCommand linkCommandCopy = new LinkCommand(INDEX_FIRST_ITEM, INDEX_FIRST_ITEM);
        assertTrue(linkCommand.equals(linkCommandCopy));

        // same object -> returns true
        assertTrue(linkCommand.equals(linkCommand));

        // null -> returns false
        assertFalse(linkCommand.equals(null));

        // different type -> returns false
        assertFalse(linkCommand.equals(5));

        // different student index -> returns false
        LinkCommand linkCommandDifferentStudent = new LinkCommand(INDEX_FIRST_ITEM, INDEX_SECOND_ITEM);
        assertFalse(linkCommand.equals(linkCommandDifferentStudent));

        // different class index -> returns false
        LinkCommand linkCommandDifferentClass = new LinkCommand(INDEX_SECOND_ITEM, INDEX_FIRST_ITEM);
        assertFalse(linkCommand.equals(linkCommandDifferentClass));
    }

    /**
     * Returns a new {@code ModuleClass} based on the given {@code moduleClass} but with the specified {@code student}
     * added.
     * Requires {@code student} to not have an existing link with {@code moduleClass}.
     */
    private static ModuleClass manualLinkStudentToModuleClass(ModuleClass moduleClass, Student student) {
        Set<UUID> studentUuids = new HashSet<>(moduleClass.getStudentUuids());

        assertFalse(studentUuids.contains(student.getUuid()), "Test precondition error: The selected module class"
                + " already contains the selected student.");

        studentUuids.add(student.getUuid());
        return new ModuleClass(moduleClass.getName(), studentUuids, moduleClass.getLessons());
    }
}
