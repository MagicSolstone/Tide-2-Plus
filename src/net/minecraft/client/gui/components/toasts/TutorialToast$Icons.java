package net.minecraft.client.gui.components.toasts;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public enum TutorialToast$Icons {
   MOVEMENT_KEYS(Identifier.withDefaultNamespace("toast/movement_keys")),
   MOUSE(Identifier.withDefaultNamespace("toast/mouse")),
   TREE(Identifier.withDefaultNamespace("toast/tree")),
   RECIPE_BOOK(Identifier.withDefaultNamespace("toast/recipe_book")),
   WOODEN_PLANKS(Identifier.withDefaultNamespace("toast/wooden_planks")),
   SOCIAL_INTERACTIONS(Identifier.withDefaultNamespace("toast/social_interactions")),
   RIGHT_CLICK(Identifier.withDefaultNamespace("toast/right_click"));

   private final Identifier sprite;

   private TutorialToast$Icons(final Identifier sprite) {
      this.sprite = sprite;
   }

   public void extractRenderState(final GuiGraphicsExtractor graphics, final int x, final int y) {
      graphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.sprite, x, y, 20, 20);
   }
}
