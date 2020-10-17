package tutorspet.logic;

import java.nio.file.Path;

import javafx.collections.ObservableList;
import tutorspet.commons.core.GuiSettings;
import tutorspet.logic.commands.CommandResult;
import tutorspet.logic.commands.exceptions.CommandException;
import tutorspet.logic.parser.exceptions.ParseException;
import tutorspet.model.ReadOnlyTutorsPet;
import tutorspet.model.moduleclass.ModuleClass;
import tutorspet.model.student.Student;

/**
 * API of the Logic component.
 */
public interface Logic {
    /**
     * Executes the command and returns the result.
     * @param commandText The command as entered by the user.
     * @return the result of the command execution.
     * @throws CommandException If an error occurs during command execution.
     * @throws ParseException If an error occurs during parsing.
     */
    CommandResult execute(String commandText) throws CommandException, ParseException;

    /**
     * Returns the TutorsPet.
     *
     * @see tutorspet.model.Model#getTutorsPet()
     */
    ReadOnlyTutorsPet getTutorsPet();

    /**
     * Returns an unmodifiable view of the filtered list of students.
     */
    ObservableList<Student> getFilteredStudentList();

    /**
     * Returns an unmodifiable view of the filtered list of classes.
     */
    ObservableList<ModuleClass> getFilteredModuleClassList();

    /**
     * Returns the user prefs' Tutor's Pet file path.
     */
    Path getTutorsPetFilePath();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Set the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);
}
