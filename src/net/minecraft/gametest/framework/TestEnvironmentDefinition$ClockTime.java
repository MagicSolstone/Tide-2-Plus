package net.minecraft.gametest.framework;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.clock.WorldClock;

public record TestEnvironmentDefinition$ClockTime(Holder<WorldClock> clock, int time) implements TestEnvironmentDefinition<Long> {
   public static final MapCodec<TestEnvironmentDefinition$ClockTime> CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            WorldClock.CODEC.fieldOf("clock").forGetter(TestEnvironmentDefinition$ClockTime::clock),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("time").forGetter(TestEnvironmentDefinition$ClockTime::time)
         )
         .apply(i, TestEnvironmentDefinition$ClockTime::new)
   );

   public Long setup(final ServerLevel level) {
      MinecraftServer server = level.getServer();
      long previous = server.clockManager().getInstance(this.clock).totalTicks();
      server.clockManager().setTotalTicks(this.clock, this.time);
      return previous;
   }

   public void teardown(final ServerLevel level, final Long saveData) {
      MinecraftServer server = level.getServer();
      server.clockManager().setTotalTicks(this.clock, saveData);
   }

   @Override
   public MapCodec<TestEnvironmentDefinition$ClockTime> codec() {
      return CODEC;
   }
}
