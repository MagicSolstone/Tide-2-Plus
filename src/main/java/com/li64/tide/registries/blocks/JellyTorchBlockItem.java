package com.li64.tide.registries.blocks;

import com.li64.tide.registries.items.TooltipItem;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.level.block.Block;

import java.util.function.Consumer;

public class JellyTorchBlockItem extends StandingAndWallBlockItem implements TooltipItem {
    public JellyTorchBlockItem(Block normal, Block wall, Direction direction, Properties properties) {
        super(normal, wall, direction, properties);
    }

    @Override
    public void addTooltip(ItemStack stack, Consumer<Component> tooltip) {
        Style gray = Component.empty().getStyle().withColor(ChatFormatting.GRAY);
        tooltip.accept(Component.translatable("item.tide.jelly_torch.desc").setStyle(gray));
    }
}
