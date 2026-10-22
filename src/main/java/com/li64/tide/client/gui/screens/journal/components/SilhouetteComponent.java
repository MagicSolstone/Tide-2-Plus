package com.li64.tide.client.gui.screens.journal.components;

import com.li64.tide.client.gui.screens.journal.FishingJournal;
import com.li64.tide.client.gui.screens.journal.ProfileComponent;
import com.li64.tide.data.fishing.FishData;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SilhouetteComponent extends ProfileComponent {
    private final ItemStack fish;
    private final FishData data;

    public SilhouetteComponent(ItemStack fish, FishData data) {
        this.fish = fish;
        this.data = data;
    }

    @Override
    public void render(@NotNull GuiGraphicsExtractor graphics, Font font, int x, int y, int mouseX, int mouseY, float partialTick) {
        int centerX = x + AREA_WIDTH / 2;
        int centerY = y + getRequiredHeight() / 2;

        // get fish sizing and offsets
        boolean isLarge = data.profile().altSprite().isPresent();
        Identifier alternateTexture = data.profile().altSprite().orElse(null);
        int fishSize = isLarge ? data.profile().altSpriteSize().orElse(16) : 16;
        final int shadowOffset = 1;
//
//        // set shadow color
//        graphics.flush();
//        RenderSystem.setShaderColor(0.8431f, 0.7098f, 0.5804f, 1f);
//
//        // render fish silhouette
//        if (!isLarge) FishingJournal.renderItemSilhouette(graphics, fish,
//                centerX - fishSize / 2 + shadowOffset,
//                centerY - fishSize / 2 + shadowOffset);
//        else FishingJournal.renderTextureSilhouette(graphics, alternateTexture,
//                centerX - fishSize / 2 + shadowOffset,
//                centerY - fishSize / 2 + shadowOffset, fishSize, fishSize);

        // setup shader color for silhouette
        graphics.flush();
//        RenderSystem.setShaderColor(0.8431f, 0.7098f, 0.5804f, 1f); // #d7b694
        RenderSystem.setShaderColor(215 / 255f, 181 / 255f, 148 / 255f, 1f); // #b08561
//        RenderSystem.setShaderColor(176 / 255f, 133 / 255f, 97 / 255f, 1f); // #b08561

        if (!isLarge) FishingJournal.renderItemSilhouette(graphics, fish,
                centerX - fishSize / 2, centerY - fishSize / 2);
        else FishingJournal.renderTextureSilhouette(graphics, alternateTexture,
                centerX - fishSize / 2, centerY - fishSize / 2, fishSize, fishSize);

        // reset shader color
        graphics.flush();
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
    }

    @Override
    public int getRequiredHeight() {
        return data.profile().altSpriteSize().orElse(16) + 2;
    }
}
