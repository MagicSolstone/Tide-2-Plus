package com.li64.tide.client.gui.overlays;

import net.minecraft.util.ARGB;
import net.minecraft.client.renderer.RenderPipelines;
import com.li64.tide.registries.entities.misc.fishing.HookAccessor;
import com.li64.tide.util.TideUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class SonarOverlay {
    private static final Identifier SLOT_BACKGROUND_SPRITE = TideUtils.sprite("bait/slot_background");
    private static final float MAX_TIMEOUT = 20f;
    private static final float MAX_FADEOUT = 20f;

    private static ItemStack stack;
    private static float timeout;
    private static float fadeout;

    @SuppressWarnings("DataFlowIssue")
    public static void render(GuiGraphicsExtractor graphics, float dt) {
        if (stack == null) return;
        Player player = Minecraft.getInstance().player;
        boolean hooked = !HookAccessor.bobberRemoved(player) && HookAccessor.getHook(player).isFishHooked();

        if (timeout > 0f) timeout -= dt;
        else if (!hooked) fadeout -= dt;
        if (fadeout < 0f) {
            stack = null;
            return;
        }

        int x = graphics.guiWidth() / 2 - 8;
        int y = graphics.guiHeight() / 2 + 12;
        float alpha = fadeout / MAX_FADEOUT;

        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SLOT_BACKGROUND_SPRITE, x - 4, y - 4, 24, 24, ARGB.white(alpha));
        if (alpha > 0.4f) graphics.item(stack, x, y); // (26.x can't fade items, so it just goes as the slot fades)
    }

    public static void show(ItemStack stack) {
        SonarOverlay.stack = stack;
        SonarOverlay.timeout = MAX_TIMEOUT;
        SonarOverlay.fadeout = MAX_FADEOUT;
    }
}
