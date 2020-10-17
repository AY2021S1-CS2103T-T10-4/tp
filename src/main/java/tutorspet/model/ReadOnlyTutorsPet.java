package tutorspet.model;

import javafx.collections.ObservableList;
import tutorspet.model.moduleclass.ModuleClass;
import tutorspet.model.student.Student;

/**
 * Unmodifiable view of Tutor's Pet
 */
public interface ReadOnlyTutorsPet {

    /**
     * Returns an unmodifiable view of the students list.
     * This list will not contain any duplicate students.
     */
    ObservableList<Student> getStudentList();

    /**
     * Returns an unmodifiable view of the ModuleClass list.
     * This list will not contain any duplicate ModuleClasses.
     */
    ObservableList<ModuleClass> getModuleClassList();
}
