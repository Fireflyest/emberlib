package io.fireflyest.emberlib.command.args;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

/**
 * 世界参数
 * @author Fireflyest
 * @since 1.0
 */
public final class WorldArgs implements Argument {

    @Override
    public List<String> tab(@Nonnull CommandSender sender, @Nonnull String arg) {
        final List<String> argList = new ArrayList<>();
        for (World world : Bukkit.getWorlds()) {
            final String worldName = world.getName();
            if (worldName.startsWith(arg)) {
                argList.add(worldName);
            }
        }
        return argList;
    }
    
}
