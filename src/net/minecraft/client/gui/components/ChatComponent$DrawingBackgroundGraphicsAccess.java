package net.minecraft.client.gui.components;

import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.ActiveTextCollector;
import net.minecraft.client.gui.ActiveTextCollector$Parameters;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.GuiGraphicsExtractor$HoveredTextEffects;
import net.minecraft.client.gui.TextAlignment;
import net.minecraft.client.multiplayer.chat.GuiMessageTag;
import net.minecraft.client.multiplayer.chat.GuiMessageTag$Icon;
import net.minecraft.util.ARGB;
import net.minecraft.util.FormattedCharSequence;
import org.joml.Matrix3x2f;

@Environment(EnvType.CLIENT)
class ChatComponent$DrawingBackgroundGraphicsAccess implements ChatComponent$ChatGraphicsAccess {
   private final GuiGraphicsExtractor graphics;
   private final ActiveTextCollector textRenderer;
   private ActiveTextCollector$Parameters parameters;

   public ChatComponent$DrawingBackgroundGraphicsAccess(final GuiGraphicsExtractor graphics) {
      this.graphics = graphics;
      this.textRenderer = graphics.textRenderer(GuiGraphicsExtractor$HoveredTextEffects.NONE, null);
      this.parameters = this.textRenderer.defaultParameters();
   }

   @Override
   public void updatePose(final Consumer<Matrix3x2f> updater) {
      updater.accept(this.graphics.pose());
      this.parameters = this.parameters.withPose(new Matrix3x2f(this.graphics.pose()));
   }

   @Override
   public void fill(final int x0, final int y0, final int x1, final int y1, final int color) {
      this.graphics.fill(x0, y0, x1, y1, color);
   }

   @Override
   public boolean handleMessage(final int textTop, final float opacity, final FormattedCharSequence message) {
      this.textRenderer.accept(TextAlignment.LEFT, 0, textTop, this.parameters.withOpacity(opacity), message);
      return false;
   }

   @Override
   public void handleTag(final int x0, final int y0, final int x1, final int y1, final float opacity, final GuiMessageTag tag) {
      int indicatorColor = ARGB.color(opacity, tag.indicatorColor());
      this.graphics.fill(x0, y0, x1, y1, indicatorColor);
   }

   @Override
   public void handleTagIcon(final int left, final int bottom, final boolean forceVisible, final GuiMessageTag tag, final GuiMessageTag$Icon icon) {
   }
}
