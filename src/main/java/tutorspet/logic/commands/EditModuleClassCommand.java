package tutorspet.logic.commands;

import static java.util.Objects.requireNonNull;
import static tutorspet.commons.util.CollectionUtil.requireAllNonNull;
import static tutorspet.logic.parser.CliSyntax.PREFIX_NAME;
import static tutorspet.model.Model.PREDICATE_SHOW_ALL_MODULE_CLASS;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import tutorspet.commons.core.Messages;
import tutorspet.commons.core.index.Index;
import tutorspet.commons.util.CollectionUtil;
import tutorspet.logic.commands.exceptions.CommandException;
import tutorspet.model.Model;
import tutorspet.model.components.name.Name;
import tutorspet.model.lesson.Lesson;
import tutorspet.model.moduleclass.ModuleClass;

/**
 * Edits the details of an existing class.
 */
public class EditModuleClassCommand extends Command {

    public static final String COMMAND_WORD = "edit-class";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the class identified "
            + "by the index number used in the displayed class list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME]\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_NAME + "CS2103T Tutorial T10";

    public static final String MESSAGE_EDIT_MODULE_CLASS_SUCCESS = "Edited Class: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_MODULE_CLASS = "This class already exists.";

    private final Index index;
    private final EditModuleClassDescriptor editModuleClassDescriptor;

    /**
     * @param index of the class in the filtered class list to edit.
     * @param editModuleClassDescriptor details to edit the class with.
     */
    public EditModuleClassCommand(Index index, EditModuleClassDescriptor editModuleClassDescriptor) {
        requireAllNonNull(index, editModuleClassDescriptor);

        this.index = index;
        this.editModuleClassDescriptor = new EditModuleClassDescriptor(editModuleClassDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<ModuleClass> lastShownList = model.getFilteredModuleClassList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_MODULE_CLASS_DISPLAYED_INDEX);
        }

        ModuleClass moduleClassToEdit = lastShownList.get(index.getZeroBased());
        ModuleClass editedModuleClass = createEditedModuleClass(moduleClassToEdit, editModuleClassDescriptor);

        if (!moduleClassToEdit.isSameModuleClass(editedModuleClass) && model.hasModuleClass(editedModuleClass)) {
            throw new CommandException(MESSAGE_DUPLICATE_MODULE_CLASS);
        }

        model.setModuleClass(moduleClassToEdit, editedModuleClass);
        model.updateFilteredModuleClassList(PREDICATE_SHOW_ALL_MODULE_CLASS);
        String message = String.format(MESSAGE_EDIT_MODULE_CLASS_SUCCESS, editedModuleClass);
        model.commit(message);
        return new CommandResult(message);
    }

    /**
     * Creates and returns a {@code ModuleClass} with the details of {@code moduleClassToEdit}
     * edited with {@code editModuleClassDescriptor}.
     */
    private static ModuleClass createEditedModuleClass(ModuleClass moduleClassToEdit,
        EditModuleClassDescriptor editModuleClassDescriptor) {
        assert moduleClassToEdit != null;
        Name updatedName = editModuleClassDescriptor.getName().orElse(moduleClassToEdit.getName());
        Set<UUID> studentUuids = moduleClassToEdit.getStudentUuids();
        List<Lesson> lessons = moduleClassToEdit.getLessons();

        return new ModuleClass(updatedName, studentUuids, lessons);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditModuleClassCommand)) {
            return false;
        }

        // state check
        EditModuleClassCommand e = (EditModuleClassCommand) other;
        return index.equals(e.index)
                && editModuleClassDescriptor.equals(e.editModuleClassDescriptor);
    }

    /**
     * Stores the details to edit the class with. Each non-empty field will replace the
     * corresponding field value of the class.
     */
    public static class EditModuleClassDescriptor {

        private Name name;

        public EditModuleClassDescriptor() {}

        /**
         * Copy constructor.
         */
        public EditModuleClassDescriptor(EditModuleClassDescriptor toCopy) {
            setName(toCopy.name);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditModuleClassDescriptor)) {
                return false;
            }

            // state check
            EditModuleClassDescriptor e = (EditModuleClassDescriptor) other;

            return getName().equals(e.getName());
        }
    }
}

