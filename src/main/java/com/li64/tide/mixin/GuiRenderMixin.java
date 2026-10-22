package com.li64.tide.mixin;

import com.li64.tide.client.gui.TideGuiOverlays;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.DeltaTracker;

@Mixin(value = Gui.class)
public class GuiRenderMixin {
    @Inject(at = @At(value = "TAIL"), method = "render")
    public void render(GuiGraphics graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        TideGuiOverlays.render(graphics);
    }
}
