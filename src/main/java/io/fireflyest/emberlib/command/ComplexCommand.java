package io.fireflyest.emberlib.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * 复杂指令
 * 
 * @author Fireflyest
 * @since 1.0
 */
public abstract class ComplexCommand extends AbstractCommand 
                                     implements CommandExecutor, TabCompleter {

    /**
     * 子指令
     */
    protected final Map<String, SubCommand> subCommands = new HashMap<>();

    /**
     * 复杂指令
     * 
     * @param name 指令名称
     */
    protected ComplexCommand(@Nullable String name) {
        super(name);

        // 第一个参数的提示为子指令
        this.getArguments().add((sender, arg) -> {
            final List<String> argList = new ArrayList<>();
            for (String string : subCommands.keySet()) {
                if (string.startsWith(arg)) {
                    argList.add(string);
                }
            }
            return argList;
        });
    }

    /**
     * 复杂指令
     */
    protected ComplexCommand() {
        this(null);
    }

    @Override
    public boolean onCommand(CommandSender sender, 
                             Command command, 
                             String label, 
                             String[] args) {
        
        return this.executeCommand(sender, args);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, 
                                      Command command, 
                                      String label, 
                                      String[] args) {
        int index = args.length - 1;
        List<String> list = Collections.emptyList();
        SubCommand subCommand = null;
        if (index == 0) { // 复杂指令的子指令列表
            list = this.getArgumentTab(index, sender, args[index]);
        } else if ((subCommand = subCommands.get(args[0])) != null) { // 转由子指令提示
            index--;
            list = subCommand.getArgumentTab(index, sender, args[index]);
        }
        return list;
    }

    @Override
    public ComplexCommand name(String name) {
        super.name(name);
        return this;
    }

    @Override
    public ComplexCommand schedule(boolean async) {
        super.schedule(async);
        return this;
    }

    /**
     * 添加子指令
     * 
     * @param subCommand 子指令
     * @return 本身
     */
    public ComplexCommand addSub(@Nonnull SubCommand subCommand) {
        final String subCommandName = StringUtils.removeStart(subCommand.getName(), this.getName());
        this.subCommands.put(subCommandName, subCommand.name(subCommandName));
        return this;
    }

    /**
     * 添加子指令
     * 
     * @param name 子指令名称
     * @param subCommand 子指令
     * @return 本身
     */
    public ComplexCommand addSub(@Nonnull String name, @Nonnull SubCommand subCommand) {
        this.subCommands.put(name, subCommand.name(name));
        return this;
    }

    /**
     * 应用到插件
     * 
     * @param plugin 插件
     * @return 本身
     */
    public ComplexCommand apply(@Nonnull JavaPlugin plugin) {
        this.plugin = plugin;
        final PluginCommand command = plugin.getCommand(this.getName());
        if (command != null) {
            final String permission = command.getPermission();
            if (permission != null) {
                for (SubCommand subCommand : subCommands.values()) {
                    subCommand.permission(permission + "." + subCommand.getName());
                }
            }
            command.setExecutor(this);
            command.setTabCompleter(this);
        }
        return this;
    }

    /**
     * 执行指令
     * @param sender 执行者
     * @param args 参数
     * @return 执行是否有效
     */
    private boolean executeCommand(@Nonnull CommandSender sender, @Nonnull String[] args) {
        boolean valid = false;
        boolean schedule = false;
        boolean async = false;
        SubCommand subCommand = null;
        CommandRunnable runnable = null;
        if (args.length == 0) {
            schedule = this.isSchedule();
            async = this.isAsync();
            runnable = this.runnable(sender, args);
        } else if ((subCommand = subCommands.get(args[0])) != null) {
            final String[] subArgs = new String[args.length - 1];
            System.arraycopy(args, 1, subArgs, 0, args.length - 1);
            schedule = this.isSchedule();
            async = subCommand.isAsync();
            runnable = subCommand.runnable(sender, subArgs);
        }
        if (runnable != null) {
            if (schedule && plugin != null) {
                if (async) {
                    runnable.runTaskAsynchronously(plugin);
                } else {
                    runnable.runTask(plugin);
                }
                valid = true;
            } else {
                runnable.run();
                valid = runnable.isValid();
            }
        }
        return valid;
    }

}
