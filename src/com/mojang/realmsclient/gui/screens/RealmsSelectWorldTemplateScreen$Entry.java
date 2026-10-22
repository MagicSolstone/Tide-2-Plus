package com.mojang.realmsclient.gui.screens;

import com.mojang.realmsclient.dto.WorldTemplate;
import com.mojang.realmsclient.util.RealmsTextureManager;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.ObjectSelectionList$Entry;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
class RealmsSelectWorldTemplateScreen$Entry extends ObjectSelectionList$Entry<RealmsSelectWorldTemplateScreen$Entry> {
   private static final WidgetSprites WEBSITE_LINK_SPRITES = new WidgetSprites(
      Identifier.withDefaultNamespace("icon/link"), Identifier.withDefaultNamespace("icon/link_highlighted")
   );
   private static final WidgetSprites TRAILER_LINK_SPRITES = new WidgetSprites(
      Identifier.withDefaultNamespace("icon/video_link"), Identifier.withDefaultNamespace("icon/video_link_highlighted")
   );
   private static final Component PUBLISHER_LINK_TOOLTIP = Component.translatable("mco.template.info.tooltip");
   private static final Component TRAILER_LINK_TOOLTIP = Component.translatable("mco.template.trailer.tooltip");
   public final WorldTemplate template;
   @Nullable
   private ImageButton websiteButton;
   @Nullable
   private ImageButton trailerButton;

   public RealmsSelectWorldTemplateScreen$Entry(final RealmsSelectWorldTemplateScreen this$0, final WorldTemplate template) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
      this.template = template;
      if (template.link() != null) {
         this.websiteButton = new ImageButton(15, 15, WEBSITE_LINK_SPRITES, ConfirmLinkScreen.confirmLink(this$0x, template.link()), PUBLISHER_LINK_TOOLTIP);
         this.websiteButton.setTooltip(Tooltip.create(PUBLISHER_LINK_TOOLTIP));
      }

      if (template.trailer() != null) {
         this.trailerButton = new ImageButton(15, 15, TRAILER_LINK_SPRITES, ConfirmLinkScreen.confirmLink(this$0x, template.trailer()), TRAILER_LINK_TOOLTIP);
         this.trailerButton.setTooltip(Tooltip.create(TRAILER_LINK_TOOLTIP));
      }
   }

   @Override
   public boolean mouseClicked(final MouseButtonEvent event, final boolean doubleClick) {
      this.this$0.selectedTemplate = this.template;
      this.this$0.updateButtonStates();
      if (doubleClick && this.isFocused()) {
         this.this$0.callback.accept(this.template);
      }

      if (this.websiteButton != null) {
         this.websiteButton.mouseClicked(event, doubleClick);
      }

      if (this.trailerButton != null) {
         this.trailerButton.mouseClicked(event, doubleClick);
      }

      return super.mouseClicked(event, doubleClick);
   }

   @Override
   public void extractContent(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final boolean hovered, final float a) {
      graphics.blit(
         RenderPipelines.GUI_TEXTURED,
         RealmsTextureManager.worldTemplate(this.template.id(), this.template.image()),
         this.getContentX() + 1,
         this.getContentY() + 1 + 1,
         0.0F,
         0.0F,
         38,
         38,
         38,
         38
      );
      graphics.blitSprite(RenderPipelines.GUI_TEXTURED, RealmsSelectWorldTemplateScreen.SLOT_FRAME_SPRITE, this.getContentX(), this.getContentY() + 1, 40, 40);
      int padding = 5;
      int versionTextWidth = RealmsSelectWorldTemplateScreen.access$000(this.this$0).width(this.template.version());
      if (this.websiteButton != null) {
         this.websiteButton.setPosition(this.getContentRight() - versionTextWidth - this.websiteButton.getWidth() - 10, this.getContentY());
         this.websiteButton.extractRenderState(graphics, mouseX, mouseY, a);
      }

      if (this.trailerButton != null) {
         this.trailerButton.setPosition(this.getContentRight() - versionTextWidth - this.trailerButton.getWidth() * 2 - 15, this.getContentY());
         this.trailerButton.extractRenderState(graphics, mouseX, mouseY, a);
      }

      int textX = this.getContentX() + 45 + 20;
      int textY = this.getContentY() + 5;
      graphics.text(RealmsSelectWorldTemplateScreen.access$100(this.this$0), this.template.name(), textX, textY, -1);
      graphics.text(
         RealmsSelectWorldTemplateScreen.access$200(this.this$0), this.template.version(), this.getContentRight() - versionTextWidth - 5, textY, -6250336
      );
      graphics.text(RealmsSelectWorldTemplateScreen.access$300(this.this$0), this.template.author(), textX, textY + 9 + 5, -6250336);
      if (!this.template.recommendedPlayers().isBlank()) {
         graphics.text(
            RealmsSelectWorldTemplateScreen.access$500(this.this$0), this.template.recommendedPlayers(), textX, this.getContentBottom() - 9 / 2 - 5, -8355712
         );
      }
   }

   @Override
   public Component getNarration() {
      Component entryName = CommonComponents.joinLines(
         Component.literal(this.template.name()),
         Component.translatable("mco.template.select.narrate.authors", this.template.author()),
         Component.literal(this.template.recommendedPlayers()),
         Component.translatable("mco.template.select.narrate.version", this.template.version())
      );
      return Component.translatable("narrator.select", entryName);
   }
}
