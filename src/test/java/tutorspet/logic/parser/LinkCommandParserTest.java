package tutorspet.logic.parser;

import static tutorspet.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static tutorspet.logic.parser.CommandParserTestUtil.assertParseFailure;
import static tutorspet.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static tutorspet.testutil.TypicalIndexes.INDEX_FIRST_ITEM;
import static tutorspet.testutil.TypicalIndexes.INDEX_SECOND_ITEM;
import static tutorspet.testutil.TypicalIndexes.INDEX_THIRD_ITEM;

import org.junit.jupiter.api.Test;

import tutorspet.logic.commands.LinkCommand;

public class LinkCommandParserTest {

    private LinkCommandParser parser = new LinkCommandParser();

    @Test
    public void parse_validArgs_returnsLinkCommand() {
        assertParseSuccess(parser, " c/2 s/1", new LinkCommand(INDEX_SECOND_ITEM, INDEX_FIRST_ITEM));

        // multiple class indexes -> last class index accepted
        assertParseSuccess(parser, " c/2 s/1 c/3", new LinkCommand(INDEX_THIRD_ITEM, INDEX_FIRST_ITEM));

        // multiple student indexes -> last student accepted
        assertParseSuccess(parser, " s/1 c/2 s/3", new LinkCommand(INDEX_SECOND_ITEM, INDEX_THIRD_ITEM));
    }

    @Test
    public void parse_missingStudentIndex_throwsParseException() {
        assertParseFailure(parser, " c/1", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                LinkCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingClassIndex_throwsParseException() {
        assertParseFailure(parser, " s/1", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                LinkCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidStudentIndex_throwsParseException() {
        assertParseFailure(parser, " s/a c/2", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                LinkCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidClassIndex_throwsParseException() {
        assertParseFailure(parser, " s/1 c/a", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                LinkCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_emptyStudentIndex_throwsParseException() {
        assertParseFailure(parser, " s/ c/1", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                LinkCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_emptyClassIndex_throwsParseException() {
        assertParseFailure(parser, " s/1 c/", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                LinkCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, " a", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                LinkCommand.MESSAGE_USAGE));
    }
}
