package net.minecraft.client.gui.screens.worldselection;

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
public class AbstractGameRulesScreen$CategoryRuleEntry extends AbstractGameRulesScreen$RuleEntry {
   private final FocusableTextWidget widget;

   public AbstractGameRulesScreen$CategoryRuleEntry(final AbstractGameRulesScreen this$0, final Component label) {
      Objects.requireNonNull(this$0);
      super(null);
      this.widget = FocusableTextWidget.builder(label, AbstractGameRulesScreen.access$000(this$0).font)
         .alwaysShowBorder(false)
         .backgroundFill(FocusableTextWidget$BackgroundFill.ON_FOCUS)
         .build();
   }

   @Override
   public void extractContent(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final boolean hovered, final float a) {
      this.widget.setPosition(this.getContentXMiddle() - this.widget.getWidth() / 2, this.getContentYMiddle() - this.widget.getHeight() / 2);
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
}
