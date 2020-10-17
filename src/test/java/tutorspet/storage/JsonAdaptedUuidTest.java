package tutorspet.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static tutorspet.logic.commands.CommandTestUtil.INVALID_STUDENT_UUID;
import static tutorspet.logic.commands.CommandTestUtil.VALID_UUID_AMY;
import static tutorspet.testutil.Assert.assertThrows;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import tutorspet.commons.exceptions.IllegalValueException;

public class JsonAdaptedUuidTest {

    /** A {@code JsonAdaptedUuid} for testing with {@code VALID_UUID_AMY} as UUID. */
    public static final JsonAdaptedUuid VALID_JSON_ADAPTED_UUID = new JsonAdaptedUuid(UUID.fromString(VALID_UUID_AMY));

    @Test
    public void toModelType_validUuidString_returnsUuid() throws Exception {
        JsonAdaptedUuid uuid = new JsonAdaptedUuid(VALID_UUID_AMY);
        assertEquals(UUID.fromString(VALID_UUID_AMY), uuid.toModelType());
    }

    @Test
    public void toModelType_validUuid_returnsUuid() throws Exception {
        JsonAdaptedUuid uuid = new JsonAdaptedUuid(UUID.fromString(VALID_UUID_AMY));
        assertEquals(UUID.fromString(VALID_UUID_AMY), uuid.toModelType());
    }

    @Test
    public void toModelType_invalidUuid_throwsIllegalValueException() {
        JsonAdaptedUuid uuid = new JsonAdaptedUuid(INVALID_STUDENT_UUID);
        assertThrows(IllegalValueException.class, JsonAdaptedUuid.MESSAGE_INVALID_UUID, uuid::toModelType);
    }

    @Test
    public void toModelType_nullUuid_throwsIllegalValueException() {
        JsonAdaptedUuid uuid = new JsonAdaptedUuid((String) null);
        assertThrows(IllegalValueException.class, JsonAdaptedUuid.MESSAGE_NULL_UUID, uuid::toModelType);
    }
}
