package net.minecraft.client.gui.screens.achievement;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ObjectSelectionList$Entry;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.stats.Stat;

@Environment(EnvType.CLIENT)
class StatsScreen$GeneralStatisticsList$Entry extends ObjectSelectionList$Entry<StatsScreen$GeneralStatisticsList$Entry> {
   private final Stat<Identifier> stat;
   private final Component statDisplay;

   private StatsScreen$GeneralStatisticsList$Entry(final Stat<Identifier> this$1, final Stat stat) {
      Objects.requireNonNull(this$1x);
      this.this$1 = this$1x;
      super();
      this.stat = stat;
      this.statDisplay = Component.translatable(StatsScreen.getTranslationKey(stat));
   }

   private String getValueText() {
      return this.stat.format(this.this$1.this$0.stats.getValue(this.stat));
   }

   @Override
   public void extractContent(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final boolean hovered, final float a) {
      int y = this.getContentYMiddle() - 9 / 2;
      int index = this.this$1.children().indexOf(this);
      int color = index % 2 == 0 ? -1 : -4539718;
      graphics.text(StatsScreen.access$100(this.this$1.this$0), this.statDisplay, this.getContentX() + 2, y, color);
      String msg = this.getValueText();
      graphics.text(
         StatsScreen.access$200(this.this$1.this$0), msg, this.getContentRight() - StatsScreen.access$300(this.this$1.this$0).width(msg) - 4, y, color
      );
   }

   @Override
   public Component getNarration() {
      return Component.translatable("narrator.select", Component.empty().append(this.statDisplay).append(CommonComponents.SPACE).append(this.getValueText()));
   }
}
