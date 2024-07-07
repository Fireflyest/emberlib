package io.fireflyest.emberlib.command;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;
import io.fireflyest.emberlib.command.args.Argument;

/**
 * 简单指令
 * 
 * @author Fireflyest
 * @since 
 */
public abstract class SimpleCommand extends AbstractCommand 
                                    implements CommandExecutor, TabCompleter {

    /**
     * 简单指令
     * 
     * @param name 名称
     */
    protected SimpleCommand(@Nullable String name) {
        super(name);
    }

    /**
     * 简单指令
     */
    protected SimpleCommand() {
        this(null);
    }

    @Override
    public boolean onCommand(CommandSender sender, 
                             Command command, 
                             String label, 
                             String[] args) {
        boolean valid = false;
        switch (args.length) {
            case 0:
                valid = execute(sender);
                break;
            case 1:
                valid = execute(sender, args[0]);
                break;
            case 2:
                valid = execute(sender, args[0], args[1]);
                break;
            case 3:
                valid = execute(sender, args[0], args[1], args[2]);
                break;
            default:
                valid = execute(sender, args);
                break;
        }
        return valid;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(CommandSender sender, 
                                      Command command, 
                                      String label, 
                                      String[] args) {
        final int index = args.length - 1;
        return this.getArgumentTab(index, sender, args[index]);
    }

    /**
     * 添加变量
     * @param arg 变量
     * @return 本身
     */
    public SimpleCommand addArg(@Nonnull Argument arg) {
        if (arguments.size() < MAX_ARGS) {
            arguments.add(arg);
        }
        return this;
    }

    /**
     * 应用到插件
     * 
     * @param plugin 插件
     */
    public AbstractCommand apply(@Nonnull JavaPlugin plugin) {
        final PluginCommand command = plugin.getCommand(this.getName());
        if (command != null) {
            command.setExecutor(this);
            command.setTabCompleter(this);
        }
        return this;
    }
    
}
