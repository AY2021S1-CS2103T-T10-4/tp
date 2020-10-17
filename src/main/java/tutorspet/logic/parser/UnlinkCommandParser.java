package tutorspet.logic.parser;

import static java.util.Objects.requireNonNull;
import static tutorspet.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static tutorspet.logic.parser.CliSyntax.PREFIX_CLASS_INDEX;
import static tutorspet.logic.parser.CliSyntax.PREFIX_STUDENT_INDEX;

import tutorspet.commons.core.index.Index;
import tutorspet.logic.commands.UnlinkCommand;
import tutorspet.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new UnlinkCommandParser object.
 */
public class UnlinkCommandParser implements Parser<UnlinkCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the UnlinkCommand
     * and returns a UnlinkCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format.
     */
    public UnlinkCommand parse(String args) throws ParseException {
        requireNonNull(args);

        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_STUDENT_INDEX, PREFIX_CLASS_INDEX);

        Index studentIndex;
        Index moduleClassIndex;

        boolean isStudentIndexPresent = argMultimap.getValue(PREFIX_STUDENT_INDEX).isPresent();
        boolean isModuleClassIndexPresent = argMultimap.getValue(PREFIX_CLASS_INDEX).isPresent();
        if (!isStudentIndexPresent || !isModuleClassIndexPresent) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnlinkCommand.MESSAGE_USAGE));
        }

        try {
            studentIndex = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_STUDENT_INDEX).get());
            moduleClassIndex = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_CLASS_INDEX).get());
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnlinkCommand.MESSAGE_USAGE), pe);
        }

        return new UnlinkCommand(moduleClassIndex, studentIndex);
    }
}
