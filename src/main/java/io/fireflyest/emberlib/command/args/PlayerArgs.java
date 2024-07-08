package io.fireflyest.emberlib.command.args;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bukkit.command.CommandSender;

/**
 * 玩家名称
 * 
 * @author Fireflyest
 * @since 1.0
 */
public final class PlayerArgs implements Argument {

    @Override
    @Nullable
    public List<String> tab(@Nonnull CommandSender sender, @Nonnull String arg) {
        return null;
    }
    
}
