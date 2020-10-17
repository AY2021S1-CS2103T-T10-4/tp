package tutorspet.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tutorspet.logic.commands.CommandTestUtil.DESC_LESSON_FRI_8_TO_10;
import static tutorspet.logic.commands.CommandTestUtil.DESC_LESSON_WED_2_TO_4;
import static tutorspet.logic.commands.CommandTestUtil.VALID_DAY_FRI_LESSON_FRI_8_TO_10;
import static tutorspet.logic.commands.CommandTestUtil.VALID_VENUE_S17_0302_LESSON_FRI_8_TO_10;
import static tutorspet.logic.commands.CommandTestUtil.assertCommandFailure;
import static tutorspet.logic.commands.CommandTestUtil.assertCommandSuccess;
import static tutorspet.logic.commands.CommandTestUtil.showModuleClassAtIndex;
import static tutorspet.testutil.Assert.assertThrows;
import static tutorspet.testutil.TypicalIndexes.INDEX_FIRST_ITEM;
import static tutorspet.testutil.TypicalIndexes.INDEX_SECOND_ITEM;
import static tutorspet.testutil.TypicalIndexes.INDEX_THIRD_ITEM;
import static tutorspet.testutil.TypicalLesson.LESSON_FRI_8_TO_10;
import static tutorspet.testutil.TypicalTutorsPet.getTypicalTutorsPet;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import tutorspet.commons.core.Messages;
import tutorspet.commons.core.index.Index;
import tutorspet.model.Model;
import tutorspet.model.ModelManager;
import tutorspet.model.TutorsPet;
import tutorspet.model.UserPrefs;
import tutorspet.model.attendance.AttendanceRecordList;
import tutorspet.model.lesson.Lesson;
import tutorspet.model.moduleclass.ModuleClass;
import tutorspet.testutil.EditLessonDescriptorBuilder;
import tutorspet.testutil.LessonBuilder;

public class EditLessonCommandTest {

    private static final Lesson EDITED_LESSON = new LessonBuilder().build();
    private static final EditLessonCommand.EditLessonDescriptor EDITED_LESSON_DESCRIPTOR =
            new EditLessonDescriptorBuilder(EDITED_LESSON).build();

    private Model model = new ModelManager(getTypicalTutorsPet(), new UserPrefs());

