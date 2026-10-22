package net.minecraft.client.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

@Environment(EnvType.CLIENT)
enum Hud$HeartType {
   CONTAINER(
      Identifier.withDefaultNamespace("hud/heart/container"),
      Identifier.withDefaultNamespace("hud/heart/container_blinking"),
      Identifier.withDefaultNamespace("hud/heart/container"),
      Identifier.withDefaultNamespace("hud/heart/container_blinking"),
      Identifier.withDefaultNamespace("hud/heart/container_hardcore"),
      Identifier.withDefaultNamespace("hud/heart/container_hardcore_blinking"),
      Identifier.withDefaultNamespace("hud/heart/container_hardcore"),
      Identifier.withDefaultNamespace("hud/heart/container_hardcore_blinking")
   ),
   NORMAL(
      Identifier.withDefaultNamespace("hud/heart/full"),
      Identifier.withDefaultNamespace("hud/heart/full_blinking"),
      Identifier.withDefaultNamespace("hud/heart/half"),
      Identifier.withDefaultNamespace("hud/heart/half_blinking"),
      Identifier.withDefaultNamespace("hud/heart/hardcore_full"),
      Identifier.withDefaultNamespace("hud/heart/hardcore_full_blinking"),
      Identifier.withDefaultNamespace("hud/heart/hardcore_half"),
      Identifier.withDefaultNamespace("hud/heart/hardcore_half_blinking")
   ),
   POISIONED(
      Identifier.withDefaultNamespace("hud/heart/poisoned_full"),
      Identifier.withDefaultNamespace("hud/heart/poisoned_full_blinking"),
      Identifier.withDefaultNamespace("hud/heart/poisoned_half"),
      Identifier.withDefaultNamespace("hud/heart/poisoned_half_blinking"),
      Identifier.withDefaultNamespace("hud/heart/poisoned_hardcore_full"),
      Identifier.withDefaultNamespace("hud/heart/poisoned_hardcore_full_blinking"),
      Identifier.withDefaultNamespace("hud/heart/poisoned_hardcore_half"),
      Identifier.withDefaultNamespace("hud/heart/poisoned_hardcore_half_blinking")
   ),
   WITHERED(
      Identifier.withDefaultNamespace("hud/heart/withered_full"),
      Identifier.withDefaultNamespace("hud/heart/withered_full_blinking"),
      Identifier.withDefaultNamespace("hud/heart/withered_half"),
      Identifier.withDefaultNamespace("hud/heart/withered_half_blinking"),
      Identifier.withDefaultNamespace("hud/heart/withered_hardcore_full"),
      Identifier.withDefaultNamespace("hud/heart/withered_hardcore_full_blinking"),
      Identifier.withDefaultNamespace("hud/heart/withered_hardcore_half"),
      Identifier.withDefaultNamespace("hud/heart/withered_hardcore_half_blinking")
   ),
   ABSORBING(
      Identifier.withDefaultNamespace("hud/heart/absorbing_full"),
      Identifier.withDefaultNamespace("hud/heart/absorbing_full_blinking"),
      Identifier.withDefaultNamespace("hud/heart/absorbing_half"),
      Identifier.withDefaultNamespace("hud/heart/absorbing_half_blinking"),
      Identifier.withDefaultNamespace("hud/heart/absorbing_hardcore_full"),
      Identifier.withDefaultNamespace("hud/heart/absorbing_hardcore_full_blinking"),
      Identifier.withDefaultNamespace("hud/heart/absorbing_hardcore_half"),
      Identifier.withDefaultNamespace("hud/heart/absorbing_hardcore_half_blinking")
   ),
   FROZEN(
      Identifier.withDefaultNamespace("hud/heart/frozen_full"),
      Identifier.withDefaultNamespace("hud/heart/frozen_full_blinking"),
      Identifier.withDefaultNamespace("hud/heart/frozen_half"),
      Identifier.withDefaultNamespace("hud/heart/frozen_half_blinking"),
      Identifier.withDefaultNamespace("hud/heart/frozen_hardcore_full"),
      Identifier.withDefaultNamespace("hud/heart/frozen_hardcore_full_blinking"),
      Identifier.withDefaultNamespace("hud/heart/frozen_hardcore_half"),
      Identifier.withDefaultNamespace("hud/heart/frozen_hardcore_half_blinking")
   );

   private final Identifier full;
   private final Identifier fullBlinking;
   private final Identifier half;
   private final Identifier halfBlinking;
   private final Identifier hardcoreFull;
   private final Identifier hardcoreFullBlinking;
   private final Identifier hardcoreHalf;
   private final Identifier hardcoreHalfBlinking;

   private Hud$HeartType(
      final Identifier full,
      final Identifier fullBlinking,
      final Identifier half,
      final Identifier halfBlinking,
      final Identifier hardcoreFull,
      final Identifier hardcoreFullBlinking,
      final Identifier hardcoreHalf,
      final Identifier hardcoreHalfBlinking
   ) {
      this.full = full;
      this.fullBlinking = fullBlinking;
      this.half = half;
      this.halfBlinking = halfBlinking;
      this.hardcoreFull = hardcoreFull;
      this.hardcoreFullBlinking = hardcoreFullBlinking;
      this.hardcoreHalf = hardcoreHalf;
      this.hardcoreHalfBlinking = hardcoreHalfBlinking;
   }

   public Identifier getSprite(final boolean isHardcore, final boolean isHalf, final boolean isBlink) {
      if (!isHardcore) {
         if (isHalf) {
            return isBlink ? this.halfBlinking : this.half;
         } else {
            return isBlink ? this.fullBlinking : this.full;
         }
      } else if (isHalf) {
         return isBlink ? this.hardcoreHalfBlinking : this.hardcoreHalf;
      } else {
         return isBlink ? this.hardcoreFullBlinking : this.hardcoreFull;
      }
   }

   private static Hud$HeartType forPlayer(final Player player) {
      Hud$HeartType type;
      if (player.hasEffect(MobEffects.POISON)) {
         type = POISIONED;
      } else if (player.hasEffect(MobEffects.WITHER)) {
         type = WITHERED;
      } else if (player.isFullyFrozen()) {
         type = FROZEN;
      } else {
         type = NORMAL;
      }

      return type;
   }
}
