package seedu.address.model.moduleclass;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalModuleClass.CS2100_TUTORIAL;
import static seedu.address.testutil.TypicalModuleClass.CS2103T_TUTORIAL;
import static seedu.address.testutil.TypicalModuleClass.STUDENT_UUID_1;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.moduleclass.exceptions.DuplicateModuleClassException;
import seedu.address.model.moduleclass.exceptions.ModuleClassNotFoundException;
import seedu.address.testutil.ModuleClassBuilder;

public class UniqueModuleClassListTest {

    private final UniqueModuleClassList uniqueModuleClassList = new UniqueModuleClassList();

    @Test
    public void contains_nullModuleClass_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueModuleClassList.contains(null));
    }

    @Test
    public void contains_moduleClassNotInList_returnsFalse() {
        assertFalse(uniqueModuleClassList.contains(CS2103T_TUTORIAL));
    }

    @Test
    public void contains_moduleClassInList_returnsTrue() {
        uniqueModuleClassList.add(CS2103T_TUTORIAL);
        assertTrue(uniqueModuleClassList.contains(CS2103T_TUTORIAL));
    }

    @Test
    public void contains_moduleClassWithSameIdentityFieldsInList_returnsTrue() {
        uniqueModuleClassList.add(CS2103T_TUTORIAL);
        ModuleClass editedCs2103t = new ModuleClassBuilder(CS2103T_TUTORIAL).withStudentIds(STUDENT_UUID_1).build();
        assertTrue(uniqueModuleClassList.contains(editedCs2103t));
    }

    @Test
    public void add_nullModuleClass_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueModuleClassList.add(null));
    }

    @Test
    public void add_duplicateModuleClass_throwsDuplicateModuleClassException() {
        uniqueModuleClassList.add(CS2103T_TUTORIAL);
        assertThrows(DuplicateModuleClassException.class, () -> uniqueModuleClassList.add(CS2103T_TUTORIAL));
    }

    @Test
    public void add_moduleClassWithSameIdentityFieldsInList_throwsDuplicateModuleClassException() {
        uniqueModuleClassList.add(CS2103T_TUTORIAL);
        ModuleClass editedCs2103t = new ModuleClassBuilder(CS2103T_TUTORIAL).withStudentIds(STUDENT_UUID_1).build();
        assertThrows(DuplicateModuleClassException.class, () -> uniqueModuleClassList.add(CS2103T_TUTORIAL));
    }

    @Test
    public void setModuleClass_nullTargetModuleClass_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueModuleClassList.setModuleClass(null, CS2103T_TUTORIAL));
    }

    @Test
    public void setModuleClass_nullEditedModuleClass_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueModuleClassList.setModuleClass(CS2103T_TUTORIAL, null));
    }

    @Test
    public void setModuleClass_targetModuleClassNotInList_throwsModuleClassNotFoundException() {
        assertThrows(ModuleClassNotFoundException.class, ()
            -> uniqueModuleClassList.setModuleClass(CS2103T_TUTORIAL, CS2103T_TUTORIAL));
    }

    @Test
    public void remove_nullModuleClass_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueModuleClassList.remove(null));
    }

    @Test
    public void remove_moduleClassDoesNotExist_throwsModuleClassNotFoundException() {
        assertThrows(ModuleClassNotFoundException.class, () -> uniqueModuleClassList.remove(CS2103T_TUTORIAL));
    }

    @Test
    public void remove_existingModuleClass_removesModuleClass() {
        uniqueModuleClassList.add(CS2103T_TUTORIAL);
        uniqueModuleClassList.remove(CS2103T_TUTORIAL);
        UniqueModuleClassList expectedUniqueModuleClassList = new UniqueModuleClassList();
        assertEquals(expectedUniqueModuleClassList, uniqueModuleClassList);
    }

    @Test
    public void setModuleClass_nullUniqueModuleClassList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, ()
            -> uniqueModuleClassList.setModuleClass((UniqueModuleClassList) null));
    }

    @Test
    public void setModuleClass_uniqueModuleClassList_replacesOwnListWithProvidedUniqueModuleClassList() {
        uniqueModuleClassList.add(CS2103T_TUTORIAL);
        UniqueModuleClassList expectedUniqueModuleClassList = new UniqueModuleClassList();
        expectedUniqueModuleClassList.add(CS2100_TUTORIAL);
        uniqueModuleClassList.setModuleClass(expectedUniqueModuleClassList);
        assertEquals(expectedUniqueModuleClassList, uniqueModuleClassList);
    }

    @Test
    public void setModuleClass_nullList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueModuleClassList.setModuleClass((List<ModuleClass>) null));
    }

    @Test
    public void setModuleClass_list_replacesOwnListWithProvidedList() {
        uniqueModuleClassList.add(CS2103T_TUTORIAL);
        List<ModuleClass> moduleClassList = Collections.singletonList(CS2100_TUTORIAL);
        uniqueModuleClassList.setModuleClass(moduleClassList);
        UniqueModuleClassList expectedUniqueModuleClassList = new UniqueModuleClassList();
        expectedUniqueModuleClassList.add(CS2100_TUTORIAL);
        assertEquals(expectedUniqueModuleClassList, uniqueModuleClassList);
    }

    @Test
    public void setModuleClass_listWithDuplicateModuleClasses_throwsDuplicateModuleClassException() {
        List<ModuleClass> listWithDuplicateModuleClasses = Arrays.asList(CS2103T_TUTORIAL, CS2103T_TUTORIAL);
        assertThrows(DuplicateModuleClassException.class, ()
            -> uniqueModuleClassList.setModuleClass(listWithDuplicateModuleClasses));
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, ()
            -> uniqueModuleClassList.asUnmodifiableObservableList().remove(0));
    }
}