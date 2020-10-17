package tutorspet.model.moduleclass;

import java.util.function.Predicate;

/**
 * Tests that a {@code ModuleClass}'s identity is the same as the target {@code ModuleClass}.
 */
public class SameModuleClassPredicate implements Predicate<ModuleClass> {

    private ModuleClass target;

    public SameModuleClassPredicate(ModuleClass target) {
        this.target = target;
    }

    @Override
    public boolean test(ModuleClass moduleClass) {
        return moduleClass.isSameModuleClass(target);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof SameModuleClassPredicate // handles nulls
                && target.equals(((SameModuleClassPredicate) other)
                .target)); // state check
    }
}
