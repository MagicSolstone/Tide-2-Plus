package net.minecraft.client.gui.components;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.OptionInstance$OptionInstanceSliderButton;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class OptionsList extends ContainerObjectSelectionList<OptionsList$AbstractEntry> {
   private static final int BIG_BUTTON_WIDTH = 310;
   private static final int DEFAULT_ITEM_HEIGHT = 25;
   private final OptionsSubScreen screen;

   public OptionsList(final Minecraft minecraft, final int width, final OptionsSubScreen screen) {
      super(minecraft, width, screen.layout.getContentHeight(), screen.layout.getHeaderHeight(), 25);
      this.centerListVertically = false;
      this.screen = screen;
   }

   public void addBig(final OptionInstance<?> option) {
      this.addEntry(OptionsList$Entry.big(this.minecraft.options, option, this.screen));
   }

   public void addBig(final AbstractWidget widget) {
      this.addEntry(OptionsList$Entry.big(widget, this.screen));
   }

   public void addSmall(final OptionInstance<?>... options) {
      for (int i = 0; i < options.length; i += 2) {
         OptionInstance<?> secondOption = i < options.length - 1 ? options[i + 1] : null;
         this.addEntry(OptionsList$Entry.small(this.minecraft.options, options[i], secondOption, this.screen));
      }
   }

   public void addSmall(final List<AbstractWidget> widgets) {
      for (int i = 0; i < widgets.size(); i += 2) {
         this.addSmall(widgets.get(i), i < widgets.size() - 1 ? widgets.get(i + 1) : null);
      }
   }

   public void addSmall(final AbstractWidget firstOption, @Nullable final AbstractWidget secondOption) {
      this.addEntry(OptionsList$Entry.small(firstOption, secondOption, this.screen));
   }

   public void addSmall(final AbstractWidget firstOption, final OptionInstance<?> firstOptionInstance, @Nullable final AbstractWidget secondOption) {
      this.addEntry(OptionsList$Entry.small(firstOption, firstOptionInstance, secondOption, this.screen));
   }

   public void addHeader(final Component text) {
      int lineHeight = 9;
      int paddingTop = this.children().isEmpty() ? 0 : lineHeight * 2;
      this.addEntry(new OptionsList$HeaderEntry(this.screen, text, paddingTop), paddingTop + lineHeight * 2);
   }

   @Override
   public int getRowWidth() {
      return 310;
   }

   @Nullable
   public AbstractWidget findOption(final OptionInstance<?> option) {
      for (OptionsList$AbstractEntry child : this.children()) {
         if (child instanceof OptionsList$Entry entry) {
            AbstractWidget widgetForOption = entry.findOption(option);
            if (widgetForOption != null) {
               return widgetForOption;
            }
         }
      }

      return null;
   }

   public void applyUnsavedChanges() {
      for (OptionsList$AbstractEntry child : this.children()) {
         if (child instanceof OptionsList$Entry entry) {
            for (OptionsList$OptionInstanceWidget optionInstanceWidget : entry.children) {
               if (optionInstanceWidget.optionInstance() != null
                  && optionInstanceWidget.widget() instanceof OptionInstance$OptionInstanceSliderButton<?> optionSlider) {
                  optionSlider.applyUnsavedValue();
               }
            }
         }
      }
   }

   public void resetOption(final OptionInstance<?> option) {
      for (OptionsList$AbstractEntry child : this.children()) {
         if (child instanceof OptionsList$Entry entry) {
            for (OptionsList$OptionInstanceWidget optionInstanceWidget : entry.children) {
               if (optionInstanceWidget.optionInstance() == option && optionInstanceWidget.widget() instanceof ResettableOptionWidget resettableOptionWidget) {
                  resettableOptionWidget.resetValue();
                  return;
               }
            }
         }
      }
   }
}
