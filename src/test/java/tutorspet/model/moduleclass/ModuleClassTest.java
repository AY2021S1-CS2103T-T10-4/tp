package tutorspet.model.moduleclass;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tutorspet.logic.commands.CommandTestUtil.VALID_NAME_CS2100_LAB;
import static tutorspet.testutil.TypicalLesson.LESSON_FRI_8_TO_10;
import static tutorspet.testutil.TypicalLesson.LESSON_THU_10_TO_11;
import static tutorspet.testutil.TypicalLesson.LESSON_WED_2_TO_4;
import static tutorspet.testutil.TypicalModuleClass.CS2100_LAB;
import static tutorspet.testutil.TypicalModuleClass.CS2103T_TUTORIAL;
import static tutorspet.testutil.TypicalStudent.ALICE;
import static tutorspet.testutil.TypicalStudent.AMY;
import static tutorspet.testutil.TypicalStudent.BENSON;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import tutorspet.model.components.name.Name;
import tutorspet.model.lesson.Lesson;
import tutorspet.testutil.ModuleClassBuilder;

public class ModuleClassTest {

    @Test
    public void constructor_nameOnly_hasNoStudents() {
        Name name = new Name(VALID_NAME_CS2100_LAB);
        ModuleClass moduleClass = new ModuleClass(name);
        assertEquals(0, moduleClass.getStudentUuids().size());
    }

    @Test
    public void constructor_withStudents_hasCorrectStudents() {
        Name name = new Name(VALID_NAME_CS2100_LAB);
        Set<UUID> studentUuids = new HashSet<>(Collections.singletonList(ALICE.getUuid()));
        ModuleClass moduleClass = new ModuleClass(name, studentUuids, Collections.emptyList());
        assertEquals(studentUuids.size(), moduleClass.getStudentUuids().size());
        assertTrue(moduleClass.getStudentUuids().contains(ALICE.getUuid()));
    }

    @Test
    public void constructor_withStudents_studentsDefensivelyCopied() {
        Name name = new Name(VALID_NAME_CS2100_LAB);
        Set<UUID> studentUuids = new HashSet<>(Collections.singletonList(ALICE.getUuid()));
        ModuleClass moduleClass = new ModuleClass(name, studentUuids, Collections.emptyList());
        studentUuids.add(BENSON.getUuid());
        assertFalse(moduleClass.getStudentUuids().contains(BENSON.getUuid()));
    }

    @Test
    public void constructor_withLessons_hasCorrectLessons() {
        Name name = new Name(VALID_NAME_CS2100_LAB);
        List<Lesson> lessons = Collections.singletonList(LESSON_FRI_8_TO_10);
        ModuleClass moduleClass = new ModuleClass(name, Collections.emptySet(), lessons);
        assertEquals(lessons.size(), moduleClass.getLessons().size());
        assertTrue(moduleClass.getLessons().contains(LESSON_FRI_8_TO_10));
    }

    @Test
    public void constructor_withLessons_lessonsDefensivelyCopied() {
        Name name = new Name(VALID_NAME_CS2100_LAB);
        List<Lesson> lessons = new ArrayList<>(Collections.singletonList(LESSON_WED_2_TO_4));
        ModuleClass moduleClass = new ModuleClass(name, Collections.emptySet(), lessons);
        lessons.add(LESSON_FRI_8_TO_10);
        assertFalse(moduleClass.getLessons().contains(LESSON_FRI_8_TO_10));
    }

    @Test
    public void hasStudentUuid_studentInModuleClass_returnsTrue() {
        ModuleClass moduleClass = new ModuleClassBuilder().withStudentUuids(ALICE.getUuid(), BENSON.getUuid()).build();
        assertTrue(moduleClass.hasStudentUuid(ALICE.getUuid()));
    }

    @Test
    public void hasStudentUuid_nullUuid_throwsNullPointerException() {
        ModuleClass moduleClass = new ModuleClassBuilder().build();
        assertThrows(NullPointerException.class, () -> moduleClass.hasStudentUuid(null));
    }

    @Test
    public void hasStudentUuid_studentNotInModuleClass_returnsFalse() {
        ModuleClass moduleClass = new ModuleClassBuilder().withStudentUuids(ALICE.getUuid()).build();
        assertFalse(moduleClass.hasStudentUuid(BENSON.getUuid()));
    }

