package net.minecraft.client.gui.screens.telemetry;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.MultiLineTextWidget;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.layouts.SpacerElement;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

@Environment(EnvType.CLIENT)
class TelemetryEventWidget$ContentBuilder {
   private final int width;
   private final LinearLayout layout;
   private final MutableComponent narration = Component.empty();

   public TelemetryEventWidget$ContentBuilder(final int width) {
      this.width = width;
      this.layout = LinearLayout.vertical();
      this.layout.defaultCellSetting().alignHorizontallyLeft();
      this.layout.addChild(SpacerElement.width(width));
   }

   public void addLine(final Font font, final Component line) {
      this.addLine(font, line, 0);
   }

   public void addLine(final Font font, final Component line, final int paddingBottom) {
      this.layout.addChild(new MultiLineTextWidget(line, font).setMaxWidth(this.width), s -> s.paddingBottom(paddingBottom));
      this.narration.append(line).append("\n");
   }

   public void addHeader(final Font font, final Component line) {
      this.layout
         .addChild(new MultiLineTextWidget(line, font).setMaxWidth(this.width - 64).setCentered(true), s -> s.alignHorizontallyCenter().paddingHorizontal(32));
      this.narration.append(line).append("\n");
   }

   public void addSpacer(final int height) {
      this.layout.addChild(SpacerElement.height(height));
   }

   public TelemetryEventWidget$Content build() {
      this.layout.arrangeElements();
      return new TelemetryEventWidget$Content(this.layout, this.narration);
   }
}
