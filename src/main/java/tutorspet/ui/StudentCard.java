package tutorspet.ui;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import tutorspet.model.components.tag.Tag;
import tutorspet.model.student.Student;

/**
 * A UI component that displays information of a {@code Student}.
 */
public class StudentCard extends UiPart<Region> {

    private static final List<Tag> listOfTags = new ArrayList<>();
    private static final Color[] colors = Color.values();
    private static final int NUMBER_OF_COLORS = colors.length;
    private static final String FXML = "StudentListCard.fxml";
    private static final String TELEGRAM_PREFIX = "@";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Student student;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label telegram;
    @FXML
    private Label email;
    @FXML
    private FlowPane tags;

    /**
     * Creates a {@code StudentCode} with the given {@code Student} and index to display.
     */
    public StudentCard(Student student, int displayedIndex) {
        super(FXML);
        this.student = student;
        id.setText(displayedIndex + ". ");
        name.setText(student.getName().fullName);
        telegram.setText(TELEGRAM_PREFIX + student.getTelegram().value);
        email.setText(student.getEmail().value);
        student.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(createTag(tag)));
    }

    /**
     * Creates a {@code Label} with the given {@code Tag} details.
     */
    public Label createTag(Tag tag) {
        Color color;

        if (listOfTags.contains(tag)) {
            int index = listOfTags.indexOf(tag);
            color = colors[index % NUMBER_OF_COLORS];
        } else {
            listOfTags.add(tag);
            color = colors[listOfTags.size() % NUMBER_OF_COLORS];
        }

        Label label = new Label(tag.tagName);
        label.setStyle("-fx-background-color:" + color.getHexValue() + ";");
        return label;
    }

    @Override
    public boolean equals(Object other) {

        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof StudentCard)) {
            return false;
        }

        // state check
        StudentCard card = (StudentCard) other;
        return id.getText().equals(card.id.getText())
                && student.equals(card.student);
    }
}
