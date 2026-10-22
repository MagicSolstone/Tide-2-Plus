package net.minecraft.client.gui;

import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.renderer.state.gui.GuiTextRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;
import org.joml.Matrix3x2f;
import org.joml.Vector2f;
import org.joml.Vector2fc;

@Environment(EnvType.CLIENT)
public interface ActiveTextCollector {
   double PERIOD_PER_SCROLLED_PIXEL = 0.5;
   double MIN_SCROLL_PERIOD = 3.0;

   ActiveTextCollector$Parameters defaultParameters();

   void defaultParameters(ActiveTextCollector$Parameters newParameters);

   default void accept(final int x, final int y, final FormattedCharSequence text) {
      this.accept(TextAlignment.LEFT, x, y, this.defaultParameters(), text);
   }

   default void accept(final int x, final int y, final Component text) {
      this.accept(TextAlignment.LEFT, x, y, this.defaultParameters(), text.getVisualOrderText());
   }

   default void accept(final TextAlignment alignment, final int anchorX, final int y, final ActiveTextCollector$Parameters parameters, final Component text) {
      this.accept(alignment, anchorX, y, parameters, text.getVisualOrderText());
   }

   void accept(TextAlignment alignment, int anchorX, int y, ActiveTextCollector$Parameters parameters, FormattedCharSequence text);

   default void accept(final TextAlignment alignment, final int anchorX, final int y, final Component text) {
      this.accept(alignment, anchorX, y, text.getVisualOrderText());
   }

   default void accept(final TextAlignment alignment, final int anchorX, final int y, final FormattedCharSequence text) {
      this.accept(alignment, anchorX, y, this.defaultParameters(), text);
   }

   void acceptScrolling(Component message, int centerX, int left, int right, int top, int bottom, ActiveTextCollector$Parameters parameters);

   default void acceptScrolling(final Component message, final int centerX, final int left, final int right, final int top, final int bottom) {
      this.acceptScrolling(message, centerX, left, right, top, bottom, this.defaultParameters());
   }

   default void acceptScrollingWithDefaultCenter(final Component message, final int left, final int right, final int top, final int bottom) {
      this.acceptScrolling(message, (left + right) / 2, left, right, top, bottom);
   }

   default void defaultScrollingHelper(
      final Component message,
      final int centerX,
      final int left,
      final int right,
      final int top,
      final int bottom,
      final int lineWidth,
      final int lineHeight,
      final ActiveTextCollector$Parameters parameters
   ) {
      int textTop = (top + bottom - lineHeight) / 2 + 1;
      int availableMessageWidth = right - left;
      if (lineWidth > availableMessageWidth) {
         int maxPosition = lineWidth - availableMessageWidth;
         double time = Util.getMillis() / 1000.0;
         double period = Math.max(maxPosition * 0.5, 3.0);
         double alpha = Math.sin((Math.PI / 2) * Math.cos((Math.PI * 2) * time / period)) / 2.0 + 0.5;
         double pos = Mth.lerp(alpha, 0.0, (double)maxPosition);
         ActiveTextCollector$Parameters localParameters = parameters.withScissor(left, right, top, bottom);
         this.accept(TextAlignment.LEFT, left - (int)pos, textTop, localParameters, message.getVisualOrderText());
      } else {
         int textX = Mth.clamp(centerX, left + lineWidth / 2, right - lineWidth / 2);
         this.accept(TextAlignment.CENTER, textX, textTop, message);
      }
   }

   static void findElementUnderCursor(final GuiTextRenderState text, final float testX, final float testY, final Consumer<Style> output) {
      ScreenRectangle bounds = text.bounds();
      if (bounds != null && bounds.containsPoint((int)testX, (int)testY)) {
         Vector2fc localMousePos = text.pose.invert(new Matrix3x2f()).transformPosition(new Vector2f(testX, testY));
         float localMouseX = localMousePos.x();
         float localMouseY = localMousePos.y();
         text.ensurePrepared().visit(new ActiveTextCollector$1(localMouseX, localMouseY, output));
      }
   }

   static boolean isPointInRectangle(final float x, final float y, final float left, final float top, final float right, final float bottom) {
      return x >= left && x < right && y >= top && y < bottom;
   }
}
