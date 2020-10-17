package tutorspet.testutil;

import static tutorspet.logic.parser.CliSyntax.PREFIX_NAME;

import tutorspet.logic.commands.AddModuleClassCommand;
import tutorspet.logic.commands.EditModuleClassCommand.EditModuleClassDescriptor;
import tutorspet.model.moduleclass.ModuleClass;

/**
 * A utility class for ModuleClass.
 */
public class ModuleClassUtil {

    /**
     * Returns an add class command string for adding the {@code moduleClass}.
     */
    public static String getAddModuleClassCommand(ModuleClass moduleClass) {
        return AddModuleClassCommand.COMMAND_WORD + " " + getModuleDetails(moduleClass);
    }

    /**
     * Returns the part of the command string for the given {@code moduleClass}'s details.
     */
    public static String getModuleDetails(ModuleClass moduleClass) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_NAME + moduleClass.getName().toString());
        return sb.toString();
    }

    /**
     * Returns the part of command string for the given {@code EditModuleClassDescriptor}'s details.
     */
    public static String getEditModuleClassDescriptorDetails(EditModuleClassDescriptor descriptor) {
        StringBuilder sb = new StringBuilder();
        descriptor.getName().ifPresent(name -> sb.append(PREFIX_NAME).append(name.fullName));
        return sb.toString();
    }
}
