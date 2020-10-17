package tutorspet.logic.commands;

import static java.util.Objects.requireNonNull;

import tutorspet.model.Model;
import tutorspet.model.StateRecords;

/**
 * Lists all recorded {@code Command}s to the user.
 */
public class ViewHistoryCommand extends Command {

    public static final String COMMAND_WORD = "view-history";

    public static final String CURRENT_INDICATOR = "> ";

    public static final String MESSAGE_TEMPLATE = "History:%s";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        StateRecords stateRecords = model.viewStateRecords();
        return new CommandResult(createMessage(stateRecords));
    }

    private String createMessage(StateRecords stateRecords) {
        assert stateRecords != null;

        int currentStateIndex = stateRecords.getCurrentIndex();
        int stateCount = stateRecords.getStateRecords().size();

        assert stateCount != 0;

        StringBuilder summary = new StringBuilder();

        for (int i = stateCount - 1; i >= 0; i--) {
            summary.append("\n");
            if (i == currentStateIndex) {
                summary.append(CURRENT_INDICATOR);
            }
            summary.append(stateRecords.getStateRecords().get(i));
        }
        return String.format(MESSAGE_TEMPLATE, summary.toString());
    }
}
