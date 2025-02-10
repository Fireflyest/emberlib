package io.fireflyest.emberlib.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.ItemTag;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;
import net.md_5.bungee.api.chat.hover.content.Entity;
import net.md_5.bungee.api.chat.hover.content.Item;
import net.md_5.bungee.api.chat.hover.content.Text;

/**
 * 聊天文本构建
 * 
 * @author Fireflyest
 * @since 1.0
 */
public final class ChatUtils {
    
    private static final String MINECRAFT = "minecraft:";

    private ChatUtils() {
    }

    /**
     * 物品显示悬浮事件
     * 
     * @param itemStack 物品
     * @return 悬浮显示事件
     */
    public static HoverEvent hoverItemStack(@Nonnull ItemStack itemStack) {
        final String type = MINECRAFT + itemStack.getType().toString().toLowerCase();
        final String tagString = CraftUtils.toTagString(itemStack);
        final Item item = new Item(type, itemStack.getAmount(), ItemTag.ofNbt(tagString));
        return new HoverEvent(HoverEvent.Action.SHOW_ITEM, item);
    }

    /**
     * 展示物品
     * 
     * @param itemStack 物品
     * @param command 点击执行的指令
     * @return 悬浮显示物品的文本
     */
    public static BaseComponent[] textItemStack(@Nonnull ItemStack itemStack, 
                                                @Nullable String command) {
        final ComponentBuilder builder = new ComponentBuilder();
        final int amount = itemStack.getAmount();
        final String type = itemStack.getType().toString().toLowerCase();
        final String name = ItemUtils.getDisplayName(itemStack);
        final String tagString = CraftUtils.toTagString(itemStack);
        final Item item = new Item(MINECRAFT + type, amount, ItemTag.ofNbt(tagString));
        if (StringUtils.isEmpty(name)) {
            final String translatable = 
                (itemStack.getType().isBlock() ? "block" : "item") + ".minecraft." + type;
            final TranslatableComponent trans = new TranslatableComponent(translatable);
            builder.append(trans);
        } else {
            builder.append(name);
        }
        // 悬浮显示
        builder.event(new HoverEvent(HoverEvent.Action.SHOW_ITEM, item));
        // 点击事件
        if (command != null) {
            builder.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        }
        return builder.create();
    }

    /**
     * 展示生物
     * 
     * @param livingEntity 生物
     * @param command 点击执行的指令
     * @return 悬浮显示生物的文本
     */
    public static BaseComponent[] textEntity(@Nonnull LivingEntity livingEntity, 
                                             @Nullable String command) {
        final ComponentBuilder builder = new ComponentBuilder();
        final String type = livingEntity.getType().toString().toLowerCase();
        final String uuid = livingEntity.getUniqueId().toString();
        final String name = livingEntity.getName();
        final Entity entity = new Entity(MINECRAFT + type, uuid, new TextComponent(name));
        final BaseComponent displayName;
        if (StringUtils.isEmpty(name)) {
            displayName = new TranslatableComponent("entity.minecraft." + type);
        } else {
            displayName = new TextComponent(livingEntity.getName());
        }
        // 悬浮显示
        builder.append(displayName).event(new HoverEvent(HoverEvent.Action.SHOW_ENTITY, entity));
        // 点击事件
        if (command != null) {
            builder.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        }
        return builder.create();
    }

    /**
     * 文本
     * 
     * @param text 文本
     * @param command 点击执行的指令
     * @param hover 悬浮显示的文本
     * @return 文本组件
     */
    public static BaseComponent[] text(@Nonnull String text, 
                                       @Nullable String command, 
                                       @Nullable String hover) {
        final ComponentBuilder builder = new ComponentBuilder(text);
        if (command != null) {
            builder.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        }
        if (hover != null) {
            builder.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(hover)));
        }
        return builder.create();
    }

}
