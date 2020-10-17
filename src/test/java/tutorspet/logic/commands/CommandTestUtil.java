package tutorspet.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tutorspet.logic.parser.CliSyntax.PREFIX_DAY;
import static tutorspet.logic.parser.CliSyntax.PREFIX_EMAIL;
import static tutorspet.logic.parser.CliSyntax.PREFIX_END_TIME;
import static tutorspet.logic.parser.CliSyntax.PREFIX_NAME;
import static tutorspet.logic.parser.CliSyntax.PREFIX_NUMBER_OF_OCCURRENCES;
import static tutorspet.logic.parser.CliSyntax.PREFIX_PARTICIPATION_SCORE;
import static tutorspet.logic.parser.CliSyntax.PREFIX_START_TIME;
import static tutorspet.logic.parser.CliSyntax.PREFIX_TAG;
import static tutorspet.logic.parser.CliSyntax.PREFIX_TELEGRAM;
import static tutorspet.logic.parser.CliSyntax.PREFIX_VENUE;
import static tutorspet.logic.parser.CliSyntax.PREFIX_WEEK;
import static tutorspet.testutil.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import tutorspet.commons.core.index.Index;
import tutorspet.logic.commands.exceptions.CommandException;
import tutorspet.model.Model;
import tutorspet.model.TutorsPet;
import tutorspet.model.attendance.Attendance;
import tutorspet.model.attendance.AttendanceRecord;
import tutorspet.model.attendance.AttendanceRecordList;
import tutorspet.model.attendance.Week;
import tutorspet.model.lesson.Day;
import tutorspet.model.moduleclass.ModuleClass;
import tutorspet.model.student.Student;
import tutorspet.testutil.EditLessonDescriptorBuilder;
import tutorspet.testutil.EditModuleClassDescriptorBuilder;
import tutorspet.testutil.EditStudentDescriptorBuilder;
import tutorspet.testutil.LessonBuilder;
import tutorspet.testutil.StudentBuilder;

/**
 * Contains helper methods for testing commands.
 */
public class CommandTestUtil {

    public static final String PREAMBLE_WHITESPACE = "\t  \r  \n";
    public static final String PREAMBLE_NON_EMPTY = "NonEmptyPreamble";

    // student-related constants

    public static final String VALID_UUID_AMY = "99fa08df-5cd6-42cd-84dd-3e08cfe74224";
    public static final String VALID_UUID_BOB = "a3090aa6-6633-43bd-b137-64c514299e59";
    public static final String VALID_NAME_AMY = "Amy Bee";
    public static final String VALID_NAME_BOB = "Bob Choo";
    public static final String VALID_TELEGRAM_AMY = "Amy_B";
    public static final String VALID_TELEGRAM_BOB = "bobCHoo";
    public static final String VALID_EMAIL_AMY = "amy@example.com";
    public static final String VALID_EMAIL_BOB = "bob@example.com";
    public static final String VALID_TAG_AVERAGE = "average";
    public static final String VALID_TAG_EXPERIENCED = "experienced";

    public static final String NAME_DESC_AMY = " " + PREFIX_NAME + VALID_NAME_AMY;
    public static final String NAME_DESC_BOB = " " + PREFIX_NAME + VALID_NAME_BOB;
    public static final String TELEGRAM_DESC_AMY = " " + PREFIX_TELEGRAM + VALID_TELEGRAM_AMY;
    public static final String TELEGRAM_DESC_BOB = " " + PREFIX_TELEGRAM + VALID_TELEGRAM_BOB;
    public static final String EMAIL_DESC_AMY = " " + PREFIX_EMAIL + VALID_EMAIL_AMY;
    public static final String EMAIL_DESC_BOB = " " + PREFIX_EMAIL + VALID_EMAIL_BOB;
    public static final String TAG_DESC_EXPERIENCED = " " + PREFIX_TAG + VALID_TAG_EXPERIENCED;
    public static final String TAG_DESC_AVERAGE = " " + PREFIX_TAG + VALID_TAG_AVERAGE;

    public static final String INVALID_NAME = "James&";
    public static final String INVALID_STUDENT_UUID = "584346cb-8886-4518-8282-";
    public static final String INVALID_NAME_DESC = " " + PREFIX_NAME + INVALID_NAME; // '&' not allowed in names
    public static final String INVALID_TELEGRAM_DESC = " " + PREFIX_TELEGRAM + "91%19"; // '%' not allowed in telegram
    public static final String INVALID_EMAIL_DESC = " " + PREFIX_EMAIL + "bob!yahoo"; // missing '@' symbol
    public static final String INVALID_TAG_DESC = " " + PREFIX_TAG + "good*"; // '*' not allowed in tags

    public static final EditStudentCommand.EditStudentDescriptor DESC_AMY;
    public static final EditStudentCommand.EditStudentDescriptor DESC_BOB;

