package tutorspet.testutil;

import java.time.LocalTime;

import tutorspet.logic.commands.EditLessonCommand.EditLessonDescriptor;
import tutorspet.model.lesson.Day;
import tutorspet.model.lesson.Lesson;
import tutorspet.model.lesson.Venue;

/**
 * A utility class to help with building EditLessonDescriptor objects.
 */
public class EditLessonDescriptorBuilder {

    private EditLessonDescriptor descriptor;

    public EditLessonDescriptorBuilder() {
        descriptor = new EditLessonDescriptor();
    }

    public EditLessonDescriptorBuilder(EditLessonDescriptor descriptor) {
        this.descriptor = new EditLessonDescriptor(descriptor);
    }

    /**
     * Returns an {@code EditLessonDescriptor} with fields containing {@code lesson}'s details.
     */
    public EditLessonDescriptorBuilder(Lesson lesson) {
        descriptor = new EditLessonDescriptor();
        descriptor.setStartTime(lesson.getStartTime());
        descriptor.setEndTime(lesson.getEndTime());
        descriptor.setDay(lesson.getDay());
        descriptor.setVenue(lesson.getVenue());
    }

    /**
     * Sets the {@code startTime} of the {@code EditLessonDescriptor} that we are building.
     */
    public EditLessonDescriptorBuilder withStartTime(String startTime) {
        descriptor.setStartTime(LocalTime.parse(startTime, Lesson.TIME_FORMATTER));
        return this;
    }

    /**
     * Sets the {@code endTime} of the {@code EditLessonDescriptor} that we are building.
     */
    public EditLessonDescriptorBuilder withEndTime(String endTime) {
        descriptor.setEndTime(LocalTime.parse(endTime, Lesson.TIME_FORMATTER));
        return this;
    }

    /**
     * Sets the {@code Day} of the {@code EditLessonDescriptor} that we are building.
     */
    public EditLessonDescriptorBuilder withDay(String day) {
        descriptor.setDay(Day.createDay(day));
        return this;
    }

    /**
     * Sets the {@code Venue} of the {@code EditLessonDescriptor} that we are building.
     */
    public EditLessonDescriptorBuilder withVenue(String venue) {
        descriptor.setVenue(new Venue(venue));
        return this;
    }

    public EditLessonDescriptor build() {
        return descriptor;
    }
}
