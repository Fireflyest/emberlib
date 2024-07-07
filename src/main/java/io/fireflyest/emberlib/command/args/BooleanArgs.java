package io.fireflyest.emberlib.command.args;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import org.bukkit.command.CommandSender;

/**
 * 布尔参数
 * 
 * @author Fireflyest
 * @since 1.0
 */
public class BooleanArgs implements Argument {

    @Override
    public List<String> tab(@Nonnull CommandSender sender, @Nonnull String arg) {
        final List<String> argList = new ArrayList<>(2);
        if ("true".startsWith(arg)) {
            argList.add("true");
        }
        if ("false".startsWith(arg)) {
            argList.add("false");
        }
        return argList;
    }
    
}
