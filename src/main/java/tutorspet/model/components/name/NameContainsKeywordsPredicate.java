package tutorspet.model.components.name;

import java.util.List;
import java.util.function.Predicate;

import tutorspet.commons.util.StringUtil;

/**
 * Tests that a {@code HasName} object's {@code Name} matches any of the keywords given.
 */
public class NameContainsKeywordsPredicate<T extends HasName> implements Predicate<T> {

    private final List<String> keywords;

    public NameContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(T t) {
        return keywords.stream()
                .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(t.getName().fullName, keyword));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof NameContainsKeywordsPredicate // instanceOf handles null
            && keywords.equals(((NameContainsKeywordsPredicate<?>) other)
                .keywords)); // state check
    }
}
