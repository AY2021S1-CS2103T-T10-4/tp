package tutorspet.logic.commands;

import static java.util.Objects.requireNonNull;

import tutorspet.model.Model;
import tutorspet.model.TutorsPet;

/**
 * Clears all data in the student manager.
 */
public class ResetCommand extends Command {

    public static final String COMMAND_WORD = "reset";

    public static final String MESSAGE_SUCCESS = "All data in Tutor's Pet have been cleared!";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.setTutorsPet(new TutorsPet());
        model.commit(MESSAGE_SUCCESS);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
