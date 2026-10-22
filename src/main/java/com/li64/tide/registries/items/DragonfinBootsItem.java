package com.li64.tide.registries.items;

import com.li64.tide.registries.TideArmorMaterials;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.function.Consumer;

public class DragonfinBootsItem extends Item implements TooltipItem {
    public DragonfinBootsItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, net.minecraft.world.item.Item.TooltipContext context, net.minecraft.world.item.component.TooltipDisplay display, java.util.function.Consumer<Component> lines, TooltipFlag flag) {
        super.appendHoverText(stack, context, display, lines, flag);
        this.addTooltip(stack, lines);
    }

    @Override
    public void addTooltip(ItemStack stack, Consumer<Component> tooltip) {
        Style gold = Component.empty().getStyle().withColor(ChatFormatting.GOLD);
        tooltip.accept(Component.translatable("item.tide.dragonfin_boots.desc_0").setStyle(gold));
        tooltip.accept(Component.translatable("item.tide.dragonfin_boots.desc_1").setStyle(gold));
    }
}
