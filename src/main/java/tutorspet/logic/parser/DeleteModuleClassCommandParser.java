package tutorspet.logic.parser;

import static tutorspet.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import tutorspet.commons.core.index.Index;
import tutorspet.logic.commands.DeleteModuleClassCommand;
import tutorspet.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DeleteModuleClassCommand object.
 */
public class DeleteModuleClassCommandParser implements Parser<DeleteModuleClassCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteModuleClassCommand
     * and returns a DeleteModuleClassCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format.
     */
    public DeleteModuleClassCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new DeleteModuleClassCommand(index);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteModuleClassCommand.MESSAGE_USAGE), pe);
        }
    }
}
