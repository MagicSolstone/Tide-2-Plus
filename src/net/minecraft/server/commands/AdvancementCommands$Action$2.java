package net.minecraft.server.commands;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.server.level.ServerPlayer;

enum AdvancementCommands$Action$2 {
   private AdvancementCommands$Action$2(final String key) {
   }

   @Override
   protected boolean perform(final ServerPlayer player, final AdvancementHolder advancement) {
      AdvancementProgress progress = player.getAdvancements().getOrStartProgress(advancement);
      if (!progress.hasProgress()) {
         return false;
      } else {
         for (String criterion : progress.getCompletedCriteria()) {
            player.getAdvancements().revoke(advancement, criterion);
         }

         return true;
      }
   }

   @Override
   protected boolean performCriterion(final ServerPlayer player, final AdvancementHolder advancement, final String criterion) {
      return player.getAdvancements().revoke(advancement, criterion);
   }
}
