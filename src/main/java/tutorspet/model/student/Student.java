package tutorspet.model.student;

import static tutorspet.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import tutorspet.model.components.name.HasName;
import tutorspet.model.components.name.Name;
import tutorspet.model.components.tag.Tag;

/**
 * Represents a Student.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Student implements HasName {

    // identity fields
    private final UUID uuid;
    private final Name name;
    private final Telegram telegram;
    private final Email email;

    // data fields
    private final Set<Tag> tags = new HashSet<>();

    /**
     * Every field must be present and not null.
     * Creates a new student with a randomly generated UUID.
     */
    public Student(Name name, Telegram telegram, Email email, Set<Tag> tags) {
        requireAllNonNull(name, telegram, email, tags);

        this.uuid = UUID.randomUUID();
        this.name = name;
        this.telegram = telegram;
        this.email = email;
        this.tags.addAll(tags);
    }

    /**
     * Overloads student constructor so that we can return a new student
     * with the same UUID to ensure object immutability.
     */
    public Student(UUID uuid, Name name, Telegram telegram, Email email, Set<Tag> tags) {
        requireAllNonNull(uuid, name, telegram, email, tags);

        this.uuid = uuid;
        this.name = name;
        this.telegram = telegram;
        this.email = email;
        this.tags.addAll(tags);
    }

    public UUID getUuid() {
        return uuid;
    }

    @Override
    public Name getName() {
        return name;
    }

    public Telegram getTelegram() {
        return telegram;
    }

    public Email getEmail() {
        return email;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns true if both students of the same name have at least one other identity field that is the same.
     * This defines a weaker notion of equality between two students.
     */
    public boolean isSameStudent(Student otherStudent) {
        if (otherStudent == this) {
            return true;
        }

        return otherStudent != null
                && otherStudent.getName().equals(getName())
                && (otherStudent.getTelegram().equals(getTelegram()) || otherStudent.getEmail().equals(getEmail()));
    }

    /**
     * Returns true if both students have the same UUID.
     */
    public boolean hasSameUuid(Student otherStudent) {
        return otherStudent != null
                && otherStudent.getUuid().equals(getUuid());
    }

    /**
     * Returns true if both students have the same identity and data fields.
     * This defines a stronger notion of equality between two students.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Student)) {
            return false;
        }

        Student otherStudent = (Student) other;
        return otherStudent.getName().equals(getName())
                && otherStudent.getTelegram().equals(getTelegram())
                && otherStudent.getEmail().equals(getEmail())
                && otherStudent.getTags().equals(getTags());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(uuid, name, telegram, email, tags);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getName())
                .append(" Telegram: ")
                .append(getTelegram())
                .append(" Email: ")
                .append(getEmail())
                .append(" Tags: ");
        getTags().forEach(builder::append);
        return builder.toString();
    }
}
