package net.minecraft.client.gui.screens.options;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ObjectSelectionList$Entry;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.resources.language.LanguageInfo;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public class LanguageSelectScreen$LanguageSelectionList$Entry extends ObjectSelectionList$Entry<LanguageSelectScreen$LanguageSelectionList$Entry> {
   private final String code;
   private final Component language;

   public LanguageSelectScreen$LanguageSelectionList$Entry(
      final LanguageSelectScreen$LanguageSelectionList this$1, final String code, final LanguageInfo language
   ) {
      Objects.requireNonNull(this$1);
      this.this$1 = this$1;
      super();
      this.code = code;
      this.language = language.toComponent();
   }

   @Override
   public void extractContent(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final boolean hovered, final float a) {
      graphics.centeredText(
         LanguageSelectScreen.access$000(this.this$1.this$0),
         this.language,
         LanguageSelectScreen$LanguageSelectionList.access$100(this.this$1) / 2,
         this.getContentYMiddle() - 9 / 2,
         -1
      );
   }

   @Override
   public boolean keyPressed(final KeyEvent event) {
      if (event.isSelection()) {
         this.select();
         this.this$1.this$0.onDone();
         return true;
      } else {
         return super.keyPressed(event);
      }
   }

   @Override
   public boolean mouseClicked(final MouseButtonEvent event, final boolean doubleClick) {
      this.select();
      if (doubleClick) {
         this.this$1.this$0.onDone();
      }

      return super.mouseClicked(event, doubleClick);
   }

   private void select() {
      this.this$1.setSelected(this);
   }

   @Override
   public Component getNarration() {
      return Component.translatable("narrator.select", this.language);
   }
}
