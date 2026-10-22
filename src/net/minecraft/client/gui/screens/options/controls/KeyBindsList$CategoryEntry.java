package net.minecraft.client.gui.screens.options.controls;

import java.util.List;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping$Category;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.FocusableTextWidget;
import net.minecraft.client.gui.components.FocusableTextWidget$BackgroundFill;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;

@Environment(EnvType.CLIENT)
public class KeyBindsList$CategoryEntry extends KeyBindsList$Entry {
   private final FocusableTextWidget categoryName;

   public KeyBindsList$CategoryEntry(final KeyBindsList this$0, final KeyMapping$Category category) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
      this.categoryName = FocusableTextWidget.builder(
            category.label().copy().withStyle(ChatFormatting.UNDERLINE, ChatFormatting.BOLD), KeyBindsList.access$000(this$0).font
         )
         .alwaysShowBorder(false)
         .backgroundFill(FocusableTextWidget$BackgroundFill.ON_FOCUS)
         .build();
   }

   @Override
   public void extractContent(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final boolean hovered, final float a) {
      this.categoryName
         .setPosition(KeyBindsList.access$100(this.this$0) / 2 - this.categoryName.getWidth() / 2, this.getContentBottom() - this.categoryName.getHeight());
      this.categoryName.extractRenderState(graphics, mouseX, mouseY, a);
   }

   @Override
   public List<? extends GuiEventListener> children() {
      return List.of(this.categoryName);
   }

   @Override
   public List<? extends NarratableEntry> narratables() {
      return List.of(this.categoryName);
   }

   @Override
   public void refreshEntry() {
   }
}
