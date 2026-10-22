package net.minecraft.client.gui.screens.achievement;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ObjectSelectionList$Entry;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.EntityType;

@Environment(EnvType.CLIENT)
class StatsScreen$MobsStatisticsList$MobRow extends ObjectSelectionList$Entry<StatsScreen$MobsStatisticsList$MobRow> {
   private final Component mobName;
   private final Component kills;
   private final Component killedBy;
   private final boolean hasKills;
   private final boolean wasKilledBy;

   public StatsScreen$MobsStatisticsList$MobRow(final EntityType<?> this$1, final EntityType type) {
      Objects.requireNonNull(this$1x);
      this.this$1 = this$1x;
      super();
      this.mobName = type.getDescription();
      int kills = this$1x.this$0.stats.getValue(Stats.ENTITY_KILLED.get(type));
      if (kills == 0) {
         this.kills = Component.translatable("stat_type.minecraft.killed.none", this.mobName);
         this.hasKills = false;
      } else {
         this.kills = Component.translatable("stat_type.minecraft.killed", kills, this.mobName);
         this.hasKills = true;
      }

      int killedBy = this$1x.this$0.stats.getValue(Stats.ENTITY_KILLED_BY.get(type));
      if (killedBy == 0) {
         this.killedBy = Component.translatable("stat_type.minecraft.killed_by.none", this.mobName);
         this.wasKilledBy = false;
      } else {
         this.killedBy = Component.translatable("stat_type.minecraft.killed_by", this.mobName, killedBy);
         this.wasKilledBy = true;
      }
   }

   @Override
   public void extractContent(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final boolean hovered, final float a) {
      graphics.text(StatsScreen.access$1000(this.this$1.this$0), this.mobName, this.getContentX() + 2, this.getContentY() + 1, -1);
      graphics.text(
         StatsScreen.access$1100(this.this$1.this$0), this.kills, this.getContentX() + 2 + 10, this.getContentY() + 1 + 9, this.hasKills ? -4539718 : -8355712
      );
      graphics.text(
         StatsScreen.access$1300(this.this$1.this$0),
         this.killedBy,
         this.getContentX() + 2 + 10,
         this.getContentY() + 1 + 9 * 2,
         this.wasKilledBy ? -4539718 : -8355712
      );
   }

   @Override
   public Component getNarration() {
      return Component.translatable("narrator.select", CommonComponents.joinForNarration(this.kills, this.killedBy));
   }
}
