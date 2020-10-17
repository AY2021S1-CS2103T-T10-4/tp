package tutorspet.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import javafx.collections.ObservableList;
import tutorspet.commons.exceptions.IllegalValueException;
import tutorspet.model.ReadOnlyTutorsPet;
import tutorspet.model.TutorsPet;
import tutorspet.model.moduleclass.ModuleClass;
import tutorspet.model.student.Student;

/**
 * An Immutable TutorsPet that is serializable to JSON format.
 */
@JsonRootName(value = "tutorspet")
class JsonSerializableTutorsPet {

    public static final String MESSAGE_DUPLICATE_STUDENT = "Students list contains duplicate student(s).";
    public static final String MESSAGE_DUPLICATE_MODULE_CLASS = "Class list contains duplicate class(es).";
    public static final String MESSAGE_INVALID_STUDENTS_IN_CLASS = "Invalid student(s) found in class(es).";

    private final List<JsonAdaptedStudent> students = new ArrayList<>();
    private final List<JsonAdaptedModuleClass> classes = new ArrayList<>();

    /**
     * Constructs a {@code JsonSerializableTutorsPet} with the given students and classes.
     */
    @JsonCreator
    public JsonSerializableTutorsPet(@JsonProperty("students") List<JsonAdaptedStudent> students,
                                     @JsonProperty("classes") List<JsonAdaptedModuleClass> classes) {
        this.students.addAll(students);
        this.classes.addAll(classes);
    }

    /**
     * Converts a given {@code ReadOnlyTutorsPet} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableTutorsPet}.
     */
    public JsonSerializableTutorsPet(ReadOnlyTutorsPet source) {
        students.addAll(source.getStudentList().stream().map(JsonAdaptedStudent::new).collect(Collectors.toList()));
        classes.addAll(source.getModuleClassList().stream()
                .map(JsonAdaptedModuleClass::new).collect(Collectors.toList()));
    }

    /**
     * Converts students into the model's {@code TutorsPet} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    private void studentsToModelType(TutorsPet tutorsPet) throws IllegalValueException {
        for (JsonAdaptedStudent jsonAdaptedStudent : students) {
            Student student = jsonAdaptedStudent.toModelType();
            if (tutorsPet.hasStudent(student) || tutorsPet.hasStudentUuid(student)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_STUDENT);
            }
            tutorsPet.addStudent(student);
        }
    }

    /**
     * Converts classes into the model's {@code TutorsPet} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    private void classesToModelType(TutorsPet tutorsPet) throws IllegalValueException {
        // Get all UUIDs under "students" field in tutorspet.json.
        ObservableList<Student> students = tutorsPet.getStudentList();
        Set<UUID> uniqueStudentUuids = new HashSet<>();
        for (Student student : students) {
            uniqueStudentUuids.add(student.getUuid());
        }

        for (JsonAdaptedModuleClass jsonAdaptedModuleClass : classes) {
            ModuleClass moduleClass = jsonAdaptedModuleClass.toModelType();
            if (tutorsPet.hasModuleClass(moduleClass)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_MODULE_CLASS);
            }

            // Check that the set of student UUIDs within a class is a subset of
            // all student UUIDs in uniqueStudentUuids. Otherwise, Tutor's Pet will not
            // boot up due to data corruption.
            Set<UUID> moduleClassStudentUuids = moduleClass.getStudentUuids();
            if (!uniqueStudentUuids.containsAll(moduleClassStudentUuids)) {
                throw new IllegalValueException(MESSAGE_INVALID_STUDENTS_IN_CLASS);
            }

            tutorsPet.addModuleClass(moduleClass);
        }
    }

    /**
     * Converts this Tutor's Pet into the model's {@code TutorsPet} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public TutorsPet toModelType() throws IllegalValueException {
        TutorsPet tutorsPet = new TutorsPet();
        studentsToModelType(tutorsPet);
        classesToModelType(tutorsPet);
        return tutorsPet;
    }
}
