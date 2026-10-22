package net.minecraft.client.gui.screens;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ObjectSelectionList$Entry;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.core.Holder$Reference;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.biome.Biome;

@Environment(EnvType.CLIENT)
class CreateBuffetWorldScreen$BiomeList$Entry extends ObjectSelectionList$Entry<CreateBuffetWorldScreen$BiomeList$Entry> {
   private final Holder$Reference<Biome> biome;
   private final Component name;

   public CreateBuffetWorldScreen$BiomeList$Entry(final Holder$Reference<Biome> this$1, final Holder$Reference biome) {
      Objects.requireNonNull(this$1x);
      this.this$1 = this$1x;
      super();
      this.biome = biome;
      Identifier id = biome.key().identifier();
      String translationKey = id.toLanguageKey("biome");
      if (Language.getInstance().has(translationKey)) {
         this.name = Component.translatable(translationKey);
      } else {
         this.name = Component.literal(id.toString());
      }
   }

   @Override
   public Component getNarration() {
      return Component.translatable("narrator.select", this.name);
   }

   @Override
   public void extractContent(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final boolean hovered, final float a) {
      graphics.text(this.this$1.this$0.font, this.name, this.getContentX() + 5, this.getContentY() + 2, -1);
   }

   @Override
   public boolean mouseClicked(final MouseButtonEvent event, final boolean doubleClick) {
      this.this$1.setSelected(this);
      return super.mouseClicked(event, doubleClick);
   }
}
