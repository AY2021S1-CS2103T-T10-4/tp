package tutorspet.model.student;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tutorspet.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static tutorspet.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static tutorspet.logic.commands.CommandTestUtil.VALID_TAG_AVERAGE;
import static tutorspet.logic.commands.CommandTestUtil.VALID_TELEGRAM_BOB;
import static tutorspet.testutil.Assert.assertThrows;
import static tutorspet.testutil.TypicalStudent.ALICE;
import static tutorspet.testutil.TypicalStudent.BOB;

import org.junit.jupiter.api.Test;

import tutorspet.testutil.StudentBuilder;

public class StudentTest {

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Student student = new StudentBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> student.getTags().remove(0));
    }

    @Test
    public void isSameStudent() {
        // same object -> returns true
        assertTrue(ALICE.isSameStudent(ALICE));

        // null -> returns false
        assertFalse(ALICE.isSameStudent(null));

        // different telegram and email -> returns false
        Student editedAlice = new StudentBuilder(ALICE).withTelegram(VALID_TELEGRAM_BOB)
                .withEmail(VALID_EMAIL_BOB).build();
        assertFalse(ALICE.isSameStudent(editedAlice));

        // different name -> returns false
        editedAlice = new StudentBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.isSameStudent(editedAlice));

        // same name, same telegram, different attributes -> returns true
        editedAlice = new StudentBuilder(ALICE).withEmail(VALID_EMAIL_BOB)
                .withTags(VALID_TAG_AVERAGE).build();
        assertTrue(ALICE.isSameStudent(editedAlice));

        // same name, same email, different attributes -> returns true
        editedAlice = new StudentBuilder(ALICE).withTelegram(VALID_TELEGRAM_BOB)
                .withTags(VALID_TAG_AVERAGE).build();
        assertTrue(ALICE.isSameStudent(editedAlice));

        // same name, same telegram, same email, different attributes -> returns true
        editedAlice = new StudentBuilder(ALICE).withTags(VALID_TAG_AVERAGE).build();
        assertTrue(ALICE.isSameStudent(editedAlice));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Student aliceCopy = new StudentBuilder(ALICE).build();
        assertTrue(ALICE.equals(aliceCopy));

        // same object -> returns true
        assertTrue(ALICE.equals(ALICE));

        // null -> returns false
        assertFalse(ALICE.equals(null));

        // different type -> returns false
        assertFalse(ALICE.equals(5));

        // different student -> returns false
        assertFalse(ALICE.equals(BOB));

        // different name -> returns false
        Student editedAlice = new StudentBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different telegram -> returns false
        editedAlice = new StudentBuilder(ALICE).withTelegram(VALID_TELEGRAM_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different email -> returns false
        editedAlice = new StudentBuilder(ALICE).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different tags -> returns false
        editedAlice = new StudentBuilder(ALICE).withTags(VALID_TAG_AVERAGE).build();
        assertFalse(ALICE.equals(editedAlice));
    }
}
