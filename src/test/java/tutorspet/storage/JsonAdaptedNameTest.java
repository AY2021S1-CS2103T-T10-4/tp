package tutorspet.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static tutorspet.logic.commands.CommandTestUtil.INVALID_NAME;
import static tutorspet.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static tutorspet.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import tutorspet.commons.exceptions.IllegalValueException;
import tutorspet.model.components.name.Name;

public class JsonAdaptedNameTest {

    /** A {@code JsonAdaptedName} for testing with {@code VALID_NAME_AMY} as name. */
    public static final JsonAdaptedName VALID_JSON_ADAPTED_NAME = new JsonAdaptedName(VALID_NAME_AMY);

    @Test
    public void toModelType_validName_returnsName() throws Exception {
        JsonAdaptedName validName = new JsonAdaptedName(VALID_NAME_AMY);
        assertEquals(new Name(VALID_NAME_AMY), validName.toModelType());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedName invalidName = new JsonAdaptedName(INVALID_NAME);
        assertThrows(IllegalValueException.class, Name.MESSAGE_CONSTRAINTS, invalidName::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedName nullName = new JsonAdaptedName((String) null);
        assertThrows(IllegalValueException.class, Name.MESSAGE_CONSTRAINTS, nullName::toModelType);
    }
}
