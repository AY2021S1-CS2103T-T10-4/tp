package tutorspet.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tutorspet.model.Model.PREDICATE_SHOW_ALL_MODULE_CLASS;
import static tutorspet.model.Model.PREDICATE_SHOW_ALL_STUDENTS;
import static tutorspet.model.VersionedTutorsPetTest.COMMIT_MESSAGE_1;
import static tutorspet.model.VersionedTutorsPetTest.COMMIT_MESSAGE_2;
import static tutorspet.testutil.Assert.assertThrows;
import static tutorspet.testutil.TypicalModuleClass.CS2100_LAB;
import static tutorspet.testutil.TypicalModuleClass.CS2103T_TUTORIAL;
import static tutorspet.testutil.TypicalStudent.ALICE;
import static tutorspet.testutil.TypicalStudent.AMY;
import static tutorspet.testutil.TypicalStudent.BENSON;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import tutorspet.commons.core.GuiSettings;
import tutorspet.model.components.name.NameContainsKeywordsPredicate;
import tutorspet.model.exception.RedoStateException;
import tutorspet.model.exception.UndoStateException;
import tutorspet.model.moduleclass.ModuleClass;
import tutorspet.model.moduleclass.exceptions.DuplicateModuleClassException;
import tutorspet.model.moduleclass.exceptions.ModuleClassNotFoundException;
import tutorspet.model.student.exceptions.StudentNotFoundException;
import tutorspet.testutil.ModuleClassBuilder;
import tutorspet.testutil.TutorsPetBuilder;

public class ModelManagerTest {

    private ModelManager modelManager = new ModelManager();

    @Test
    public void constructor() {
        assertEquals(new UserPrefs(), modelManager.getUserPrefs());
        assertEquals(new GuiSettings(), modelManager.getGuiSettings());
        assertEquals(new TutorsPet(), new TutorsPet(modelManager.getTutorsPet()));
    }

