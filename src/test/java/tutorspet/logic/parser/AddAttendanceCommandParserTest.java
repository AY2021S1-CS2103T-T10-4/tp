package tutorspet.logic.parser;

import static tutorspet.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static tutorspet.logic.commands.CommandTestUtil.INVALID_PARTICIPATION_LOWER_BOUND_SCORE_DESC;
import static tutorspet.logic.commands.CommandTestUtil.INVALID_PARTICIPATION_UPPER_BOUND_SCORE_DESC;
import static tutorspet.logic.commands.CommandTestUtil.INVALID_WEEK_LOWER_BOUND_DESC;
import static tutorspet.logic.commands.CommandTestUtil.INVALID_WEEK_UPPER_BOUND_DESC;
import static tutorspet.logic.commands.CommandTestUtil.PARTICIPATION_SCORE_DESC_51;
import static tutorspet.logic.commands.CommandTestUtil.PARTICIPATION_SCORE_DESC_80;
import static tutorspet.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static tutorspet.logic.commands.CommandTestUtil.VALID_PARTICIPATION_SCORE_80;
import static tutorspet.logic.commands.CommandTestUtil.VALID_WEEK_VALUE_5;
import static tutorspet.logic.commands.CommandTestUtil.WEEK_DESC_WEEK_VALUE_3;
import static tutorspet.logic.commands.CommandTestUtil.WEEK_DESC_WEEK_VALUE_5;
import static tutorspet.logic.parser.CliSyntax.PREFIX_PARTICIPATION_SCORE;
import static tutorspet.logic.parser.CliSyntax.PREFIX_WEEK;
import static tutorspet.logic.parser.CommandParserTestUtil.assertParseFailure;
import static tutorspet.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static tutorspet.testutil.TypicalIndexes.INDEX_FIRST_ITEM;

import org.junit.jupiter.api.Test;

import tutorspet.commons.core.index.Index;
import tutorspet.logic.commands.AddAttendanceCommand;
import tutorspet.model.attendance.Attendance;
import tutorspet.model.attendance.Week;

public class AddAttendanceCommandParserTest {

