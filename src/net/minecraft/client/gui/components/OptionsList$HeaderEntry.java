package net.minecraft.client.gui.components;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public class OptionsList$HeaderEntry extends OptionsList$AbstractEntry {
   private final Screen screen;
   private final int paddingTop;
   private final FocusableTextWidget widget;

   protected OptionsList$HeaderEntry(final Screen screen, final Component text, final int paddingTop) {
      this.screen = screen;
      this.paddingTop = paddingTop;
      this.widget = FocusableTextWidget.builder(text, screen.getFont())
         .alwaysShowBorder(false)
         .backgroundFill(FocusableTextWidget$BackgroundFill.ON_FOCUS)
         .build();
   }

   @Override
   public List<? extends NarratableEntry> narratables() {
      return List.of(this.widget);
   }

   @Override
   public void extractContent(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final boolean hovered, final float a) {
      this.widget.setPosition(this.screen.width / 2 - this.widget.getWidth() / 2, this.getContentY() + this.paddingTop);
      this.widget.extractRenderState(graphics, mouseX, mouseY, a);
   }

   @Override
   public List<? extends GuiEventListener> children() {
      return List.of(this.widget);
   }
}
