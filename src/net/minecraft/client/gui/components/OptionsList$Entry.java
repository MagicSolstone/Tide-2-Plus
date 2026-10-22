package net.minecraft.client.gui.components;

import com.google.common.collect.Lists;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class OptionsList$Entry extends OptionsList$AbstractEntry {
   private final List<OptionsList$OptionInstanceWidget> children;
   private final Screen screen;
   private static final int X_OFFSET = 160;

   private OptionsList$Entry(final List<OptionsList$OptionInstanceWidget> widgets, final Screen screen) {
      this.children = widgets;
      this.screen = screen;
   }

   public static OptionsList$Entry big(final Options options, final OptionInstance<?> optionInstance, final Screen screen) {
      return new OptionsList$Entry(List.of(new OptionsList$OptionInstanceWidget(optionInstance.createButton(options, 0, 0, 310), optionInstance)), screen);
   }

   public static OptionsList$Entry big(final AbstractWidget widget, final Screen screen) {
      widget.setWidth(310);
      return new OptionsList$Entry(List.of(new OptionsList$OptionInstanceWidget(widget, null)), screen);
   }

   public static OptionsList$Entry small(final AbstractWidget leftWidget, @Nullable final AbstractWidget rightWidget, final Screen screen) {
      return rightWidget == null
         ? new OptionsList$Entry(List.of(new OptionsList$OptionInstanceWidget(leftWidget)), screen)
         : new OptionsList$Entry(List.of(new OptionsList$OptionInstanceWidget(leftWidget), new OptionsList$OptionInstanceWidget(rightWidget)), screen);
   }

   public static OptionsList$Entry small(
      final AbstractWidget leftWidget, final OptionInstance<?> leftWidgetOptionInstance, @Nullable final AbstractWidget rightWidget, final Screen screen
   ) {
      return rightWidget == null
         ? new OptionsList$Entry(List.of(new OptionsList$OptionInstanceWidget(leftWidget, leftWidgetOptionInstance)), screen)
         : new OptionsList$Entry(
            List.of(new OptionsList$OptionInstanceWidget(leftWidget, leftWidgetOptionInstance), new OptionsList$OptionInstanceWidget(rightWidget)), screen
         );
   }

   public static OptionsList$Entry small(
      final Options options, final OptionInstance<?> optionA, @Nullable final OptionInstance<?> optionB, final OptionsSubScreen screen
   ) {
      AbstractWidget buttonA = optionA.createButton(options);
      return optionB == null
         ? new OptionsList$Entry(List.of(new OptionsList$OptionInstanceWidget(buttonA, optionA)), screen)
         : new OptionsList$Entry(
            List.of(new OptionsList$OptionInstanceWidget(buttonA, optionA), new OptionsList$OptionInstanceWidget(optionB.createButton(options), optionB)),
            screen
         );
   }

   @Override
   public void extractContent(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final boolean hovered, final float a) {
      int xOffset = 0;
      int x = this.screen.width / 2 - 155;

      for (OptionsList$OptionInstanceWidget optionInstanceWidget : this.children) {
         optionInstanceWidget.widget().setPosition(x + xOffset, this.getContentY());
         optionInstanceWidget.widget().extractRenderState(graphics, mouseX, mouseY, a);
         xOffset += 160;
      }
   }

   @Override
   public List<? extends GuiEventListener> children() {
      return Lists.transform(this.children, OptionsList$OptionInstanceWidget::widget);
   }

   @Override
   public List<? extends NarratableEntry> narratables() {
      return Lists.transform(this.children, OptionsList$OptionInstanceWidget::widget);
   }

   @Nullable
   public AbstractWidget findOption(final OptionInstance<?> option) {
      for (OptionsList$OptionInstanceWidget child : this.children) {
         if (child.optionInstance == option) {
            return child.widget();
         }
      }

      return null;
   }
}
