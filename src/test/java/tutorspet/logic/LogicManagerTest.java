package tutorspet.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static tutorspet.commons.core.Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX;
import static tutorspet.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static tutorspet.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static tutorspet.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static tutorspet.logic.commands.CommandTestUtil.TELEGRAM_DESC_AMY;
import static tutorspet.testutil.Assert.assertThrows;
import static tutorspet.testutil.TypicalStudent.AMY;

import java.io.IOException;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import tutorspet.logic.commands.AddStudentCommand;
import tutorspet.logic.commands.CommandResult;
import tutorspet.logic.commands.ListStudentCommand;
import tutorspet.logic.commands.exceptions.CommandException;
import tutorspet.logic.parser.exceptions.ParseException;
import tutorspet.model.Model;
import tutorspet.model.ModelManager;
import tutorspet.model.ReadOnlyTutorsPet;
import tutorspet.model.UserPrefs;
import tutorspet.model.student.Student;
import tutorspet.storage.JsonTutorsPetStorage;
import tutorspet.storage.JsonUserPrefsStorage;
import tutorspet.storage.StorageManager;
import tutorspet.testutil.StudentBuilder;

public class LogicManagerTest {

    private static final IOException DUMMY_IO_EXCEPTION = new IOException("dummy exception");

    @TempDir
    public Path temporaryFolder;

    private Model model = new ModelManager();
    private Logic logic;

    @BeforeEach
    public void setUp() {
        JsonTutorsPetStorage tutorsPetStorage =
                new JsonTutorsPetStorage(temporaryFolder.resolve("tutorsPet.json"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(temporaryFolder.resolve("userPrefs.json"));
        StorageManager storage = new StorageManager(tutorsPetStorage, userPrefsStorage);
        logic = new LogicManager(model, storage);
    }

    @Test
    public void execute_invalidCommandFormat_throwsParseException() {
        String invalidCommand = "uicfhmowqewca";
        assertParseException(invalidCommand, MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void execute_commandExecutionError_throwsCommandException() {
        String deleteStudentCommand = "delete-student 9";
        assertCommandException(deleteStudentCommand, MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validCommand_success() throws Exception {
        String listCommand = ListStudentCommand.COMMAND_WORD;
        assertCommandSuccess(listCommand, ListStudentCommand.MESSAGE_LIST_ALL_SUCCESS, model);
    }

    @Test
    public void execute_storageThrowsIoException_throwsCommandException() {
        // Setup LogicManager with JsonTutorsPetIoExceptionThrowingStub
        JsonTutorsPetStorage tutorsPetStorage =
                new JsonTutorsPetIoExceptionThrowingStub(temporaryFolder.resolve("ioExceptionTutorsPet.json"));
        JsonUserPrefsStorage userPrefsStorage =
                new JsonUserPrefsStorage(temporaryFolder.resolve("ioExceptionUserPrefs.json"));
        StorageManager storage = new StorageManager(tutorsPetStorage, userPrefsStorage);
        logic = new LogicManager(model, storage);

        // Execute add student command
        String addStudentCommand = AddStudentCommand.COMMAND_WORD + NAME_DESC_AMY + TELEGRAM_DESC_AMY + EMAIL_DESC_AMY;
        Student expectedStudent = new StudentBuilder(AMY).withTags().build();
        ModelManager expectedModel = new ModelManager();
        expectedModel.addStudent(expectedStudent);
        expectedModel.commit(String.format(AddStudentCommand.MESSAGE_SUCCESS, expectedStudent));
        String expectedMessage = LogicManager.FILE_OPS_ERROR_MESSAGE + DUMMY_IO_EXCEPTION;
        assertCommandFailure(addStudentCommand, CommandException.class, expectedMessage, expectedModel);
    }

    @Test
    public void getFilteredStudentList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> logic.getFilteredStudentList().remove(0));
    }

    @Test
    public void getFilteredModuleClassList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> logic.getFilteredModuleClassList().remove(0));
    }

    /**
     * Executes the command and confirms that
     * - no exceptions are thrown <br>
     * - the feedback message is equal to {@code expectedMessage} <br>
     * - the internal model manager state is the same as that in {@code expectedModel} <br>
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandSuccess(String inputCommand, String expectedMessage,
            Model expectedModel) throws CommandException, ParseException {
        CommandResult result = logic.execute(inputCommand);
        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertEquals(expectedModel, model);
    }

    /**
     * Executes the command, confirms that a ParseException is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertParseException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, ParseException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that a CommandException is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, CommandException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that the exception is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandFailure(String inputCommand, Class<? extends Throwable> expectedException,
            String expectedMessage) {
        Model expectedModel = new ModelManager(model.getTutorsPet(), new UserPrefs());
        assertCommandFailure(inputCommand, expectedException, expectedMessage, expectedModel);
    }

    /**
     * Executes the command and confirms that
     * - the {@code expectedException} is thrown <br>
     * - the resulting error message is equal to {@code expectedMessage} <br>
     * - the internal model manager state is the same as that in {@code expectedModel} <br>
     * @see #assertCommandSuccess(String, String, Model)
     */
    private void assertCommandFailure(String inputCommand, Class<? extends Throwable> expectedException,
            String expectedMessage, Model expectedModel) {
        assertThrows(expectedException, expectedMessage, () -> logic.execute(inputCommand));
        assertEquals(expectedModel, model);
    }

    /**
     * A stub class to throw an {@code IOException} when the save method is called.
     */
    private static class JsonTutorsPetIoExceptionThrowingStub extends JsonTutorsPetStorage {

        private JsonTutorsPetIoExceptionThrowingStub(Path filePath) {
            super(filePath);
        }

        @Override
        public void saveTutorsPet(ReadOnlyTutorsPet tutorsPet, Path filePath) throws IOException {
            throw DUMMY_IO_EXCEPTION;
        }
    }
}
