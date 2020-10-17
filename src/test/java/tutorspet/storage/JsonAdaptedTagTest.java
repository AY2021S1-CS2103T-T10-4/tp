package tutorspet.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static tutorspet.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static tutorspet.logic.commands.CommandTestUtil.VALID_TAG_AVERAGE;
import static tutorspet.logic.commands.CommandTestUtil.VALID_TAG_EXPERIENCED;
import static tutorspet.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import tutorspet.commons.exceptions.IllegalValueException;
import tutorspet.model.components.tag.Tag;

public class JsonAdaptedTagTest {

    public static final JsonAdaptedTag VALID_JSON_ADAPTED_TAG_AVERAGE = new JsonAdaptedTag(VALID_TAG_AVERAGE);
    public static final JsonAdaptedTag VALID_JSON_ADAPTED_TAG_EXPERIENCED = new JsonAdaptedTag(VALID_TAG_EXPERIENCED);

    @Test
    public void toModelType_validTag_returnsTag() throws Exception {
        Tag validTag = new Tag(VALID_TAG_AVERAGE);
        JsonAdaptedTag validJsonAdaptedTag = new JsonAdaptedTag(validTag);
        assertEquals(validTag, validJsonAdaptedTag.toModelType());
    }

    @Test
    public void toModelType_validTagName_returnsTag() throws Exception {
        JsonAdaptedTag validJsonAdaptedTag = new JsonAdaptedTag(VALID_TAG_AVERAGE);
        assertEquals(new Tag(VALID_TAG_AVERAGE), validJsonAdaptedTag.toModelType());
    }

    @Test
    public void toModelType_invalidTagName_throwsIllegalValueException() {
        JsonAdaptedTag invalidName = new JsonAdaptedTag(INVALID_TAG_DESC);
        assertThrows(IllegalValueException.class, Tag.MESSAGE_CONSTRAINTS, invalidName::toModelType);
    }

    @Test
    public void toModelType_nullTagName_throwsIllegalValueException() {
        JsonAdaptedTag nullTagName = new JsonAdaptedTag((String) null);
        assertThrows(IllegalValueException.class, Tag.MESSAGE_CONSTRAINTS, nullTagName::toModelType);
    }

    @Test
    public void getTagName_validTagName_returnsTagName() {
        JsonAdaptedTag validJsonAdaptedTag = new JsonAdaptedTag(VALID_TAG_AVERAGE);
        assertEquals(VALID_TAG_AVERAGE, validJsonAdaptedTag.getTagName());
    }
}
