package tutorspet.logic.parser;

import static tutorspet.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static tutorspet.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tutorspet.logic.commands.AddAttendanceCommand;
import tutorspet.logic.commands.AddLessonCommand;
import tutorspet.logic.commands.AddModuleClassCommand;
import tutorspet.logic.commands.AddStudentCommand;
import tutorspet.logic.commands.ClearModuleClassCommand;
import tutorspet.logic.commands.ClearStudentCommand;
import tutorspet.logic.commands.Command;
import tutorspet.logic.commands.DeleteAttendanceCommand;
import tutorspet.logic.commands.DeleteLessonCommand;
import tutorspet.logic.commands.DeleteModuleClassCommand;
import tutorspet.logic.commands.DeleteStudentCommand;
import tutorspet.logic.commands.EditLessonCommand;
import tutorspet.logic.commands.EditModuleClassCommand;
import tutorspet.logic.commands.EditStudentCommand;
import tutorspet.logic.commands.ExitCommand;
import tutorspet.logic.commands.FindModuleClassCommand;
import tutorspet.logic.commands.FindStudentCommand;
import tutorspet.logic.commands.HelpCommand;
import tutorspet.logic.commands.LinkCommand;
import tutorspet.logic.commands.ListCommand;
import tutorspet.logic.commands.ListModuleClassCommand;
import tutorspet.logic.commands.ListStudentCommand;
import tutorspet.logic.commands.RedoCommand;
import tutorspet.logic.commands.ResetCommand;
import tutorspet.logic.commands.UndoCommand;
import tutorspet.logic.commands.UnlinkCommand;
import tutorspet.logic.commands.ViewHistoryCommand;
import tutorspet.logic.parser.exceptions.ParseException;

/**
 * Parses user input.
 */
public class TutorsPetParser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string.
     * @return the command based on the user input.
     * @throws ParseException if the user input does not conform the expected format.
     */
    public Command parseCommand(String userInput) throws ParseException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());

        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");
        switch (commandWord) {

        case AddStudentCommand.COMMAND_WORD:
            return new AddStudentCommandParser().parse(arguments);

        case ListStudentCommand.COMMAND_WORD:
            return new ListStudentCommandParser().parse(arguments);

        case EditStudentCommand.COMMAND_WORD:
            return new EditStudentCommandParser().parse(arguments);

        case DeleteStudentCommand.COMMAND_WORD:
            return new DeleteStudentCommandParser().parse(arguments);

        case ClearStudentCommand.COMMAND_WORD:
            return new ClearStudentCommand();

        case FindStudentCommand.COMMAND_WORD:
            return new FindStudentCommandParser().parse(arguments);

        case AddModuleClassCommand.COMMAND_WORD:
            return new AddModuleClassCommandParser().parse(arguments);

        case ListModuleClassCommand.COMMAND_WORD:
            return new ListModuleClassCommand();

        case EditModuleClassCommand.COMMAND_WORD:
            return new EditModuleClassCommandParser().parse(arguments);

        case DeleteModuleClassCommand.COMMAND_WORD:
            return new DeleteModuleClassCommandParser().parse(arguments);

        case ClearModuleClassCommand.COMMAND_WORD:
            return new ClearModuleClassCommand();

        case FindModuleClassCommand.COMMAND_WORD:
            return new FindModuleClassCommandParser().parse(arguments);

        case ListCommand.COMMAND_WORD:
            return new ListCommand();

        case LinkCommand.COMMAND_WORD:
            return new LinkCommandParser().parse(arguments);

        case UnlinkCommand.COMMAND_WORD:
            return new UnlinkCommandParser().parse(arguments);

        case AddLessonCommand.COMMAND_WORD:
            return new AddLessonCommandParser().parse(arguments);

        case EditLessonCommand.COMMAND_WORD:
            return new EditLessonCommandParser().parse(arguments);

        case DeleteLessonCommand.COMMAND_WORD:
            return new DeleteLessonCommandParser().parse(arguments);

        case AddAttendanceCommand.COMMAND_WORD:
            return new AddAttendanceCommandParser().parse(arguments);

        case DeleteAttendanceCommand.COMMAND_WORD:
            return new DeleteAttendanceCommandParser().parse(arguments);

        case ViewHistoryCommand.COMMAND_WORD:
            return new ViewHistoryCommand();

        case UndoCommand.COMMAND_WORD:
            return new UndoCommand();

        case RedoCommand.COMMAND_WORD:
            return new RedoCommand();

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();

        case ResetCommand.COMMAND_WORD:
            return new ResetCommand();

        default:
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
    }
}
