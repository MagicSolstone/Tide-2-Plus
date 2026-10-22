package net.minecraft.client.multiplayer.chat;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public enum GuiMessageTag$Icon {
   CHAT_MODIFIED(Identifier.withDefaultNamespace("icon/chat_modified"), 9, 9);

   public final Identifier sprite;
   public final int width;
   public final int height;

   private GuiMessageTag$Icon(final Identifier sprite, final int width, final int height) {
      this.sprite = sprite;
      this.width = width;
      this.height = height;
   }

   public void extractRenderState(final GuiGraphicsExtractor graphics, final int x, final int y) {
      graphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.sprite, x, y, this.width, this.height);
   }
}
