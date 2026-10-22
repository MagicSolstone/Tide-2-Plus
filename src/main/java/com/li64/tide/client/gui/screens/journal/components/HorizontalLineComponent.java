package com.li64.tide.client.gui.screens.journal.components;

import net.minecraft.client.renderer.RenderPipelines;
import com.li64.tide.client.gui.screens.journal.ProfileComponent;
import com.li64.tide.util.TideUtils;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.jetbrains.annotations.NotNull;

public class HorizontalLineComponent extends ProfileComponent {
    private final boolean top;

    public HorizontalLineComponent(boolean top) {
        this.top = top;
    }

    @Override
    public void render(@NotNull GuiGraphicsExtractor graphics, Font font, int x, int y, int mouseX, int mouseY, float partialTick) {
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, top ? ProfileComponent.LINE_TOP : ProfileComponent.LINE_BOTTOM, x, y + 4, ProfileComponent.AREA_WIDTH, 2);
    }

    @Override
    public int getRequiredHeight() {
        return 10;
    }
}
