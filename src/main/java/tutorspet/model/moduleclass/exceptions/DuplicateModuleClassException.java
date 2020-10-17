package tutorspet.model.moduleclass.exceptions;

public class DuplicateModuleClassException extends RuntimeException {

    public DuplicateModuleClassException() {
        super("Operation would result in duplicate classes");
    }
}
