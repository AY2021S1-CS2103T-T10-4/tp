package tutorspet.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import tutorspet.commons.core.LogsCenter;
import tutorspet.commons.exceptions.DataConversionException;
import tutorspet.model.ReadOnlyTutorsPet;
import tutorspet.model.ReadOnlyUserPrefs;
import tutorspet.model.UserPrefs;

/**
 * Manages storage of TutorsPet data in local storage.
 */
public class StorageManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private TutorsPetStorage tutorsPetStorage;
    private UserPrefsStorage userPrefsStorage;

    /**
     * Creates a {@code StorageManager} with the given {@code TutorsPetStorage} and {@code UserPrefStorage}.
     */
    public StorageManager(TutorsPetStorage tutorsPetStorage, UserPrefsStorage userPrefsStorage) {
        super();
        this.tutorsPetStorage = tutorsPetStorage;
        this.userPrefsStorage = userPrefsStorage;
    }

    // ================ UserPrefs methods ==============================

    @Override
    public Path getUserPrefsFilePath() {
        return userPrefsStorage.getUserPrefsFilePath();
    }

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException {
        return userPrefsStorage.readUserPrefs();
    }

    @Override
    public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException {
        userPrefsStorage.saveUserPrefs(userPrefs);
    }


    // ================ TutorsPet methods ==============================

    @Override
    public Path getTutorsPetFilePath() {
        return tutorsPetStorage.getTutorsPetFilePath();
    }

    @Override
    public Optional<ReadOnlyTutorsPet> readTutorsPet() throws DataConversionException, IOException {
        return readTutorsPet(tutorsPetStorage.getTutorsPetFilePath());
    }

    @Override
    public Optional<ReadOnlyTutorsPet> readTutorsPet(Path filePath) throws DataConversionException, IOException {
        logger.fine("Attempting to read data from file: " + filePath);
        return tutorsPetStorage.readTutorsPet(filePath);
    }

    @Override
    public void saveTutorsPet(ReadOnlyTutorsPet tutorsPet) throws IOException {
        saveTutorsPet(tutorsPet, tutorsPetStorage.getTutorsPetFilePath());
    }

    @Override
    public void saveTutorsPet(ReadOnlyTutorsPet tutorsPet, Path filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        tutorsPetStorage.saveTutorsPet(tutorsPet, filePath);
    }
}
