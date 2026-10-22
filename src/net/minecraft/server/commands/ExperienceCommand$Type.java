package net.minecraft.server.commands;

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.ToIntFunction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

enum ExperienceCommand$Type {
   POINTS("points", Player::giveExperiencePoints, (p, a) -> {
      if (a >= p.getXpNeededForNextLevel()) {
         return false;
      } else {
         p.setExperiencePoints(a);
         return true;
      }
   }, p -> Mth.floor(p.experienceProgress * p.getXpNeededForNextLevel())),
   LEVELS("levels", ServerPlayer::giveExperienceLevels, (p, a) -> {
      p.setExperienceLevels(a);
      return true;
   }, p -> p.experienceLevel);

   public final BiConsumer<ServerPlayer, Integer> add;
   public final BiPredicate<ServerPlayer, Integer> set;
   public final ToIntFunction<ServerPlayer> query;
   public final String queryTranslationKey;
   public final CommandResponseTracker$MessagesWithArg<ServerPlayer, Integer> addResponse;
   public final CommandResponseTracker$MessagesWithArg<ServerPlayer, Integer> setResponse;

   private ExperienceCommand$Type(
      final String name, final BiConsumer<ServerPlayer, Integer> add, final BiPredicate<ServerPlayer, Integer> set, final ToIntFunction<ServerPlayer> query
   ) {
      this.add = add;
      this.addResponse = CommandResponseTracker.messages(
         (player, var2, amount) -> Component.translatable("commands.experience.add." + name + ".success.single", amount, player.getDisplayName()),
         (playerCount, var2, amount) -> Component.translatable("commands.experience.add." + name + ".success.multiple", amount, playerCount)
      );
      this.set = set;
      this.setResponse = CommandResponseTracker.messages(
         ExperienceCommand.ERROR_SET_POINTS_INVALID,
         (player, var2, amount) -> Component.translatable("commands.experience.set." + name + ".success.single", amount, player.getDisplayName()),
         (playerCount, var2, amount) -> Component.translatable("commands.experience.set." + name + ".success.multiple", amount, playerCount)
      );
      this.query = query;
      this.queryTranslationKey = "commands.experience.query." + name;
   }

   public Component queryResponse(final ServerPlayer target, final int result) {
      return Component.translatable(this.queryTranslationKey, target.getDisplayName(), result);
   }
}
