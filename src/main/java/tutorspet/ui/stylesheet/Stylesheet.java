package tutorspet.ui.stylesheet;

import tutorspet.ui.stylesheet.exceptions.StylesheetException;

public enum Stylesheet {
    LIGHT("LightTheme.css"),
    ALTERNATE("AlternateTheme.css"),
    DARK("DarkTheme.css"),
    EXTENSION("Extensions.css");

    public static final String ERROR_MESSAGE = "Stylesheet cannot be found: ";
    public static final String SUCCESS_MESSAGE = "Stylesheet switched to: ";

    private static final String DIRECTORY = "/view/";
    private final String path;

    Stylesheet(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return path;
    }

    public String getStylesheet() throws StylesheetException {
        try {
            return getClass().getResource(DIRECTORY + path).toExternalForm();
        } catch (NullPointerException e) {
            throw new StylesheetException(ERROR_MESSAGE + path);
        }
    }
}
