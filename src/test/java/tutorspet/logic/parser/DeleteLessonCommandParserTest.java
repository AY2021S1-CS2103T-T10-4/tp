package tutorspet.logic.parser;

import static tutorspet.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static tutorspet.logic.parser.CommandParserTestUtil.assertParseFailure;
import static tutorspet.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static tutorspet.testutil.TypicalIndexes.INDEX_FIRST_ITEM;
import static tutorspet.testutil.TypicalIndexes.INDEX_SECOND_ITEM;

import org.junit.jupiter.api.Test;

import tutorspet.logic.commands.DeleteLessonCommand;

/**
 * As we are only doing white-box testing, out test cases do not cover path variations
 * outside of the DeleteLessonCommand code. For example, inputs "1" and "1 abc" take the
 * same path through the DeleteLessonCommand, and therefore we test only one of them.
 * The path variations for those two cases occur inside the ParserUtil, and
 * therefore should be covered by the ParserUtilTest.
 */
public class DeleteLessonCommandParserTest {

    private DeleteLessonCommandParser parser = new DeleteLessonCommandParser();

    @Test
    public void parse_validArgs_returnsDeleteLessonCommand() {
        assertParseSuccess(parser, " c/1 l/1", new DeleteLessonCommand(INDEX_FIRST_ITEM, INDEX_FIRST_ITEM));

        // multiple class indexes -> last class index accepted
        assertParseSuccess(parser, " c/1 l/1 c/2", new DeleteLessonCommand(INDEX_SECOND_ITEM, INDEX_FIRST_ITEM));

        // multiple lesson indexes -> last lesson index accepted
        assertParseSuccess(parser, " l/1 c/1 l/2", new DeleteLessonCommand(INDEX_FIRST_ITEM, INDEX_SECOND_ITEM));
    }

    @Test
    public void parse_missingClassIndex_throwsParseException() {
        assertParseFailure(parser, " l/1", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                DeleteLessonCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingLessonIndex_throwsParseException() {
        assertParseFailure(parser, " c/1", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                DeleteLessonCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidClassIndex_throwsParseException() {
        assertParseFailure(parser, " c/a l/1", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                DeleteLessonCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidLessonIndex_throwsParseException() {
        assertParseFailure(parser, " c/1 l/a", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                DeleteLessonCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_emptyClassIndex_throwsParseException() {
        assertParseFailure(parser, " c/ l/1", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                DeleteLessonCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_emptyLessonIndex_throwsParseException() {
        assertParseFailure(parser, " c/1 l/", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                DeleteLessonCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidArgs_throwParseException() {
        assertParseFailure(parser, " a", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                DeleteLessonCommand.MESSAGE_USAGE));
    }
}
