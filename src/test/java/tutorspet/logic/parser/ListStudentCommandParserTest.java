package tutorspet.logic.parser;

import static tutorspet.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static tutorspet.logic.parser.CommandParserTestUtil.assertParseFailure;
import static tutorspet.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static tutorspet.testutil.TypicalIndexes.INDEX_FIRST_ITEM;
import static tutorspet.testutil.TypicalIndexes.INDEX_SECOND_ITEM;

import org.junit.jupiter.api.Test;

import tutorspet.logic.commands.ListStudentCommand;
import tutorspet.logic.commands.ListStudentInClassCommand;

public class ListStudentCommandParserTest {

    private ListStudentCommandParser parser = new ListStudentCommandParser();

    @Test
    public void parse_validArgs_returnsLinkCommand() {
        // no args
        assertParseSuccess(parser, " ", new ListStudentCommand());

        // class specified
        assertParseSuccess(parser, " c/2", new ListStudentInClassCommand(INDEX_SECOND_ITEM));

        // multiple class indexes specified -> last class index accepted
        assertParseSuccess(parser, " c/2 c/1", new ListStudentInClassCommand(INDEX_FIRST_ITEM));
    }

    @Test
    public void parse_emptyClassIndex_throwsParseException() {
        assertParseFailure(parser, " c/", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                ListStudentInClassCommand.MESSAGE_USAGE));
    }
}
