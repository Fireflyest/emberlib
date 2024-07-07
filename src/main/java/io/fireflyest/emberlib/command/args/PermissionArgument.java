package io.fireflyest.emberlib.command.args;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

/**
 * 权限参数提示
 * 
 * @author Fireflyest
 * @since 1.0
 */
public class PermissionArgument implements Argument {

    @Override
    public List<String> tab(@Nonnull CommandSender sender, @Nonnull String arg) {
        final List<String> argList = new ArrayList<>();
        final Set<Permission> permissions = Bukkit.getPluginManager().getPermissions();
        for (Permission permission : permissions) {
            final String permissionName = permission.getName();
            if (permissionName.startsWith(arg)) {
                argList.add(permissionName);
            }
        }
        return argList;
    }
    
}
