package tutorspet.storage;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import tutorspet.commons.core.LogsCenter;
import tutorspet.commons.exceptions.DataConversionException;
import tutorspet.commons.exceptions.IllegalValueException;
import tutorspet.commons.util.FileUtil;
import tutorspet.commons.util.JsonUtil;
import tutorspet.model.ReadOnlyTutorsPet;

/**
 * A class to access TutorsPet data stored as a json file on the hard disk.
 */
public class JsonTutorsPetStorage implements TutorsPetStorage {

    private static final Logger logger = LogsCenter.getLogger(JsonTutorsPetStorage.class);

    private Path filePath;

    public JsonTutorsPetStorage(Path filePath) {
        this.filePath = filePath;
    }

    public Path getTutorsPetFilePath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlyTutorsPet> readTutorsPet() throws DataConversionException {
        return readTutorsPet(filePath);
    }

    /**
     * Similar to {@link #readTutorsPet()}.
     *
     * @param filePath location of the data. Cannot be null.
     * @throws DataConversionException if the file is not in the correct format.
     */
    public Optional<ReadOnlyTutorsPet> readTutorsPet(Path filePath) throws DataConversionException {
        requireNonNull(filePath);

        Optional<JsonSerializableTutorsPet> jsonTutorsPet = JsonUtil.readJsonFile(
                filePath, JsonSerializableTutorsPet.class);
        if (!jsonTutorsPet.isPresent()) {
            return Optional.empty();
        }

        try {
            return Optional.of(jsonTutorsPet.get().toModelType());
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + filePath + ": " + ive.getMessage());
            throw new DataConversionException(ive);
        }
    }

    @Override
    public void saveTutorsPet(ReadOnlyTutorsPet tutorsPet) throws IOException {
        saveTutorsPet(tutorsPet, filePath);
    }

    /**
     * Similar to {@link #saveTutorsPet(ReadOnlyTutorsPet)}.
     *
     * @param filePath location of the data. Cannot be null.
     */
    public void saveTutorsPet(ReadOnlyTutorsPet tutorsPet, Path filePath) throws IOException {
        requireNonNull(tutorsPet);
        requireNonNull(filePath);

        FileUtil.createIfMissing(filePath);
        JsonUtil.saveJsonFile(new JsonSerializableTutorsPet(tutorsPet), filePath);
    }
}
