package tutorspet.model.student;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

/**
 * Tests that a {@code Student}'s {@code UUID} is in the given collection.
 */
public class StudentInUuidCollectionPredicate implements Predicate<Student> {

    private final Collection<UUID> studentUuids;

    public StudentInUuidCollectionPredicate(Collection<UUID> studentUuids) {
        this.studentUuids = studentUuids;
    }

    @Override
    public boolean test(Student student) {
        return studentUuids.stream().anyMatch(uuid -> student.getUuid().equals(uuid));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof StudentInUuidCollectionPredicate)) {
            return false;
        }

        // We can ignore duplicate UUIDs provided in a list as a given student
        // either matches none or all.
        Set<UUID> uuids = new HashSet<>(studentUuids);
        Set<UUID> otherUuids = new HashSet<>(((StudentInUuidCollectionPredicate) other).studentUuids);

        return uuids.equals(otherUuids);
    }
}
