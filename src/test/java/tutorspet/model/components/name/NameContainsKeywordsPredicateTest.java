package tutorspet.model.components.name;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

public class NameContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList("first");
        List<String> secondPredicateKeywordList = Arrays.asList("first", "second");

        NameContainsKeywordsPredicate<HasNameStub> firstPredicate =
                new NameContainsKeywordsPredicate<>(firstPredicateKeywordList);
        NameContainsKeywordsPredicate<HasNameStub> secondPredicate =
                new NameContainsKeywordsPredicate<>(secondPredicateKeywordList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        NameContainsKeywordsPredicate<HasNameStub> firstPredicateCopy =
                new NameContainsKeywordsPredicate<>(firstPredicateKeywordList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different predicate -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_nameContainsKeywords_returnsTrue() {
        // one keyword
        NameContainsKeywordsPredicate<HasNameStub> predicate =
                new NameContainsKeywordsPredicate<>(Collections.singletonList(
                "Valid"));
        assertTrue(predicate.test(new HasNameStub()));

        // multiple keywords
        predicate = new NameContainsKeywordsPredicate<>(Arrays.asList("Valid", "Name"));
        assertTrue(predicate.test(new HasNameStub()));

        // only one matching keyword
        predicate = new NameContainsKeywordsPredicate<>(Arrays.asList("Valid", "Invalid"));
        assertTrue(predicate.test(new HasNameStub()));

        // mixed-case keywords
        predicate = new NameContainsKeywordsPredicate<>(Arrays.asList("Invalid", "nAme"));
        assertTrue(predicate.test(new HasNameStub()));
    }

    @Test
    public void test_nameDoesNotContainKeywords_returnsFalse() {
        // zero keywords
        NameContainsKeywordsPredicate<HasNameStub> predicate =
                new NameContainsKeywordsPredicate<>(Collections.emptyList());
        assertFalse(predicate.test(new HasNameStub()));

        // non-matching keyword
        predicate = new NameContainsKeywordsPredicate<>(Arrays.asList("Invalid"));
        assertFalse(predicate.test(new HasNameStub()));
    }

    private static class HasNameStub implements HasName {

        private Name name;

        HasNameStub() {
            name = new Name("Valid Name");
        }

        public Name getName() {
            return name;
        }
    }
}
