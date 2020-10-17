package tutorspet.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static tutorspet.testutil.TypicalTutorsPet.getTypicalTutorsPet;

import java.nio.file.Path;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import tutorspet.commons.core.GuiSettings;
import tutorspet.model.ReadOnlyTutorsPet;
import tutorspet.model.TutorsPet;
import tutorspet.model.UserPrefs;

public class StorageManagerTest {

    @TempDir
    public Path testFolder;

    private StorageManager storageManager;

    @BeforeEach
    public void setUp() {
        JsonTutorsPetStorage tutorsPetStorage = new JsonTutorsPetStorage(getTempFilePath("ab"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(getTempFilePath("prefs"));
        storageManager = new StorageManager(tutorsPetStorage, userPrefsStorage);
    }

    private Path getTempFilePath(String fileName) {
        return testFolder.resolve(fileName);
    }

    @Test
    public void prefsReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link JsonUserPrefsStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link JsonUserPrefsStorageTest} class.
         */
        UserPrefs original = new UserPrefs();
        original.setGuiSettings(new GuiSettings(300, 600, 4, 6));
        storageManager.saveUserPrefs(original);
        UserPrefs retrieved = storageManager.readUserPrefs().get();
        assertEquals(original, retrieved);
    }

    @Test
    public void tutorsPetReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link JsonTutorsPetStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link JsonTutorsPetStorageTest} class.
         */
        TutorsPet original = getTypicalTutorsPet();

        // workaround as storage functionality for ModuleClasses has not been implemented
        original.setModuleClasses(new ArrayList<>());

        storageManager.saveTutorsPet(original);
        ReadOnlyTutorsPet retrieved = storageManager.readTutorsPet().get();
        assertEquals(original, new TutorsPet(retrieved));
    }

    @Test
    public void getTutorsPetFilePath() {
        assertNotNull(storageManager.getTutorsPetFilePath());
    }
}
