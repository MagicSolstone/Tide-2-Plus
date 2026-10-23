package com.li64.tide.client.gui;

import com.li64.tide.client.gui.overlays.CastBarOverlay;
import com.li64.tide.client.gui.overlays.CatchMinigameOverlay;
import com.li64.tide.client.gui.overlays.SonarOverlay;
import com.li64.tide.client.gui.overlays.FishingInfoOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public class TideGuiOverlays {
    public static void render(GuiGraphicsExtractor graphics) {
        float dt = Minecraft.getInstance().getDeltaTracker().getRealtimeDeltaTicks();
        CatchMinigameOverlay.render(graphics, dt);
        CastBarOverlay.render(graphics, dt);
        SonarOverlay.render(graphics, dt);
        FishingInfoOverlay.render(graphics, dt);
        com.li64.tide.client.gui.overlays.RodStatsOverlay.render(graphics, dt);
        com.li64.tide.client.gui.overlays.CatchBannerOverlay.render(graphics, dt);
    }
}
