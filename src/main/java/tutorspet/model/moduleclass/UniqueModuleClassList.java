package tutorspet.model.moduleclass;

import static java.util.Objects.requireNonNull;
import static tutorspet.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import tutorspet.model.moduleclass.exceptions.DuplicateModuleClassException;
import tutorspet.model.moduleclass.exceptions.ModuleClassNotFoundException;

/**
 * A list of {@code ModuleClass} that enforces uniqueness between its elements and does not allow nulls.
 * A {@code ModuleClass} is considered unique by comparing using {@code ModuleClass#isSameModuleClass(ModuleClass)}.
 * As such, adding and updating of {@code ModuleClass} uses ModuleClass#isSameModuleClass(ModuleClass) for equality
 * so as to ensure that the {@code ModuleClass} being added or updated is unique in terms of identity in the
 * UniqueModuleClassList. However, the removal of a {@code ModuleClass} uses ModuleClass#equals(Object) so as to ensure
 * that the {@code ModuleClass} with exactly the same fields will be removed.
 *
 * Supports a minimal set of list operations.
 *
 * @see ModuleClass#isSameModuleClass(ModuleClass)
 */
public class UniqueModuleClassList implements Iterable<ModuleClass> {

    private final ObservableList<ModuleClass> internalList = FXCollections.observableArrayList();
    private final ObservableList<ModuleClass> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    /**
     * Returns true if the list contains an equivalent {@code ModuleClass} as the given argument.
     *
     * @throws NullPointerException if the given argument is null.
     */
    public boolean contains(ModuleClass toCheck) throws NullPointerException {
        requireNonNull(toCheck);

        return internalList.stream().anyMatch(toCheck::isSameModuleClass);
    }

    /**
     * Adds a {@code ModuleClass} to the list.
     * The {@code ModuleClass} must not already exist in the list.
     *
     * @throws NullPointerException if the given argument is null.
     * @throws DuplicateModuleClassException if the identity of {@code toAdd} already exists in the list.
     */
    public void add(ModuleClass toAdd) throws NullPointerException, DuplicateModuleClassException {
        requireNonNull(toAdd);

        if (contains(toAdd)) {
            throw new DuplicateModuleClassException();
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the {@code ModuleClass} {@code target} in the list with {@code editedModuleClass}.
     * {@code target} must exist in the list.
     * The {@code ModuleClass} identity of {@code editedModuleClass} must not be the same
     * as another existing {@code ModuleClass} in the list.
     *
     * @throws NullPointerException if the given argument is null.
     * @throws ModuleClassNotFoundException if {@code target} does not exist in the list.
     * @throws DuplicateModuleClassException if the identity of {@code editedModuleClass} already exists in the list.
     */
    public void setModuleClass(ModuleClass target, ModuleClass editedModuleClass)
            throws NullPointerException, ModuleClassNotFoundException, DuplicateModuleClassException {
        requireAllNonNull(target, editedModuleClass);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new ModuleClassNotFoundException();
        }

        if (!target.isSameModuleClass(editedModuleClass) && contains(editedModuleClass)) {
            throw new DuplicateModuleClassException();
        }

        internalList.set(index, editedModuleClass);
    }

    public void setModuleClass(UniqueModuleClassList replacement) {
        requireNonNull(replacement);

        internalList.setAll(replacement.internalList);
    }

    /**
     * Replaces the contents of this list with {@code moduleClasses}.
     * {@code moduleClasses} must not contain duplicate {@code ModuleClass}.
     *
     * @throws NullPointerException if any of the given arguments are null.
     * @throws DuplicateModuleClassException if {@code moduleClasses} contains duplicate {@code ModuleClass}.
     */
    public void setModuleClass(List<ModuleClass> moduleClasses)
            throws NullPointerException, DuplicateModuleClassException {
        requireAllNonNull(moduleClasses);

        if (!moduleClassesAreUnique(moduleClasses)) {
            throw new DuplicateModuleClassException();
        }

        internalList.setAll(moduleClasses);
    }

    /**
     * Removes the equivalent {@code ModuleClass} from the list.
     * The {@code ModuleClass} must exist in the list.
     *
     * @throws NullPointerException if the given argument is null.
     * @throws ModuleClassNotFoundException if {@code toRemove} does not exist in the list.
     */
    public void remove(ModuleClass toRemove) throws NullPointerException, ModuleClassNotFoundException {
        requireNonNull(toRemove);

        if (!internalList.remove(toRemove)) {
            throw new ModuleClassNotFoundException();
        }
    }

    /**
     * Removes the specified {@code UUID} from all {@code ModuleClass}es in the class list.
     */
    public void removeUuid(UUID uuidToRemove) {
        requireNonNull(uuidToRemove);

        internalList.setAll(internalList.stream()
                .map(moduleClass -> {
                    Set<UUID> modifiedUuids = new HashSet<>(moduleClass.getStudentUuids());
                    modifiedUuids.remove(uuidToRemove);
                    return new ModuleClass(moduleClass.getName(), modifiedUuids, moduleClass.getLessons());
                })
                .collect(Collectors.toList()));
    }

    /**
     * Removes all {@code Student UUID}s from every {@code ModuleClass} in the class list.
     */
    public void removeAllStudentUuids() {
        internalList.setAll(internalList.stream()
                .map(moduleClass ->
                        new ModuleClass(moduleClass.getName(), Collections.emptySet(), moduleClass.getLessons()))
                .collect(Collectors.toList()));
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<ModuleClass> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public Iterator<ModuleClass> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniqueModuleClassList // instanceof handles nulls
                && internalList.equals(((UniqueModuleClassList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    /**
     * Returns true if {@code moduleClasses} contains only unique {@code ModuleClass}.
     */
    private boolean moduleClassesAreUnique(List<ModuleClass> moduleClasses) {
        for (int i = 0; i < moduleClasses.size() - 1; i++) {
            for (int j = i + 1; j < moduleClasses.size(); j++) {
                if (moduleClasses.get(i).isSameModuleClass(moduleClasses.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }
}
