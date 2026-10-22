package net.minecraft.world.clock;

import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import java.util.Map;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Mth;
import net.minecraft.world.level.gamerules.GameRules;

public class ServerClockManager$ServerClockInstance implements ClockInstance {
   private final Map<ResourceKey<ClockTimeMarker>, ClockTimeMarker> timeMarkers = new Reference2ObjectOpenHashMap();
   private long totalTicks;
   private float partialTick;
   private float rate = 1.0F;
   private boolean paused;

   public void loadFrom(final ClockState state) {
      this.totalTicks = state.totalTicks();
      this.partialTick = state.partialTick();
      this.rate = state.rate();
      this.paused = state.paused();
   }

   public void tick() {
      if (!this.paused) {
         this.partialTick = this.partialTick + this.rate;
         int fullTicks = Mth.floor(this.partialTick);
         this.partialTick -= fullTicks;
         this.totalTicks += fullTicks;
      }
   }

   @Override
   public long totalTicks() {
      return this.totalTicks;
   }

   @Override
   public float partialTick() {
      return this.partialTick;
   }

   @Override
   public float rate() {
      return this.rate;
   }

   @Override
   public boolean isPaused() {
      return this.paused;
   }

   public ClockState packState() {
      return new ClockState(this.totalTicks, this.partialTick, this.rate, this.paused);
   }

   public ClockNetworkState packNetworkState(final MinecraftServer server) {
      boolean advanceTime = server.getGlobalGameRules().get(GameRules.ADVANCE_TIME);
      boolean paused = this.paused || !advanceTime;
      return new ClockNetworkState(this.totalTicks, this.partialTick, paused ? 0.0F : this.rate);
   }
}
