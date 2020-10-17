package tutorspet.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tutorspet.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static tutorspet.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static tutorspet.logic.commands.CommandTestUtil.VALID_PARTICIPATION_SCORE_80;
import static tutorspet.logic.parser.CliSyntax.PREFIX_CLASS_INDEX;
import static tutorspet.logic.parser.CliSyntax.PREFIX_LESSON_INDEX;
import static tutorspet.logic.parser.CliSyntax.PREFIX_PARTICIPATION_SCORE;
import static tutorspet.logic.parser.CliSyntax.PREFIX_STUDENT_INDEX;
import static tutorspet.logic.parser.CliSyntax.PREFIX_WEEK;
import static tutorspet.testutil.Assert.assertThrows;
import static tutorspet.testutil.TypicalIndexes.INDEX_FIRST_ITEM;
import static tutorspet.testutil.TypicalIndexes.INDEX_SECOND_ITEM;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import tutorspet.logic.commands.AddAttendanceCommand;
import tutorspet.logic.commands.AddLessonCommand;
import tutorspet.logic.commands.AddModuleClassCommand;
import tutorspet.logic.commands.AddStudentCommand;
import tutorspet.logic.commands.ClearModuleClassCommand;
import tutorspet.logic.commands.ClearStudentCommand;
import tutorspet.logic.commands.DeleteAttendanceCommand;
import tutorspet.logic.commands.DeleteLessonCommand;
import tutorspet.logic.commands.DeleteModuleClassCommand;
import tutorspet.logic.commands.DeleteStudentCommand;
import tutorspet.logic.commands.EditLessonCommand;
import tutorspet.logic.commands.EditModuleClassCommand;
import tutorspet.logic.commands.EditModuleClassCommand.EditModuleClassDescriptor;
import tutorspet.logic.commands.EditStudentCommand;
import tutorspet.logic.commands.EditStudentCommand.EditStudentDescriptor;
import tutorspet.logic.commands.ExitCommand;
import tutorspet.logic.commands.FindModuleClassCommand;
import tutorspet.logic.commands.FindStudentCommand;
import tutorspet.logic.commands.HelpCommand;
import tutorspet.logic.commands.LinkCommand;
import tutorspet.logic.commands.ListCommand;
import tutorspet.logic.commands.ListModuleClassCommand;
import tutorspet.logic.commands.ListStudentCommand;
import tutorspet.logic.commands.ListStudentInClassCommand;
import tutorspet.logic.commands.RedoCommand;
import tutorspet.logic.commands.ResetCommand;
import tutorspet.logic.commands.UndoCommand;
import tutorspet.logic.commands.UnlinkCommand;
import tutorspet.logic.commands.ViewHistoryCommand;
import tutorspet.logic.parser.exceptions.ParseException;
import tutorspet.model.attendance.Attendance;
import tutorspet.model.attendance.Week;
import tutorspet.model.components.name.NameContainsKeywordsPredicate;
import tutorspet.model.lesson.Lesson;
import tutorspet.model.moduleclass.ModuleClass;
import tutorspet.model.student.Student;
import tutorspet.testutil.EditLessonDescriptorBuilder;
import tutorspet.testutil.EditModuleClassDescriptorBuilder;
import tutorspet.testutil.EditStudentDescriptorBuilder;
import tutorspet.testutil.LessonBuilder;
import tutorspet.testutil.LessonUtil;
import tutorspet.testutil.ModuleClassBuilder;
import tutorspet.testutil.ModuleClassUtil;
import tutorspet.testutil.StudentBuilder;
import tutorspet.testutil.StudentUtil;

public class TutorsPetParserTest {

    private static final String NON_EMPTY_STRING = " 3";
    private final TutorsPetParser parser = new TutorsPetParser();

    @Test
    public void parseCommand_addStudent() throws Exception {
        Student student = new StudentBuilder().build();
        AddStudentCommand command = (AddStudentCommand) parser.parseCommand(StudentUtil.getAddStudentCommand(student));
        assertEquals(new AddStudentCommand(student), command);
    }

