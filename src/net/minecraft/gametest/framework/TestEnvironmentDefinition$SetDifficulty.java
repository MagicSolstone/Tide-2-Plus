package net.minecraft.gametest.framework;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;

public record TestEnvironmentDefinition$SetDifficulty(Difficulty difficulty) implements TestEnvironmentDefinition<Difficulty> {
   public static final MapCodec<TestEnvironmentDefinition$SetDifficulty> CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(Difficulty.CODEC.fieldOf("difficulty").forGetter(TestEnvironmentDefinition$SetDifficulty::difficulty))
         .apply(i, TestEnvironmentDefinition$SetDifficulty::new)
   );

   public Difficulty setup(final ServerLevel level) {
      Difficulty oldDifficulty = level.getDifficulty();
      level.getServer().setDifficulty(this.difficulty, true);
      return oldDifficulty;
   }

   public void teardown(final ServerLevel level, final Difficulty saveData) {
      level.getServer().setDifficulty(saveData, true);
   }

   @Override
   public MapCodec<TestEnvironmentDefinition$SetDifficulty> codec() {
      return CODEC;
   }
}
