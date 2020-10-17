package tutorspet.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tutorspet.logic.commands.CommandTestUtil.VALID_TAG_AVERAGE;
import static tutorspet.testutil.Assert.assertThrows;
import static tutorspet.testutil.TypicalModuleClass.CS2100_LAB;
import static tutorspet.testutil.TypicalModuleClass.CS2103T_TUTORIAL;
import static tutorspet.testutil.TypicalStudent.ALICE;
import static tutorspet.testutil.TypicalStudent.AMY;
import static tutorspet.testutil.TypicalStudent.BENSON;
import static tutorspet.testutil.TypicalStudent.GEORGE;
import static tutorspet.testutil.TypicalTutorsPet.getTypicalTutorsPet;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import tutorspet.model.moduleclass.ModuleClass;
import tutorspet.model.moduleclass.exceptions.DuplicateModuleClassException;
import tutorspet.model.moduleclass.exceptions.ModuleClassNotFoundException;
import tutorspet.model.student.Student;
import tutorspet.model.student.exceptions.DuplicateStudentException;
import tutorspet.testutil.ModuleClassBuilder;
import tutorspet.testutil.StudentBuilder;
import tutorspet.testutil.TutorsPetBuilder;
import tutorspet.testutil.TypicalModuleClass;
import tutorspet.testutil.TypicalStudent;

public class TutorsPetTest {

