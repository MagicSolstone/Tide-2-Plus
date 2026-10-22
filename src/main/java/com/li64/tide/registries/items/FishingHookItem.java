package com.li64.tide.registries.items;

import com.li64.tide.Tide;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

public class FishingHookItem extends AbstractTooltipItem {
    private final String description;

    public FishingHookItem(Properties properties) {
        this(properties, "");
    }

    public FishingHookItem(Properties properties, String description) {
        super(properties);
        this.description = description;
    }

    @Override
    public void addTooltip(ItemStack stack, Consumer<Component> tooltip) {
        if (description.isEmpty()) return;
        Style gray = Component.empty().getStyle().withColor(ChatFormatting.GRAY);
        Style blue = Component.empty().getStyle().withColor(ChatFormatting.BLUE);
        tooltip.accept(Component.empty());
        tooltip.accept(Component.translatable("text.tide.accessory_tooltip.prefix").setStyle(gray));
        tooltip.accept(Component.translatable(description).setStyle(blue));
    }

    public static Identifier getTexture(ItemStack stack) {
        return Tide.resource("textures/entity/fishing_hook/" + BuiltInRegistries.ITEM
                .getKey(stack.getItem()).getPath() + ".png");
    }
}
