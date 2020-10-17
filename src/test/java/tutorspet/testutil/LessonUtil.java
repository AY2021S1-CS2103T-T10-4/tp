package tutorspet.testutil;

import static tutorspet.logic.parser.CliSyntax.PREFIX_CLASS_INDEX;
import static tutorspet.logic.parser.CliSyntax.PREFIX_DAY;
import static tutorspet.logic.parser.CliSyntax.PREFIX_END_TIME;
import static tutorspet.logic.parser.CliSyntax.PREFIX_NUMBER_OF_OCCURRENCES;
import static tutorspet.logic.parser.CliSyntax.PREFIX_START_TIME;
import static tutorspet.logic.parser.CliSyntax.PREFIX_VENUE;

import tutorspet.commons.core.index.Index;
import tutorspet.logic.commands.AddLessonCommand;
import tutorspet.logic.commands.EditLessonCommand;
import tutorspet.model.lesson.Lesson;

/**
 * A utility class for Lesson.
 */
public class LessonUtil {

    /**
     * Returns an add lesson command string for adding the {@code lesson}.
     */
    public static String getAddLessonCommand(Index moduleClassIndex, Lesson lesson) {
        return AddLessonCommand.COMMAND_WORD + " "
                + PREFIX_CLASS_INDEX + moduleClassIndex.getOneBased() + " "
                + getLessonDetails(lesson);
    }

    /**
     * Returns the part of the command string for the given {@code lesson}'s details.
     */
    public static String getLessonDetails(Lesson lesson) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_DAY + lesson.getDay().toString() + " ");
        sb.append(PREFIX_START_TIME + lesson.getStartTime().toString() + " ");
        sb.append(PREFIX_END_TIME + lesson.getEndTime().toString() + " ");
        sb.append(PREFIX_VENUE + lesson.getVenue().toString() + " ");
        sb.append(PREFIX_NUMBER_OF_OCCURRENCES + lesson.getNumberOfOccurrences().toString() + " ");
        return sb.toString();
    }

    /**
     * Returns the part of command string for the given {@code EditLessonDescriptor}'s details.
     */
    public static String getEditLessonDescriptorDetails(EditLessonCommand.EditLessonDescriptor descriptor) {
        StringBuilder sb = new StringBuilder();
        descriptor.getStartTime().ifPresent(startTime -> sb.append(PREFIX_START_TIME)
                .append(startTime.toString()).append(" "));
        descriptor.getEndTime().ifPresent(endTime -> sb.append(PREFIX_END_TIME)
                .append(endTime.toString()).append(" "));
        descriptor.getDay().ifPresent(day -> sb.append(PREFIX_DAY).append(day.toString()).append(" "));
        descriptor.getVenue().ifPresent(venue -> sb.append(PREFIX_VENUE).append(venue.toString()).append(" "));

        return sb.toString();
    }
}