    @Test
    public void parseCommand_listStudent() throws Exception {
        ListStudentCommand command = (ListStudentCommand) parser.parseCommand(ListStudentCommand.COMMAND_WORD);
        ListStudentCommand altCommand =
                (ListStudentCommand) parser.parseCommand(ListStudentCommand.COMMAND_WORD
                        + " " + PREFIX_CLASS_INDEX + INDEX_SECOND_ITEM.getOneBased());
        assertEquals(new ListStudentCommand(), command);
        assertEquals(new ListStudentInClassCommand(INDEX_SECOND_ITEM), altCommand);
    }

    @Test
    public void parseCommand_editStudent() throws Exception {
        Student student = new StudentBuilder().build();
        EditStudentDescriptor descriptor = new EditStudentDescriptorBuilder(student).build();
        EditStudentCommand command = (EditStudentCommand) parser.parseCommand(EditStudentCommand.COMMAND_WORD + " "
                + INDEX_FIRST_ITEM.getOneBased() + " " + StudentUtil.getEditStudentDescriptorDetails(descriptor));
        assertEquals(new EditStudentCommand(INDEX_FIRST_ITEM, descriptor), command);
    }

    @Test
    public void parseCommand_deleteStudent() throws Exception {
        DeleteStudentCommand command = (DeleteStudentCommand) parser.parseCommand(
                DeleteStudentCommand.COMMAND_WORD + " " + INDEX_FIRST_ITEM.getOneBased());
        assertEquals(new DeleteStudentCommand(INDEX_FIRST_ITEM), command);
    }

    @Test
    public void parseCommand_clearStudent() throws Exception {
        assertTrue(parser.parseCommand(ClearStudentCommand.COMMAND_WORD) instanceof ClearStudentCommand);
        assertTrue(parser.parseCommand(ClearStudentCommand.COMMAND_WORD + NON_EMPTY_STRING)
                instanceof ClearStudentCommand);
    }

