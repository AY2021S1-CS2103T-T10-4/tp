package tutorspet.logic.parser;

import static tutorspet.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static tutorspet.logic.commands.CommandTestUtil.DAY_DESC_LESSON_FRI_8_TO_10;
import static tutorspet.logic.commands.CommandTestUtil.DAY_DESC_LESSON_WED_2_TO_4;
import static tutorspet.logic.commands.CommandTestUtil.END_TIME_DESC_LESSON_FRI_8_TO_10;
import static tutorspet.logic.commands.CommandTestUtil.END_TIME_DESC_LESSON_WED_2_TO_4;
import static tutorspet.logic.commands.CommandTestUtil.INVALID_DAY_DESC;
import static tutorspet.logic.commands.CommandTestUtil.INVALID_END_TIME_DESC;
import static tutorspet.logic.commands.CommandTestUtil.INVALID_NUMBER_OF_OCCURRENCES_DESC;
import static tutorspet.logic.commands.CommandTestUtil.INVALID_START_TIME_DESC;
import static tutorspet.logic.commands.CommandTestUtil.INVALID_VENUE_DESC;
import static tutorspet.logic.commands.CommandTestUtil.NUMBER_OF_OCCURRENCES_DESC_LESSON_FRI_8_TO_10;
import static tutorspet.logic.commands.CommandTestUtil.NUMBER_OF_OCCURRENCES_DESC_LESSON_WED_2_TO_4;
import static tutorspet.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static tutorspet.logic.commands.CommandTestUtil.START_TIME_DESC_LESSON_FRI_8_TO_10;
import static tutorspet.logic.commands.CommandTestUtil.START_TIME_DESC_LESSON_WED_2_TO_4;
import static tutorspet.logic.commands.CommandTestUtil.VALID_DAY_FRI_LESSON_FRI_8_TO_10;
import static tutorspet.logic.commands.CommandTestUtil.VALID_END_TIME_1000_LESSON_FRI_8_TO_10;
import static tutorspet.logic.commands.CommandTestUtil.VALID_NUMBER_OF_OCCURRENCES_13_LESSON_FRI_8_TO_10;
import static tutorspet.logic.commands.CommandTestUtil.VALID_START_TIME_0800_LESSON_FRI_8_TO_10;
import static tutorspet.logic.commands.CommandTestUtil.VALID_VENUE_S17_0302_LESSON_FRI_8_TO_10;
import static tutorspet.logic.commands.CommandTestUtil.VENUE_DESC_LESSON_FRI_8_TO_10;
import static tutorspet.logic.commands.CommandTestUtil.VENUE_DESC_LESSON_WED_2_TO_4;
import static tutorspet.logic.parser.CommandParserTestUtil.assertParseFailure;
import static tutorspet.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static tutorspet.logic.parser.ParserUtil.MESSAGE_INVALID_TIME;
import static tutorspet.testutil.TypicalIndexes.INDEX_FIRST_ITEM;
import static tutorspet.testutil.TypicalLesson.LESSON_FRI_8_TO_10;

import org.junit.jupiter.api.Test;

import tutorspet.logic.commands.AddLessonCommand;
import tutorspet.model.lesson.Day;
import tutorspet.model.lesson.Lesson;
import tutorspet.model.lesson.NumberOfOccurrences;
import tutorspet.model.lesson.Venue;
import tutorspet.testutil.LessonBuilder;

public class AddLessonCommandParserTest {

