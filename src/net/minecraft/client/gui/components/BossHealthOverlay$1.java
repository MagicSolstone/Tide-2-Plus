package net.minecraft.client.gui.components;

import java.util.Objects;
import java.util.UUID;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBossEventPacket$Handler;
import net.minecraft.world.BossEvent$BossBarColor;
import net.minecraft.world.BossEvent$BossBarOverlay;

@Environment(EnvType.CLIENT)
class BossHealthOverlay$1 implements ClientboundBossEventPacket$Handler {
   BossHealthOverlay$1(final BossHealthOverlay this$0) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public void add(
      final UUID id,
      final Component name,
      final float progress,
      final BossEvent$BossBarColor color,
      final BossEvent$BossBarOverlay overlay,
      final boolean darkenScreen,
      final boolean playMusic,
      final boolean createWorldFog
   ) {
      this.this$0.events.put(id, new LerpingBossEvent(id, name, progress, color, overlay, darkenScreen, playMusic, createWorldFog));
   }

   @Override
   public void remove(final UUID id) {
      this.this$0.events.remove(id);
   }

   @Override
   public void updateProgress(final UUID id, final float progress) {
      this.this$0.events.get(id).setProgress(progress);
   }

   @Override
   public void updateName(final UUID id, final Component name) {
      this.this$0.events.get(id).setName(name);
   }

   @Override
   public void updateStyle(final UUID id, final BossEvent$BossBarColor color, final BossEvent$BossBarOverlay overlay) {
      LerpingBossEvent event = this.this$0.events.get(id);
      event.setColor(color);
      event.setOverlay(overlay);
   }

   @Override
   public void updateProperties(final UUID id, final boolean darkenScreen, final boolean playMusic, final boolean createWorldFog) {
      LerpingBossEvent event = this.this$0.events.get(id);
      event.setDarkenScreen(darkenScreen);
      event.setPlayBossMusic(playMusic);
      event.setCreateWorldFog(createWorldFog);
   }
}
