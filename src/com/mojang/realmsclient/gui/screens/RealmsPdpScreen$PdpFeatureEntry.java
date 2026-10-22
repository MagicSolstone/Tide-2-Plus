package com.mojang.realmsclient.gui.screens;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
class RealmsPdpScreen$PdpFeatureEntry extends RealmsPdpScreen$AbstractPdpEntry {
   private final Identifier image;
   private final List<Component> descriptions;

   private RealmsPdpScreen$PdpFeatureEntry(final RealmsPdpScreen this$0, final Identifier image, final Component title, final Component... descriptions) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(title);
      this.image = image;
      this.descriptions = List.of(descriptions);
   }

   private int entryHeight() {
      return Math.max(45, this.textHeight()) + 16;
   }

   private int textHeight() {
      int height = RealmsPdpScreen.access$800(this.this$0).wordWrapHeight(this.title, 182) + 4;

      for (Component description : this.descriptions) {
         height += 2;
         height += RealmsPdpScreen.access$900(this.this$0).wordWrapHeight(description, 182);
      }

      return height;
   }

   @Override
   public void extractContent(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final boolean hovered, final float a) {
      int contentY = this.getContentY() + 4;
      graphics.blit(RenderPipelines.GUI_TEXTURED, this.image, this.getContentX() + 4, contentY, 0.0F, 0.0F, 80, 45, 80, 45);
      int textY = graphics.textWithWordWrap(RealmsPdpScreen.access$1000(this.this$0), this.title, this.titleX(), contentY, 182, -1) + 4;

      for (Component description : this.descriptions) {
         textY = graphics.textWithWordWrap(RealmsPdpScreen.access$1100(this.this$0), description, this.titleX(), textY, 182, -1) + 2;
      }
   }

   @Override
   public Component getNarration() {
      List<Component> lines = new ArrayList<>(this.descriptions.size() + 1);
      lines.add(this.title);
      lines.addAll(this.descriptions);
      return Component.translatable("narrator.select", CommonComponents.joinLines(lines));
   }

   private int titleX() {
      return this.getContentX() + 4 + 80 + 16;
   }
}