    static {
        DESC_AMY = new EditStudentDescriptorBuilder(new StudentBuilder().withUuid(VALID_UUID_AMY).build())
                .withName(VALID_NAME_AMY).withTelegram(VALID_TELEGRAM_AMY)
                .withEmail(VALID_EMAIL_AMY).withTags(VALID_TAG_EXPERIENCED).build();
        DESC_BOB = new EditStudentDescriptorBuilder(new StudentBuilder().withUuid(VALID_UUID_BOB).build())
                .withName(VALID_NAME_BOB).withTelegram(VALID_TELEGRAM_BOB)
                .withEmail(VALID_EMAIL_BOB).withTags(VALID_TAG_AVERAGE, VALID_TAG_EXPERIENCED).build();
    }

    // moduleClass-related constants

    public static final String VALID_NAME_CS2103T_TUTORIAL = "CS2103T Tutorial";
    public static final String VALID_NAME_CS2100_LAB = "CS2100 Lab";
    public static final String VALID_NAME_CS2030_TUTORIAL = "CS2030 Tutorial";
    public static final String VALID_NAME_CS2100_TUTORIAL = "CS2100 Tutorial";

    public static final String NAME_DESC_CS2103T_TUTORIAL = " " + PREFIX_NAME + VALID_NAME_CS2103T_TUTORIAL;
    public static final String NAME_DESC_CS2100_LAB = " " + PREFIX_NAME + VALID_NAME_CS2100_LAB;

    public static final EditModuleClassCommand.EditModuleClassDescriptor DESC_CS2100_LAB;
    public static final EditModuleClassCommand.EditModuleClassDescriptor DESC_CS2103T_TUTORIAL;

    static {
        DESC_CS2100_LAB = new EditModuleClassDescriptorBuilder().withName(VALID_NAME_CS2100_LAB).build();
        DESC_CS2103T_TUTORIAL = new EditModuleClassDescriptorBuilder().withName(VALID_NAME_CS2103T_TUTORIAL).build();
    }

    // lesson-related constants

    public static final String VALID_START_TIME_1400_LESSON_WED_2_TO_4 = "14:00";
    public static final String VALID_START_TIME_0800_LESSON_FRI_8_TO_10 = "08:00";
    public static final String VALID_END_TIME_1600_LESSON_WED_2_TO_4 = "16:00";
    public static final String VALID_END_TIME_1000_LESSON_FRI_8_TO_10 = "10:00";
    public static final Day VALID_DAY_WED_LESSON_WED_2_TO_4 = Day.WEDNESDAY;
    public static final Day VALID_DAY_FRI_LESSON_FRI_8_TO_10 = Day.FRIDAY;
    public static final int VALID_NUMBER_OF_OCCURRENCES_7_LESSON_WED_2_TO_4 = 7;
    public static final int VALID_NUMBER_OF_OCCURRENCES_13_LESSON_FRI_8_TO_10 = 13;
    public static final String VALID_VENUE_COM1_B111_LESSON_WED_2_TO_4 = "COM1-B111";
    public static final String VALID_VENUE_S17_0302_LESSON_FRI_8_TO_10 = "S17-0302";

    // WED_2_TO_4
    public static final String START_TIME_DESC_LESSON_WED_2_TO_4 =
            " " + PREFIX_START_TIME + VALID_START_TIME_1400_LESSON_WED_2_TO_4;
    public static final String END_TIME_DESC_LESSON_WED_2_TO_4 =
            " " + PREFIX_END_TIME + VALID_END_TIME_1600_LESSON_WED_2_TO_4;
    public static final String DAY_DESC_LESSON_WED_2_TO_4 =
            " " + PREFIX_DAY + VALID_DAY_WED_LESSON_WED_2_TO_4;
    public static final String NUMBER_OF_OCCURRENCES_DESC_LESSON_WED_2_TO_4 =
            " " + PREFIX_NUMBER_OF_OCCURRENCES + VALID_NUMBER_OF_OCCURRENCES_7_LESSON_WED_2_TO_4;
    public static final String VENUE_DESC_LESSON_WED_2_TO_4 =
            " " + PREFIX_VENUE + VALID_VENUE_COM1_B111_LESSON_WED_2_TO_4;

    // FRI_8_TO_10
    public static final String START_TIME_DESC_LESSON_FRI_8_TO_10 =
            " " + PREFIX_START_TIME + VALID_START_TIME_0800_LESSON_FRI_8_TO_10;
    public static final String END_TIME_DESC_LESSON_FRI_8_TO_10 =
            " " + PREFIX_END_TIME + VALID_END_TIME_1000_LESSON_FRI_8_TO_10;
    public static final String DAY_DESC_LESSON_FRI_8_TO_10 =
            " " + PREFIX_DAY + VALID_DAY_FRI_LESSON_FRI_8_TO_10;
    public static final String NUMBER_OF_OCCURRENCES_DESC_LESSON_FRI_8_TO_10 =
            " " + PREFIX_NUMBER_OF_OCCURRENCES + VALID_NUMBER_OF_OCCURRENCES_13_LESSON_FRI_8_TO_10;
    public static final String VENUE_DESC_LESSON_FRI_8_TO_10 =
            " " + PREFIX_VENUE + VALID_VENUE_S17_0302_LESSON_FRI_8_TO_10;

