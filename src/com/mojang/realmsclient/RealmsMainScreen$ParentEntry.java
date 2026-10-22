package com.mojang.realmsclient;

import com.mojang.realmsclient.dto.RealmsServer;
import com.mojang.realmsclient.util.RealmsUtil;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.WidgetTooltipHolder;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
class RealmsMainScreen$ParentEntry extends RealmsMainScreen$Entry {
   private final RealmsServer server;
   private final WidgetTooltipHolder tooltip;

   public RealmsMainScreen$ParentEntry(final RealmsMainScreen this$0, final RealmsServer server) {
      Objects.requireNonNull(this$0);
      super(this$0);
      this.tooltip = new WidgetTooltipHolder();
      this.server = server;
      if (!server.expired) {
         this.tooltip.set(Tooltip.create(Component.translatable("mco.snapshot.parent.tooltip")));
      }
   }

   @Override
   public void extractContent(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final boolean hovered, final float a) {
      this.extractStatusLights(this.server, graphics, this.getContentRight(), this.getContentY(), mouseX, mouseY);
      RealmsUtil.extractPlayerFace(graphics, this.getContentX(), this.getContentY(), 32, this.server.ownerUUID);
      this.extractFirstLine(graphics, this.getContentY(), this.getContentX(), this.getContentWidth(), -8355712, this.server);
      this.extractSecondLine(graphics, this.getContentY(), this.getContentX(), this.getContentWidth(), this.server);
      this.extractThirdLine(graphics, this.getContentY(), this.getContentX(), this.server);
      this.tooltip
         .refreshTooltipForNextRenderPass(
            graphics,
            mouseX,
            mouseY,
            hovered,
            this.isFocused(),
            new ScreenRectangle(this.getContentX(), this.getContentY(), this.getContentWidth(), this.getContentHeight())
         );
   }

   @Override
   public Component getNarration() {
      return Component.literal(Objects.requireNonNullElse(this.server.name, "unknown server"));
   }
}
