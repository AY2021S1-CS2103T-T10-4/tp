package tutorspet.model.student;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tutorspet.testutil.TypicalStudent.ALICE;
import static tutorspet.testutil.TypicalStudent.BENSON;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;

class StudentInUuidCollectionPredicateTest {

    @Test
    public void test_studentInCollection_returnsTrue() {
        // one student
        StudentInUuidCollectionPredicate predicate =
                new StudentInUuidCollectionPredicate(Collections.singleton(ALICE.getUuid()));
        assertTrue(predicate.test(ALICE));

        // multiple students
        predicate = new StudentInUuidCollectionPredicate(Arrays.asList(ALICE.getUuid(), BENSON.getUuid()));
        assertTrue(predicate.test(ALICE));

        // duplicate students
        predicate = new StudentInUuidCollectionPredicate(Arrays.asList(ALICE.getUuid(), ALICE.getUuid()));
        assertTrue(predicate.test(ALICE));
    }

    @Test
    public void test_studentNotInCollection_returnsFalse() {
        // zero students
        StudentInUuidCollectionPredicate predicate =
                new StudentInUuidCollectionPredicate(Collections.emptyList());
        assertFalse(predicate.test(ALICE));

        // non-matching student
        predicate = new StudentInUuidCollectionPredicate(Collections.singleton(BENSON.getUuid()));
        assertFalse(predicate.test(ALICE));
    }

    @Test
    public void equals() {
        List<UUID> firstPredicateStudentUuidList = Collections.singletonList(ALICE.getUuid());
        List<UUID> secondPredicateStudentUuidList = Arrays.asList(ALICE.getUuid(), BENSON.getUuid());
        Set<UUID> secondPredicateStudentUuidSet = new HashSet<>(secondPredicateStudentUuidList);
        List<UUID> thirdPredicateStudentUuidList = Arrays.asList(ALICE.getUuid(), ALICE.getUuid());

        StudentInUuidCollectionPredicate firstPredicate =
                new StudentInUuidCollectionPredicate(firstPredicateStudentUuidList);
        StudentInUuidCollectionPredicate secondPredicateListVersion =
                new StudentInUuidCollectionPredicate(secondPredicateStudentUuidList);
        StudentInUuidCollectionPredicate secondPredicateSetVersion =
                new StudentInUuidCollectionPredicate(secondPredicateStudentUuidSet);
        StudentInUuidCollectionPredicate thirdPredicate =
                new StudentInUuidCollectionPredicate(thirdPredicateStudentUuidList);

        // same students -> returns true
        StudentInUuidCollectionPredicate firstPredicateCopy =
                new StudentInUuidCollectionPredicate(firstPredicateStudentUuidList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same students different collection type -> returns true
        assertTrue(secondPredicateListVersion.equals(secondPredicateSetVersion));

        // duplicate students -> return true
        assertTrue(firstPredicate.equals(thirdPredicate));

        // different types -> returns false
        assertFalse(firstPredicate.equals(5));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different students -> returns false
        assertFalse(firstPredicate.equals(secondPredicateListVersion));
    }
}
