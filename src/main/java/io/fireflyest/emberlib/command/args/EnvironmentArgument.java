package io.fireflyest.emberlib.command.args;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import org.bukkit.World.Environment;
import org.bukkit.command.CommandSender;

/**
 * 环境提示
 * 
 * @author Fireflyest
 * @since 
 */
public class EnvironmentArgument implements Argument {

    @Override
    public List<String> tab(@Nonnull CommandSender sender, @Nonnull String arg) {
        final List<String> argList = new ArrayList<>();
        for (Environment environment : Environment.values()) {
            final String environmentName = environment.name();
            if (environmentName.startsWith(arg)) {
                argList.add(environmentName);
            }
        }
        return argList;
    }
    
}
