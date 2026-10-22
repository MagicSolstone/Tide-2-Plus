package net.minecraft.server.commands;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.server.level.ServerPlayer;

enum AdvancementCommands$Action$1 {
   private AdvancementCommands$Action$1(final String key) {
   }

   @Override
   protected boolean perform(final ServerPlayer player, final AdvancementHolder advancement) {
      AdvancementProgress progress = player.getAdvancements().getOrStartProgress(advancement);
      if (progress.isDone()) {
         return false;
      } else {
         for (String criterion : progress.getRemainingCriteria()) {
            player.getAdvancements().award(advancement, criterion);
         }

         return true;
      }
   }

   @Override
   protected boolean performCriterion(final ServerPlayer player, final AdvancementHolder advancement, final String criterion) {
      return player.getAdvancements().award(advancement, criterion);
   }
}
