package tutorspet.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static tutorspet.storage.JsonAdaptedStudent.MISSING_FIELD_MESSAGE_FORMAT;
import static tutorspet.storage.JsonAdaptedUuid.MESSAGE_INVALID_UUID;
import static tutorspet.testutil.Assert.assertThrows;
import static tutorspet.testutil.TypicalStudent.BENSON;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import tutorspet.commons.exceptions.IllegalValueException;
import tutorspet.model.components.name.Name;
import tutorspet.model.student.Email;
import tutorspet.model.student.Telegram;

public class JsonAdaptedStudentTest {

    public static final String VALID_UUID = BENSON.getUuid().toString();

    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_TELEGRAM = "r@chel";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_TAG = "#Average";
    private static final String INVALID_UUID = "584346cb-8886-4518-8282-";

    private static final JsonAdaptedName VALID_NAME = new JsonAdaptedName(BENSON.getName().toString());
    private static final String VALID_TELEGRAM = BENSON.getTelegram().toString();
    private static final String VALID_EMAIL = BENSON.getEmail().toString();
    private static final List<JsonAdaptedTag> VALID_TAGS = BENSON.getTags().stream()
            .map(JsonAdaptedTag::new)
            .collect(Collectors.toList());

    @Test
    public void toModelType_validStudentDetails_returnsStudent() throws Exception {
        JsonAdaptedStudent student = new JsonAdaptedStudent(BENSON);
        assertEquals(BENSON, student.toModelType());
    }

    @Test
    public void toModelType_validIndividualStudentDetails_returnsStudent() throws Exception {
        JsonAdaptedStudent student = new JsonAdaptedStudent(
                VALID_UUID, VALID_NAME, VALID_TELEGRAM, VALID_EMAIL, VALID_TAGS);
        assertEquals(BENSON, student.toModelType());
    }

    @Test
    public void toModelType_invalidUuid_throwsIllegalValueException() {
        JsonAdaptedStudent student =
                new JsonAdaptedStudent(INVALID_UUID, VALID_NAME, VALID_TELEGRAM, VALID_EMAIL, VALID_TAGS);
        String expectedMessage = String.format(MESSAGE_INVALID_UUID);
        assertThrows(IllegalValueException.class, expectedMessage, student::toModelType);
    }

    @Test
    public void toModelType_nullUuid_throwsIllegalValueException() {
        JsonAdaptedStudent student =
                new JsonAdaptedStudent(null, VALID_NAME, VALID_TELEGRAM, VALID_EMAIL, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, UUID.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, student::toModelType);
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedName invalidName = new JsonAdaptedName(INVALID_NAME);
        JsonAdaptedStudent student =
                new JsonAdaptedStudent(VALID_UUID, invalidName, VALID_TELEGRAM, VALID_EMAIL, VALID_TAGS);
        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, student::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedStudent student =
                new JsonAdaptedStudent(VALID_UUID, null, VALID_TELEGRAM, VALID_EMAIL, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, student::toModelType);
    }

    @Test
    public void toModelType_invalidTelegram_throwsIllegalValueException() {
        JsonAdaptedStudent student =
                new JsonAdaptedStudent(VALID_UUID, VALID_NAME, INVALID_TELEGRAM, VALID_EMAIL, VALID_TAGS);
        String expectedMessage = Telegram.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, student::toModelType);
    }

    @Test
    public void toModelType_nullTelegram_throwsIllegalValueException() {
        JsonAdaptedStudent student =
                new JsonAdaptedStudent(VALID_UUID, VALID_NAME, null, VALID_EMAIL, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Telegram.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, student::toModelType);
    }

    @Test
    public void toModelType_invalidEmail_throwsIllegalValueException() {
        JsonAdaptedStudent student =
                new JsonAdaptedStudent(VALID_UUID, VALID_NAME, VALID_TELEGRAM, INVALID_EMAIL, VALID_TAGS);
        String expectedMessage = Email.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, student::toModelType);
    }

    @Test
    public void toModelType_nullEmail_throwsIllegalValueException() {
        JsonAdaptedStudent student =
                new JsonAdaptedStudent(VALID_UUID, VALID_NAME, VALID_TELEGRAM, null, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, student::toModelType);
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<JsonAdaptedTag> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(new JsonAdaptedTag(INVALID_TAG));
        JsonAdaptedStudent student =
                new JsonAdaptedStudent(VALID_UUID, VALID_NAME, VALID_TELEGRAM, VALID_EMAIL, invalidTags);
        assertThrows(IllegalValueException.class, student::toModelType);
    }
}
