package seedu.address.model;

import javafx.collections.ObservableList;
import seedu.address.model.student.Student;

/**
 * Unmodifiable view of Tutor's Pet
 */
public interface ReadOnlyTutorsPet {

    /**
     * Returns an unmodifiable view of the students list.
     * This list will not contain any duplicate students.
     */
    ObservableList<Student> getStudentList();

}