    public static final String INVALID_DAY_DESC = " " + PREFIX_DAY + "ASDDAY"; // 'ASDDAY' is not a day
    public static final String INVALID_START_TIME_DESC =
            " " + PREFIX_START_TIME + "123412x"; // '123412x' is not a valid time
    public static final String INVALID_END_TIME_DESC =
            " " + PREFIX_END_TIME + "123412x"; // '123412x' is not a valid time
    public static final String INVALID_VENUE_DESC = " " + PREFIX_VENUE + "&&&&&&"; // '&&&&&&' is not a venue
    public static final String INVALID_NUMBER_OF_OCCURRENCES_DESC =
            " " + PREFIX_NUMBER_OF_OCCURRENCES + "asd"; // 'asd' is not a number

    public static final EditLessonCommand.EditLessonDescriptor DESC_LESSON_WED_2_TO_4;
    public static final EditLessonCommand.EditLessonDescriptor DESC_LESSON_FRI_8_TO_10;

    static {
        DESC_LESSON_WED_2_TO_4 = new EditLessonDescriptorBuilder(new LessonBuilder().build())
                .withStartTime(VALID_START_TIME_1400_LESSON_WED_2_TO_4)
                .withEndTime(VALID_END_TIME_1600_LESSON_WED_2_TO_4)
                .withDay(VALID_DAY_WED_LESSON_WED_2_TO_4.toString())
                .withVenue(VALID_VENUE_COM1_B111_LESSON_WED_2_TO_4).build();
        DESC_LESSON_FRI_8_TO_10 = new EditLessonDescriptorBuilder(new LessonBuilder().build())
                .withStartTime(VALID_START_TIME_0800_LESSON_FRI_8_TO_10)
                .withEndTime(VALID_END_TIME_1000_LESSON_FRI_8_TO_10)
                .withDay(VALID_DAY_FRI_LESSON_FRI_8_TO_10.toString())
                .withVenue(VALID_VENUE_S17_0302_LESSON_FRI_8_TO_10).build();
    }

    // attendance-related constants

    public static final int VALID_WEEK_VALUE_3 = 3;
    public static final int VALID_WEEK_VALUE_5 = 5;
    public static final Index VALID_WEEK_INDEX_5 = Index.fromOneBased(VALID_WEEK_VALUE_5);
    public static final int VALID_PARTICIPATION_SCORE_33 = 33;
    public static final int VALID_PARTICIPATION_SCORE_51 = 51;
    public static final int VALID_PARTICIPATION_SCORE_80 = 80;
    public static final Week VALID_WEEK_1 = new Week(Index.fromOneBased(1));
    public static final Week VALID_WEEK_5 = new Week(Index.fromOneBased(VALID_WEEK_VALUE_5));
    public static final Attendance VALID_ATTENDANCE = new Attendance(VALID_PARTICIPATION_SCORE_80);
    public static final Attendance VALID_ATTENDANCE_33 = new Attendance(VALID_PARTICIPATION_SCORE_33);
    public static final Attendance VALID_ATTENDANCE_51 = new Attendance(VALID_PARTICIPATION_SCORE_51);

    public static final String PARTICIPATION_SCORE_DESC_80 =
            " " + PREFIX_PARTICIPATION_SCORE + VALID_PARTICIPATION_SCORE_80;
    public static final String PARTICIPATION_SCORE_DESC_51 =
            " " + PREFIX_PARTICIPATION_SCORE + VALID_PARTICIPATION_SCORE_51;
    public static final String WEEK_DESC_WEEK_VALUE_5 =
            " " + PREFIX_WEEK + VALID_WEEK_VALUE_5;
    public static final String WEEK_DESC_WEEK_VALUE_3 =
            " " + PREFIX_WEEK + VALID_WEEK_VALUE_3;

    public static final AttendanceRecord EMPTY_ATTENDANCE_RECORD = new AttendanceRecord();
    public static final Map<UUID, Attendance> VALID_ATTENDANCE_LIST = new HashMap<>();

    static {
        VALID_ATTENDANCE_LIST.put(UUID.fromString(VALID_UUID_AMY), new Attendance(VALID_PARTICIPATION_SCORE_33));
        VALID_ATTENDANCE_LIST.put(UUID.fromString(VALID_UUID_BOB), new Attendance(VALID_PARTICIPATION_SCORE_51));
    }

