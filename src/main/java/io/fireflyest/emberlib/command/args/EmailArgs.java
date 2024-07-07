package io.fireflyest.emberlib.command.args;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import org.bukkit.command.CommandSender;

/**
 * 邮箱参数
 * 
 * @author Fireflyest
 * @since 1.0
 */
public class EmailArgs implements Argument {

    @Override
    public List<String> tab(@Nonnull CommandSender sender, @Nonnull String arg) {
        final List<String> argList = new ArrayList<>(5);
        if (!arg.contains("@")) {
            argList.add(arg + "@qq.com");
            argList.add(arg + "@163.com");
            argList.add(arg + "@126.com");
            argList.add(arg + "@outlook.com");
            argList.add(arg + "@gmail.com");
        }
        return argList;
    }
    
}
