package tutorspet.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static tutorspet.testutil.Assert.assertThrows;
import static tutorspet.testutil.TypicalStudent.ALICE;
import static tutorspet.testutil.TypicalStudent.HOON;
import static tutorspet.testutil.TypicalStudent.IDA;
import static tutorspet.testutil.TypicalTutorsPet.getTypicalTutorsPet;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import tutorspet.commons.exceptions.DataConversionException;
import tutorspet.model.ReadOnlyTutorsPet;
import tutorspet.model.TutorsPet;

public class JsonTutorsPetStorageTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonTutorsPetStorageTest");

    @TempDir
    public Path testFolder;

    @Test
    public void readTutorsPet_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> readTutorsPet(null));
    }

    private java.util.Optional<ReadOnlyTutorsPet> readTutorsPet(String filePath) throws Exception {
        return new JsonTutorsPetStorage(Paths.get(filePath)).readTutorsPet(addToTestDataPathIfNotNull(filePath));
    }

    private Path addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER.resolve(prefsFileInTestDataFolder)
                : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readTutorsPet("NonExistentFile.json").isPresent());
    }

    @Test
    public void read_notJsonFormat_exceptionThrown() {
        assertThrows(DataConversionException.class, () -> readTutorsPet("notJsonFormatTutorsPet.json"));
    }

    @Test
    public void readTutorsPet_invalidStudentTutorsPet_throwDataConversionException() {
        assertThrows(DataConversionException.class, () -> readTutorsPet("invalidStudentTutorsPet.json"));
    }

    @Test
    public void readTutorsPet_invalidAndValidStudentTutorsPet_throwDataConversionException() {
        assertThrows(DataConversionException.class, () -> readTutorsPet("invalidAndValidStudentTutorsPet.json"));
    }

    @Test
    public void readTutorsPet_invalidStudentUuidTutorsPet_throwDataConversionException() {
        assertThrows(DataConversionException.class, () -> readTutorsPet("invalidStudentUuidTutorsPet.json"));
    }

    @Test
    public void readTutorsPet_invalidClassTutorsPet_throwDataConversionException() {
        assertThrows(DataConversionException.class, () -> readTutorsPet("invalidClassTutorsPet.json"));
    }

    @Test
    public void readTutorsPet_invalidAndValidClassTutorsPet_throwDataConversionException() {
        assertThrows(DataConversionException.class, () -> readTutorsPet("invalidAndValidClassTutorsPet.json"));
    }

    @Test
    public void readAndSaveTutorsPet_allInOrder_success() throws Exception {
        Path filePath = testFolder.resolve("TempTutorsPet.json");
        TutorsPet original = getTypicalTutorsPet();

        // workaround as storage functionality for ModuleClasses has not been implemented
        original.setModuleClasses(new ArrayList<>());

        JsonTutorsPetStorage jsonTutorsPetStorage = new JsonTutorsPetStorage(filePath);

        // Save in new file and read back
        jsonTutorsPetStorage.saveTutorsPet(original, filePath);
        ReadOnlyTutorsPet readBack = jsonTutorsPetStorage.readTutorsPet(filePath).get();
        assertEquals(original, new TutorsPet(readBack));

        // Modify data, overwrite exiting file, and read back
        original.addStudent(HOON);
        original.deleteStudent(ALICE);
        jsonTutorsPetStorage.saveTutorsPet(original, filePath);
        readBack = jsonTutorsPetStorage.readTutorsPet(filePath).get();
        assertEquals(original, new TutorsPet(readBack));

        // Save and read without specifying file path
        original.addStudent(IDA);
        jsonTutorsPetStorage.saveTutorsPet(original); // file path not specified
        readBack = jsonTutorsPetStorage.readTutorsPet().get(); // file path not specified
        assertEquals(original, new TutorsPet(readBack));
    }

    @Test
    public void saveTutorsPet_nullTutorsPet_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveTutorsPet(null, "SomeFile.json"));
    }

    /**
     * Saves {@code tutorsPet} at the specified {@code filePath}.
     */
    private void saveTutorsPet(ReadOnlyTutorsPet tutorsPet, String filePath) {
        try {
            new JsonTutorsPetStorage(Paths.get(filePath))
                    .saveTutorsPet(tutorsPet, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void saveTutorsPet_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveTutorsPet(new TutorsPet(), null));
    }
}
