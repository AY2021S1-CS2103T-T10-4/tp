package tutorspet.model.lesson.exceptions;

/**
 * Signals that the operation will result in duplicate Lessons.
 */
public class DuplicateLessonException extends RuntimeException {

    public DuplicateLessonException() {
        super("Operation would result in duplicate lessons");
    }
}
