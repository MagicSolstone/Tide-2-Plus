package net.minecraft.client.gui.components;

import com.mojang.blaze3d.platform.cursor.CursorTypes;
import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.ActiveTextCollector;
import net.minecraft.client.gui.ActiveTextCollector$Parameters;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.GuiGraphicsExtractor$HoveredTextEffects;
import net.minecraft.client.gui.TextAlignment;
import net.minecraft.client.multiplayer.chat.GuiMessageTag;
import net.minecraft.client.multiplayer.chat.GuiMessageTag$Icon;
import net.minecraft.network.chat.Style;
import net.minecraft.util.ARGB;
import net.minecraft.util.FormattedCharSequence;
import org.joml.Matrix3x2f;
import org.joml.Vector2f;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
class ChatComponent$DrawingFocusedGraphicsAccess implements ChatComponent$ChatGraphicsAccess, Consumer<Style> {
   private final GuiGraphicsExtractor graphics;
   private final Font font;
   private final ActiveTextCollector textRenderer;
   private ActiveTextCollector$Parameters parameters;
   private final int globalMouseX;
   private final int globalMouseY;
   private final Vector2f localMousePos = new Vector2f();
   @Nullable
   private Style hoveredStyle;
   private final boolean changeCursorOnInsertions;

   public ChatComponent$DrawingFocusedGraphicsAccess(
      final GuiGraphicsExtractor graphics, final Font font, final int mouseX, final int mouseY, final boolean changeCursorOnInsertions
   ) {
      this.graphics = graphics;
      this.font = font;
      this.textRenderer = graphics.textRenderer(GuiGraphicsExtractor$HoveredTextEffects.TOOLTIP_AND_CURSOR, this);
      this.globalMouseX = mouseX;
      this.globalMouseY = mouseY;
      this.changeCursorOnInsertions = changeCursorOnInsertions;
      this.parameters = this.textRenderer.defaultParameters();
      this.updateLocalMousePos();
   }

   private void updateLocalMousePos() {
      this.graphics.pose().invert(new Matrix3x2f()).transformPosition(this.globalMouseX, this.globalMouseY, this.localMousePos);
   }

   @Override
   public void updatePose(final Consumer<Matrix3x2f> updater) {
      updater.accept(this.graphics.pose());
      this.parameters = this.parameters.withPose(new Matrix3x2f(this.graphics.pose()));
      this.updateLocalMousePos();
   }

   @Override
   public void fill(final int x0, final int y0, final int x1, final int y1, final int color) {
      this.graphics.fill(x0, y0, x1, y1, color);
   }

   public void accept(final Style style) {
      this.hoveredStyle = style;
   }

   @Override
   public boolean handleMessage(final int textTop, final float opacity, final FormattedCharSequence message) {
      this.hoveredStyle = null;
      this.textRenderer.accept(TextAlignment.LEFT, 0, textTop, this.parameters.withOpacity(opacity), message);
      if (this.changeCursorOnInsertions && this.hoveredStyle != null && this.hoveredStyle.getInsertion() != null) {
         this.graphics.requestCursor(CursorTypes.POINTING_HAND);
      }

      return this.hoveredStyle != null;
   }

   private boolean isMouseOver(final int left, final int top, final int right, final int bottom) {
      return ActiveTextCollector.isPointInRectangle(this.localMousePos.x, this.localMousePos.y, left, top, right, bottom);
   }

   @Override
   public void handleTag(final int x0, final int y0, final int x1, final int y1, final float opacity, final GuiMessageTag tag) {
      int indicatorColor = ARGB.color(opacity, tag.indicatorColor());
      this.graphics.fill(x0, y0, x1, y1, indicatorColor);
      if (this.isMouseOver(x0, y0, x1, y1)) {
         this.showTooltip(tag);
      }
   }

   @Override
   public void handleTagIcon(final int left, final int bottom, final boolean forceVisible, final GuiMessageTag tag, final GuiMessageTag$Icon icon) {
      int top = bottom - icon.height - 1;
      int right = left + icon.width;
      boolean isMouseOver = this.isMouseOver(left, top, right, bottom);
      if (isMouseOver) {
         this.showTooltip(tag);
      }

      if (forceVisible || isMouseOver) {
         icon.extractRenderState(this.graphics, left, top);
      }
   }

   private void showTooltip(final GuiMessageTag tag) {
      if (tag.text() != null) {
         this.graphics.setTooltipForNextFrame(this.font, this.font.split(tag.text(), 210), this.globalMouseX, this.globalMouseY);
      }
   }
}
