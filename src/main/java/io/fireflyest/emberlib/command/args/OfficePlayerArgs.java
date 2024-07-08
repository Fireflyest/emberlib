package io.fireflyest.emberlib.command.args;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

/**
 * 离线玩家名称参数
 * 
 * @author Fireflyest
 * @since 1.0
 */
public final class OfficePlayerArgs implements Argument {

    @Override
    public List<String> tab(@Nonnull CommandSender sender, @Nonnull String arg) {
        final List<String> argList = new ArrayList<>();
        this.putOfflinePlayer(argList, arg);
        return argList;
    }
    
    /**
     * 离线玩家提示
     * @param argList 提示列表
     * @param arg 参数
     */
    private void putOfflinePlayer(List<String> argList, String arg) {
        for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
            if (argList.size() > 20) {
                break;
            }
            final String name = offlinePlayer.getName();
            if (name != null && name.startsWith(arg)) {
                argList.add(name);
            }
        }
    }

}
