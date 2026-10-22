package com.li64.tide.client.gui;

import com.li64.tide.client.gui.overlays.CastBarOverlay;
import com.li64.tide.client.gui.overlays.CatchMinigameOverlay;
import com.li64.tide.client.gui.overlays.SonarOverlay;
import com.li64.tide.client.gui.overlays.FishingInfoOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public class TideGuiOverlays {
    public static void render(GuiGraphics graphics) {
        float dt = Minecraft.getInstance().getTimer().getRealtimeDeltaTicks();
        CatchMinigameOverlay.render(graphics, dt);
        CastBarOverlay.render(graphics, dt);
        SonarOverlay.render(graphics, dt);
        FishingInfoOverlay.render(graphics, dt);
    }
}
