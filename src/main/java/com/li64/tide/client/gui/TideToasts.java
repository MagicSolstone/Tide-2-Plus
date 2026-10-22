package com.li64.tide.client.gui;

import com.li64.tide.client.Gui26;
import net.minecraft.client.renderer.RenderPipelines;
import com.li64.tide.Tide;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.toasts.ToastManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class TideToasts {
    // TODO: maybe make some custom art for this?
    private static final Identifier TEXTURE = Tide.resource("minecraft", "toast/recipe");

    public static void display(Toast toast) {
        Minecraft.getInstance().gui.toastManager().addToast(toast);
    }

    public static final class NewPageToast implements Toast {
        private final Component title;
        private final Component description;
        private final ItemStack displayedItem;
        private Visibility wantedVisibility = Visibility.SHOW;

        public NewPageToast(Component title, Component description, ItemStack displayedItem) {
            this.title = title;
            this.description = description;
            this.displayedItem = displayedItem;
            if (description == null) Tide.LOG.error("Component cannot be null");
        }

        @Override
        public @NotNull Visibility getWantedVisibility() {
            return this.wantedVisibility;
        }

        @Override
        public void update(@NotNull ToastManager manager, long fullyVisibleForMs) {
            this.wantedVisibility = fullyVisibleForMs >= 5000.0 * manager.getNotificationDisplayTimeMultiplier() ? Visibility.HIDE : Visibility.SHOW;
        }

        @Override
        public void extractRenderState(@NotNull GuiGraphicsExtractor graphics, @NotNull Font font, long fullyVisibleForMs) {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, TEXTURE, 0, 0, this.width(), this.height());
            Gui26.text(graphics, font, title, 30, 7, -11534256, false);
            Gui26.text(graphics, font, description, 30, 18, -16777216, false);
            graphics.fakeItem(displayedItem, 8, 8);
        }
    }
}