    @Test
    public void parseCommand_findStudent() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindStudentCommand command = (FindStudentCommand) parser.parseCommand(
                FindStudentCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindStudentCommand(new NameContainsKeywordsPredicate<>(keywords)), command);
    }

    @Test
    public void parseCommand_addModuleClass() throws Exception {
        ModuleClass moduleClass = new ModuleClassBuilder().build();
        AddModuleClassCommand command = (AddModuleClassCommand) parser
                .parseCommand(ModuleClassUtil.getAddModuleClassCommand(moduleClass));
        assertEquals(new AddModuleClassCommand(moduleClass), command);
    }

    @Test
    public void parseCommand_listModuleClass() throws Exception {
        assertTrue(parser.parseCommand(ListModuleClassCommand.COMMAND_WORD) instanceof ListModuleClassCommand);
        assertTrue(parser.parseCommand(ListModuleClassCommand.COMMAND_WORD + NON_EMPTY_STRING)
                instanceof ListModuleClassCommand);
    }

    @Test
    public void parseCommand_editModuleClass() throws Exception {
        ModuleClass moduleClass = new ModuleClassBuilder().build();
        EditModuleClassDescriptor descriptor = new EditModuleClassDescriptorBuilder(moduleClass).build();
        EditModuleClassCommand command =
                (EditModuleClassCommand) parser.parseCommand(EditModuleClassCommand.COMMAND_WORD + " "
                        + INDEX_FIRST_ITEM.getOneBased() + " "
                        + ModuleClassUtil.getEditModuleClassDescriptorDetails(descriptor));
        assertEquals(new EditModuleClassCommand(INDEX_FIRST_ITEM, descriptor), command);
    }

    @Test
    public void parseCommand_deleteModuleClass() throws Exception {
        DeleteModuleClassCommand command = (DeleteModuleClassCommand) parser.parseCommand(
                DeleteModuleClassCommand.COMMAND_WORD + " " + INDEX_FIRST_ITEM.getOneBased());
        assertEquals(new DeleteModuleClassCommand(INDEX_FIRST_ITEM), command);
    }

    @Test
    public void parseCommand_clearModuleClass() throws Exception {
        assertTrue(parser.parseCommand(ClearModuleClassCommand.COMMAND_WORD) instanceof ClearModuleClassCommand);
        assertTrue(parser.parseCommand(ClearModuleClassCommand.COMMAND_WORD + NON_EMPTY_STRING)
                instanceof ClearModuleClassCommand);
    }

    @Test
    public void parseCommand_findModuleClass() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindModuleClassCommand command = (FindModuleClassCommand) parser.parseCommand(
                FindModuleClassCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindModuleClassCommand(new NameContainsKeywordsPredicate<>(keywords)), command);
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD) instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + NON_EMPTY_STRING) instanceof ListCommand);
    }

    @Test
    public void parseCommand_addLesson() throws Exception {
        Lesson lesson = new LessonBuilder().build();
        AddLessonCommand command =
                (AddLessonCommand) parser.parseCommand(LessonUtil.getAddLessonCommand(INDEX_FIRST_ITEM, lesson));
        assertEquals(new AddLessonCommand(INDEX_FIRST_ITEM, lesson), command);
    }

    @Test
    public void parseCommand_editLesson() throws Exception {
        Lesson lesson = new LessonBuilder().build();
        EditLessonCommand.EditLessonDescriptor descriptor = new EditLessonDescriptorBuilder(lesson).build();
        EditLessonCommand command =
                (EditLessonCommand) parser.parseCommand(EditLessonCommand.COMMAND_WORD + " "
                        + PREFIX_CLASS_INDEX + INDEX_FIRST_ITEM.getOneBased() + " "
                        + PREFIX_LESSON_INDEX + INDEX_SECOND_ITEM.getOneBased() + " "
                        + LessonUtil.getEditLessonDescriptorDetails(descriptor));
        assertEquals(new EditLessonCommand(INDEX_FIRST_ITEM, INDEX_SECOND_ITEM, descriptor), command);
    }

    @Test
    public void parseCommand_deleteLesson() throws Exception {
        DeleteLessonCommand expectedCommand = new DeleteLessonCommand(INDEX_FIRST_ITEM, INDEX_SECOND_ITEM);
        DeleteLessonCommand command =
                (DeleteLessonCommand) parser.parseCommand(DeleteLessonCommand.COMMAND_WORD + " "
                        + PREFIX_CLASS_INDEX + INDEX_FIRST_ITEM.getOneBased() + " "
                        + PREFIX_LESSON_INDEX + INDEX_SECOND_ITEM.getOneBased());
        DeleteLessonCommand altCommand =
                (DeleteLessonCommand) parser.parseCommand(DeleteLessonCommand.COMMAND_WORD + " "
                        + PREFIX_LESSON_INDEX + INDEX_SECOND_ITEM.getOneBased() + " "
                        + PREFIX_CLASS_INDEX + INDEX_FIRST_ITEM.getOneBased());
        assertEquals(expectedCommand, command);
        assertEquals(expectedCommand, altCommand);
    }

    @Test
    public void parseCommand_addAttendance() throws Exception {
        Week week = new Week(INDEX_FIRST_ITEM);
        Attendance attendance = new Attendance(VALID_PARTICIPATION_SCORE_80);
        AddAttendanceCommand expectedCommand =
                new AddAttendanceCommand(INDEX_FIRST_ITEM, INDEX_FIRST_ITEM, INDEX_FIRST_ITEM, week, attendance);
        AddAttendanceCommand command =
                (AddAttendanceCommand) parser.parseCommand(AddAttendanceCommand.COMMAND_WORD + " "
                        + PREFIX_CLASS_INDEX + INDEX_FIRST_ITEM.getOneBased() + " "
                        + PREFIX_LESSON_INDEX + INDEX_FIRST_ITEM.getOneBased() + " "
                        + PREFIX_STUDENT_INDEX + INDEX_FIRST_ITEM.getOneBased() + " "
                        + PREFIX_WEEK + INDEX_FIRST_ITEM.getOneBased() + " "
                        + PREFIX_PARTICIPATION_SCORE + "80 ");
        assertEquals(expectedCommand, command);
    }

    @Test
    public void parseCommand_deleteAttendance() throws Exception {
        Week week = new Week(INDEX_FIRST_ITEM);
        DeleteAttendanceCommand expectedCommand =
                new DeleteAttendanceCommand(INDEX_FIRST_ITEM, INDEX_FIRST_ITEM, INDEX_FIRST_ITEM, week);
        DeleteAttendanceCommand command =
                (DeleteAttendanceCommand) parser.parseCommand(DeleteAttendanceCommand.COMMAND_WORD + " "
                        + PREFIX_CLASS_INDEX + INDEX_FIRST_ITEM.getOneBased() + " "
                        + PREFIX_LESSON_INDEX + INDEX_FIRST_ITEM.getOneBased() + " "
                        + PREFIX_STUDENT_INDEX + INDEX_FIRST_ITEM.getOneBased() + " "
                        + PREFIX_WEEK + INDEX_FIRST_ITEM.getOneBased());
        assertEquals(expectedCommand, command);
    }

    @Test
    public void parseCommand_unlink() throws Exception {
        UnlinkCommand expectedCommand = new UnlinkCommand(INDEX_SECOND_ITEM, INDEX_FIRST_ITEM);
        UnlinkCommand command = (UnlinkCommand) parser.parseCommand(UnlinkCommand.COMMAND_WORD + " "
                + PREFIX_CLASS_INDEX + INDEX_SECOND_ITEM.getOneBased() + " "
                + PREFIX_STUDENT_INDEX + INDEX_FIRST_ITEM.getOneBased());
        UnlinkCommand altCommand = (UnlinkCommand) parser.parseCommand(UnlinkCommand.COMMAND_WORD + " "
                + PREFIX_STUDENT_INDEX + INDEX_FIRST_ITEM.getOneBased() + " "
                + PREFIX_CLASS_INDEX + INDEX_SECOND_ITEM.getOneBased());
        assertEquals(expectedCommand, command);
        assertEquals(expectedCommand, altCommand);
    }

    @Test
    public void parseCommand_link() throws Exception {
        LinkCommand expectedCommand = new LinkCommand(INDEX_SECOND_ITEM, INDEX_FIRST_ITEM);
        LinkCommand command = (LinkCommand) parser.parseCommand(LinkCommand.COMMAND_WORD + " "
                + PREFIX_CLASS_INDEX + INDEX_SECOND_ITEM.getOneBased() + " "
                + PREFIX_STUDENT_INDEX + INDEX_FIRST_ITEM.getOneBased());
        LinkCommand altCommand = (LinkCommand) parser.parseCommand(LinkCommand.COMMAND_WORD + " "
                + PREFIX_STUDENT_INDEX + INDEX_FIRST_ITEM.getOneBased() + " "
                + PREFIX_CLASS_INDEX + INDEX_SECOND_ITEM.getOneBased());
        assertEquals(expectedCommand, command);
        assertEquals(expectedCommand, altCommand);
    }

    @Test
    public void parseCommand_undo() throws Exception {
        assertTrue(parser.parseCommand(UndoCommand.COMMAND_WORD) instanceof UndoCommand);
    }

    @Test
    public void parseCommand_redo() throws Exception {
        assertTrue(parser.parseCommand(RedoCommand.COMMAND_WORD) instanceof RedoCommand);
    }

    @Test
    public void parseCommand_viewHistory() throws Exception {
        assertTrue(parser.parseCommand(ViewHistoryCommand.COMMAND_WORD) instanceof ViewHistoryCommand);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + NON_EMPTY_STRING) instanceof ExitCommand);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + NON_EMPTY_STRING) instanceof HelpCommand);
    }

    @Test
    public void parseCommand_reset() throws Exception {
        assertTrue(parser.parseCommand(ResetCommand.COMMAND_WORD) instanceof ResetCommand);
        assertTrue(parser.parseCommand(ResetCommand.COMMAND_WORD + NON_EMPTY_STRING) instanceof ResetCommand);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), ()
            -> parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand("unknownCommand"));
    }
}
