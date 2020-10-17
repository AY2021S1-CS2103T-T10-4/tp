package tutorspet.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tutorspet.logic.commands.CommandTestUtil.DESC_CS2100_LAB;
import static tutorspet.logic.commands.CommandTestUtil.DESC_CS2103T_TUTORIAL;
import static tutorspet.logic.commands.CommandTestUtil.VALID_NAME_CS2103T_TUTORIAL;

import org.junit.jupiter.api.Test;

import tutorspet.testutil.EditModuleClassDescriptorBuilder;

public class EditModuleClassDescriptorTest {

    @Test
    public void equals() {
        // same value -> return true
        EditModuleClassCommand.EditModuleClassDescriptor descriptorWithSameValues =
                new EditModuleClassCommand.EditModuleClassDescriptor(DESC_CS2100_LAB);
        assertTrue(DESC_CS2100_LAB.equals(descriptorWithSameValues));

        // same object -> return true
        assertTrue(DESC_CS2100_LAB.equals(DESC_CS2100_LAB));

        // null -> return false
        assertFalse(DESC_CS2100_LAB.equals(null));

        // different type -> return false
        assertFalse(DESC_CS2100_LAB.equals(5));

        // different value -> return false
        assertFalse(DESC_CS2100_LAB.equals(DESC_CS2103T_TUTORIAL));

        // different name -> return false
        EditModuleClassCommand.EditModuleClassDescriptor editedCs2100Lab =
                new EditModuleClassDescriptorBuilder(DESC_CS2100_LAB).withName(VALID_NAME_CS2103T_TUTORIAL)
                        .build();
        assertFalse(DESC_CS2100_LAB.equals(editedCs2100Lab));
    }
}
