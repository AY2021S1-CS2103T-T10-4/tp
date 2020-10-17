package tutorspet.logic.parser;

import static tutorspet.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static tutorspet.logic.parser.CliSyntax.PREFIX_NAME;
import static tutorspet.logic.parser.ParserUtil.arePrefixesPresent;

import tutorspet.logic.commands.AddModuleClassCommand;
import tutorspet.logic.parser.exceptions.ParseException;
import tutorspet.model.components.name.Name;
import tutorspet.model.moduleclass.ModuleClass;

/**
 * Parses input arguments and creates a new AddModuleClassCommand object.
 */
public class AddModuleClassCommandParser implements Parser<AddModuleClassCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddModuleClassCommand
     * and returns an AddModuleClassCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format.
     */
    public AddModuleClassCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME);

        if (!arePrefixesPresent(argMultimap, PREFIX_NAME)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddModuleClassCommand.MESSAGE_USAGE));
        }

        Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());

        ModuleClass moduleClass = new ModuleClass(name);

        return new AddModuleClassCommand(moduleClass);
    }
}
