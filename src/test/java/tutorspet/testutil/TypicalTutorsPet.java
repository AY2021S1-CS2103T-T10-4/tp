package tutorspet.testutil;

import tutorspet.model.TutorsPet;
import tutorspet.model.moduleclass.ModuleClass;
import tutorspet.model.student.Student;

public class TypicalTutorsPet {

    /**
     * Returns a {@code TutorsPet} with all the typical students and classes.
     */
    public static TutorsPet getTypicalTutorsPet() {
        TutorsPet tp = new TutorsPet();
        for (Student student : TypicalStudent.getTypicalStudents()) {
            tp.addStudent(student);
        }
        for (ModuleClass moduleClass : TypicalModuleClass.getTypicalModuleClasses()) {
            tp.addModuleClass(moduleClass);
        }
        return tp;
    }

    /**
     * Returns a {@code TutorsPet} with no predefined links between typical students and classes.
     */
    public static TutorsPet getNoLinkTutorsPet() {
        TutorsPet tp = new TutorsPet();
        for (Student student : TypicalStudent.getTypicalStudents()) {
            tp.addStudent(student);
        }
        for (ModuleClass moduleClass : TypicalModuleClass.getTypicalModuleClasses()) {
            tp.addModuleClass(new ModuleClassBuilder(moduleClass).withStudentUuids().build());
        }
        return tp;
    }

    /**
     * Returns a {@code TutorsPet} with only typical classes, without students and their uuids.
     */
    public static TutorsPet getOnlyModuleClassTutorsPet() {
        TutorsPet tp = new TutorsPet();
        for (ModuleClass moduleClass : TypicalModuleClass.getTypicalModuleClasses()) {
            tp.addModuleClass(new ModuleClassBuilder(moduleClass).withStudentUuids().build());
        }
        return tp;
    }

    /**
     * Returns a {@code TutorsPet} with only typical students, without classes.
     */
    public static TutorsPet getOnlyStudentsTutorsPet() {
        TutorsPet tp = new TutorsPet();
        for (Student student : TypicalStudent.getTypicalStudents()) {
            tp.addStudent(student);
        }
        return tp;
    }
}
