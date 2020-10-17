package tutorspet.model;

import java.nio.file.Path;

import tutorspet.commons.core.GuiSettings;

/**
 * Unmodifiable view of user prefs.
 */
public interface ReadOnlyUserPrefs {

    GuiSettings getGuiSettings();

    Path getTutorsPetFilePath();
}
