package tutorspet.model.moduleclass;

import static java.util.Objects.requireNonNull;
import static tutorspet.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import tutorspet.model.components.name.HasName;
import tutorspet.model.components.name.Name;
import tutorspet.model.lesson.Lesson;
import tutorspet.model.student.Student;

/**
 * Represents a Class.
 * Contains information on the students enrolled in this class identified by their {@code UUID}.
 * Students must exist in the student manager.
 * Contains {@link Lesson}s (if any).
 * Guarantees: details are present and not null, field values are immutable.
 *
 * @see Student#getUuid()
 */
public class ModuleClass implements HasName {

    // identity fields
    private final Name name;

    // data fields
    private final Set<UUID> studentUuids = new HashSet<>();
    private final List<Lesson> lessons = new ArrayList<>();

    /**
     * Name must be present and not null.
     */
    public ModuleClass(Name name) {
        requireNonNull(name);

        this.name = name;
    }

    /**
     * Every field must be present and not null.
     */
    public ModuleClass(Name name, Set<UUID> studentUuids, List<Lesson> lessons) {
        requireAllNonNull(name, studentUuids, lessons);

        this.name = name;
        this.studentUuids.addAll(studentUuids);
        this.lessons.addAll(lessons);
    }

    @Override
    public Name getName() {
        return name;
    }

    /**
     * Returns an immutable set of student {@code UUID}s, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<UUID> getStudentUuids() {
        return Collections.unmodifiableSet(studentUuids);
    }

    /**
     * Returns an immutable ordered list of {@code lessons}, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public List<Lesson> getLessons() {
        return Collections.unmodifiableList(lessons);
    }

    /**
     * Returns true if the class contains an equivalent student {@code UUID} as the given argument.
     *
     * @throws NullPointerException if the given student {@code UUID} is null.
     */
    public boolean hasStudentUuid(UUID toCheck) throws NullPointerException {
        return studentUuids.stream().anyMatch(toCheck::equals);
    }

    /**
     * Returns true if the class contains the same {@code lesson} as the given argument.
     *
     * @throws NullPointerException if the given {@code lesson} is null.
     */
    public boolean hasLesson(Lesson toCheck) throws NullPointerException {
        return lessons.stream().anyMatch(toCheck::isSameLesson);
    }

    /**
     * Returns true if both classes have the same name.
     * This defines a weaker notion of equality between two classes.
     */
    public boolean isSameModuleClass(ModuleClass otherModuleClass) {
        if (otherModuleClass == this) {
            return true;
        }

        return otherModuleClass != null && otherModuleClass.getName().equals(getName());
    }

    /**
     * Returns true if both classes have the same name, students and lessons.
     * This defines a stronger notion of equality between two classes.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof ModuleClass)) {
            return false;
        }

        ModuleClass otherModuleClass = (ModuleClass) other;
        return otherModuleClass.getName().equals(getName())
                && otherModuleClass.getStudentUuids().equals(getStudentUuids())
                && otherModuleClass.getLessons().equals(getLessons());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, studentUuids, lessons);
    }

    @Override
    public String toString() {
        return getName().toString();
    }
}