    @Test
    public void constructor_nullModuleClasdIndex_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new EditLessonCommand(
                null, INDEX_FIRST_ITEM, EDITED_LESSON_DESCRIPTOR));
    }

    @Test
    public void constructor_nullLessonIndex_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new EditLessonCommand(
                INDEX_FIRST_ITEM, null, EDITED_LESSON_DESCRIPTOR));
    }

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        EditLessonCommand.EditLessonDescriptor editLessonDescriptor =
                new EditLessonDescriptorBuilder(DESC_LESSON_FRI_8_TO_10).build();
        EditLessonCommand editLessonCommand = new EditLessonCommand(
                INDEX_FIRST_ITEM, INDEX_FIRST_ITEM, editLessonDescriptor);

        ModuleClass moduleClass = model.getFilteredModuleClassList().get(INDEX_FIRST_ITEM.getZeroBased());
        AttendanceRecordList attendanceRecordList = getAttendancerecordList(moduleClass, INDEX_FIRST_ITEM);
        Lesson editedLesson = new LessonBuilder(LESSON_FRI_8_TO_10)
                .withAttendanceRecordList(attendanceRecordList).build();

        List<Lesson> editedLessonList = Arrays.asList(editedLesson);
        ModuleClass updatedModuleClass = new ModuleClass(
                moduleClass.getName(), moduleClass.getStudentUuids(), editedLessonList);

        String expectedMessage = String.format(EditLessonCommand.MESSAGE_EDIT_LESSON_SUCCESS, editedLesson);

        Model expectedModel = new ModelManager(new TutorsPet(model.getTutorsPet()), new UserPrefs());
        expectedModel.setModuleClass(moduleClass, updatedModuleClass);
        expectedModel.commit(expectedMessage);

        assertCommandSuccess(editLessonCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        EditLessonCommand.EditLessonDescriptor descriptor = new EditLessonDescriptorBuilder()
                .withDay(VALID_DAY_FRI_LESSON_FRI_8_TO_10.toString())
                .withVenue(VALID_VENUE_S17_0302_LESSON_FRI_8_TO_10).build();
        EditLessonCommand editLessonCommand = new EditLessonCommand(INDEX_FIRST_ITEM, INDEX_FIRST_ITEM, descriptor);

        ModuleClass firstModuleClass = model.getFilteredModuleClassList()
                .get(INDEX_FIRST_ITEM.getZeroBased());
        LessonBuilder firstLessonInList = new LessonBuilder(firstModuleClass.getLessons()
                .get(INDEX_FIRST_ITEM.getZeroBased()));
        AttendanceRecordList attendanceRecordList = getAttendancerecordList(firstModuleClass, INDEX_FIRST_ITEM);

        Lesson editedLesson = firstLessonInList
                .withDay(VALID_DAY_FRI_LESSON_FRI_8_TO_10)
                .withVenue(VALID_VENUE_S17_0302_LESSON_FRI_8_TO_10)
                .withAttendanceRecordList(attendanceRecordList).build();
        List<Lesson> editedLessonList = Arrays.asList(editedLesson);
        ModuleClass updatedModuleClass = new ModuleClass(
                firstModuleClass.getName(), firstModuleClass.getStudentUuids(), editedLessonList);

        String expectedMessage = String.format(EditLessonCommand.MESSAGE_EDIT_LESSON_SUCCESS, editedLesson);

        Model expectedModel = new ModelManager(new TutorsPet(model.getTutorsPet()), new UserPrefs());
        expectedModel.setModuleClass(firstModuleClass, updatedModuleClass);
        expectedModel.commit(expectedMessage);

        assertCommandSuccess(editLessonCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        EditLessonCommand editLessonCommand = new EditLessonCommand(
                INDEX_FIRST_ITEM, INDEX_FIRST_ITEM, new EditLessonCommand.EditLessonDescriptor());
        System.out.println(new EditLessonCommand.EditLessonDescriptor().getDay());
        Lesson editedLesson = model.getFilteredModuleClassList().get(INDEX_FIRST_ITEM.getZeroBased())
                .getLessons().get(INDEX_FIRST_ITEM.getZeroBased());

        String expectedMessage = String.format(EditLessonCommand.MESSAGE_EDIT_LESSON_SUCCESS, editedLesson);

        Model expectedModel = new ModelManager(new TutorsPet(model.getTutorsPet()), new UserPrefs());
        expectedModel.commit(expectedMessage);

        assertCommandSuccess(editLessonCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showModuleClassAtIndex(model, INDEX_SECOND_ITEM);

        EditLessonCommand editLessonCommand = new EditLessonCommand(INDEX_FIRST_ITEM, INDEX_SECOND_ITEM,
                new EditLessonDescriptorBuilder().withVenue(VALID_VENUE_S17_0302_LESSON_FRI_8_TO_10).build());

        ModuleClass moduleClassInFilteredList = model.getFilteredModuleClassList()
                .get(INDEX_FIRST_ITEM.getZeroBased());
        Lesson lessonToEdit = moduleClassInFilteredList.getLessons().get(INDEX_SECOND_ITEM.getZeroBased());
        AttendanceRecordList attendanceRecordList =
                getAttendancerecordList(moduleClassInFilteredList, INDEX_SECOND_ITEM);

        Lesson editedLesson = new LessonBuilder(lessonToEdit)
                .withVenue(VALID_VENUE_S17_0302_LESSON_FRI_8_TO_10)
                .withAttendanceRecordList(attendanceRecordList).build();

        String expectedMessage = String.format(EditLessonCommand.MESSAGE_EDIT_LESSON_SUCCESS, editedLesson);

        List<Lesson> editedLessonList = Arrays.asList(
                moduleClassInFilteredList.getLessons().get(INDEX_FIRST_ITEM.getZeroBased()),
                editedLesson);
        ModuleClass updatedModuleClass = new ModuleClass(
                moduleClassInFilteredList.getName(), moduleClassInFilteredList.getStudentUuids(), editedLessonList);

        Model expectedModel = new ModelManager(new TutorsPet(model.getTutorsPet()), new UserPrefs());
        expectedModel.setModuleClass(moduleClassInFilteredList, updatedModuleClass);
        expectedModel.commit(expectedMessage);

        assertCommandSuccess(editLessonCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateLessonUnfilteredList_failure() {
        ModuleClass secondModuleClass = model.getFilteredModuleClassList().get(INDEX_SECOND_ITEM.getZeroBased());
        Lesson secondClassFirstLesson = secondModuleClass.getLessons().get(INDEX_FIRST_ITEM.getZeroBased());

        EditLessonCommand.EditLessonDescriptor descriptor =
                new EditLessonDescriptorBuilder(secondClassFirstLesson).build();
        EditLessonCommand editLessonCommand = new EditLessonCommand(INDEX_SECOND_ITEM, INDEX_SECOND_ITEM, descriptor);

        assertCommandFailure(editLessonCommand, model, EditLessonCommand.MESSAGE_DUPLICATE_LESSON);
    }

    @Test
    public void execute_invalidModuleClassIndex_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredModuleClassList().size() + 1);
        EditLessonCommand editLessonCommand = new EditLessonCommand(outOfBoundIndex, INDEX_FIRST_ITEM,
                new EditLessonDescriptorBuilder().withVenue(VALID_VENUE_S17_0302_LESSON_FRI_8_TO_10).build());

        assertCommandFailure(editLessonCommand, model, Messages.MESSAGE_INVALID_MODULE_CLASS_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of class list.
     */
    @Test
    public void execute_invalidModuleClassIndexFilteredList_failure() {
        showModuleClassAtIndex(model, INDEX_SECOND_ITEM);
        Index outOfBoundIndex = INDEX_THIRD_ITEM;
        // ensures that outOfBoundIndex is still in bounds of student list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getTutorsPet().getModuleClassList().size());

        EditLessonCommand editLessonCommand = new EditLessonCommand(outOfBoundIndex, INDEX_FIRST_ITEM,
                new EditLessonDescriptorBuilder().withVenue(VALID_VENUE_S17_0302_LESSON_FRI_8_TO_10).build());

        assertCommandFailure(editLessonCommand, model, Messages.MESSAGE_INVALID_MODULE_CLASS_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidLessonIndex_failure() {
        Index moduleClassIndex = INDEX_FIRST_ITEM;
        Index outOfBoundIndex = INDEX_THIRD_ITEM;
        EditLessonCommand editLessonCommand = new EditLessonCommand(moduleClassIndex, outOfBoundIndex,
                new EditLessonDescriptorBuilder().withVenue(VALID_VENUE_S17_0302_LESSON_FRI_8_TO_10).build());

        assertCommandFailure(editLessonCommand, model, Messages.MESSAGE_INVALID_LESSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        final EditLessonCommand standardCommand = new EditLessonCommand(
                INDEX_FIRST_ITEM, INDEX_FIRST_ITEM, DESC_LESSON_FRI_8_TO_10);

        // same values -> returns true
        EditLessonCommand.EditLessonDescriptor copyDescriptor =
                new EditLessonCommand.EditLessonDescriptor(DESC_LESSON_FRI_8_TO_10);
        EditLessonCommand commandWithSameValues = new EditLessonCommand(
                INDEX_FIRST_ITEM, INDEX_FIRST_ITEM, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(1));

        // different module class index -> returns false
        assertFalse(standardCommand.equals(new EditLessonCommand(
                INDEX_SECOND_ITEM, INDEX_FIRST_ITEM, DESC_LESSON_FRI_8_TO_10)));

        // different lesson index -> returns false
        assertFalse(standardCommand.equals(new EditLessonCommand(
                INDEX_FIRST_ITEM, INDEX_SECOND_ITEM, DESC_LESSON_FRI_8_TO_10)));

        // different class and lesson index -> returns false
        assertFalse(standardCommand.equals(new EditLessonCommand(
                INDEX_SECOND_ITEM, INDEX_SECOND_ITEM, DESC_LESSON_FRI_8_TO_10)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditLessonCommand(
                INDEX_FIRST_ITEM, INDEX_FIRST_ITEM, DESC_LESSON_WED_2_TO_4)));
    }

    private AttendanceRecordList getAttendancerecordList(ModuleClass moduleClass, Index index) {
        return moduleClass.getLessons().get(index.getZeroBased()).getAttendanceRecordList();
    }
}
