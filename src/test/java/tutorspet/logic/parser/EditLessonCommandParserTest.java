package tutorspet.logic.parser;

import static tutorspet.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static tutorspet.logic.commands.CommandTestUtil.DAY_DESC_LESSON_FRI_8_TO_10;
import static tutorspet.logic.commands.CommandTestUtil.DAY_DESC_LESSON_WED_2_TO_4;
import static tutorspet.logic.commands.CommandTestUtil.END_TIME_DESC_LESSON_FRI_8_TO_10;
import static tutorspet.logic.commands.CommandTestUtil.INVALID_DAY_DESC;
import static tutorspet.logic.commands.CommandTestUtil.INVALID_END_TIME_DESC;
import static tutorspet.logic.commands.CommandTestUtil.INVALID_START_TIME_DESC;
import static tutorspet.logic.commands.CommandTestUtil.INVALID_VENUE_DESC;
import static tutorspet.logic.commands.CommandTestUtil.START_TIME_DESC_LESSON_FRI_8_TO_10;
import static tutorspet.logic.commands.CommandTestUtil.VALID_DAY_FRI_LESSON_FRI_8_TO_10;
import static tutorspet.logic.commands.CommandTestUtil.VALID_DAY_WED_LESSON_WED_2_TO_4;
import static tutorspet.logic.commands.CommandTestUtil.VALID_END_TIME_1000_LESSON_FRI_8_TO_10;
import static tutorspet.logic.commands.CommandTestUtil.VALID_START_TIME_0800_LESSON_FRI_8_TO_10;
import static tutorspet.logic.commands.CommandTestUtil.VALID_VENUE_COM1_B111_LESSON_WED_2_TO_4;
import static tutorspet.logic.commands.CommandTestUtil.VALID_VENUE_S17_0302_LESSON_FRI_8_TO_10;
import static tutorspet.logic.commands.CommandTestUtil.VENUE_DESC_LESSON_FRI_8_TO_10;
import static tutorspet.logic.commands.CommandTestUtil.VENUE_DESC_LESSON_WED_2_TO_4;
import static tutorspet.logic.parser.CommandParserTestUtil.assertParseFailure;
import static tutorspet.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static tutorspet.testutil.TypicalIndexes.INDEX_FIRST_ITEM;
import static tutorspet.testutil.TypicalIndexes.INDEX_SECOND_ITEM;

import org.junit.jupiter.api.Test;

import tutorspet.commons.core.index.Index;
import tutorspet.logic.commands.EditLessonCommand;
import tutorspet.model.lesson.Day;
import tutorspet.model.lesson.Venue;
import tutorspet.testutil.EditLessonDescriptorBuilder;

public class EditLessonCommandParserTest {

