package net.minecraft.network.protocol.game;

import java.util.UUID;
import net.minecraft.network.chat.Component;
import net.minecraft.world.BossEvent$BossBarColor;
import net.minecraft.world.BossEvent$BossBarOverlay;

public interface ClientboundBossEventPacket$Handler {
   default void add(
      final UUID id,
      final Component name,
      final float progress,
      final BossEvent$BossBarColor color,
      final BossEvent$BossBarOverlay overlay,
      final boolean darkenScreen,
      final boolean playMusic,
      final boolean createWorldFog
   ) {
   }

   default void remove(final UUID id) {
   }

   default void updateProgress(final UUID id, final float progress) {
   }

   default void updateName(final UUID id, final Component name) {
   }

   default void updateStyle(final UUID id, final BossEvent$BossBarColor color, final BossEvent$BossBarOverlay overlay) {
   }

   default void updateProperties(final UUID id, final boolean darkenScreen, final boolean playMusic, final boolean createWorldFog) {
   }
}
