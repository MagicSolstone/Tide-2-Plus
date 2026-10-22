package net.minecraft.client.renderer.state.gui;

import java.util.ArrayList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.state.gui.pip.PictureInPictureRenderState;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
class GuiRenderState$Node {
   @Nullable
   public final GuiRenderState$Node parent;
   @Nullable
   public GuiRenderState$Node up;
   @Nullable
   public List<GuiElementRenderState> elementStates;
   @Nullable
   public List<GuiElementRenderState> glyphStates;
   @Nullable
   public List<GuiItemRenderState> itemStates;
   @Nullable
   public List<GuiTextRenderState> textStates;
   @Nullable
   public List<PictureInPictureRenderState> picturesInPictureStates;

   private GuiRenderState$Node(@Nullable final GuiRenderState$Node parent) {
      this.parent = parent;
   }

   public void addItem(final GuiItemRenderState itemState) {
      if (this.itemStates == null) {
         this.itemStates = new ArrayList<>();
      }

      this.itemStates.add(itemState);
   }

   public void addText(final GuiTextRenderState textState) {
      if (this.textStates == null) {
         this.textStates = new ArrayList<>();
      }

      this.textStates.add(textState);
   }

   public void addPicturesInPictureState(final PictureInPictureRenderState picturesInPictureState) {
      if (this.picturesInPictureStates == null) {
         this.picturesInPictureStates = new ArrayList<>();
      }

      this.picturesInPictureStates.add(picturesInPictureState);
   }

   public void addGuiElement(final GuiElementRenderState blitState) {
      if (this.elementStates == null) {
         this.elementStates = new ArrayList<>();
      }

      this.elementStates.add(blitState);
   }

   public void addGlyph(final GuiElementRenderState glyphState) {
      if (this.glyphStates == null) {
         this.glyphStates = new ArrayList<>();
      }

      this.glyphStates.add(glyphState);
   }
}
