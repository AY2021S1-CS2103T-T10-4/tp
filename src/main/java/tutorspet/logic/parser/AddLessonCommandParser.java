package tutorspet.logic.parser;

import static java.util.Objects.requireNonNull;
import static tutorspet.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static tutorspet.logic.parser.CliSyntax.PREFIX_CLASS_INDEX;
import static tutorspet.logic.parser.CliSyntax.PREFIX_DAY;
import static tutorspet.logic.parser.CliSyntax.PREFIX_END_TIME;
import static tutorspet.logic.parser.CliSyntax.PREFIX_NUMBER_OF_OCCURRENCES;
import static tutorspet.logic.parser.CliSyntax.PREFIX_START_TIME;
import static tutorspet.logic.parser.CliSyntax.PREFIX_VENUE;
import static tutorspet.logic.parser.ParserUtil.arePrefixesPresent;

import java.time.LocalTime;

import tutorspet.commons.core.index.Index;
import tutorspet.logic.commands.AddLessonCommand;
import tutorspet.logic.parser.exceptions.ParseException;
import tutorspet.model.lesson.Day;
import tutorspet.model.lesson.Lesson;
import tutorspet.model.lesson.NumberOfOccurrences;
import tutorspet.model.lesson.Venue;

/**
 * Parses input arguments and creates a new AddLessonCommand object.
 */
public class AddLessonCommandParser implements Parser<AddLessonCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddLessonCommand
     * and returns an AddLessonCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format.
     */
    public AddLessonCommand parse(String args) throws ParseException {
        requireNonNull(args);

        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_CLASS_INDEX, PREFIX_DAY,
                        PREFIX_START_TIME, PREFIX_END_TIME, PREFIX_VENUE, PREFIX_NUMBER_OF_OCCURRENCES);

        Index moduleClassIndex;

        // parse moduleClass index
        boolean isModuleClassIndexPresent = argMultimap.getValue(PREFIX_CLASS_INDEX).isPresent();
        if (!isModuleClassIndexPresent) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddLessonCommand.MESSAGE_USAGE));
        }

        try {
            moduleClassIndex = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_CLASS_INDEX).get());
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddLessonCommand.MESSAGE_USAGE), pe);
        }

        // parse lesson data values
        if (!arePrefixesPresent(argMultimap, PREFIX_DAY,
                PREFIX_START_TIME, PREFIX_END_TIME, PREFIX_VENUE, PREFIX_NUMBER_OF_OCCURRENCES)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddLessonCommand.MESSAGE_USAGE));
        }

        Day day = ParserUtil.parseDay(argMultimap.getValue(PREFIX_DAY).get());
        LocalTime startTime = ParserUtil.parseTime(argMultimap.getValue(PREFIX_START_TIME).get());
        LocalTime endTime = ParserUtil.parseTime(argMultimap.getValue(PREFIX_END_TIME).get());
        Venue venue = ParserUtil.parseVenue(argMultimap.getValue(PREFIX_VENUE).get());
        NumberOfOccurrences numberOfOccurrences =
                ParserUtil.parseNumberOfOccurrences(argMultimap.getValue(PREFIX_NUMBER_OF_OCCURRENCES).get());

        Lesson lesson = new Lesson(startTime, endTime, day, numberOfOccurrences, venue);

        return new AddLessonCommand(moduleClassIndex, lesson);
    }
}