    /**
     * A {@code AttendanceRecord} for testing with two {@code Attendance}s:
     * - AMY with score 33
     * - BOB with score 51
     */
    public static final AttendanceRecord VALID_ATTENDANCE_RECORD = new AttendanceRecord(VALID_ATTENDANCE_LIST);
    /**
     * A {@code AttendanceRecordList} for testing with two {@code AttendanceRecord}s:
     * - Week 1: Empty
     * - Week 2: As defined in {@link CommandTestUtil#VALID_ATTENDANCE_RECORD}.
     */
    public static final AttendanceRecordList VALID_ATTENDANCE_RECORD_LIST =
            new AttendanceRecordList(Arrays.asList(EMPTY_ATTENDANCE_RECORD, VALID_ATTENDANCE_RECORD));

    public static final int INVALID_PARTICIPATION_SCORE_MINUS_1 = -1;
    public static final int INVALID_PARTICIPATION_SCORE_101 = 101;
    public static final String INVALID_PARTICIPATION_LOWER_BOUND_SCORE_DESC =
            " " + PREFIX_PARTICIPATION_SCORE + "-1";
    public static final String INVALID_PARTICIPATION_UPPER_BOUND_SCORE_DESC =
            " " + PREFIX_PARTICIPATION_SCORE + "101";
    public static final String INVALID_WEEK_LOWER_BOUND_DESC =
            " " + PREFIX_WEEK + "0";
    public static final String INVALID_WEEK_UPPER_BOUND_DESC =
            " " + PREFIX_WEEK + "53";

    /**
     * Executes the given {@code command}, confirms that <br>
     * - the returned {@link CommandResult} matches {@code expectedCommandResult} <br>
     * - the {@code actualModel} matches {@code expectedModel}
     */
    public static void assertCommandSuccess(Command command, Model actualModel, CommandResult expectedCommandResult,
            Model expectedModel) {
        try {
            CommandResult result = command.execute(actualModel);
            assertEquals(expectedCommandResult, result);
            assertEquals(expectedModel, actualModel);
        } catch (CommandException ce) {
            throw new AssertionError("Execution of command should not fail.", ce);
        }
    }

    /**
     * Convenience wrapper to {@link #assertCommandSuccess(Command, Model, CommandResult, Model)}
     * that takes a string {@code expectedMessage}.
     */
    public static void assertCommandSuccess(Command command, Model actualModel, String expectedMessage,
            Model expectedModel) {
        CommandResult expectedCommandResult = new CommandResult(expectedMessage);
        assertCommandSuccess(command, actualModel, expectedCommandResult, expectedModel);
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - a {@code CommandException} is thrown <br>
     * - the CommandException message matches {@code expectedMessage} <br>
     * - the Tutor's Pet, filtered student list and filtered module class list in {@code actualModel} remain unchanged
     */
    public static void assertCommandFailure(Command command, Model actualModel, String expectedMessage) {
        // we are unable to defensively copy the model for comparison later, so we can
        // only do so by copying its components.
        TutorsPet expectedTutorsPet = new TutorsPet(actualModel.getTutorsPet());
        List<Student> expectedFilteredStudentList = new ArrayList<>(actualModel.getFilteredStudentList());
        List<ModuleClass> expectedFilteredModuleClassList = new ArrayList<>(actualModel.getFilteredModuleClassList());

        assertThrows(CommandException.class, expectedMessage, () -> command.execute(actualModel));
        assertEquals(expectedTutorsPet, actualModel.getTutorsPet());
        assertEquals(expectedFilteredStudentList, actualModel.getFilteredStudentList());
        assertEquals(expectedFilteredModuleClassList, actualModel.getFilteredModuleClassList());
    }

    /**
     * Updates {@code model}'s {@code filteredStudents} list to show only the student at the given
     * {@code targetIndex} in the {@code model}'s Tutor's Pet.
     */
    public static void showStudentAtIndex(Model model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < model.getFilteredStudentList().size());

        Student student = model.getFilteredStudentList().get(targetIndex.getZeroBased());
        model.updateFilteredStudentList(toTest -> toTest.equals(student));

        assertEquals(1, model.getFilteredStudentList().size());
    }

    /**
     * Updates {@code model}'s {@code filteredModuleClasses} list to show only the ModuleClass at the given
     * {@code targetIndex} in the {@code model}'s Tutor's Pet.
     */
    public static void showModuleClassAtIndex(Model model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < model.getFilteredModuleClassList().size());

        ModuleClass moduleClass = model.getFilteredModuleClassList().get(targetIndex.getZeroBased());
        model.updateFilteredModuleClassList(toTest -> toTest.equals(moduleClass));

        assertEquals(1, model.getFilteredModuleClassList().size());
    }
}
