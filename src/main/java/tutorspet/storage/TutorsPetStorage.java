package tutorspet.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import tutorspet.commons.exceptions.DataConversionException;
import tutorspet.model.ReadOnlyTutorsPet;
import tutorspet.model.TutorsPet;

/**
 * Represents a storage for {@link TutorsPet}.
 */
public interface TutorsPetStorage {

    /**
     * Returns the file path of the data file.
     */
    Path getTutorsPetFilePath();

    /**
     * Returns TutorsPet data as a {@link ReadOnlyTutorsPet}.
     *   Returns {@code Optional.empty()} if storage file is not found.
     * @throws DataConversionException if the data in storage is not in the expected format.
     * @throws IOException if there was any problem when reading from the storage.
     */
    Optional<ReadOnlyTutorsPet> readTutorsPet() throws DataConversionException, IOException;

    /**
     * @see #getTutorsPetFilePath()
     */
    Optional<ReadOnlyTutorsPet> readTutorsPet(Path filePath) throws DataConversionException, IOException;

    /**
     * Saves the given {@link ReadOnlyTutorsPet} to the storage.
     * @param tutorsPet cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveTutorsPet(ReadOnlyTutorsPet tutorsPet) throws IOException;

    /**
     * @see #saveTutorsPet(ReadOnlyTutorsPet)
     */
    void saveTutorsPet(ReadOnlyTutorsPet tutorsPet, Path filePath) throws IOException;
}
