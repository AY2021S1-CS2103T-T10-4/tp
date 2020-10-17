package tutorspet.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tutorspet.logic.commands.CommandTestUtil.assertCommandFailure;
import static tutorspet.logic.commands.CommandTestUtil.assertCommandSuccess;
import static tutorspet.testutil.Assert.assertThrows;
import static tutorspet.testutil.TypicalIndexes.INDEX_FIRST_ITEM;
import static tutorspet.testutil.TypicalTutorsPet.getOnlyModuleClassTutorsPet;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import tutorspet.commons.core.Messages;
import tutorspet.commons.core.index.Index;
import tutorspet.model.Model;
import tutorspet.model.ModelManager;
import tutorspet.model.UserPrefs;
import tutorspet.model.lesson.Lesson;
import tutorspet.model.moduleclass.ModuleClass;
import tutorspet.testutil.LessonBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code AddLessonCommand}.
 */
public class AddLessonCommandTest {

    private Model model = new ModelManager(getOnlyModuleClassTutorsPet(), new UserPrefs());

    @Test
    public void constructor_nullLesson_throwNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddLessonCommand(INDEX_FIRST_ITEM, null));
    }

    @Test
    public void constructor_nullClassIndex_throwNullPointerException() {
        Lesson lesson = new LessonBuilder().build();
        assertThrows(NullPointerException.class, () -> new AddLessonCommand(null, lesson));
    }

    @Test
    public void execute_unfilteredList_success() {
        Index moduleClassIndex = INDEX_FIRST_ITEM;

        // manually add first lesson to first class
        ModuleClass moduleClass = model.getFilteredModuleClassList().get(moduleClassIndex.getZeroBased());
        Lesson lesson = new LessonBuilder().build();
        ModuleClass modifiedModuleClass = manualAddLessonToModuleClass(moduleClass, lesson);

        String expectedMessage = String.format(AddLessonCommand.MESSAGE_SUCCESS, lesson);
        Model expectedModel = new ModelManager(model.getTutorsPet(), new UserPrefs());
        expectedModel.setModuleClass(moduleClass, modifiedModuleClass);
        expectedModel.commit(expectedMessage);

        assertCommandSuccess(new AddLessonCommand(moduleClassIndex, lesson), model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_existingLesson_failure() {
        Index moduleClassIndex = INDEX_FIRST_ITEM;

        // manually add first lesson to first class
        ModuleClass moduleClass = model.getFilteredModuleClassList().get(moduleClassIndex.getZeroBased());
        Lesson lesson = new LessonBuilder().build();
        ModuleClass modifiedModuleClass = manualAddLessonToModuleClass(moduleClass, lesson);

        // update model with modified class
        model.setModuleClass(moduleClass, modifiedModuleClass);

        AddLessonCommand addLessonCommand = new AddLessonCommand(INDEX_FIRST_ITEM, lesson);

        assertCommandFailure(addLessonCommand, model, AddLessonCommand.MESSAGE_EXISTING_LESSON);
    }

    @Test
    public void execute_invalidClassIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredModuleClassList().size() + 1);
        Lesson lesson = new LessonBuilder().build();
        AddLessonCommand addLessonCommand = new AddLessonCommand(outOfBoundIndex, lesson);

        assertCommandFailure(addLessonCommand, model, Messages.MESSAGE_INVALID_MODULE_CLASS_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        Lesson lessonAtCom1 = new LessonBuilder().withVenue("COM1-0001").build();
        Lesson lessonAtCom2 = new LessonBuilder().withVenue("COM2-0002").build();
        AddLessonCommand addLessonCom1Command = new AddLessonCommand(INDEX_FIRST_ITEM, lessonAtCom1);
        AddLessonCommand addLessonCom2Command = new AddLessonCommand(INDEX_FIRST_ITEM, lessonAtCom2);

        // same object -> returns true
        assertTrue(addLessonCom1Command.equals(addLessonCom1Command));

        // same values -> returns true
        AddLessonCommand addLessonCom1CommandCopy = new AddLessonCommand(INDEX_FIRST_ITEM, lessonAtCom1);
        assertTrue(addLessonCom1Command.equals(addLessonCom1CommandCopy));

        // different type -> returns false
        assertFalse(addLessonCom1Command.equals(5));

        // null -> returns false
        assertFalse(addLessonCom1Command.equals(null));

        // different lessons -> returns false
        assertFalse(addLessonCom1Command.equals(addLessonCom2Command));
    }

    /**
     * Returns a new {@code ModuleClass} based on the given {@code moduleClass} but with the specified {@code lesson}
     * added.
     * {@code lesson} must not be found in {@code moduleClass}.
     */
    private static ModuleClass manualAddLessonToModuleClass(ModuleClass moduleClass, Lesson lesson) {
        List<Lesson> lessons = new ArrayList<>(moduleClass.getLessons());

        assertFalse(lessons.contains(lesson), "Test precondition error: The selected module class"
                + " already contains the selected lesson.");

        lessons.add(lesson);
        return new ModuleClass(moduleClass.getName(), moduleClass.getStudentUuids(), lessons);
    }
}
