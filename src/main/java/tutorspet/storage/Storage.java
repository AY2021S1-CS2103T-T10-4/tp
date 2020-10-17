package tutorspet.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import tutorspet.commons.exceptions.DataConversionException;
import tutorspet.model.ReadOnlyTutorsPet;
import tutorspet.model.ReadOnlyUserPrefs;
import tutorspet.model.UserPrefs;

/**
 * API of the Storage component
 */
public interface Storage extends TutorsPetStorage, UserPrefsStorage {

    @Override
    Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException;

    @Override
    void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException;

    @Override
    Path getTutorsPetFilePath();

    @Override
    Optional<ReadOnlyTutorsPet> readTutorsPet() throws DataConversionException, IOException;

    @Override
    void saveTutorsPet(ReadOnlyTutorsPet tutorsPet) throws IOException;
}
