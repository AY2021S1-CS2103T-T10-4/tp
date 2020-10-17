package tutorspet.logic.parser;

import static tutorspet.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static tutorspet.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static tutorspet.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static tutorspet.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static tutorspet.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static tutorspet.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static tutorspet.logic.commands.CommandTestUtil.INVALID_TELEGRAM_DESC;
import static tutorspet.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static tutorspet.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static tutorspet.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static tutorspet.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static tutorspet.logic.commands.CommandTestUtil.TAG_DESC_AVERAGE;
import static tutorspet.logic.commands.CommandTestUtil.TAG_DESC_EXPERIENCED;
import static tutorspet.logic.commands.CommandTestUtil.TELEGRAM_DESC_AMY;
import static tutorspet.logic.commands.CommandTestUtil.TELEGRAM_DESC_BOB;
import static tutorspet.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static tutorspet.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static tutorspet.logic.commands.CommandTestUtil.VALID_TAG_AVERAGE;
import static tutorspet.logic.commands.CommandTestUtil.VALID_TAG_EXPERIENCED;
import static tutorspet.logic.commands.CommandTestUtil.VALID_TELEGRAM_BOB;
import static tutorspet.logic.parser.CommandParserTestUtil.assertParseFailure;
import static tutorspet.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static tutorspet.testutil.TypicalStudent.AMY;
import static tutorspet.testutil.TypicalStudent.BOB;

import org.junit.jupiter.api.Test;

import tutorspet.logic.commands.AddStudentCommand;
import tutorspet.model.components.name.Name;
import tutorspet.model.components.tag.Tag;
import tutorspet.model.student.Email;
import tutorspet.model.student.Student;
import tutorspet.model.student.Telegram;
import tutorspet.testutil.StudentBuilder;

public class AddStudentCommandParserTest {

    private AddStudentCommandParser parser = new AddStudentCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Student expectedStudent = new StudentBuilder(BOB).withTags(VALID_TAG_EXPERIENCED).build();

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + NAME_DESC_BOB + TELEGRAM_DESC_BOB + EMAIL_DESC_BOB
                + TAG_DESC_EXPERIENCED, new AddStudentCommand(expectedStudent));

        // multiple names - last name accepted
        assertParseSuccess(parser, NAME_DESC_AMY + NAME_DESC_BOB + TELEGRAM_DESC_BOB + EMAIL_DESC_BOB
                + TAG_DESC_EXPERIENCED, new AddStudentCommand(expectedStudent));

        // multiple telegram - last telegram accepted
        assertParseSuccess(parser, NAME_DESC_BOB + TELEGRAM_DESC_AMY + TELEGRAM_DESC_BOB + EMAIL_DESC_BOB
                + TAG_DESC_EXPERIENCED, new AddStudentCommand(expectedStudent));

        // multiple emails - last email accepted
        assertParseSuccess(parser, NAME_DESC_BOB + TELEGRAM_DESC_BOB + EMAIL_DESC_AMY + EMAIL_DESC_BOB
                + TAG_DESC_EXPERIENCED, new AddStudentCommand(expectedStudent));

        // multiple tags - all accepted
        Student expectedStudentMultipleTags = new StudentBuilder(BOB)
                .withTags(VALID_TAG_EXPERIENCED, VALID_TAG_AVERAGE).build();
        assertParseSuccess(parser, NAME_DESC_BOB + TELEGRAM_DESC_BOB + EMAIL_DESC_BOB
                + TAG_DESC_AVERAGE + TAG_DESC_EXPERIENCED, new AddStudentCommand(expectedStudentMultipleTags));
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        // zero tags
        Student expectedStudent = new StudentBuilder(AMY).withTags().build();
        assertParseSuccess(parser, NAME_DESC_AMY + TELEGRAM_DESC_AMY + EMAIL_DESC_AMY,
                new AddStudentCommand(expectedStudent));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddStudentCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser, VALID_NAME_BOB + TELEGRAM_DESC_BOB + EMAIL_DESC_BOB,
                expectedMessage);

        // missing telegram prefix
        assertParseFailure(parser, NAME_DESC_BOB + VALID_TELEGRAM_BOB + EMAIL_DESC_BOB,
                expectedMessage);

        // missing email prefix
        assertParseFailure(parser, NAME_DESC_BOB + TELEGRAM_DESC_BOB + VALID_EMAIL_BOB,
                expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, VALID_NAME_BOB + VALID_TELEGRAM_BOB + VALID_EMAIL_BOB,
                expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + TELEGRAM_DESC_BOB + EMAIL_DESC_BOB
                + TAG_DESC_AVERAGE + TAG_DESC_EXPERIENCED, Name.MESSAGE_CONSTRAINTS);

        // invalid telegram
        assertParseFailure(parser, NAME_DESC_BOB + INVALID_TELEGRAM_DESC + EMAIL_DESC_BOB
                + TAG_DESC_AVERAGE + TAG_DESC_EXPERIENCED, Telegram.MESSAGE_CONSTRAINTS);

        // invalid email
        assertParseFailure(parser, NAME_DESC_BOB + TELEGRAM_DESC_BOB + INVALID_EMAIL_DESC
                + TAG_DESC_AVERAGE + TAG_DESC_EXPERIENCED, Email.MESSAGE_CONSTRAINTS);

        // invalid tag
        assertParseFailure(parser, NAME_DESC_BOB + TELEGRAM_DESC_BOB + EMAIL_DESC_BOB
                + INVALID_TAG_DESC + VALID_TAG_EXPERIENCED, Tag.MESSAGE_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + NAME_DESC_BOB + TELEGRAM_DESC_BOB + EMAIL_DESC_BOB
                + TAG_DESC_AVERAGE + TAG_DESC_EXPERIENCED,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddStudentCommand.MESSAGE_USAGE));

        // two invalid values, only first invalid value reported
        assertParseFailure(parser, INVALID_NAME_DESC + TELEGRAM_DESC_BOB + INVALID_EMAIL_DESC,
                Name.MESSAGE_CONSTRAINTS);
    }
}
