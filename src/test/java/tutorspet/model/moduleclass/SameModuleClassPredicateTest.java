package tutorspet.model.moduleclass;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tutorspet.testutil.TypicalModuleClass.CS2100_LAB;
import static tutorspet.testutil.TypicalModuleClass.CS2100_TUTORIAL;
import static tutorspet.testutil.TypicalStudent.AMY;

import org.junit.jupiter.api.Test;

import tutorspet.testutil.ModuleClassBuilder;

public class SameModuleClassPredicateTest {

    @Test
    public void test_sameModuleClass_returnsTrue() {
        // same object
        SameModuleClassPredicate predicate =
                new SameModuleClassPredicate(CS2100_TUTORIAL);
        assertTrue(predicate.test(CS2100_TUTORIAL));

        // same identity different students
        ModuleClass modifiedCs1101sTutorial = new ModuleClassBuilder(CS2100_TUTORIAL)
                .withStudentUuids(AMY.getUuid()).build();
        assertTrue(predicate.test(modifiedCs1101sTutorial));
    }

    @Test
    public void test_differentModuleClass_returnsFalse() {
        SameModuleClassPredicate predicate =
                new SameModuleClassPredicate(null);
        assertFalse(predicate.test(CS2100_LAB));

        predicate = new SameModuleClassPredicate(CS2100_TUTORIAL);
        assertFalse(predicate.test(CS2100_LAB));
    }

    @Test
    public void equals() {
        ModuleClass firstPredicateModuleClass = CS2100_TUTORIAL;
        ModuleClass secondPredicateModuleClass = CS2100_LAB;

        SameModuleClassPredicate firstPredicate =
                new SameModuleClassPredicate(firstPredicateModuleClass);
        SameModuleClassPredicate secondPredicate =
                new SameModuleClassPredicate(secondPredicateModuleClass);

        // same ModuleClass -> returns true
        SameModuleClassPredicate firstPredicateCopy =
                new SameModuleClassPredicate(firstPredicateModuleClass);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // different type -> returns true
        assertFalse(firstPredicate.equals(5));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different ModuleClass -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }
}
