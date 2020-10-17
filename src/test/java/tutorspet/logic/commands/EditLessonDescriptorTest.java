package tutorspet.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tutorspet.logic.commands.CommandTestUtil.DESC_LESSON_FRI_8_TO_10;
import static tutorspet.logic.commands.CommandTestUtil.DESC_LESSON_WED_2_TO_4;
import static tutorspet.logic.commands.CommandTestUtil.VALID_DAY_FRI_LESSON_FRI_8_TO_10;
import static tutorspet.logic.commands.CommandTestUtil.VALID_VENUE_S17_0302_LESSON_FRI_8_TO_10;

import org.junit.jupiter.api.Test;

import tutorspet.testutil.EditLessonDescriptorBuilder;

public class EditLessonDescriptorTest {

    @Test
    public void equals() {
        // same value -> return true
        EditLessonCommand.EditLessonDescriptor descriptorWithSameValues =
                new EditLessonCommand.EditLessonDescriptor(DESC_LESSON_WED_2_TO_4);
        assertTrue(DESC_LESSON_WED_2_TO_4.equals(descriptorWithSameValues));

        // same object -> returns true
        assertTrue(DESC_LESSON_WED_2_TO_4.equals(DESC_LESSON_WED_2_TO_4));

        // null -> returns false
        assertFalse(DESC_LESSON_WED_2_TO_4.equals(null));

        // different types -> returns false
        assertFalse(DESC_LESSON_WED_2_TO_4.equals(5));

        // different values -> returns false
        assertFalse(DESC_LESSON_WED_2_TO_4.equals(DESC_LESSON_FRI_8_TO_10));

        // different startTime -> returns false
        EditLessonCommand.EditLessonDescriptor editedLesson = new EditLessonDescriptorBuilder(DESC_LESSON_WED_2_TO_4)
                .withStartTime("00:01").build();
        assertFalse(DESC_LESSON_WED_2_TO_4.equals(editedLesson));

        // different endTime -> returns false
        editedLesson = new EditLessonDescriptorBuilder(DESC_LESSON_WED_2_TO_4)
                .withEndTime("23:59").build();
        assertFalse(DESC_LESSON_WED_2_TO_4.equals(editedLesson));

        // different day -> returns false
        editedLesson = new EditLessonDescriptorBuilder(DESC_LESSON_WED_2_TO_4)
                .withDay(VALID_DAY_FRI_LESSON_FRI_8_TO_10.toString()).build();
        assertFalse(DESC_LESSON_WED_2_TO_4.equals(editedLesson));

        // different venue -> returns false
        editedLesson = new EditLessonDescriptorBuilder(DESC_LESSON_WED_2_TO_4)
                .withVenue(VALID_VENUE_S17_0302_LESSON_FRI_8_TO_10).build();
        assertFalse(DESC_LESSON_WED_2_TO_4.equals(editedLesson));
    }
}
