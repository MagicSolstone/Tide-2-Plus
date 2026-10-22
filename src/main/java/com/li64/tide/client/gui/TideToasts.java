package com.li64.tide.client.gui;

import com.li64.tide.Tide;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class TideToasts {
    // TODO: maybe make some custom art for this?
    private static final Identifier TEXTURE = Tide.resource("minecraft", "toast/recipe");

    public static void display(Toast toast) {
        Minecraft.getInstance().getToasts().addToast(toast);
    }

    public record NewPageToast(Component title, Component description, ItemStack displayedItem) implements Toast {
        public NewPageToast(Component title, Component description, ItemStack displayedItem) {
            this.title = title;
            this.description = description;
            this.displayedItem = displayedItem;
            if (description == null) Tide.LOG.error("Component cannot be null");
        }

        @Override
        public @NotNull Visibility render(GuiGraphicsExtractor graphics, ToastComponent component, long time) {
            graphics.blitSprite(TEXTURE, 0, 0, this.width(), this.height());
            graphics.drawString(component.getMinecraft().font, title, 30, 7, -11534256, false);
            graphics.drawString(component.getMinecraft().font, description, 30, 18, -16777216, false);
            graphics.renderFakeItem(displayedItem, 8, 8);
            return (double) (time) >= 5000.0D * component.getNotificationDisplayTimeMultiplier() ? Visibility.HIDE : Visibility.SHOW;
        }
    }
}