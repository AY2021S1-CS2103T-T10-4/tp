package tutorspet.logic.parser;

import static java.util.Objects.requireNonNull;
import static tutorspet.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static tutorspet.logic.parser.CliSyntax.PREFIX_CLASS_INDEX;

import tutorspet.commons.core.index.Index;
import tutorspet.logic.commands.ListStudentCommand;
import tutorspet.logic.commands.ListStudentInClassCommand;
import tutorspet.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ListStudentCommandParser object.
 */
public class ListStudentCommandParser implements Parser<ListStudentCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ListStudentCommand
     * and returns a ListStudentCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format.
     */
    public ListStudentCommand parse(String args) throws ParseException {
        requireNonNull(args);

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_CLASS_INDEX);

        if (argMultimap.getValue(PREFIX_CLASS_INDEX).isEmpty()) {
            return new ListStudentCommand();
        }

        try {
            Index moduleClassIndex = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_CLASS_INDEX).get());
            return new ListStudentInClassCommand(moduleClassIndex);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListStudentInClassCommand.MESSAGE_USAGE), pe);
        }
    }
}
