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
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
class RealmsPdpScreen$PdpHeaderEntry extends RealmsPdpScreen$AbstractPdpEntry {
   private static final int DESCRIPTION_ICON_SIZE = 8;
   @Nullable
   private final RealmsPdpScreen$PdpHeaderEntry$Subtitle subtitle;

   public RealmsPdpScreen$PdpHeaderEntry(final RealmsPdpScreen this$0, @Nullable final Component title, final RealmsPdpScreen$PdpHeaderEntry$Subtitle subtitle) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(title);
      this.subtitle = subtitle;
   }

   @Override
   public void extractContent(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final boolean hovered, final float a) {
      int textY = graphics.textWithWordWrap(RealmsPdpScreen.access$000(this.this$0), this.title, this.titleX(), this.getContentY() + 4, 182, -1) + 4;
      if (this.subtitle != null) {
         int iconX = this.getContentXMiddle() - Math.min(182, RealmsPdpScreen.access$100(this.this$0).width(this.subtitle.subTitle)) / 2 - 12;
         int iconY = textY + (9 - 8) / 2;
         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.subtitle.icon, iconX, iconY, 8, 8);
         int subtitleX = this.getContentXMiddle() - Math.min(182, RealmsPdpScreen.access$300(this.this$0).width(this.subtitle.subTitle)) / 2;
         graphics.textWithWordWrap(RealmsPdpScreen.access$400(this.this$0), this.subtitle.subTitle, subtitleX, textY, 182, -1);
      }
   }

   private int entryHeight() {
      return this.textHeight() + 16;
   }

   private int textHeight() {
      int height = RealmsPdpScreen.access$500(this.this$0).wordWrapHeight(this.title, 182) + 4;
      if (this.subtitle != null) {
         height += 2;
         height += RealmsPdpScreen.access$600(this.this$0).wordWrapHeight(this.subtitle.subTitle, 182);
      }

      return height;
   }

   @Override
   public Component getNarration() {
      List<Component> lines = new ArrayList<>();
      lines.add(this.title);
      if (this.subtitle != null) {
         lines.add(this.subtitle.subTitle);
      }

      return Component.translatable("narrator.select", CommonComponents.joinLines(lines));
   }

   private int titleX() {
      return this.getContentXMiddle() - Math.min(182, RealmsPdpScreen.access$700(this.this$0).width(this.title)) / 2;
   }
}
