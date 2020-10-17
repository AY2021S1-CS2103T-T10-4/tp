package tutorspet.logic.commands;

import static java.util.Objects.requireNonNull;
import static tutorspet.logic.parser.CliSyntax.PREFIX_EMAIL;
import static tutorspet.logic.parser.CliSyntax.PREFIX_NAME;
import static tutorspet.logic.parser.CliSyntax.PREFIX_TAG;
import static tutorspet.logic.parser.CliSyntax.PREFIX_TELEGRAM;

import tutorspet.logic.commands.exceptions.CommandException;
import tutorspet.model.Model;
import tutorspet.model.student.Student;

/**
 * Adds a student to the student manager.
 */
public class AddStudentCommand extends Command {

    public static final String COMMAND_WORD = "add-student";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a student to the student manager. "
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_TELEGRAM + "TELEGRAM "
            + PREFIX_EMAIL + "EMAIL "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "John Doe "
            + PREFIX_TELEGRAM + "johnDO3 "
            + PREFIX_EMAIL + "johnd@example.com "
            + PREFIX_TAG + "CS2103 Tutorial "
            + PREFIX_TAG + "Experienced";

    public static final String MESSAGE_SUCCESS = "New student added: %1$s";
    public static final String MESSAGE_DUPLICATE_STUDENT = "This student already exists.";

    private final Student toAdd;

    /**
     * Creates an AddStudentCommand to add the specified {@code Student}.
     */
    public AddStudentCommand(Student student) {
        requireNonNull(student);

        toAdd = student;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasStudent(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_STUDENT);
        }

        model.addStudent(toAdd);
        String message = String.format(MESSAGE_SUCCESS, toAdd);
        model.commit(message);
        return new CommandResult(message);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddStudentCommand // instanceof handles nulls
                && toAdd.equals(((AddStudentCommand) other).toAdd));
    }
}
