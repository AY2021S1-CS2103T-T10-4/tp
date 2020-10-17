package tutorspet.model;

import static java.util.Objects.requireNonNull;
import static tutorspet.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import tutorspet.commons.core.GuiSettings;
import tutorspet.commons.core.LogsCenter;
import tutorspet.model.moduleclass.ModuleClass;
import tutorspet.model.student.Student;

/**
 * Represents the in-memory model of the Tutor's Pet data.
 */
public class ModelManager implements Model {

    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final VersionedTutorsPet versionedTutorsPet;
    private final UserPrefs userPrefs;
    private final FilteredList<Student> filteredStudents;
    private final FilteredList<ModuleClass> filteredModuleClasses;

    /**
     * Initializes a ModelManager with the given tutorsPet and userPrefs.
     */
    public ModelManager(ReadOnlyTutorsPet tutorsPet, ReadOnlyUserPrefs userPrefs) {
        super();
        requireAllNonNull(tutorsPet, userPrefs);

        logger.fine("Initializing with Tutor's Pet: " + tutorsPet + " and user prefs " + userPrefs);

        versionedTutorsPet = new VersionedTutorsPet(tutorsPet);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredStudents = new FilteredList<>(versionedTutorsPet.getStudentList());
        filteredModuleClasses = new FilteredList<>(versionedTutorsPet.getModuleClassList());
    }

    public ModelManager() {
        this(new TutorsPet(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);

        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);

        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getTutorsPetFilePath() {
        return userPrefs.getTutorsPetFilePath();
    }

    @Override
    public void setTutorsPetFilePath(Path tutorsPetFilePath) {
        requireNonNull(tutorsPetFilePath);

        userPrefs.setTutorsPetFilePath(tutorsPetFilePath);
    }

    //=========== TutorsPet ================================================================================

    @Override
    public void setTutorsPet(ReadOnlyTutorsPet tutorsPet) {
        versionedTutorsPet.resetData(tutorsPet);
    }

    @Override
    public ReadOnlyTutorsPet getTutorsPet() {
        return versionedTutorsPet;
    }

    @Override
    public void commit(String commitMessage) {
        versionedTutorsPet.commit(commitMessage);
    }

    @Override
    public boolean canUndo() {
        return versionedTutorsPet.canUndo();
    }

    @Override
    public String undo() {
        return versionedTutorsPet.undo();
    }

    @Override
    public boolean canRedo() {
        return versionedTutorsPet.canRedo();
    }

    @Override
    public String redo() {
        return versionedTutorsPet.redo();
    }

    @Override
    public StateRecords viewStateRecords() {
        return versionedTutorsPet.viewStateRecords();
    }

    @Override
    public boolean hasStudent(Student student) {
        requireNonNull(student);

        return versionedTutorsPet.hasStudent(student);
    }

    @Override
    public void addStudent(Student student) {
        versionedTutorsPet.addStudent(student);
        updateFilteredStudentList(PREDICATE_SHOW_ALL_STUDENTS);
    }

    @Override
    public void setStudent(Student target, Student editedStudent) {
        requireAllNonNull(target, editedStudent);

        versionedTutorsPet.setStudent(target, editedStudent);
    }

    @Override
    public void deleteStudent(Student target) {
        requireNonNull(target);

        versionedTutorsPet.deleteStudent(target);
    }

    @Override
    public void deleteAllStudents() {
        versionedTutorsPet.deleteAllStudents();
    }

    @Override
    public boolean hasModuleClass(ModuleClass moduleClass) {
        requireNonNull(moduleClass);

        return versionedTutorsPet.hasModuleClass(moduleClass);
    }

    @Override
    public void addModuleClass(ModuleClass moduleClass) {
        versionedTutorsPet.addModuleClass(moduleClass);
        updateFilteredModuleClassList(PREDICATE_SHOW_ALL_MODULE_CLASS);
    }

    @Override
    public void setModuleClass(ModuleClass target, ModuleClass editedModuleClass) {
        requireAllNonNull(target, editedModuleClass);

        versionedTutorsPet.setModuleClass(target, editedModuleClass);
    }

    @Override
    public void deleteModuleClass(ModuleClass target) {
        versionedTutorsPet.deleteModuleClass(target);
    }

    @Override
    public void deleteAllModuleClasses() {
        versionedTutorsPet.deleteAllModuleClasses();
    }

    //=========== Filtered Student List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Student} backed by the internal list of
     * {@code versionedTutorsPet}
     */
    @Override
    public ObservableList<Student> getFilteredStudentList() {
        return filteredStudents;
    }

    @Override
    public void updateFilteredStudentList(Predicate<Student> predicate) {
        requireNonNull(predicate);

        filteredStudents.setPredicate(predicate);
    }

    //=========== Filtered ModuleClass List Accessors =============================================================

    @Override
    public ObservableList<ModuleClass> getFilteredModuleClassList() {
        return filteredModuleClasses;
    }

    @Override
    public void updateFilteredModuleClassList(Predicate<ModuleClass> predicate) {
        requireNonNull(predicate);

        filteredModuleClasses.setPredicate(predicate);
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return versionedTutorsPet.equals(other.versionedTutorsPet)
                && userPrefs.equals(other.userPrefs)
                && filteredStudents.equals(other.filteredStudents)
                && filteredModuleClasses.equals(other.filteredModuleClasses);
    }
}
