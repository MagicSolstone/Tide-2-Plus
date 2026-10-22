package com.li64.tide.client.gui.screens;

import com.li64.tide.Tide;
import com.li64.tide.client.gui.menus.AnglingTableMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class AnglingTableScreen extends ItemCombinerScreen<AnglingTableMenu> {
    public static final Identifier GUI_LOCATION = Tide.resource("textures/gui/container/angling_table.png");

    public AnglingTableScreen(AnglingTableMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component, GUI_LOCATION);
        this.titleLabelX = 56;
        this.titleLabelY = 8;
    }

    @Override
    protected void renderErrorIcon(@NotNull GuiGraphics graphics, int p_266822_, int p_267045_) {}
}
