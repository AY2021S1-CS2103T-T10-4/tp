package tutorspet.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tutorspet.testutil.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import tutorspet.logic.commands.exceptions.CommandException;
import tutorspet.model.ReadOnlyTutorsPet;
import tutorspet.model.TutorsPet;
import tutorspet.model.student.Student;
import tutorspet.testutil.ModelStub;
import tutorspet.testutil.StudentBuilder;

public class AddStudentCommandTest {

    @Test
    public void constructor_nullStudent_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddStudentCommand(null));
    }

    @Test
    public void execute_studentAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingStudentAdded modelStub = new ModelStubAcceptingStudentAdded();
        Student validStudent = new StudentBuilder().build();

        CommandResult commandResult = new AddStudentCommand(validStudent).execute(modelStub);

        assertEquals(String.format(AddStudentCommand.MESSAGE_SUCCESS, validStudent), commandResult.getFeedbackToUser());
        assertEquals(Collections.singletonList(validStudent), modelStub.studentsAdded);
        assertEquals(Collections.singletonList(String.format(AddStudentCommand.MESSAGE_SUCCESS, validStudent)),
                modelStub.commitMessages);
    }

    @Test
    public void execute_duplicateStudent_throwsCommandException() {
        Student validStudent = new StudentBuilder().build();
        AddStudentCommand addStudentCommand = new AddStudentCommand(validStudent);
        ModelStubWithStudent modelStub = new ModelStubWithStudent(validStudent);

        assertThrows(CommandException.class, AddStudentCommand.MESSAGE_DUPLICATE_STUDENT, () ->
                addStudentCommand.execute(modelStub));
    }

    @Test
    public void equals() {
        Student alice = new StudentBuilder().withName("Alice").build();
        Student bob = new StudentBuilder().withName("Bob").build();
        AddStudentCommand addAliceCommand = new AddStudentCommand(alice);
        AddStudentCommand addBobCommand = new AddStudentCommand(bob);

        // same object -> returns true
        assertTrue(addAliceCommand.equals(addAliceCommand));

        // same values -> returns true
        AddStudentCommand addAliceCommandCopy = new AddStudentCommand(alice);
        assertTrue(addAliceCommand.equals(addAliceCommandCopy));

        // different types -> returns false
        assertFalse(addAliceCommand.equals(1));

        // null -> returns false
        assertFalse(addAliceCommand.equals(null));

        // different student -> returns false
        assertFalse(addAliceCommand.equals(addBobCommand));
    }

    /**
     * A Model stub that contains a single student.
     */
    private class ModelStubWithStudent extends ModelStub {

        private final Student student;

        ModelStubWithStudent(Student student) {
            requireNonNull(student);

            this.student = student;
        }

        @Override
        public boolean hasStudent(Student student) {
            requireNonNull(student);

            return this.student.isSameStudent(student);
        }
    }

    /**
     * A Model stub that always accepts the student being added.
     */
    private class ModelStubAcceptingStudentAdded extends ModelStub {

        final ArrayList<Student> studentsAdded = new ArrayList<>();
        final ArrayList<String> commitMessages = new ArrayList<>();

        @Override
        public void commit(String commitMessage) {
            commitMessages.add(commitMessage);
        }

        @Override
        public boolean hasStudent(Student student) {
            requireNonNull(student);

            return studentsAdded.stream().anyMatch(student::isSameStudent);
        }

        @Override
        public void addStudent(Student student) {
            requireNonNull(student);

            studentsAdded.add(student);
        }

        @Override
        public ReadOnlyTutorsPet getTutorsPet() {
            return new TutorsPet();
        }
    }
}
