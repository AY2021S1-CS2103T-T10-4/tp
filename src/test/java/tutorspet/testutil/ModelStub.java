package tutorspet.testutil;

import java.nio.file.Path;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import tutorspet.commons.core.GuiSettings;
import tutorspet.model.Model;
import tutorspet.model.ReadOnlyTutorsPet;
import tutorspet.model.ReadOnlyUserPrefs;
import tutorspet.model.StateRecords;
import tutorspet.model.moduleclass.ModuleClass;
import tutorspet.model.student.Student;

/**
 * A default model stub that have all of the methods failing.
 */
public class ModelStub implements Model {

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public GuiSettings getGuiSettings() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public Path getTutorsPetFilePath() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void setTutorsPetFilePath(Path tutorsPetFilePath) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void setTutorsPet(ReadOnlyTutorsPet newData) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public ReadOnlyTutorsPet getTutorsPet() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void commit(String commitMessage) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public boolean canUndo() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public String undo() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public boolean canRedo() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public String redo() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public StateRecords viewStateRecords() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public boolean hasStudent(Student student) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void addStudent(Student student) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void setStudent(Student target, Student editedStudent) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void deleteStudent(Student target) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void deleteAllStudents() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public boolean hasModuleClass(ModuleClass moduleClass) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void addModuleClass(ModuleClass moduleClass) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void setModuleClass(ModuleClass target, ModuleClass editedModuleClass) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void deleteModuleClass(ModuleClass target) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void deleteAllModuleClasses() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public ObservableList<Student> getFilteredStudentList() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void updateFilteredStudentList(Predicate<Student> predicate) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public ObservableList<ModuleClass> getFilteredModuleClassList() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void updateFilteredModuleClassList(Predicate<ModuleClass> predicate) {
        throw new AssertionError("This method should not be called.");
    }
}