    @Test
    public void setUserPrefs_nullUserPrefs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setUserPrefs(null));
    }

    @Test
    public void setUserPrefs_validUserPrefs_copiesUserPrefs() {
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setTutorsPetFilePath(Paths.get("address/book/file/path"));
        userPrefs.setGuiSettings(new GuiSettings(1, 2, 3, 4));
        modelManager.setUserPrefs(userPrefs);
        assertEquals(userPrefs, modelManager.getUserPrefs());

        // Modifying userPrefs should not modify modelManager's userPrefs
        UserPrefs oldUserPrefs = new UserPrefs(userPrefs);
        userPrefs.setTutorsPetFilePath(Paths.get("new/address/book/file/path"));
        assertEquals(oldUserPrefs, modelManager.getUserPrefs());
    }

    @Test
    public void setGuiSettings_nullGuiSettings_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setGuiSettings(null));
    }

    @Test
    public void setGuiSettings_validGuiSettings_setsGuiSettings() {
        GuiSettings guiSettings = new GuiSettings(1, 2, 3, 4);
        modelManager.setGuiSettings(guiSettings);
        assertEquals(guiSettings, modelManager.getGuiSettings());
    }

    @Test
    public void setTutorsPetFilePath_nullPath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setTutorsPetFilePath(null));
    }

    @Test
    public void setTutorsPetFilePath_validPath_setsTutorsPetFilePath() {
        Path path = Paths.get("tutors/pet/file/path");
        modelManager.setTutorsPetFilePath(path);
        assertEquals(path, modelManager.getTutorsPetFilePath());
    }

    @Test
    public void commit_nullMessage_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.commit(null));
    }

    @Test
    public void commit_removesFutureStates() {
        modelManager.commit(COMMIT_MESSAGE_1);
        modelManager.undo();
        assertTrue(modelManager.canRedo());
        modelManager.commit(COMMIT_MESSAGE_2);
        assertFalse(modelManager.canRedo());
    }

    @Test
    public void canUndo_hasPreviousState_returnsTrue() {
        modelManager.commit(COMMIT_MESSAGE_1);
        assertTrue(modelManager.canUndo());
    }

    @Test
    public void canUndo_noPreviousState_returnsFalse() {
        assertFalse(modelManager.canUndo());
    }

    @Test
    public void undo_hasPreviousState_returnsCommitMessage() {
        modelManager.commit(COMMIT_MESSAGE_1);
        assertEquals(COMMIT_MESSAGE_1, modelManager.undo());
    }

    @Test
    public void undo_preservesFutureState() {
        modelManager.commit(COMMIT_MESSAGE_1);
        modelManager.undo();
        assertTrue(modelManager.canRedo());
    }

    @Test
    public void undo_noPreviousState_throwsUndoStateException() {
        assertThrows(UndoStateException.class, () -> modelManager.undo());
    }

    @Test
    public void canRedo_hasNextState_returnsTrue() {
        modelManager.commit(COMMIT_MESSAGE_1);
        modelManager.undo();
        assertTrue(modelManager.canRedo());
    }

    @Test
    public void canRedo_noNextState_returnsFalse() {
        assertFalse(modelManager.canRedo());
    }

    @Test
    public void redo_hasNextState_returnsCommitMessage() {
        modelManager.commit(COMMIT_MESSAGE_1);
        modelManager.undo();
        assertEquals(COMMIT_MESSAGE_1, modelManager.redo());
    }

    @Test
    public void redo_preservesRedoneState() {
        modelManager.commit(COMMIT_MESSAGE_1);
        modelManager.commit(COMMIT_MESSAGE_2);
        assertEquals(COMMIT_MESSAGE_2, modelManager.undo());
        assertEquals(COMMIT_MESSAGE_2, modelManager.redo());
        assertTrue(modelManager.canUndo());
        assertEquals(COMMIT_MESSAGE_2, modelManager.undo());
    }

    @Test
    public void redo_noNextState_throwsRedoStateException() {
        assertThrows(RedoStateException.class, () -> modelManager.redo());
    }

    @Test
    public void hasStudent_nullStudent_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.hasStudent(null));
    }

    @Test
    public void hasStudent_studentNotInModelManager_returnsFalse() {
        assertFalse(modelManager.hasStudent(ALICE));
    }

    @Test
    public void hasStudent_studentInModelManager_returnsTrue() {
        modelManager.addStudent(ALICE);
        assertTrue(modelManager.hasStudent(ALICE));
    }

    @Test
    public void deleteStudent_nullStudent_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.deleteStudent(null));
    }

    @Test
    public void deleteStudent_studentNotInModelManager_throwsStudentNotFoundException() {
        assertThrows(StudentNotFoundException.class, () -> modelManager.deleteStudent(ALICE));
    }

    @Test
    public void deleteStudent_studentInModelManager_deletesStudent() {
        modelManager.addStudent(ALICE);
        modelManager.addStudent(BENSON);
        modelManager.addModuleClass(CS2103T_TUTORIAL);

        modelManager.deleteStudent(ALICE);
        modelManager.deleteStudent(BENSON);

        ModelManager expectedModelManager = new ModelManager();
        expectedModelManager.addModuleClass(new ModuleClassBuilder(CS2103T_TUTORIAL).withStudentUuids().build());
        assertEquals(expectedModelManager, modelManager);
    }

    @Test
    public void hasModuleClass_nullModuleClass_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.hasModuleClass(null));
    }

    @Test
    public void hasModuleClass_moduleClassNotInModelManager_returnsFalse() {
        assertFalse(modelManager.hasModuleClass(CS2103T_TUTORIAL));
    }

    @Test
    public void hasModuleClass_moduleClassInModelManager_returnsTrue() {
        modelManager.addModuleClass(CS2103T_TUTORIAL);
        assertTrue(modelManager.hasModuleClass(CS2103T_TUTORIAL));
    }

    @Test
    public void deleteModuleClass_nullModuleClass_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.deleteModuleClass(null));
    }

    @Test
    public void deleteModuleClass_moduleClassNotInModelManager_throwsModuleClassNotFoundException() {
        assertThrows(ModuleClassNotFoundException.class, () -> modelManager.deleteModuleClass(CS2103T_TUTORIAL));
    }

    @Test
    public void deleteModuleClass_moduleClassInModelManager_deletesModuleClass() {
        modelManager.addModuleClass(CS2103T_TUTORIAL);
        modelManager.deleteModuleClass(CS2103T_TUTORIAL);
        Model expectedModelManager = new ModelManager();
        assertEquals(expectedModelManager, modelManager);
    }

    @Test
    public void addModuleClass_nullModuleClass_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.addModuleClass(null));
    }

    @Test
    public void addModuleClass_duplicateModuleClassInModelManager_throwsDuplicateModuleClassException() {
        modelManager.addModuleClass(CS2103T_TUTORIAL);
        assertThrows(DuplicateModuleClassException.class, () -> modelManager.addModuleClass(CS2103T_TUTORIAL));
    }

    @Test
    public void addModuleClass_moduleClassWithSameIdentityFieldsInModelManager_throwsDuplicateModuleClassException() {
        modelManager.addModuleClass(CS2103T_TUTORIAL);
        ModuleClass editedCs2103t = new ModuleClassBuilder(CS2103T_TUTORIAL).withStudentUuids(AMY.getUuid()).build();
        assertThrows(DuplicateModuleClassException.class, () -> modelManager.addModuleClass(editedCs2103t));
    }

    @Test
    public void setModuleClass_nullTargetModuleClass_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setModuleClass(null, CS2103T_TUTORIAL));
    }

    @Test
    public void setModuleClass_nullEditedModuleClass_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setModuleClass(CS2103T_TUTORIAL, null));
    }

    @Test
    public void setModuleClass_targetModuleClassNotInModelManager_throwsModuleClassNotFoundException() {
        assertThrows(ModuleClassNotFoundException.class, ()
            -> modelManager.setModuleClass(CS2103T_TUTORIAL, CS2103T_TUTORIAL));
    }

    @Test
    public void getFilteredStudentList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> modelManager.getFilteredStudentList().remove(0));
    }

    @Test
    public void getFilteredModuleClassList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> modelManager.getFilteredModuleClassList().remove(0));
    }

    @Test
    public void deleteAllStudents() {
        modelManager.addStudent(ALICE);
        modelManager.addStudent(BENSON);
        modelManager.addModuleClass(CS2103T_TUTORIAL);
        modelManager.addModuleClass(CS2100_LAB);

        modelManager.deleteAllStudents();

        assertFalse(modelManager.hasStudent(ALICE));
        assertFalse(modelManager.hasStudent(BENSON));
        assertTrue(modelManager.hasModuleClass(new ModuleClass(CS2103T_TUTORIAL.getName())));
        assertTrue(modelManager.hasModuleClass(new ModuleClass(CS2100_LAB.getName())));
    }

    @Test
    public void deleteAllModuleClasses() {
        modelManager.addStudent(ALICE);
        modelManager.addStudent(BENSON);
        modelManager.addModuleClass(CS2103T_TUTORIAL);
        modelManager.addModuleClass(CS2100_LAB);

        modelManager.deleteAllModuleClasses();

        assertTrue(modelManager.hasStudent(ALICE));
        assertTrue(modelManager.hasStudent(BENSON));
        assertFalse(modelManager.hasModuleClass(CS2103T_TUTORIAL));
        assertFalse(modelManager.hasModuleClass(CS2100_LAB));
    }

    @Test
    public void equals() {
        TutorsPet tutorsPet = new TutorsPetBuilder().withStudent(ALICE).withStudent(BENSON)
                .withModuleClass(CS2103T_TUTORIAL).withModuleClass(CS2100_LAB).build();
        TutorsPet differentTutorsPet = new TutorsPet();
        UserPrefs userPrefs = new UserPrefs();

        // same values -> returns true
        modelManager = new ModelManager(tutorsPet, userPrefs);
        ModelManager modelManagerCopy = new ModelManager(tutorsPet, userPrefs);
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertFalse(modelManager.equals(null));

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // different tutorsPet -> returns false
        assertFalse(modelManager.equals(new ModelManager(differentTutorsPet, userPrefs)));

        // different filteredStudentList -> returns false
        String[] keywords = ALICE.getName().fullName.split("\\s+");
        modelManager.updateFilteredStudentList(new NameContainsKeywordsPredicate<>(Arrays.asList(keywords)));
        assertFalse(modelManager.equals(new ModelManager(tutorsPet, userPrefs)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredStudentList(PREDICATE_SHOW_ALL_STUDENTS);

        // different filteredModuleClassList -> returns false
        keywords = CS2103T_TUTORIAL.getName().fullName.split("\\s+");
        modelManager.updateFilteredModuleClassList(new NameContainsKeywordsPredicate<>(Arrays.asList(keywords)));
        assertFalse(modelManager.equals(new ModelManager(tutorsPet, userPrefs)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredModuleClassList(PREDICATE_SHOW_ALL_MODULE_CLASS);

        // different userPrefs -> returns false
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setTutorsPetFilePath(Paths.get("differentFilePath"));
        assertFalse(modelManager.equals(new ModelManager(tutorsPet, differentUserPrefs)));
    }
}