    private AddAttendanceCommandParser parser = new AddAttendanceCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Attendance expectedAttendance = new Attendance(VALID_PARTICIPATION_SCORE_80);
        Week expectedWeek = new Week(Index.fromOneBased(VALID_WEEK_VALUE_5));

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + " c/1 l/1 s/1 "
                + WEEK_DESC_WEEK_VALUE_5 + PARTICIPATION_SCORE_DESC_80,
                new AddAttendanceCommand(INDEX_FIRST_ITEM, INDEX_FIRST_ITEM, INDEX_FIRST_ITEM,
                        expectedWeek, expectedAttendance));

        // multiple class indexes - last class index accepted
        assertParseSuccess(parser, " c/2 l/1 s/1 c/1"
                        + WEEK_DESC_WEEK_VALUE_5 + PARTICIPATION_SCORE_DESC_80,
                new AddAttendanceCommand(INDEX_FIRST_ITEM, INDEX_FIRST_ITEM, INDEX_FIRST_ITEM,
                        expectedWeek, expectedAttendance));

        // multiple lesson indexes - last lesson index accepted
        assertParseSuccess(parser, " c/1 l/2 s/1 l/1"
                        + WEEK_DESC_WEEK_VALUE_5 + PARTICIPATION_SCORE_DESC_80,
                new AddAttendanceCommand(INDEX_FIRST_ITEM, INDEX_FIRST_ITEM, INDEX_FIRST_ITEM,
                        expectedWeek, expectedAttendance));

        // multiple student indexes - last student index accepted
        assertParseSuccess(parser, " c/1 l/1 s/2 s/1"
                        + WEEK_DESC_WEEK_VALUE_5 + PARTICIPATION_SCORE_DESC_80,
                new AddAttendanceCommand(INDEX_FIRST_ITEM, INDEX_FIRST_ITEM, INDEX_FIRST_ITEM,
                        expectedWeek, expectedAttendance));

        // multiple week values - last week value accepted
        assertParseSuccess(parser, " c/1 l/1 s/1"
                        + WEEK_DESC_WEEK_VALUE_3 + PARTICIPATION_SCORE_DESC_80
                        + WEEK_DESC_WEEK_VALUE_5,
                new AddAttendanceCommand(INDEX_FIRST_ITEM, INDEX_FIRST_ITEM, INDEX_FIRST_ITEM,
                        expectedWeek, expectedAttendance));

        // multiple participation scores - last week value accepted
        assertParseSuccess(parser, " c/1 l/1 s/1"
                        + PARTICIPATION_SCORE_DESC_51 + PARTICIPATION_SCORE_DESC_80
                        + WEEK_DESC_WEEK_VALUE_5,
                new AddAttendanceCommand(INDEX_FIRST_ITEM, INDEX_FIRST_ITEM, INDEX_FIRST_ITEM,
                        expectedWeek, expectedAttendance));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddAttendanceCommand.MESSAGE_USAGE);

        // missing class prefix
        assertParseFailure(parser, " 1 l/1 s/1"
                        + WEEK_DESC_WEEK_VALUE_5 + PARTICIPATION_SCORE_DESC_80,
                expectedMessage);

        // missing lesson prefix
        assertParseFailure(parser, " c/1 1 s/1"
                        + WEEK_DESC_WEEK_VALUE_5 + PARTICIPATION_SCORE_DESC_80,
                expectedMessage);

        // missing student prefix
        assertParseFailure(parser, " c/1 l/1 1"
                        + WEEK_DESC_WEEK_VALUE_5 + PARTICIPATION_SCORE_DESC_80,
                expectedMessage);

        // missing week prefix
        assertParseFailure(parser, " c/1 l/1 s/1"
                        + VALID_WEEK_VALUE_5 + PARTICIPATION_SCORE_DESC_80,
                expectedMessage);

        // missing participation score prefix
        assertParseFailure(parser, " c/1 l/1 s/1"
                        + WEEK_DESC_WEEK_VALUE_5 + VALID_PARTICIPATION_SCORE_80,
                expectedMessage);

        // missing class index
        assertParseFailure(parser, " c/ l/1 s/1"
                        + WEEK_DESC_WEEK_VALUE_5 + PARTICIPATION_SCORE_DESC_80,
                expectedMessage);

        // missing lesson index
        assertParseFailure(parser, " c/1 l/ s/1"
                        + WEEK_DESC_WEEK_VALUE_5 + PARTICIPATION_SCORE_DESC_80,
                expectedMessage);

        // missing student index
        assertParseFailure(parser, " c/1 l/1 s/"
                        + WEEK_DESC_WEEK_VALUE_5 + PARTICIPATION_SCORE_DESC_80,
                expectedMessage);

        // missing week value
        assertParseFailure(parser, " c/1 l/1 s/1"
                        + PREFIX_WEEK + PARTICIPATION_SCORE_DESC_80,
                expectedMessage);

        // missing participation score
        assertParseFailure(parser, " c/1 l/1 s/1"
                        + WEEK_DESC_WEEK_VALUE_5 + PREFIX_PARTICIPATION_SCORE,
                expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddAttendanceCommand.MESSAGE_USAGE);

        // invalid class index
        assertParseFailure(parser, " c/& l/1 s/1"
                        + WEEK_DESC_WEEK_VALUE_5 + PARTICIPATION_SCORE_DESC_80,
                expectedMessage);

        // invalid lesson index
        assertParseFailure(parser, " c/1 l/& s/1"
                        + WEEK_DESC_WEEK_VALUE_5 + PARTICIPATION_SCORE_DESC_80,
                expectedMessage);

        // invalid student index
        assertParseFailure(parser, " c/1 l/1 s/&"
                        + WEEK_DESC_WEEK_VALUE_5 + PARTICIPATION_SCORE_DESC_80,
                expectedMessage);

        // invalid week value - lower-bound
        assertParseFailure(parser, " c/1 l/1 s/1"
                        + INVALID_WEEK_LOWER_BOUND_DESC + PARTICIPATION_SCORE_DESC_80,
                Week.MESSAGE_CONSTRAINTS);

        // invalid week value - upper-bound
        assertParseFailure(parser, " c/1 l/1 s/1"
                        + INVALID_WEEK_UPPER_BOUND_DESC + PARTICIPATION_SCORE_DESC_80,
                Week.MESSAGE_CONSTRAINTS);

        // invalid participation score - lower-bound
        assertParseFailure(parser, " c/1 l/1 s/1"
                        + WEEK_DESC_WEEK_VALUE_5 + INVALID_PARTICIPATION_LOWER_BOUND_SCORE_DESC,
                Attendance.MESSAGE_CONSTRAINTS);

        // invalid participation score - upper-bound
        assertParseFailure(parser, " c/1 l/1 s/1"
                        + WEEK_DESC_WEEK_VALUE_5 + INVALID_PARTICIPATION_UPPER_BOUND_SCORE_DESC,
                Attendance.MESSAGE_CONSTRAINTS);
    }
}
