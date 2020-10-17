package tutorspet.testutil;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import tutorspet.model.components.name.Name;
import tutorspet.model.lesson.Lesson;
import tutorspet.model.moduleclass.ModuleClass;

/**
 * A utility class to help with building ModuleClass objects.
 */
public class ModuleClassBuilder {

    public static final String DEFAULT_NAME = "CS1101 Tutorial";

    private Name name;
    private Set<UUID> studentUuids;
    private List<Lesson> lessons;

    /**
     * Creates a {@code ModuleClassBuilder} with the default {@code Name} and
     * empty {@code studentUuids} and {@code lessons} list.
     */
    public ModuleClassBuilder() {
        name = new Name(DEFAULT_NAME);
        studentUuids = Collections.emptySet();
        lessons = Collections.emptyList();
    }

    /**
     * Initializes the ModuleClassBuilder with the data of {@code moduleClassToCopy}.
     */
    public ModuleClassBuilder(ModuleClass moduleClassToCopy) {
        name = moduleClassToCopy.getName();
        studentUuids = new HashSet<>(moduleClassToCopy.getStudentUuids());
        lessons = moduleClassToCopy.getLessons();
    }

    /**
     * Sets the {@code Name} of the {@code ModuleClass} that we are building.
     */
    public ModuleClassBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Sets the {@code Set<UUID>} of the {@code ModuleClass} that we are building.
     * Overwrites any previous content of the {@code Set<UUID>}.
     */
    public ModuleClassBuilder withStudentUuids(UUID... studentUuids) {
        this.studentUuids = new HashSet<>(Arrays.asList(studentUuids));
        return this;
    }

    /**
     * Sets the {@code List<Lesson>} of the {@code ModuleClass} that we are building.
     * Overwrites any previous content of the {@code List<Lesson>}.
     */
    public ModuleClassBuilder withLessons(Lesson... lessons) {
        this.lessons = Arrays.asList(lessons);
        return this;
    }

    public ModuleClass build() {
        return new ModuleClass(name, studentUuids, lessons);
    }
}
