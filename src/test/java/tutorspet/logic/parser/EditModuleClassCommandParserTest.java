package tutorspet.logic.parser;

import static tutorspet.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static tutorspet.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static tutorspet.logic.commands.CommandTestUtil.NAME_DESC_CS2100_LAB;
import static tutorspet.logic.commands.CommandTestUtil.NAME_DESC_CS2103T_TUTORIAL;
import static tutorspet.logic.commands.CommandTestUtil.VALID_NAME_CS2100_LAB;
import static tutorspet.logic.commands.CommandTestUtil.VALID_NAME_CS2103T_TUTORIAL;
import static tutorspet.logic.parser.CommandParserTestUtil.assertParseFailure;
import static tutorspet.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static tutorspet.testutil.TypicalIndexes.INDEX_FIRST_ITEM;
import static tutorspet.testutil.TypicalIndexes.INDEX_SECOND_ITEM;

import org.junit.jupiter.api.Test;

import tutorspet.commons.core.index.Index;
import tutorspet.logic.commands.EditModuleClassCommand;
import tutorspet.logic.commands.EditModuleClassCommand.EditModuleClassDescriptor;
import tutorspet.model.components.name.Name;
import tutorspet.testutil.EditModuleClassDescriptorBuilder;

public class EditModuleClassCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditModuleClassCommand.MESSAGE_USAGE);

    private EditModuleClassCommandParser parser = new EditModuleClassCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, VALID_NAME_CS2100_LAB, MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(parser, "1", EditModuleClassCommand.MESSAGE_NOT_EDITED);

        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5" + NAME_DESC_CS2103T_TUTORIAL, MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, "0" + NAME_DESC_CS2103T_TUTORIAL, MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 i/ string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, "1" + INVALID_NAME_DESC, Name.MESSAGE_CONSTRAINTS); // invalid name

        // invalid name followed by valid name
        assertParseFailure(parser, "1" + INVALID_NAME_DESC + VALID_NAME_CS2103T_TUTORIAL,
                Name.MESSAGE_CONSTRAINTS);

        // valid name followed by invalid name.
        assertParseFailure(parser, "1" + NAME_DESC_CS2103T_TUTORIAL + INVALID_NAME_DESC,
                Name.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        Index targetIndex = INDEX_SECOND_ITEM;
        String userInput = targetIndex.getOneBased() + NAME_DESC_CS2103T_TUTORIAL;

        EditModuleClassDescriptor descriptor =
                new EditModuleClassDescriptorBuilder().withName(VALID_NAME_CS2103T_TUTORIAL).build();
        EditModuleClassCommand expectedCommand = new EditModuleClassCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedFields_acceptsLast() {
        Index targetIndex = INDEX_FIRST_ITEM;
        String userInput = targetIndex.getOneBased() + NAME_DESC_CS2100_LAB + NAME_DESC_CS2103T_TUTORIAL;

        EditModuleClassDescriptor descriptor =
                new EditModuleClassDescriptorBuilder().withName(VALID_NAME_CS2103T_TUTORIAL).build();
        EditModuleClassCommand expectedCommand = new EditModuleClassCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_invalidValueFollowedByValidValue_success() {
        // no other valid values specified
        Index targetIndex = INDEX_FIRST_ITEM;
        String userInput = targetIndex.getOneBased() + INVALID_NAME_DESC + NAME_DESC_CS2103T_TUTORIAL;
        EditModuleClassDescriptor descriptor =
                new EditModuleClassDescriptorBuilder().withName(VALID_NAME_CS2103T_TUTORIAL).build();
        EditModuleClassCommand expectedCommand = new EditModuleClassCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }
}
