package tutorspet.testutil;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import tutorspet.model.components.name.Name;
import tutorspet.model.components.tag.Tag;
import tutorspet.model.student.Email;
import tutorspet.model.student.Student;
import tutorspet.model.student.Telegram;
import tutorspet.model.util.SampleDataUtil;

/**
 * A utility class to help with building Student objects.
 */
public class StudentBuilder {

    public static final String DEFAULT_UUID = "edbf256e-ea32-4853-8591-5df758873b11";
    public static final String DEFAULT_NAME = "Generated Student";
    public static final String DEFAULT_TELEGRAM = "generated_student";
    public static final String DEFAULT_EMAIL = "generated_student@gmail.com";

    private UUID uuid;
    private Name name;
    private Telegram telegram;
    private Email email;
    private Set<Tag> tags;

    /**
     * Creates a {@code StudentBuilder} with the default details.
     */
    public StudentBuilder() {
        uuid = UUID.fromString(DEFAULT_UUID);
        name = new Name(DEFAULT_NAME);
        telegram = new Telegram(DEFAULT_TELEGRAM);
        email = new Email(DEFAULT_EMAIL);
        tags = Collections.emptySet();
    }

    /**
     * Initializes the StudentBuilder with the data of {@code studentToCopy}.
     */
    public StudentBuilder(Student studentToCopy) {
        uuid = studentToCopy.getUuid();
        name = studentToCopy.getName();
        telegram = studentToCopy.getTelegram();
        email = studentToCopy.getEmail();
        tags = new HashSet<>(studentToCopy.getTags());
    }

    /**
     * Sets the {@code UUID} of the {@code Student} that we are building.
     */
    public StudentBuilder withUuid(String uuid) {
        this.uuid = UUID.fromString(uuid);
        return this;
    }

    /**
     * Sets the {@code Name} of the {@code Student} that we are building.
     */
    public StudentBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Student} that we are building.
     */
    public StudentBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the {@code Telegram} of the {@code Student} that we are building.
     */
    public StudentBuilder withTelegram(String telegram) {
        this.telegram = new Telegram(telegram);
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code Student} that we are building.
     */
    public StudentBuilder withEmail(String email) {
        this.email = new Email(email);
        return this;
    }

    public Student build() {
        return new Student(uuid, name, telegram, email, tags);
    }
}
