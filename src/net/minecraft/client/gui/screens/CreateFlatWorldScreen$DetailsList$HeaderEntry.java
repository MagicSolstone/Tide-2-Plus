package net.minecraft.client.gui.screens;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
class CreateFlatWorldScreen$DetailsList$HeaderEntry extends CreateFlatWorldScreen$DetailsList$Entry {
   private final Font font;

   public CreateFlatWorldScreen$DetailsList$HeaderEntry(final Font font) {
      this.font = font;
   }

   @Override
   public void extractContent(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final boolean hovered, final float a) {
      graphics.text(this.font, CreateFlatWorldScreen$DetailsList.LAYER_MATERIAL_TITLE, this.getContentX(), this.getContentY(), -1);
      graphics.text(
         this.font,
         CreateFlatWorldScreen$DetailsList.HEIGHT_TITLE,
         this.getContentRight() - this.font.width(CreateFlatWorldScreen$DetailsList.HEIGHT_TITLE),
         this.getContentY(),
         -1
      );
   }

   @Override
   public Component getNarration() {
      return CommonComponents.joinForNarration(CreateFlatWorldScreen$DetailsList.LAYER_MATERIAL_TITLE, CreateFlatWorldScreen$DetailsList.HEIGHT_TITLE);
   }
}
