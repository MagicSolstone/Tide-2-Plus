package net.minecraft.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Font$DisplayMode;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.TextDisplayEntityRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.Display$TextDisplay;
import net.minecraft.world.entity.Display$TextDisplay$Align;
import net.minecraft.world.entity.Display$TextDisplay$CachedInfo;
import net.minecraft.world.entity.Display$TextDisplay$CachedLine;
import net.minecraft.world.entity.Display$TextDisplay$TextRenderState;
import org.joml.Matrix4f;

@Environment(EnvType.CLIENT)
public class DisplayRenderer$TextDisplayRenderer
   extends DisplayRenderer<Display$TextDisplay, Display$TextDisplay$TextRenderState, TextDisplayEntityRenderState> {
   private final Font font;

   protected DisplayRenderer$TextDisplayRenderer(final EntityRendererProvider$Context context) {
      super(context);
      this.font = context.getFont();
   }

   public TextDisplayEntityRenderState createRenderState() {
      return new TextDisplayEntityRenderState();
   }

   public void extractRenderState(final Display$TextDisplay entity, final TextDisplayEntityRenderState state, final float partialTicks) {
      super.extractRenderState(entity, state, partialTicks);
      state.textRenderState = entity.textRenderState();
      state.cachedInfo = entity.cacheDisplay(this::splitLines);
   }

   private Display$TextDisplay$CachedInfo splitLines(final Component input, final int width) {
      List<FormattedCharSequence> lines = this.font.split(input, width);
      List<Display$TextDisplay$CachedLine> result = new ArrayList<>(lines.size());
      int maxLineWidth = 0;

      for (FormattedCharSequence line : lines) {
         int lineWidth = this.font.width(line);
         maxLineWidth = Math.max(maxLineWidth, lineWidth);
         result.add(new Display$TextDisplay$CachedLine(line, lineWidth));
      }

      return new Display$TextDisplay$CachedInfo(result, maxLineWidth);
   }

   public void submitInner(
      final TextDisplayEntityRenderState state,
      final PoseStack poseStack,
      final SubmitNodeCollector submitNodeCollector,
      final int lightCoords,
      final float interpolationProgress
   ) {
      Display$TextDisplay$TextRenderState renderState = state.textRenderState;
      byte flags = renderState.flags();
      boolean seeThrough = (flags & 2) != 0;
      Font$DisplayMode textDisplayMode = seeThrough ? Font$DisplayMode.SEE_THROUGH : Font$DisplayMode.POLYGON_OFFSET;
      boolean useDefaultBackground = (flags & 4) != 0;
      boolean shadow = (flags & 1) != 0;
      Display$TextDisplay$Align alignment = Display$TextDisplay.getAlign(flags);
      byte textOpacity = (byte)renderState.textOpacity().get(interpolationProgress);
      int backgroundColor;
      if (useDefaultBackground) {
         float backgroundAlpha = Minecraft.getInstance().gameRenderer.gameRenderState().optionsRenderState.getBackgroundOpacity(0.25F);
         backgroundColor = (int)(backgroundAlpha * 255.0F) << 24;
      } else {
         backgroundColor = renderState.backgroundColor().get(interpolationProgress);
      }

      float y = 0.0F;
      Matrix4f pose = poseStack.last().pose();
      pose.rotate((float) Math.PI, 0.0F, 1.0F, 0.0F);
      pose.scale(-0.025F, -0.025F, -0.025F);
      Display$TextDisplay$CachedInfo cachedInfo = state.cachedInfo;
      int lineSpacing = 1;
      int lineHeight = 9 + 1;
      int width = cachedInfo.width();
      int height = cachedInfo.lines().size() * lineHeight - 1;
      pose.translate(1.0F - width / 2.0F, -height, 0.0F);
      if (backgroundColor != 0) {
         submitNodeCollector.submitTextBackground(poseStack, -1.0F, -1.0F, width, height, backgroundColor, textDisplayMode, lightCoords);
      }

      for (Display$TextDisplay$CachedLine line : cachedInfo.lines()) {
         float offset = switch (alignment) {
            case LEFT -> 0.0F;
            case RIGHT -> width - line.width();
            case CENTER -> width / 2.0F - line.width() / 2.0F;
         };
         submitNodeCollector.submitText(poseStack, offset, y, line.contents(), shadow, textDisplayMode, lightCoords, textOpacity << 24 | 16777215, 0, 0);
         y += lineHeight;
      }
   }
}
