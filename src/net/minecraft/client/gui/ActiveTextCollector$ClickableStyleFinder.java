package net.minecraft.client.gui;

import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.state.gui.GuiTextRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.ARGB;
import net.minecraft.util.FormattedCharSequence;
import org.joml.Matrix3x2f;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class ActiveTextCollector$ClickableStyleFinder implements ActiveTextCollector {
   private static final ActiveTextCollector$Parameters INITIAL = new ActiveTextCollector$Parameters(new Matrix3x2f());
   private final Font font;
   private final int testX;
   private final int testY;
   private ActiveTextCollector$Parameters defaultParameters = INITIAL;
   private boolean includeInsertions;
   @Nullable
   private Style result;
   private final Consumer<Style> styleScanner = style -> {
      if (style.getClickEvent() != null || this.includeInsertions && style.getInsertion() != null) {
         this.result = style;
      }
   };

   public ActiveTextCollector$ClickableStyleFinder(final Font font, final int testX, final int testY) {
      this.font = font;
      this.testX = testX;
      this.testY = testY;
   }

   @Override
   public ActiveTextCollector$Parameters defaultParameters() {
      return this.defaultParameters;
   }

   @Override
   public void defaultParameters(final ActiveTextCollector$Parameters newParameters) {
      this.defaultParameters = newParameters;
   }

   @Override
   public void accept(
      final TextAlignment alignment, final int anchorX, final int y, final ActiveTextCollector$Parameters parameters, final FormattedCharSequence text
   ) {
      int leftX = alignment.calculateLeft(anchorX, this.font, text);
      GuiTextRenderState renderState = new GuiTextRenderState(
         this.font, text, parameters.pose(), leftX, y, ARGB.white(parameters.opacity()), 0, true, true, parameters.scissor()
      );
      ActiveTextCollector.findElementUnderCursor(renderState, this.testX, this.testY, this.styleScanner);
   }

   @Override
   public void acceptScrolling(
      final Component message,
      final int centerX,
      final int left,
      final int right,
      final int top,
      final int bottom,
      final ActiveTextCollector$Parameters parameters
   ) {
      int lineWidth = this.font.width(message);
      int lineHeight = 9;
      this.defaultScrollingHelper(message, centerX, left, right, top, bottom, lineWidth, lineHeight, parameters);
   }

   public ActiveTextCollector$ClickableStyleFinder includeInsertions(final boolean flag) {
      this.includeInsertions = flag;
      return this;
   }

   @Nullable
   public Style result() {
      return this.result;
   }
}
