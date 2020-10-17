package tutorspet.ui;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import tutorspet.commons.core.LogsCenter;
import tutorspet.logic.commands.CommandResult;
import tutorspet.logic.commands.exceptions.CommandException;
import tutorspet.logic.parser.exceptions.ParseException;

/**
 * The UI component that is responsible for receiving user command inputs.
 */
public class CommandBox extends UiPart<Region> {

    public static final String ERROR_STYLE_CLASS = "error";
    private static final String FXML = "CommandBox.fxml";
    private static final CommandHistory COMMAND_HISTORY = new CommandHistory();

    private final Logger logger = LogsCenter.getLogger(CommandBox.class);

    private final CommandExecutor commandExecutor;

    @FXML
    private TextField commandTextField;

    /**
     * Creates a {@code CommandBox} with the given {@code CommandExecutor}.
     */
    public CommandBox(CommandExecutor commandExecutor) {
        super(FXML);
        this.commandExecutor = commandExecutor;

        // Calls #setStyleToDefault() whenever there is a change to the text of the command box.
        commandTextField.textProperty().addListener((unused1, unused2, unused3) -> setStyleToDefault());

        commandTextField.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            switch (event.getCode()) {
            case UP:
                logger.fine("UP key pressed");
                recallPreviousCommand();
                event.consume();
                break;
            case DOWN:
                logger.fine("DOWN key pressed");
                recallNextCommand();
                event.consume();
                break;
            default:
                // Do not filter other key presses.
            }
        });
    }

    /**
     * Recalls the previous valid command executed by the application.
     */
    private void recallPreviousCommand() {
        if (COMMAND_HISTORY.hasPrevious()) {
            commandTextField.setText(COMMAND_HISTORY.getPrevious(commandTextField.getText()));
            logger.fine("Recalled: " + commandTextField.getText());
            commandTextField.positionCaret(commandTextField.getText().length());
        }
    }

    /**
     * Recalls the next valid command executed by the application.
     */
    private void recallNextCommand() {
        if (COMMAND_HISTORY.hasNext()) {
            commandTextField.setText(COMMAND_HISTORY.getNext());
            logger.fine("Recalled: " + commandTextField.getText());
            commandTextField.positionCaret(commandTextField.getText().length());
        } else if (COMMAND_HISTORY.hasCached()) {
            commandTextField.setText(COMMAND_HISTORY.getCached());
            logger.fine("Retrieved from cache: " + commandTextField.getText());
            commandTextField.positionCaret(commandTextField.getText().length());
        }
    }

    /**
     * Handles the Enter button pressed event.
     */
    @FXML
    private void handleCommandEntered() {
        try {
            commandExecutor.execute(commandTextField.getText());
            COMMAND_HISTORY.addHistory(commandTextField.getText());
            commandTextField.setText("");
        } catch (CommandException | ParseException e) {
            setStyleToIndicateCommandFailure();
        }
    }

    /**
     * Sets the command box style to use the default style.
     */
    private void setStyleToDefault() {
        commandTextField.getStyleClass().remove(ERROR_STYLE_CLASS);
    }

    /**
     * Sets the command box style to indicate a failed command.
     */
    private void setStyleToIndicateCommandFailure() {
        ObservableList<String> styleClass = commandTextField.getStyleClass();

        if (styleClass.contains(ERROR_STYLE_CLASS)) {
            return;
        }

        styleClass.add(ERROR_STYLE_CLASS);
    }

    /**
     * Represents a function that can execute commands.
     */
    @FunctionalInterface
    public interface CommandExecutor {

        /**
         * Executes the command and returns the result.
         *
         * @see tutorspet.logic.Logic#execute(String)
         */
        CommandResult execute(String commandText) throws CommandException, ParseException;
    }
}
