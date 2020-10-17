package tutorspet.logic.parser;

import static tutorspet.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static tutorspet.logic.parser.CommandParserTestUtil.assertParseFailure;
import static tutorspet.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import tutorspet.logic.commands.FindModuleClassCommand;
import tutorspet.model.components.name.NameContainsKeywordsPredicate;

public class FindModuleClassCommandParserTest {

    private FindModuleClassCommandParser parser = new FindModuleClassCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                FindModuleClassCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindModuleClassCommand() {
        FindModuleClassCommand expectedFindModuleClassCommand =
                new FindModuleClassCommand(new NameContainsKeywordsPredicate<>(Arrays.asList("CS1101S", "Tutorial")));

        // no leading and trailing whitespaces
        assertParseSuccess(parser, "CS1101S Tutorial", expectedFindModuleClassCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n CS1101S \n \t Tutorial  \t", expectedFindModuleClassCommand);
    }
}
