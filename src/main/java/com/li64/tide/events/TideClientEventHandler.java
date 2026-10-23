package com.li64.tide.events;

import com.li64.tide.data.item.TideTooltips;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class TideClientEventHandler {
    public static void onTooltipRender(ItemStack stack, List<Component> lines) {
        TideTooltips.add(stack, lines, Minecraft.getInstance().level != null);
    }
}
