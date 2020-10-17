package tutorspet.logic.parser;

import static java.util.Objects.requireNonNull;
import static tutorspet.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static tutorspet.logic.parser.CliSyntax.PREFIX_CLASS_INDEX;
import static tutorspet.logic.parser.CliSyntax.PREFIX_LESSON_INDEX;
import static tutorspet.logic.parser.CliSyntax.PREFIX_STUDENT_INDEX;
import static tutorspet.logic.parser.CliSyntax.PREFIX_WEEK;
import static tutorspet.logic.parser.ParserUtil.arePrefixesPresent;

import tutorspet.commons.core.index.Index;
import tutorspet.logic.commands.DeleteAttendanceCommand;
import tutorspet.logic.parser.exceptions.ParseException;
import tutorspet.model.attendance.Week;

/**
 * Parses input arguments and creates a new DeleteAttendanceCommand object.
 */
public class DeleteAttendanceCommandParser implements Parser<DeleteAttendanceCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteAttendanceCommand
     * and returns an DeleteAttendanceCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format.
     */
    public DeleteAttendanceCommand parse(String args) throws ParseException {
        requireNonNull(args);

        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_CLASS_INDEX, PREFIX_LESSON_INDEX, PREFIX_STUDENT_INDEX,
                        PREFIX_WEEK);

        Index moduleClassIndex;
        Index lessonIndex;
        Index studentIndex;
        Week week;

        if (!arePrefixesPresent(argMultimap, PREFIX_CLASS_INDEX, PREFIX_LESSON_INDEX, PREFIX_STUDENT_INDEX,
                PREFIX_WEEK)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteAttendanceCommand.MESSAGE_USAGE));
        }

        try {
            moduleClassIndex = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_CLASS_INDEX).get());
            lessonIndex = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_LESSON_INDEX).get());
            studentIndex = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_STUDENT_INDEX).get());
            week = ParserUtil.parseWeek(argMultimap.getValue(PREFIX_WEEK).get());
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteAttendanceCommand.MESSAGE_USAGE), pe);
        }

        return new DeleteAttendanceCommand(moduleClassIndex, lessonIndex, studentIndex, week);
    }
}
