package net.minecraft.client.renderer.feature;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Font$DisplayMode;
import net.minecraft.client.gui.Font$PreparedText;

@Environment(EnvType.CLIENT)
public class TextFeatureRenderer extends RenderTypeFeatureRenderer<TextFeatureRenderer$Submit> {
   public static final FeatureRendererType<TextFeatureRenderer$Submit> TYPE = FeatureRendererType.create("Text");

   @Override
   protected void buildGroup(final FeatureFrameContext context, final List<TextFeatureRenderer$Submit> submits) {
      Font font = context.font();
      TextFeatureRenderer$GlyphRenderer glyphRenderer = new TextFeatureRenderer$GlyphRenderer(this);

      for (TextFeatureRenderer$Submit submit : submits) {
         glyphRenderer.pose.set(submit.pose());
         glyphRenderer.lightCoords = submit.lightCoords();
         glyphRenderer.displayMode = submit.displayMode();
         switch (submit.content()) {
            case TextFeatureRenderer$Content$Text text:
               renderText(font, glyphRenderer, text);
               break;
            case TextFeatureRenderer$Content$StandaloneBackground standaloneBackground:
               glyphRenderer.acceptRenderable(
                  font.prepareBackground(
                     standaloneBackground.x0(), standaloneBackground.y0(), standaloneBackground.x1(), standaloneBackground.y1(), standaloneBackground.color()
                  )
               );
               break;
            default:
               throw new MatchException(null, null);
         }
      }
   }

   private static void renderText(final Font font, final TextFeatureRenderer$GlyphRenderer glyphRenderer, final TextFeatureRenderer$Content$Text content) {
      if (content.outlineColor() == 0) {
         Font$PreparedText text = font.prepareText(
            content.string(), content.x(), content.y(), content.color(), content.dropShadow(), false, content.backgroundColor()
         );
         text.visit(glyphRenderer);
      } else {
         Font$PreparedText outline = font.prepare8xTextOutline(content.string(), content.x(), content.y(), content.outlineColor());
         Font$PreparedText text = font.prepareText(content.string(), content.x(), content.y(), content.color(), false, false, 0);
         glyphRenderer.displayMode = Font$DisplayMode.NORMAL;
         outline.visit(glyphRenderer);
         glyphRenderer.displayMode = Font$DisplayMode.POLYGON_OFFSET;
         text.visit(glyphRenderer);
      }
   }
}
