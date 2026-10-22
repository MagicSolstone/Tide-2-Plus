package net.minecraft.server.commands;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.Dynamic3CommandExceptionType;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

enum AdvancementCommands$Action {
   GRANT("grant"),
   REVOKE("revoke");

   public final CommandResponseTracker$MessagesWithArg<ServerPlayer, AdvancementHolder> singleAdvancementSuccessResponse;
   public final CommandResponseTracker$DispatchWithArg<CommandSyntaxException, ServerPlayer, AdvancementHolder> singleAdvancementsError;
   public final CommandResponseTracker$MessagesWithArg<ServerPlayer, Integer> multipleAdvancementsSuccessResponse;
   public final CommandResponseTracker$DispatchWithArg<CommandSyntaxException, ServerPlayer, Integer> multipleAdvancementsError;
   public final CommandResponseTracker$MessagesWithArgs<ServerPlayer, AdvancementHolder, String> criterionSuccessResponse;
   public final CommandResponseTracker$DispatchWithArgs<CommandSyntaxException, ServerPlayer, AdvancementHolder, String> criterionError;

   private AdvancementCommands$Action(final String key) {
      this.singleAdvancementSuccessResponse = CommandResponseTracker.messages(
         (player, var2, advancement) -> Component.translatable(
            "commands.advancement." + key + ".one.to.one.success", Advancement.name(advancement), player.getDisplayName()
         ),
         (playerCount, var2, advancement) -> Component.translatable(
            "commands.advancement." + key + ".one.to.many.success", Advancement.name(advancement), playerCount
         )
      );
      Dynamic2CommandExceptionType singleAdvancementSinglePlayerError = new Dynamic2CommandExceptionType(
         (advancement, player) -> Component.translatableEscape("commands.advancement." + key + ".one.to.one.failure", advancement, player)
      );
      Dynamic2CommandExceptionType singleAdvancementMultiplePlayersError = new Dynamic2CommandExceptionType(
         (advancement, playerCount) -> Component.translatableEscape("commands.advancement." + key + ".one.to.many.failure", advancement, playerCount)
      );
      this.singleAdvancementsError = new CommandResponseTracker$DispatchWithArg<>(
         (player, var2, advancement) -> singleAdvancementSinglePlayerError.create(Advancement.name(advancement), player.getDisplayName()),
         (playerCount, var2, advancement) -> singleAdvancementMultiplePlayersError.create(Advancement.name(advancement), playerCount)
      );
      this.multipleAdvancementsSuccessResponse = CommandResponseTracker.messages(
         (player, var2, advancementCount) -> Component.translatable(
            "commands.advancement." + key + ".many.to.one.success", advancementCount, player.getDisplayName()
         ),
         (playerCount, var2, advancementCount) -> Component.translatable("commands.advancement." + key + ".many.to.many.success", advancementCount, playerCount)
      );
      Dynamic2CommandExceptionType multipleAdvancementSinglePlayerError = new Dynamic2CommandExceptionType(
         (advancementCount, player) -> Component.translatableEscape("commands.advancement." + key + ".many.to.one.failure", advancementCount, player)
      );
      Dynamic2CommandExceptionType multipleAdvancementMultiplePlayersError = new Dynamic2CommandExceptionType(
         (advancementCount, playerCount) -> Component.translatableEscape("commands.advancement." + key + ".many.to.many.failure", advancementCount, playerCount)
      );
      this.multipleAdvancementsError = new CommandResponseTracker$DispatchWithArg<>(
         (player, var2, advancementCount) -> multipleAdvancementSinglePlayerError.create(advancementCount, player.getDisplayName()),
         (playerCount, var2, advancementCount) -> multipleAdvancementMultiplePlayersError.create(advancementCount, playerCount)
      );
      this.criterionSuccessResponse = CommandResponseTracker.messages(
         (player, var2, advancement, criterion) -> Component.translatable(
            "commands.advancement." + key + ".criterion.to.one.success", criterion, Advancement.name(advancement), player.getDisplayName()
         ),
         (playerCount, var2, advancement, criterion) -> Component.translatable(
            "commands.advancement." + key + ".criterion.to.many.success", criterion, Advancement.name(advancement), playerCount
         )
      );
      Dynamic3CommandExceptionType criterionSinglePlayerError = new Dynamic3CommandExceptionType(
         (criterion, advancement, player) -> Component.translatableEscape(
            "commands.advancement." + key + ".criterion.to.one.failure", criterion, advancement, player
         )
      );
      Dynamic3CommandExceptionType criterionMultiplePlayersError = new Dynamic3CommandExceptionType(
         (criterion, advancement, playerCount) -> Component.translatableEscape(
            "commands.advancement." + key + ".criterion.to.many.failure", criterion, advancement, playerCount
         )
      );
      this.criterionError = new CommandResponseTracker$DispatchWithArgs<>(
         (player, var2, advancement, criterion) -> criterionSinglePlayerError.create(criterion, Advancement.name(advancement), player.getDisplayName()),
         (playerCount, var2, advancement, criterion) -> criterionMultiplePlayersError.create(criterion, Advancement.name(advancement), playerCount)
      );
   }

   public int perform(final ServerPlayer player, final Iterable<AdvancementHolder> advancements, final boolean showAdvancements) {
      int count = 0;
      if (!showAdvancements) {
         player.getAdvancements().flushDirty(player, true);
      }

      for (AdvancementHolder advancement : advancements) {
         if (this.perform(player, advancement)) {
            count++;
         }
      }

      if (!showAdvancements) {
         player.getAdvancements().flushDirty(player, false);
      }

      return count;
   }

   protected abstract boolean perform(ServerPlayer player, AdvancementHolder advancement);

   protected abstract boolean performCriterion(ServerPlayer player, AdvancementHolder advancement, String criterion);
}
