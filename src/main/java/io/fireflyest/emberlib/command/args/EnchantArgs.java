package io.fireflyest.emberlib.command.args;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;

/**
 * 附魔参数
 * 
 * @author Fireflyest
 * @since 1.0
 */
public final class EnchantArgs implements Argument {

    @Override
    public List<String> tab(@Nonnull CommandSender sender, @Nonnull String arg) {
        final List<String> argList = new ArrayList<>();
        for (Enchantment enchantment : Enchantment.values()) {
            final String enchantName = enchantment.getKey().getKey();
            if (enchantName.startsWith(arg)) {
                argList.add(enchantName);
            }
        }
        return argList;
    }
    
}
