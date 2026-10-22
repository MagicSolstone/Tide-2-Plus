package net.minecraft.client.gui;

import java.util.Objects;
import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.state.gui.GuiTextRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.ARGB;
import net.minecraft.util.FormattedCharSequence;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
class GuiGraphicsExtractor$RenderingTextCollector implements ActiveTextCollector, Consumer<Style> {
   private ActiveTextCollector$Parameters defaultParameters;
   private final GuiGraphicsExtractor$HoveredTextEffects hoveredTextEffects;
   @Nullable
   private final Consumer<Style> additionalConsumer;

   private GuiGraphicsExtractor$RenderingTextCollector(
      final ActiveTextCollector$Parameters this$0,
      final GuiGraphicsExtractor$HoveredTextEffects initialParameters,
      @Nullable final Consumer<Style> hoveredTextEffects,
      final Consumer additonalConsumer
   ) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
      this.defaultParameters = initialParameters;
      this.hoveredTextEffects = hoveredTextEffects;
      this.additionalConsumer = additonalConsumer;
   }

   @Override
   public ActiveTextCollector$Parameters defaultParameters() {
      return this.defaultParameters;
   }

   @Override
   public void defaultParameters(final ActiveTextCollector$Parameters newParameters) {
      this.defaultParameters = newParameters;
   }

   public void accept(final Style style) {
      if (this.hoveredTextEffects.allowTooltip && style.getHoverEvent() != null) {
         this.this$0.hoveredTextStyle = style;
      }

      if (this.hoveredTextEffects.allowCursorChanges && style.getClickEvent() != null) {
         this.this$0.clickableTextStyle = style;
      }

      if (this.additionalConsumer != null) {
         this.additionalConsumer.accept(style);
      }
   }

   @Override
   public void accept(
      final TextAlignment alignment, final int anchorX, final int y, final ActiveTextCollector$Parameters parameters, final FormattedCharSequence text
   ) {
      boolean needsFullStyleScan = this.hoveredTextEffects.allowCursorChanges || this.hoveredTextEffects.allowTooltip || this.additionalConsumer != null;
      int leftX = alignment.calculateLeft(anchorX, this.this$0.minecraft.font, text);
      GuiTextRenderState renderState = new GuiTextRenderState(
         this.this$0.minecraft.font, text, parameters.pose(), leftX, y, ARGB.white(parameters.opacity()), 0, true, needsFullStyleScan, parameters.scissor()
      );
      if (ARGB.as8BitChannel(parameters.opacity()) != 0) {
         this.this$0.guiRenderState.addText(renderState);
      }

      if (needsFullStyleScan) {
         ActiveTextCollector.findElementUnderCursor(renderState, this.this$0.mouseX, this.this$0.mouseY, this);
      }
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
      int lineWidth = this.this$0.minecraft.font.width(message);
      int lineHeight = 9;
      this.defaultScrollingHelper(message, centerX, left, right, top, bottom, lineWidth, lineHeight, parameters);
   }
}
