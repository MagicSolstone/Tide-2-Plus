package net.minecraft.client.gui.screens.worldselection;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public abstract class AbstractGameRulesScreen$GameRuleEntry extends AbstractGameRulesScreen$RuleEntry {
   private final List<FormattedCharSequence> label;
   protected final List<AbstractWidget> children;

   public AbstractGameRulesScreen$GameRuleEntry(@Nullable final List<FormattedCharSequence> this$0, final Component tooltip, final Component label) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super(tooltip);
      this.children = Lists.newArrayList();
      this.label = AbstractGameRulesScreen.access$100(this$0).font.split(label, 170);
   }

   @Override
   public List<? extends GuiEventListener> children() {
      return this.children;
   }

   @Override
   public List<? extends NarratableEntry> narratables() {
      return this.children;
   }

   protected void extractLabel(final GuiGraphicsExtractor graphics, final int rowTop, final int rowLeft) {
      if (this.label.size() == 1) {
         graphics.text(AbstractGameRulesScreen.access$200(this.this$0).font, this.label.get(0), rowLeft, rowTop + 5, -1);
      } else if (this.label.size() >= 2) {
         graphics.text(AbstractGameRulesScreen.access$300(this.this$0).font, this.label.get(0), rowLeft, rowTop, -1);
         graphics.text(AbstractGameRulesScreen.access$400(this.this$0).font, this.label.get(1), rowLeft, rowTop + 10, -1);
      }
   }
}
