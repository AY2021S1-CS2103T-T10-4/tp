package tutorspet.testutil;

import tutorspet.model.TutorsPet;
import tutorspet.model.moduleclass.ModuleClass;
import tutorspet.model.student.Student;

/**
 * A utility class to help with building TutorsPet objects.
 * Example usage: <br>
 *     {@code TutorsPet tp = new TutorsPetBuilder().withStudent("John", "Doe").build();}
 */
public class TutorsPetBuilder {

    private TutorsPet tutorsPet;

    public TutorsPetBuilder() {
        tutorsPet = new TutorsPet();
    }

    public TutorsPetBuilder(TutorsPet tutorsPet) {
        this.tutorsPet = tutorsPet;
    }

    /**
     * Adds a new {@code Student} to the {@code TutorsPet} that we are building.
     */
    public TutorsPetBuilder withStudent(Student student) {
        tutorsPet.addStudent(student);
        return this;
    }

    /**
     * Adds a new {@code ModuleClass} to the {@code TutorsPet} that we are building.
     */
    public TutorsPetBuilder withModuleClass(ModuleClass moduleClass) {
        tutorsPet.addModuleClass(moduleClass);
        return this;
    }

    public TutorsPet build() {
        return tutorsPet;
    }
}