    private final TutorsPet tutorsPet = new TutorsPet();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), tutorsPet.getStudentList());
        assertEquals(Collections.emptyList(), tutorsPet.getModuleClassList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> tutorsPet.resetData(null));
    }

    @Test
    public void resetData_withValidReadOnlyTutorsPet_replacesData() {
        TutorsPet newData = getTypicalTutorsPet();
        tutorsPet.resetData(newData);
        assertEquals(newData, tutorsPet);
    }

    //// student-related tests

    @Test
    public void resetData_withDuplicateStudents_throwsDuplicateStudentException() {
        // Two students with the same identity fields
        Student editedAlice = new StudentBuilder(ALICE).withTags(VALID_TAG_AVERAGE)
                .build();
        List<Student> newStudents = Arrays.asList(ALICE, editedAlice);
        List<ModuleClass> defaultModuleClasses = TypicalModuleClass.getTypicalModuleClasses();
        TutorsPetStub newData = new TutorsPetStub(newStudents, defaultModuleClasses);

        assertThrows(DuplicateStudentException.class, () -> tutorsPet.resetData(newData));
    }

    @Test
    public void hasStudent_nullStudent_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> tutorsPet.hasStudent(null));
    }

    @Test
    public void hasStudent_studentNotInTutorsPet_returnsFalse() {
        assertFalse(tutorsPet.hasStudent(ALICE));
    }

    @Test
    public void hasStudent_studentInTutorsPet_returnsTrue() {
        tutorsPet.addStudent(ALICE);
        assertTrue(tutorsPet.hasStudent(ALICE));
    }

    @Test
    public void hasStudent_studentWithSameIdentityFieldsInTutorsPet_returnsTrue() {
        tutorsPet.addStudent(ALICE);
        Student editedAlice = new StudentBuilder(ALICE).withTags(VALID_TAG_AVERAGE)
                .build();
        assertTrue(tutorsPet.hasStudent(editedAlice));
    }

    @Test
    public void getStudentList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> tutorsPet.getStudentList().remove(0));
    }

    @Test
    public void deleteStudent_nullStudent_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> tutorsPet.deleteStudent(null));
    }

    @Test
    public void deleteStudent_studentInTutorsPet_removesStudentAndUuid() {
        tutorsPet.addStudent(ALICE);
        tutorsPet.addStudent(BENSON);
        tutorsPet.addModuleClass(CS2103T_TUTORIAL);

        tutorsPet.deleteStudent(ALICE);

        // manually remove UUID
        Set<UUID> modifiedUuids = new HashSet<>(CS2103T_TUTORIAL.getStudentUuids());
        modifiedUuids.remove(ALICE.getUuid());
        ModuleClass modifiedTutorial = new ModuleClass(CS2103T_TUTORIAL.getName(), modifiedUuids,
                CS2103T_TUTORIAL.getLessons());

        TutorsPet expectedTutorsPet =
                new TutorsPetBuilder().withStudent(BENSON).withModuleClass(modifiedTutorial).build();
        assertEquals(expectedTutorsPet, tutorsPet);
    }

    @Test
    public void deleteAllStudents() {
        tutorsPet.addStudent(ALICE);
        tutorsPet.addStudent(BENSON);
        tutorsPet.addModuleClass(CS2103T_TUTORIAL);
        tutorsPet.addModuleClass(CS2100_LAB);

        tutorsPet.deleteAllStudents();

        assertFalse(tutorsPet.hasStudent(ALICE));
        assertFalse(tutorsPet.hasStudent(BENSON));
        assertTrue(tutorsPet.hasModuleClass(new ModuleClass(CS2103T_TUTORIAL.getName())));
        assertTrue(tutorsPet.hasModuleClass(new ModuleClass(CS2100_LAB.getName())));
    }

    //// moduleClass-related tests

    @Test
    public void resetData_withDuplicateModuleClasses_throwsDuplicateModuleClassException() {
        // Two moduleClasses with the same identity fields
        ModuleClass editedCS2103T = new ModuleClassBuilder(CS2103T_TUTORIAL).withStudentUuids().build();
        List<Student> defaultStudents = TypicalStudent.getTypicalStudents();
        List<ModuleClass> newModuleClasses = Arrays.asList(CS2103T_TUTORIAL, editedCS2103T);
        TutorsPetStub newData = new TutorsPetStub(defaultStudents, newModuleClasses);

        assertThrows(DuplicateModuleClassException.class, () -> tutorsPet.resetData(newData));
    }

    @Test
    public void hasModuleClass_nullModuleClass_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> tutorsPet.hasModuleClass(null));
    }

    @Test
    public void hasModuleClass_moduleClassNotInTutorsPet_returnsFalse() {
        assertFalse(tutorsPet.hasModuleClass(CS2103T_TUTORIAL));
    }

    @Test
    public void hasModuleClass_moduleClassInTutorsPet_returnsTrue() {
        tutorsPet.addModuleClass(CS2103T_TUTORIAL);
        assertTrue(tutorsPet.hasModuleClass(CS2103T_TUTORIAL));
    }

    @Test
    public void hasModuleClass_moduleClassWithSameIdentityFieldsInTutorsPet_returnsTrue() {
        tutorsPet.addModuleClass(CS2103T_TUTORIAL);
        ModuleClass editedModuleClass = new ModuleClassBuilder(CS2103T_TUTORIAL).withStudentUuids().build();
        assertTrue(tutorsPet.hasModuleClass(editedModuleClass));
    }

    @Test
    public void addModuleClass_nullModuleClass_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> tutorsPet.addModuleClass(null));
    }

    @Test
    public void addModuleClass_duplicateModuleClassInTutorsPet_throwsDuplicateModuleClassException() {
        tutorsPet.addModuleClass(CS2103T_TUTORIAL);
        assertThrows(DuplicateModuleClassException.class, () -> tutorsPet.addModuleClass(CS2103T_TUTORIAL));
    }

    @Test
    public void addModuleClass_moduleClassWithSameIdentityFieldsInTutorsPet_throwsDuplicateModuleClassException() {
        tutorsPet.addModuleClass(CS2103T_TUTORIAL);
        ModuleClass editedCs2103t = new ModuleClassBuilder(CS2103T_TUTORIAL).withStudentUuids(AMY.getUuid()).build();
        assertThrows(DuplicateModuleClassException.class, () -> tutorsPet.addModuleClass(editedCs2103t));
    }

    @Test
    public void setModuleClass_nullTargetModuleClass_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> tutorsPet.setModuleClass(null, CS2103T_TUTORIAL));
    }

    @Test
    public void setModuleClass_nullEditedModuleClass_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> tutorsPet.setModuleClass(CS2103T_TUTORIAL, null));
    }

    @Test
    public void setModuleClass_targetModuleClassNotInTutorsPet_throwsModuleClassNotFoundException() {
        assertThrows(ModuleClassNotFoundException.class, ()
            -> tutorsPet.setModuleClass(CS2103T_TUTORIAL, CS2103T_TUTORIAL));
    }

    @Test
    public void deleteModuleClass_nullModuleClass_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> tutorsPet.deleteModuleClass(null));
    }

    @Test
    public void deleteModuleClass_moduleClassNotInTutorsPet_throwsModuleClassNotFoundException() {
        assertThrows(ModuleClassNotFoundException.class, () -> tutorsPet.deleteModuleClass(CS2103T_TUTORIAL));
    }

    @Test
    public void deleteModuleClass_moduleClassInTutorsPet_deletesModuleClass() {
        tutorsPet.addModuleClass(CS2103T_TUTORIAL);
        tutorsPet.deleteModuleClass(CS2103T_TUTORIAL);
        TutorsPet expectedTutorsPet = new TutorsPet();
        assertEquals(expectedTutorsPet, tutorsPet);
    }

    @Test
    public void getModuleClassList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> tutorsPet.getModuleClassList().remove(0));
    }

    @Test
    public void deleteAllModuleClasses() {
        tutorsPet.addStudent(ALICE);
        tutorsPet.addStudent(BENSON);
        tutorsPet.addModuleClass(CS2103T_TUTORIAL);
        tutorsPet.addModuleClass(CS2100_LAB);

        tutorsPet.deleteAllModuleClasses();

        assertTrue(tutorsPet.hasStudent(ALICE));
        assertTrue(tutorsPet.hasStudent(BENSON));
        assertFalse(tutorsPet.hasModuleClass(CS2103T_TUTORIAL));
        assertFalse(tutorsPet.hasModuleClass(CS2100_LAB));
    }

    @Test
    public void equals() {
        tutorsPet.resetData(getTypicalTutorsPet());

        // same students and classes -> returns true
        TutorsPet tutorsPetCopy = new TutorsPet();
        tutorsPetCopy.resetData(getTypicalTutorsPet());
        assertTrue(tutorsPet.equals(tutorsPetCopy));

        // same object -> returns true
        assertTrue(tutorsPet.equals(tutorsPet));

        // null -> returns false
        assertFalse(tutorsPet.equals(null));

        // different type -> returns false
        assertFalse(tutorsPet.equals(5));

        // different students -> returns false
        TutorsPet tutorsPetDifferentStudents = new TutorsPet();
        tutorsPetDifferentStudents.resetData(getTypicalTutorsPet());
        tutorsPetDifferentStudents.deleteStudent(GEORGE); // GEORGE is not linked to any ModuleClasses.
        assertTrue(tutorsPet.getModuleClassList().equals(tutorsPetDifferentStudents.getModuleClassList()));
        assertFalse(tutorsPet.equals(tutorsPetDifferentStudents));

        // different classes -> returns false
        TutorsPet tutorsPetDifferentClasses = new TutorsPet();
        tutorsPetDifferentClasses.resetData(getTypicalTutorsPet());
        ModuleClass toDeleteClass = tutorsPetDifferentClasses.getModuleClassList().get(0);
        tutorsPetDifferentClasses.deleteModuleClass(toDeleteClass);
        assertTrue(tutorsPet.getStudentList().equals(tutorsPetDifferentClasses.getStudentList()));
        assertFalse(tutorsPet.equals(tutorsPetDifferentClasses));
    }

    /**
     * A stub ReadOnlyTutorsPet whose students list can violate interface constraints.
     */
    private static class TutorsPetStub implements ReadOnlyTutorsPet {

        private final ObservableList<Student> students = FXCollections.observableArrayList();
        private final ObservableList<ModuleClass> classes = FXCollections.observableArrayList();

        TutorsPetStub(Collection<Student> students, Collection<ModuleClass> classes) {
            this.students.setAll(students);
            this.classes.setAll(classes);
        }

        @Override
        public ObservableList<Student> getStudentList() {
            return students;
        }

        @Override
        public ObservableList<ModuleClass> getModuleClassList() {
            return classes;
        }
    }
}