    private static final Index VALID_MODULE_CLASS_INDEX_ONE = INDEX_FIRST_ITEM;
    private static final Index VALID_LESSON_INDEX_TWO = INDEX_SECOND_ITEM;
    private static final String INPUT_PREAMBLE =
            " c/" + VALID_MODULE_CLASS_INDEX_ONE.getOneBased() + " l/" + VALID_LESSON_INDEX_TWO.getOneBased();
    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditLessonCommand.MESSAGE_USAGE);

    private EditLessonCommandParser parser = new EditLessonCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no class index specified
        assertParseFailure(parser, " l/1" + DAY_DESC_LESSON_FRI_8_TO_10, MESSAGE_INVALID_FORMAT);

        // no lesson index specified
        assertParseFailure(parser, " c/1" + DAY_DESC_LESSON_FRI_8_TO_10, MESSAGE_INVALID_FORMAT);

        // no class and lesson index
        assertParseFailure(parser, DAY_DESC_LESSON_FRI_8_TO_10, MESSAGE_INVALID_FORMAT);

        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, " c/-5 l/1" + DAY_DESC_LESSON_FRI_8_TO_10, MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, " c/1 l/0" + DAY_DESC_LESSON_FRI_8_TO_10, MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, " 1 some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, " c/1 l/1 i/ string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, " c/1 l/1" + INVALID_DAY_DESC, Day.MESSAGE_CONSTRAINTS); // invalid day
        assertParseFailure(
                parser, " c/1 l/1" + INVALID_START_TIME_DESC, ParserUtil.MESSAGE_INVALID_TIME); // invalid start time
        assertParseFailure(
                parser, " c/1 l/1" + INVALID_END_TIME_DESC, ParserUtil.MESSAGE_INVALID_TIME); // invalid end time
        assertParseFailure(parser, " c/1 l/1" + INVALID_VENUE_DESC, Venue.MESSAGE_CONSTRAINTS); // invalid venue

        // invalid venue followed by valid start time
        assertParseFailure(parser, " c/1 l/1" + INVALID_VENUE_DESC + START_TIME_DESC_LESSON_FRI_8_TO_10,
                Venue.MESSAGE_CONSTRAINTS);

        // valid venue followed by invalid venue. The test case for invalid venue followed by valid venue
        // is tested at {@code parse_invalidValueFollowedByValidValue_success()}
        assertParseFailure(parser, " c/1 l/1" + VENUE_DESC_LESSON_FRI_8_TO_10 + INVALID_VENUE_DESC,
                Venue.MESSAGE_CONSTRAINTS);

        // multiple invalid values, but only the first invalid value is captured
        assertParseFailure(parser,
                " c/1 l/1" + INVALID_DAY_DESC + INVALID_START_TIME_DESC + VALID_START_TIME_0800_LESSON_FRI_8_TO_10,
                Day.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        String userInput = INPUT_PREAMBLE
                + DAY_DESC_LESSON_FRI_8_TO_10 + START_TIME_DESC_LESSON_FRI_8_TO_10
                + END_TIME_DESC_LESSON_FRI_8_TO_10 + VENUE_DESC_LESSON_FRI_8_TO_10;

        EditLessonCommand.EditLessonDescriptor descriptor = new EditLessonDescriptorBuilder()
                .withDay(VALID_DAY_FRI_LESSON_FRI_8_TO_10.toString())
                .withStartTime(VALID_START_TIME_0800_LESSON_FRI_8_TO_10)
                .withEndTime(VALID_END_TIME_1000_LESSON_FRI_8_TO_10)
                .withVenue(VALID_VENUE_S17_0302_LESSON_FRI_8_TO_10).build();
        EditLessonCommand expectedCommand = new EditLessonCommand(
                VALID_MODULE_CLASS_INDEX_ONE, VALID_LESSON_INDEX_TWO, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_someFieldsSpecified_success() {
        String userInput = INPUT_PREAMBLE + DAY_DESC_LESSON_FRI_8_TO_10 + END_TIME_DESC_LESSON_FRI_8_TO_10;

        EditLessonCommand.EditLessonDescriptor descriptor = new EditLessonDescriptorBuilder()
                .withDay(VALID_DAY_FRI_LESSON_FRI_8_TO_10.toString())
                .withEndTime(VALID_END_TIME_1000_LESSON_FRI_8_TO_10).build();
        EditLessonCommand expectedCommand = new EditLessonCommand(
                VALID_MODULE_CLASS_INDEX_ONE, VALID_LESSON_INDEX_TWO, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_oneFieldSpecified_success() {

        // day
        String userInput = INPUT_PREAMBLE + DAY_DESC_LESSON_FRI_8_TO_10;
        EditLessonCommand.EditLessonDescriptor descriptor =
                new EditLessonDescriptorBuilder().withDay(VALID_DAY_FRI_LESSON_FRI_8_TO_10.toString()).build();
        EditLessonCommand expectedCommand = new EditLessonCommand(
                VALID_MODULE_CLASS_INDEX_ONE, VALID_LESSON_INDEX_TWO, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // start time
        userInput = INPUT_PREAMBLE + START_TIME_DESC_LESSON_FRI_8_TO_10;
        descriptor = new EditLessonDescriptorBuilder().withStartTime(VALID_START_TIME_0800_LESSON_FRI_8_TO_10).build();
        expectedCommand = new EditLessonCommand(
                VALID_MODULE_CLASS_INDEX_ONE, VALID_LESSON_INDEX_TWO, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // end time
        userInput = INPUT_PREAMBLE + END_TIME_DESC_LESSON_FRI_8_TO_10;
        descriptor = new EditLessonDescriptorBuilder().withEndTime(VALID_END_TIME_1000_LESSON_FRI_8_TO_10).build();
        expectedCommand = new EditLessonCommand(
                VALID_MODULE_CLASS_INDEX_ONE, VALID_LESSON_INDEX_TWO, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // venue
        userInput = INPUT_PREAMBLE + VENUE_DESC_LESSON_FRI_8_TO_10;
        descriptor = new EditLessonDescriptorBuilder().withVenue(VALID_VENUE_S17_0302_LESSON_FRI_8_TO_10).build();
        expectedCommand = new EditLessonCommand(
                VALID_MODULE_CLASS_INDEX_ONE, VALID_LESSON_INDEX_TWO, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedFields_acceptsLast() {
        String userInput = INPUT_PREAMBLE + VENUE_DESC_LESSON_FRI_8_TO_10 + DAY_DESC_LESSON_FRI_8_TO_10
                + VENUE_DESC_LESSON_FRI_8_TO_10 + DAY_DESC_LESSON_WED_2_TO_4 + VENUE_DESC_LESSON_WED_2_TO_4;

        EditLessonCommand.EditLessonDescriptor descriptor =
                new EditLessonDescriptorBuilder()
                        .withDay(VALID_DAY_WED_LESSON_WED_2_TO_4.toString())
                        .withVenue(VALID_VENUE_COM1_B111_LESSON_WED_2_TO_4).build();
        EditLessonCommand expectedCommand = new EditLessonCommand(
                VALID_MODULE_CLASS_INDEX_ONE, VALID_LESSON_INDEX_TWO, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_invalidValueFollowedByValidValue_success() {
        // no other valid values specified
        String userInput = INPUT_PREAMBLE + INVALID_END_TIME_DESC + END_TIME_DESC_LESSON_FRI_8_TO_10;
        EditLessonCommand.EditLessonDescriptor descriptor = new EditLessonDescriptorBuilder()
                .withEndTime(VALID_END_TIME_1000_LESSON_FRI_8_TO_10).build();
        EditLessonCommand expectedCommand = new EditLessonCommand(
                VALID_MODULE_CLASS_INDEX_ONE, VALID_LESSON_INDEX_TWO, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // other valid values specified
        userInput = INPUT_PREAMBLE + START_TIME_DESC_LESSON_FRI_8_TO_10
                + INVALID_END_TIME_DESC + END_TIME_DESC_LESSON_FRI_8_TO_10;
        descriptor = new EditLessonDescriptorBuilder()
                .withStartTime(VALID_START_TIME_0800_LESSON_FRI_8_TO_10)
                .withEndTime(VALID_END_TIME_1000_LESSON_FRI_8_TO_10).build();
        expectedCommand = new EditLessonCommand(
                VALID_MODULE_CLASS_INDEX_ONE, VALID_LESSON_INDEX_TWO, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }
}
