package tutorspet.logic.parser;

import static tutorspet.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static tutorspet.logic.commands.CommandTestUtil.VALID_WEEK_1;
import static tutorspet.logic.commands.CommandTestUtil.VALID_WEEK_5;
import static tutorspet.logic.parser.CommandParserTestUtil.assertParseFailure;
import static tutorspet.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static tutorspet.testutil.TypicalIndexes.INDEX_FIRST_ITEM;
import static tutorspet.testutil.TypicalIndexes.INDEX_SECOND_ITEM;

import org.junit.jupiter.api.Test;

import tutorspet.logic.commands.DeleteAttendanceCommand;

/**
 * As we are only doing white-box testing, out test cases do not cover path variations
 * outside of the DeleteAttendanceCommand code. For example, inputs "1" and "1 abc" take the
 * same path through the DeleteAttendanceCommand, and therefore we test only one of them.
 * The path variations for those two cases occur inside the ParserUtil, and
 * therefore should be covered by the ParserUtilTest.
 */
public class DeleteAttendanceCommandParserTest {

    private DeleteAttendanceCommandParser parser = new DeleteAttendanceCommandParser();

    @Test
    public void parse_validArgs_returnsDeleteAttendanceCommand() {
        assertParseSuccess(parser, " c/1 l/1 s/1 w/1",
                new DeleteAttendanceCommand(INDEX_FIRST_ITEM, INDEX_FIRST_ITEM, INDEX_FIRST_ITEM, VALID_WEEK_1));

        // multiple class indexes -> last class index accepted
        assertParseSuccess(parser, " c/1 l/1 c/2 s/1 w/1",
                new DeleteAttendanceCommand(INDEX_SECOND_ITEM, INDEX_FIRST_ITEM, INDEX_FIRST_ITEM, VALID_WEEK_1));

        // multiple lesson indexes -> last lesson index accepted
        assertParseSuccess(parser, " c/1 l/1 s/1 l/2 w/1",
                new DeleteAttendanceCommand(INDEX_FIRST_ITEM, INDEX_SECOND_ITEM, INDEX_FIRST_ITEM, VALID_WEEK_1));

        // multiple student indexes -> last student index accepted
        assertParseSuccess(parser, " c/1 l/1 s/1 w/1 s/2",
                new DeleteAttendanceCommand(INDEX_FIRST_ITEM, INDEX_FIRST_ITEM, INDEX_SECOND_ITEM, VALID_WEEK_1));

        assertParseSuccess(parser, " c/1 l/1 s/1 w/1 w/5",
                new DeleteAttendanceCommand(INDEX_FIRST_ITEM, INDEX_FIRST_ITEM, INDEX_FIRST_ITEM, VALID_WEEK_5));
    }

    @Test
    public void parse_missingClassIndex_throwsParseException() {
        assertParseFailure(parser, " l/1 s/1 w/1", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                DeleteAttendanceCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingLessonIndex_throwsParseException() {
        assertParseFailure(parser, " c/1 s/1 w/1", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                DeleteAttendanceCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingStudentIndex_throwsParseException() {
        assertParseFailure(parser, " c/1 l/1 w/1", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                DeleteAttendanceCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingWeekIndex_throwsParseException() {
        assertParseFailure(parser, " c/1 l/1 s/1", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                DeleteAttendanceCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidClassIndex_throwsParseException() {
        assertParseFailure(parser, " c/a l/1 s/1 w/1", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                DeleteAttendanceCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidLessonIndex_throwsParseException() {
        assertParseFailure(parser, " c/1 l/a s/1 w/1", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                DeleteAttendanceCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidStudentIndex_throwsParseException() {
        assertParseFailure(parser, " c/1 l/1 s/a w/1", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                DeleteAttendanceCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidWeekIndex_throwsParseException() {
        assertParseFailure(parser, " c/1 l/1 s/1 w/a", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                DeleteAttendanceCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_emptyClassIndex_throwsParseException() {
        assertParseFailure(parser, " c/ l/1 s/1 w/1", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                DeleteAttendanceCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_emptyLessonIndex_throwsParseException() {
        assertParseFailure(parser, " c/1 l/ s/1 w/1", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                DeleteAttendanceCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_emptyStudentIndex_throwsParseException() {
        assertParseFailure(parser, " c/1 l/1 s/ w/1", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                DeleteAttendanceCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_emptyWeekIndex_throwsParseException() {
        assertParseFailure(parser, " c/1 l/1 s/1 w/", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                DeleteAttendanceCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidArgs_throwParseException() {
        assertParseFailure(parser, " a", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                DeleteAttendanceCommand.MESSAGE_USAGE));
    }
}
