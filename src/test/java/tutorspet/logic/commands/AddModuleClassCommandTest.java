package tutorspet.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tutorspet.testutil.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import tutorspet.logic.commands.exceptions.CommandException;
import tutorspet.model.ReadOnlyTutorsPet;
import tutorspet.model.TutorsPet;
import tutorspet.model.moduleclass.ModuleClass;
import tutorspet.testutil.ModelStub;
import tutorspet.testutil.ModuleClassBuilder;

public class AddModuleClassCommandTest {

    @Test
    public void constructor_nullModuleClass_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddModuleClassCommand(null));
    }

    @Test
    public void execute_moduleClassAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingModuleClassAdded modelStub = new ModelStubAcceptingModuleClassAdded();
        ModuleClass validModuleClass = new ModuleClassBuilder().build();

        CommandResult commandResult = new AddModuleClassCommand(validModuleClass).execute(modelStub);

        assertEquals(String.format(AddModuleClassCommand.MESSAGE_SUCCESS, validModuleClass),
                commandResult.getFeedbackToUser());
        assertEquals(Collections.singletonList(validModuleClass), modelStub.moduleClassesAdded);
        assertEquals(Collections.singletonList(String.format(AddModuleClassCommand.MESSAGE_SUCCESS, validModuleClass)),
                modelStub.commitMessages);
    }

    @Test
    public void execute_duplicateModuleClass_throwsCommandException() {
        ModuleClass validModuleClass = new ModuleClassBuilder().build();
        AddModuleClassCommand addModuleClassCommand = new AddModuleClassCommand(validModuleClass);
        ModelStubWithModuleClass modelStub = new ModelStubWithModuleClass(validModuleClass);

        assertThrows(CommandException.class, AddModuleClassCommand.MESSAGE_DUPLICATE_MODULE_CLASS, () ->
                addModuleClassCommand.execute(modelStub));
    }

    @Test
    public void equals() {
        ModuleClass cs1010 = new ModuleClassBuilder().withName("CS1010").build();
        ModuleClass cs2103 = new ModuleClassBuilder().withName("CS2103").build();
        AddModuleClassCommand addCs1101Command = new AddModuleClassCommand(cs1010);
        AddModuleClassCommand addCs2103Command = new AddModuleClassCommand(cs2103);

        // same object -> returns true
        assertTrue(addCs1101Command.equals(addCs1101Command));

        // same values -> returns true
        AddModuleClassCommand addCs1101CommandCopy = new AddModuleClassCommand(cs1010);
        assertTrue(addCs1101Command.equals(addCs1101CommandCopy));

        // different types -> returns false
        assertFalse(addCs1101Command.equals(1));

        // null -> returns false
        assertFalse(addCs1101Command.equals(null));

        // different ModuleClass -> returns false
        assertFalse(addCs1101Command.equals(addCs2103Command));
    }

    /**
     * A Model stub that contains a single {@code ModuleClass}.
     */
    private class ModelStubWithModuleClass extends ModelStub {

        private final ModuleClass moduleClass;

        ModelStubWithModuleClass(ModuleClass moduleClass) {
            requireNonNull(moduleClass);

            this.moduleClass = moduleClass;
        }

        @Override
        public boolean hasModuleClass(ModuleClass moduleClass) {
            requireNonNull(moduleClass);

            return this.moduleClass.isSameModuleClass(moduleClass);
        }
    }

    /**
     * A Model stub that always accepts the {@code ModuleClass} being added.
     */
    private class ModelStubAcceptingModuleClassAdded extends ModelStub {

        final ArrayList<ModuleClass> moduleClassesAdded = new ArrayList<>();
        final ArrayList<String> commitMessages = new ArrayList<>();

        @Override
        public void commit(String commitMessage) {
            commitMessages.add(commitMessage);
        }

        @Override
        public boolean hasModuleClass(ModuleClass moduleClass) {
            requireNonNull(moduleClass);

            return moduleClassesAdded.stream().anyMatch(moduleClass::isSameModuleClass);
        }

        @Override
        public void addModuleClass(ModuleClass moduleClass) {
            requireNonNull(moduleClass);

            moduleClassesAdded.add(moduleClass);
        }

        @Override
        public ReadOnlyTutorsPet getTutorsPet() {
            return new TutorsPet();
        }
    }
}
