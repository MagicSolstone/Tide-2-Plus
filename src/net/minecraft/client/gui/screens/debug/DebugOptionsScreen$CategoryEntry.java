package net.minecraft.client.gui.screens.debug;

import java.util.List;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.FocusableTextWidget;
import net.minecraft.client.gui.components.FocusableTextWidget$BackgroundFill;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
class DebugOptionsScreen$CategoryEntry extends DebugOptionsScreen$AbstractOptionEntry {
   private final FocusableTextWidget widget;

   public DebugOptionsScreen$CategoryEntry(final DebugOptionsScreen this$0, final Component category) {
      Objects.requireNonNull(this$0);
      super();
      this.widget = FocusableTextWidget.builder(category, DebugOptionsScreen.access$000(this$0).font)
         .alwaysShowBorder(false)
         .backgroundFill(FocusableTextWidget$BackgroundFill.ON_FOCUS)
         .build();
   }

   @Override
   public void extractContent(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final boolean hovered, final float a) {
      this.widget
         .setPosition(this.getContentX() + this.getContentWidth() / 2 - this.widget.getWidth() / 2, this.getContentYMiddle() - this.widget.getHeight() / 2);
      this.widget.extractRenderState(graphics, mouseX, mouseY, a);
   }

   @Override
   public List<? extends GuiEventListener> children() {
      return List.of(this.widget);
   }

   @Override
   public List<? extends NarratableEntry> narratables() {
      return List.of(this.widget);
   }

   @Override
   public void refreshEntry() {
   }
}