    @Test
    public void getStudentUuids_modifyList_throwsUnsupportedOperationException() {
        ModuleClass moduleClass = new ModuleClassBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> moduleClass.getStudentUuids().remove(0));
    }

    @Test
    public void hasLesson_lessonInModuleClass_returnsTrue() {
        ModuleClass moduleClass = new ModuleClassBuilder().withLessons(LESSON_FRI_8_TO_10, LESSON_WED_2_TO_4).build();
        assertTrue(moduleClass.hasLesson(LESSON_FRI_8_TO_10));
    }

    @Test
    public void hasLesson_nullLesson_throwsNullPointerException() {
        ModuleClass moduleClass = new ModuleClassBuilder().build();
        assertThrows(NullPointerException.class, () -> moduleClass.hasLesson(null));
    }

    @Test
    public void hasLesson_lessonNotInModuleClass_returnsFalse() {
        ModuleClass moduleClass = new ModuleClassBuilder().withLessons(LESSON_FRI_8_TO_10).build();
        assertFalse(moduleClass.hasLesson(LESSON_WED_2_TO_4));
    }

    @Test
    public void getLessons_retainsConstructedOrder() {
        List<Lesson> lessons = Arrays.asList(LESSON_FRI_8_TO_10, LESSON_WED_2_TO_4, LESSON_THU_10_TO_11);
        ModuleClass moduleClass = new ModuleClass(new Name(VALID_NAME_CS2100_LAB),
                Collections.emptySet(),
                lessons);
        assertEquals(lessons, moduleClass.getLessons());
    }

    @Test
    public void getLessons_modifyList_throwsUnsupportedOperationException() {
        ModuleClass moduleClass = new ModuleClassBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> moduleClass.getLessons().remove(0));
    }

    @Test
    public void isSameModuleClass() {
        // same object -> returns true
        assertTrue(CS2103T_TUTORIAL.isSameModuleClass(CS2103T_TUTORIAL));

        // null -> returns false
        assertFalse(CS2103T_TUTORIAL.isSameModuleClass(null));

        // different name -> returns false
        ModuleClass editedCs2103t = new ModuleClassBuilder(CS2103T_TUTORIAL).withName(VALID_NAME_CS2100_LAB)
                .build();
        assertFalse(CS2103T_TUTORIAL.isSameModuleClass(editedCs2103t));

        // different students -> returns true
        editedCs2103t = new ModuleClassBuilder(CS2103T_TUTORIAL).withStudentUuids(AMY.getUuid()).build();
        assertTrue(CS2103T_TUTORIAL.isSameModuleClass(editedCs2103t));

        // different lessons -> return true
        editedCs2103t = new ModuleClassBuilder(CS2103T_TUTORIAL).withLessons(LESSON_FRI_8_TO_10).build();
        assertTrue(CS2103T_TUTORIAL.isSameModuleClass(editedCs2103t));
    }

    @Test
    public void equals() {
        // same values -> returns true
        ModuleClass cs2103tCopy = new ModuleClassBuilder(CS2103T_TUTORIAL).build();
        assertTrue(CS2103T_TUTORIAL.equals(cs2103tCopy));

        // same object -> returns true
        assertTrue(CS2103T_TUTORIAL.equals(CS2103T_TUTORIAL));

        // null -> returns false
        assertFalse(CS2103T_TUTORIAL.equals(null));

        // different type -> returns false
        assertFalse(CS2103T_TUTORIAL.equals(5));

        // different moduleClass -> returns false
        assertFalse(CS2103T_TUTORIAL.equals(CS2100_LAB));

        // different name -> returns false
        ModuleClass editedCs2103t = new ModuleClassBuilder(CS2103T_TUTORIAL).withName(VALID_NAME_CS2100_LAB)
                .build();
        assertFalse(CS2103T_TUTORIAL.equals(editedCs2103t));

        // different students -> returns false
        editedCs2103t = new ModuleClassBuilder(CS2103T_TUTORIAL).withStudentUuids(AMY.getUuid()).build();
        assertFalse(CS2103T_TUTORIAL.equals(editedCs2103t));

        // different lessons -> return false
        editedCs2103t = new ModuleClassBuilder(CS2103T_TUTORIAL).withLessons(LESSON_FRI_8_TO_10).build();
        assertFalse(CS2103T_TUTORIAL.equals(editedCs2103t));
    }
}
