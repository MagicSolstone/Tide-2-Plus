package com.li64.tide.client.gui.overlays;

import net.minecraft.util.ARGB;
import com.li64.tide.client.Gui26;
import net.minecraft.client.renderer.RenderPipelines;
import com.li64.tide.Tide;
import com.li64.tide.compat.CompatHelper;
import com.li64.tide.data.fishing.MinigameBehavior;
import com.li64.tide.network.messages.MinigameServerMsg;
import com.li64.tide.registries.entities.misc.fishing.HookAccessor;
import com.li64.tide.util.TideUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

import java.util.Random;

public class CatchMinigameOverlay {
    private static final int INIT_DELAY_MILLIS = 200;

    private static final Identifier BAR_BG = Tide.resource("textures/gui/fishing/minigame_bg.png");
    private static final Identifier FILL_WATER = TideUtils.sprite("fishing/minigame_fill");
    private static final Identifier FILL_LAVA = TideUtils.sprite("fishing/minigame_fill_lava");
    private static final Identifier FILL_VOID = TideUtils.sprite("fishing/minigame_fill_void");
    private static final Identifier BAR_OVERLAY = Tide.resource("textures/gui/fishing/minigame_overlay.png");

    private static final Identifier MARKER = Tide.resource("textures/gui/fishing/marker.png");
    private static final Identifier SELECT = Tide.resource("textures/gui/fishing/marker_select.png");

    private static float timeLeft = 100f;
    private static float animProgress = 0f;
    private static float timer = 20f;
    private static boolean isActive = false;
    private static MutableComponent accuracyText = null;
    private static ChatFormatting textColor = ChatFormatting.WHITE;
    private static byte type;
    private static float area;
    private static float speed;
    private static float perfectZone, greatZone, badZone;
    private static MinigameBehavior behavior;
    private static long delayTime;

    public static void start(byte type, byte behavior, float area, float speed, float perfect, float great, float bad) {
        if (isActive) return;
        if (Tide.CI) Tide.LOG.info("TIDE-CI minigame");
        // an older server sends no zones: fall back to original Tide's (perfect 0.1, everything else in the area)
        CatchMinigameOverlay.perfectZone = perfect > 0 ? perfect : Math.min(0.1f, area);
        CatchMinigameOverlay.greatZone = great > 0 ? great : area;
        CatchMinigameOverlay.badZone = bad > 0 ? bad : area;

        CatchMinigameOverlay.type = type;
        CatchMinigameOverlay.behavior = MinigameBehavior.values()[behavior];
        CatchMinigameOverlay.area = area;
        CatchMinigameOverlay.speed = speed;

        accuracyText = null;
        animProgress = new Random().nextFloat() * 100f;

        timeLeft = 0;
        timer = 0;

        isActive = true;
        delayTime = System.currentTimeMillis() + INIT_DELAY_MILLIS;
    }

    public static void interact() {
        if (!isActive || System.currentTimeMillis() < delayTime) return;

        // Calculate accuracy
        float accuracy = Math.abs(getMinigamePosition());

        // the server decides whether the fish stays on; this is just what the player sees right away
        if (accuracy < perfectZone) {
            accuracyText = Component.translatable("minigame.perfect");
            textColor = ChatFormatting.GOLD;
        } else if (accuracy < greatZone) {
            accuracyText = Component.literal("Great!");
            textColor = ChatFormatting.AQUA;
        } else if (accuracy < area) {
            accuracyText = Component.literal("Good");
            textColor = ChatFormatting.GREEN;
        } else if (accuracy < badZone) {
            accuracyText = Component.literal("Bad hook...");
            textColor = ChatFormatting.YELLOW;
        } else {
            accuracyText = Component.translatable("minigame.miss");
            textColor = ChatFormatting.RED;
        }
        Tide.NETWORK.sendToServer(new MinigameServerMsg((byte) 5, accuracy));

        close();
    }

    public static void close() {
        isActive = false;
        timer = 0f;
    }

    public static void render(GuiGraphicsExtractor graphics, float dt) {
        if (CompatHelper.useStarcatcherMinigame()) return;
        if (timer >= 20f) return;
        if (!isActive) timer += dt;
        else {
            timeLeft += dt;
            animProgress += dt;

            if (timeLeft >= 80f || HookAccessor.bobberRemoved(Minecraft.getInstance().player)) {
                // Timeout
                close();
                Tide.NETWORK.sendToServer(new MinigameServerMsg((byte) 0));
            }
        }

        float alpha = -Mth.clamp((timer - 10f) / 10f, 0f, 1f) + 1f;

        int texWidth = 60;
        int texHeight = 7;

        // zone edges in pixels from the centre (the marker travels +-28 px)
        final float reach = texWidth / 2f - 2;
        int goodPx = Math.round(area * reach);
        int offset = texWidth / 2 - goodPx;

        int x = (graphics.guiWidth() - texWidth) / 2;
        int y = graphics.guiHeight() / 2 - 18;

        int color = ARGB.white(alpha);

        Identifier fillSprite = type == 1 ? FILL_LAVA : (type == 2 ? FILL_VOID : FILL_WATER);

        graphics.blit(RenderPipelines.GUI_TEXTURED, BAR_BG, x, y, 0, 0, texWidth, texHeight, texWidth, texHeight, color);
        int cx = x + texWidth / 2;
        // terrible (outer) and bad (red) zones, then the water fill for good, a lighter band for great, gold for perfect
        graphics.fill(x + 1, y + 1, x + texWidth - 1, y + texHeight - 1, ARGB.color(alpha * 0.55f, 0x4A0E0E));
        int badPx = Math.round(badZone * reach);
        graphics.fill(cx - badPx, y + 1, cx + badPx, y + texHeight - 1, ARGB.color(alpha * 0.8f, 0xC0392B));
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, fillSprite, x + offset, y, texWidth - offset * 2, texHeight, color);
        int greatPx = Math.round(greatZone * reach);
        graphics.fill(cx - greatPx, y + 2, cx + greatPx, y + texHeight - 2, ARGB.color(alpha * 0.35f, 0xFFFFFF));
        int perfectPx = Math.max(1, Math.round(perfectZone * reach));
        graphics.fill(cx - perfectPx, y + 2, cx + perfectPx, y + texHeight - 2, ARGB.color(alpha * 0.85f, 0xFFD24A));
        graphics.blit(RenderPipelines.GUI_TEXTURED, BAR_OVERLAY, x, y, 0, 0, texWidth, texHeight, texWidth, texHeight, color);

        int markerX = Math.round((x + texWidth / 2f - 2) + getMinigamePosition() * (texWidth / 2f - 2));
        Identifier markerTexture = timer % 4 < 2 ? MARKER : SELECT;
        graphics.blit(RenderPipelines.GUI_TEXTURED, markerTexture, markerX, y - 1, 0, 0, 4, 9, 4, 9, color);

        if (Tide.CLIENT_CONFIG.minigame.doFeedback && accuracyText != null) {
            Font font = Minecraft.getInstance().font;
            if (alpha >= 0.03f) {
                Gui26.text(graphics, font,
                        accuracyText.withStyle(accuracyText.getStyle().withColor(textColor)),
                        (graphics.guiWidth() - font.width(accuracyText)) / 2, y - 10,
                        ARGB.color(alpha, 0xffffff), false);
            }
        }

    }

    private static float getMinigamePosition() {
        return behavior.apply(animProgress * speed);
    }

    public static boolean isActive() {
        return isActive;
    }
}
