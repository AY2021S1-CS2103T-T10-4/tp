package tutorspet.ui;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.logging.Logger;

import tutorspet.commons.core.LogsCenter;

/**
 * Represents a stack of commands executed by the user.
 * Supports caching the current command before starting to traverse the stack from the top.
 */
public class CommandHistory {

    private final Logger logger = LogsCenter.getLogger(CommandHistory.class);
    private final List<String> history;
    private int pointer;
    private Optional<String> cache;

    /**
     * Creates a new {@code CommandHistory} instance.
     */
    public CommandHistory() {
        history = new ArrayList<>();
        pointer = 0;
        cache = Optional.empty();
    }

    /**
     * Adds the {@code command} to stack and resets the position of the pointer to above the stack.
     * Empties the cache.
     */
    public void addHistory(String command) {
        requireNonNull(command);

        logger.info("Stored in command recall: " + command);
        history.add(command);
        pointer = history.size();
        cache = Optional.empty();
    }

    /**
     * Returns true if there is a next command that can be recalled.
     */
    public boolean hasNext() {
        return history.size() > 0 && pointer < history.size() - 1;
    }

    /**
     * Returns true if there is a previous command that can be recalled.
     */
    public boolean hasPrevious() {
        return history.size() > 0 && pointer > 0;
    }

    /**
     * Returns true if there is a cached command.
     */
    public boolean hasCached() {
        return cache.isPresent();
    }

    /**
     * Returns the currently cached command and moves the pointer above the stack.
     *
     * @throws NoSuchElementException if there is no cached value.
     */
    public String getCached() throws NoSuchElementException {
        pointer = history.size();
        return cache.get();
    }

    /**
     * Returns the next command.
     *
     * @throws IndexOutOfBoundsException if there is no next command to recall.
     */
    public String getNext() throws IndexOutOfBoundsException {
        return history.get(++pointer);
    }

    /**
     * Returns the previous command.
     * Caches the {@code currentInput} if the pointer is above the stack.
     *
     * @throws IndexOutOfBoundsException if there is no previous command to recall.
     */
    public String getPrevious(String currentInput) throws IndexOutOfBoundsException {
        if (pointer == history.size()) {
            logger.fine("Cached: " + currentInput);
            cache = Optional.of(currentInput);
        }
        return history.get(--pointer);
    }
}
