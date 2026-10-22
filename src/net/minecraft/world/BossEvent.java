package net.minecraft.world;

import java.util.UUID;
import net.minecraft.network.chat.Component;

public abstract class BossEvent {
   private final UUID id;
   protected Component name;
   protected float progress;
   protected BossEvent$BossBarColor color;
   protected BossEvent$BossBarOverlay overlay;
   protected boolean darkenScreen;
   protected boolean playBossMusic;
   protected boolean createWorldFog;

   public BossEvent(final UUID id, final Component name, final BossEvent$BossBarColor color, final BossEvent$BossBarOverlay overlay) {
      this.id = id;
      this.name = name;
      this.color = color;
      this.overlay = overlay;
      this.progress = 1.0F;
   }

   public UUID getId() {
      return this.id;
   }

   public Component getName() {
      return this.name;
   }

   public void setName(final Component name) {
      this.name = name;
   }

   public float getProgress() {
      return this.progress;
   }

   public void setProgress(final float progress) {
      this.progress = progress;
   }

   public BossEvent$BossBarColor getColor() {
      return this.color;
   }

   public void setColor(final BossEvent$BossBarColor color) {
      this.color = color;
   }

   public BossEvent$BossBarOverlay getOverlay() {
      return this.overlay;
   }

   public void setOverlay(final BossEvent$BossBarOverlay overlay) {
      this.overlay = overlay;
   }

   public boolean shouldDarkenScreen() {
      return this.darkenScreen;
   }

   public BossEvent setDarkenScreen(final boolean darkenScreen) {
      this.darkenScreen = darkenScreen;
      return this;
   }

   public boolean shouldPlayBossMusic() {
      return this.playBossMusic;
   }

   public BossEvent setPlayBossMusic(final boolean playBossMusic) {
      this.playBossMusic = playBossMusic;
      return this;
   }

   public BossEvent setCreateWorldFog(final boolean createWorldFog) {
      this.createWorldFog = createWorldFog;
      return this;
   }

   public boolean shouldCreateWorldFog() {
      return this.createWorldFog;
   }
}