    private AddLessonCommandParser parser = new AddLessonCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Lesson expectedLesson = new LessonBuilder(LESSON_FRI_8_TO_10).build();

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + " c/1"
                + DAY_DESC_LESSON_FRI_8_TO_10 + START_TIME_DESC_LESSON_FRI_8_TO_10
                + END_TIME_DESC_LESSON_FRI_8_TO_10 + VENUE_DESC_LESSON_FRI_8_TO_10
                + NUMBER_OF_OCCURRENCES_DESC_LESSON_FRI_8_TO_10,
                new AddLessonCommand(INDEX_FIRST_ITEM, expectedLesson));

        // multiple class indexes - last class index accepted
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + " c/1" + " c/1"
                        + DAY_DESC_LESSON_FRI_8_TO_10 + START_TIME_DESC_LESSON_FRI_8_TO_10
                        + END_TIME_DESC_LESSON_FRI_8_TO_10 + VENUE_DESC_LESSON_FRI_8_TO_10
                        + NUMBER_OF_OCCURRENCES_DESC_LESSON_FRI_8_TO_10,
                new AddLessonCommand(INDEX_FIRST_ITEM, expectedLesson));

        // multiple days - last day accepted
        assertParseSuccess(parser, " c/1" + DAY_DESC_LESSON_WED_2_TO_4 + DAY_DESC_LESSON_FRI_8_TO_10
                        + START_TIME_DESC_LESSON_FRI_8_TO_10 + END_TIME_DESC_LESSON_FRI_8_TO_10
                        + VENUE_DESC_LESSON_FRI_8_TO_10 + NUMBER_OF_OCCURRENCES_DESC_LESSON_FRI_8_TO_10,
                new AddLessonCommand(INDEX_FIRST_ITEM, expectedLesson));

        // multiple start times - last start time accepted
        assertParseSuccess(parser, " c/1" + DAY_DESC_LESSON_FRI_8_TO_10 + START_TIME_DESC_LESSON_WED_2_TO_4
                        + START_TIME_DESC_LESSON_FRI_8_TO_10 + END_TIME_DESC_LESSON_FRI_8_TO_10
                        + VENUE_DESC_LESSON_FRI_8_TO_10 + NUMBER_OF_OCCURRENCES_DESC_LESSON_FRI_8_TO_10,
                new AddLessonCommand(INDEX_FIRST_ITEM, expectedLesson));

        // multiple end times - last end time accepted
        assertParseSuccess(parser, " c/1" + DAY_DESC_LESSON_FRI_8_TO_10 + START_TIME_DESC_LESSON_FRI_8_TO_10
                        + END_TIME_DESC_LESSON_WED_2_TO_4 + END_TIME_DESC_LESSON_FRI_8_TO_10
                        + VENUE_DESC_LESSON_FRI_8_TO_10 + NUMBER_OF_OCCURRENCES_DESC_LESSON_FRI_8_TO_10,
                new AddLessonCommand(INDEX_FIRST_ITEM, expectedLesson));

        // multiple number of venues - last venue accepted
        assertParseSuccess(parser, " c/1" + DAY_DESC_LESSON_FRI_8_TO_10 + START_TIME_DESC_LESSON_FRI_8_TO_10
                        + END_TIME_DESC_LESSON_FRI_8_TO_10 + VENUE_DESC_LESSON_WED_2_TO_4
                        + VENUE_DESC_LESSON_FRI_8_TO_10 + NUMBER_OF_OCCURRENCES_DESC_LESSON_FRI_8_TO_10,
                new AddLessonCommand(INDEX_FIRST_ITEM, expectedLesson));

        // multiple number of occurrences - last occurrence value accepted
        assertParseSuccess(parser, " c/1" + DAY_DESC_LESSON_FRI_8_TO_10 + START_TIME_DESC_LESSON_FRI_8_TO_10
                        + END_TIME_DESC_LESSON_FRI_8_TO_10 + VENUE_DESC_LESSON_FRI_8_TO_10
                        + NUMBER_OF_OCCURRENCES_DESC_LESSON_WED_2_TO_4 + NUMBER_OF_OCCURRENCES_DESC_LESSON_FRI_8_TO_10,
                new AddLessonCommand(INDEX_FIRST_ITEM, expectedLesson));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddLessonCommand.MESSAGE_USAGE);

        // missing class prefix
        assertParseFailure(parser, " 1"
                        + DAY_DESC_LESSON_FRI_8_TO_10 + START_TIME_DESC_LESSON_FRI_8_TO_10
                        + END_TIME_DESC_LESSON_FRI_8_TO_10 + VENUE_DESC_LESSON_FRI_8_TO_10
                        + NUMBER_OF_OCCURRENCES_DESC_LESSON_FRI_8_TO_10,
                expectedMessage);

        // missing day prefix
        assertParseFailure(parser, " c/1"
                        + VALID_DAY_FRI_LESSON_FRI_8_TO_10 + START_TIME_DESC_LESSON_FRI_8_TO_10
                        + END_TIME_DESC_LESSON_FRI_8_TO_10 + VENUE_DESC_LESSON_FRI_8_TO_10
                        + NUMBER_OF_OCCURRENCES_DESC_LESSON_FRI_8_TO_10,
                expectedMessage);

        // missing start time prefix
        assertParseFailure(parser, " c/1"
                        + DAY_DESC_LESSON_FRI_8_TO_10 + VALID_START_TIME_0800_LESSON_FRI_8_TO_10
                        + END_TIME_DESC_LESSON_FRI_8_TO_10 + VENUE_DESC_LESSON_FRI_8_TO_10
                        + NUMBER_OF_OCCURRENCES_DESC_LESSON_FRI_8_TO_10,
                expectedMessage);

        // missing end time prefix
        assertParseFailure(parser, " c/1"
                        + DAY_DESC_LESSON_FRI_8_TO_10 + START_TIME_DESC_LESSON_FRI_8_TO_10
                        + VALID_END_TIME_1000_LESSON_FRI_8_TO_10 + VENUE_DESC_LESSON_FRI_8_TO_10
                        + NUMBER_OF_OCCURRENCES_DESC_LESSON_FRI_8_TO_10,
                expectedMessage);

        // missing venue prefix
        assertParseFailure(parser, " c/1"
                        + DAY_DESC_LESSON_FRI_8_TO_10 + START_TIME_DESC_LESSON_FRI_8_TO_10
                        + END_TIME_DESC_LESSON_FRI_8_TO_10 + VALID_VENUE_S17_0302_LESSON_FRI_8_TO_10
                        + NUMBER_OF_OCCURRENCES_DESC_LESSON_FRI_8_TO_10,
                expectedMessage);

        // missing number of occurrences prefix
        assertParseFailure(parser, " c/1"
                        + DAY_DESC_LESSON_FRI_8_TO_10 + START_TIME_DESC_LESSON_FRI_8_TO_10
                        + END_TIME_DESC_LESSON_FRI_8_TO_10 + VENUE_DESC_LESSON_FRI_8_TO_10
                        + VALID_NUMBER_OF_OCCURRENCES_13_LESSON_FRI_8_TO_10,
                expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid class index
        assertParseFailure(parser, " c/&"
                + DAY_DESC_LESSON_FRI_8_TO_10 + START_TIME_DESC_LESSON_FRI_8_TO_10
                + END_TIME_DESC_LESSON_FRI_8_TO_10 + VENUE_DESC_LESSON_FRI_8_TO_10
                + NUMBER_OF_OCCURRENCES_DESC_LESSON_FRI_8_TO_10,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddLessonCommand.MESSAGE_USAGE));

        // invalid class index
        assertParseFailure(parser, " s/1"
                        + DAY_DESC_LESSON_FRI_8_TO_10 + START_TIME_DESC_LESSON_FRI_8_TO_10
                        + END_TIME_DESC_LESSON_FRI_8_TO_10 + VENUE_DESC_LESSON_FRI_8_TO_10
                        + NUMBER_OF_OCCURRENCES_DESC_LESSON_FRI_8_TO_10,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddLessonCommand.MESSAGE_USAGE));

        // invalid day
        assertParseFailure(parser, " c/1"
                + INVALID_DAY_DESC + START_TIME_DESC_LESSON_FRI_8_TO_10
                + END_TIME_DESC_LESSON_FRI_8_TO_10 + VENUE_DESC_LESSON_FRI_8_TO_10
                + NUMBER_OF_OCCURRENCES_DESC_LESSON_FRI_8_TO_10, Day.MESSAGE_CONSTRAINTS);

        // invalid start time
        assertParseFailure(parser, " c/1"
                + DAY_DESC_LESSON_FRI_8_TO_10 + INVALID_START_TIME_DESC
                + END_TIME_DESC_LESSON_FRI_8_TO_10 + VENUE_DESC_LESSON_FRI_8_TO_10
                + NUMBER_OF_OCCURRENCES_DESC_LESSON_FRI_8_TO_10, MESSAGE_INVALID_TIME);

        // invalid end time
        assertParseFailure(parser, " c/1"
                + DAY_DESC_LESSON_FRI_8_TO_10 + START_TIME_DESC_LESSON_FRI_8_TO_10
                + INVALID_END_TIME_DESC + VENUE_DESC_LESSON_FRI_8_TO_10
                + NUMBER_OF_OCCURRENCES_DESC_LESSON_FRI_8_TO_10, MESSAGE_INVALID_TIME);

        // invalid venue
        assertParseFailure(parser, " c/1"
                + DAY_DESC_LESSON_FRI_8_TO_10 + START_TIME_DESC_LESSON_FRI_8_TO_10
                + END_TIME_DESC_LESSON_FRI_8_TO_10 + INVALID_VENUE_DESC
                + NUMBER_OF_OCCURRENCES_DESC_LESSON_FRI_8_TO_10, Venue.MESSAGE_CONSTRAINTS);

        // invalid number of occurrences
        assertParseFailure(parser, " c/1"
                + DAY_DESC_LESSON_FRI_8_TO_10 + START_TIME_DESC_LESSON_FRI_8_TO_10
                + END_TIME_DESC_LESSON_FRI_8_TO_10 + VENUE_DESC_LESSON_FRI_8_TO_10
                + INVALID_NUMBER_OF_OCCURRENCES_DESC, NumberOfOccurrences.MESSAGE_CONSTRAINTS);
    }
}
