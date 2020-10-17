package tutorspet.storage;

import static tutorspet.storage.JsonAdaptedStudent.STUDENT_UUID_FIELD;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import tutorspet.commons.exceptions.IllegalValueException;
import tutorspet.model.components.name.Name;
import tutorspet.model.lesson.Lesson;
import tutorspet.model.moduleclass.ModuleClass;

/**
 * Jackson-friendly version of {@link ModuleClass}.
 */
public class JsonAdaptedModuleClass {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Class' %s field is missing!";
    public static final String MESSAGE_INVALID_STUDENTS_IN_LESSON = "Invalid student(s) found in lesson(s).";
    public static final String INVALID_FIELD_MESSAGE_FORMAT = "Class' %s field is invalid!";
    public static final String DUPLICATE_LESSON_MESSAGE_FORMAT = "%s contains duplicate lesson(s).";

    private final JsonAdaptedName name;
    private final List<JsonAdaptedUuid> studentUuids = new ArrayList<>();
    private final List<JsonAdaptedLesson> lessons = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedModuleClass} with the given class details.
     */
    @JsonCreator
    public JsonAdaptedModuleClass(@JsonProperty("name") JsonAdaptedName name,
                                  @JsonProperty("studentUuids") List<JsonAdaptedUuid> studentUuids,
                                  @JsonProperty("lessons") List<JsonAdaptedLesson> lessons) {
        this.name = name;
        if (studentUuids != null) {
            this.studentUuids.addAll(studentUuids);
        }
        if (lessons != null) {
            this.lessons.addAll(lessons);
        }
    }

    /**
     * Converts a given {@code ModuleClass} into a {@code JsonAdaptedModuleClass} for Jackson use.
     */
    public JsonAdaptedModuleClass(ModuleClass source) {
        name = new JsonAdaptedName(source.getName().fullName);
        studentUuids.addAll(source.getStudentUuids().stream()
               .map(JsonAdaptedUuid::new)
               .collect(Collectors.toList()));
        lessons.addAll(source.getLessons().stream()
                .map(JsonAdaptedLesson::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts the contained {@code List<JsonAdaptedUuid> studentUuids} to a {@code List<UUID>}.
     *
     * @throws IllegalValueException if any of the {@code UUID}s are null or invalid.
     */
    private List<UUID> getUuidList() throws IllegalValueException {
        List<UUID> uuidList = new ArrayList<>();
        for (JsonAdaptedUuid studentUuid : studentUuids) {
            if (studentUuid == null) {
                throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, STUDENT_UUID_FIELD));
            }

            // catch invalid UUID
            try {
                String uuidString = studentUuid.getUuidString();
                UUID.fromString(uuidString);
            } catch (IllegalArgumentException e) {
                throw new IllegalValueException(String.format(INVALID_FIELD_MESSAGE_FORMAT, STUDENT_UUID_FIELD));
            }
            uuidList.add(studentUuid.toModelType());
        }
        return uuidList;
    }

    /**
     * Checks if a {@code lesson} already exists in a list.
     * Duplicates are detected by calling {@code isSameLesson} method in {@code Lesson}.
     * Returns true if there is a duplicate.
     */
    private boolean hasDuplicateLessons(List<Lesson> lessons, Lesson lessonToCheck) {
        return lessons.stream().anyMatch(lessonToCheck::isSameLesson);
    }

    /**
     * Converts the contained {@code List<JsonAdaptedLesson> lessons} to a {@code List<Lesson>}.
     *
     * @throws IllegalValueException if any of the {@code Lesson}s are null or duplicate.
     */
    private List<Lesson> getLessonList() throws IllegalValueException {
        List<Lesson> lessonList = new ArrayList<>();
        for (JsonAdaptedLesson jsonLesson : lessons) {
            if (jsonLesson == null) {
                throw new IllegalValueException(
                        String.format(MISSING_FIELD_MESSAGE_FORMAT, Lesson.class.getSimpleName()));
            }

            if (hasDuplicateLessons(lessonList, jsonLesson.toModelType())) {
                throw new IllegalValueException(String.format(DUPLICATE_LESSON_MESSAGE_FORMAT, name));
            } else {
                lessonList.add(jsonLesson.toModelType());
            }
        }
        return lessonList;
    }

    private void validateAttendanceRecords(List<Lesson> lessonList, Set<UUID> studentUuids)
            throws IllegalValueException {
        Set<UUID> students = lessonList.stream().flatMap(lesson ->
                lesson.getAttendanceRecordList().getAttendanceRecordList().stream()
                        .flatMap(record -> record.getAttendanceRecord().keySet().stream()))
                .collect(Collectors.toUnmodifiableSet());
        for (UUID studentUuid : students) {
            if (!studentUuids.contains(studentUuid)) {
                throw new IllegalValueException(MESSAGE_INVALID_STUDENTS_IN_LESSON);
            }
        }
    }

    /**
     * Converts this Jackson-friendly adapted class object into the model's {@code ModuleClass} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted class.
     */
    public ModuleClass toModelType() throws IllegalValueException {
        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }

        final Name modelName = name.toModelType();

        List<UUID> studentUuids = getUuidList();
        final Set<UUID> studentUuidSet = new HashSet<>(studentUuids);

        final List<Lesson> lessonList = getLessonList();

        validateAttendanceRecords(lessonList, studentUuidSet);

        return new ModuleClass(modelName, studentUuidSet, lessonList);
    }
}
