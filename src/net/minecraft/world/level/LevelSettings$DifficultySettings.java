package net.minecraft.world.level;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.Difficulty;

public record LevelSettings$DifficultySettings(Difficulty difficulty, boolean hardcore, boolean locked) {
   public static final LevelSettings$DifficultySettings DEFAULT = new LevelSettings$DifficultySettings(Difficulty.NORMAL, false, false);
   public static final Codec<LevelSettings$DifficultySettings> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            Difficulty.CODEC.fieldOf("difficulty").forGetter(LevelSettings$DifficultySettings::difficulty),
            Codec.BOOL.fieldOf("hardcore").forGetter(LevelSettings$DifficultySettings::hardcore),
            Codec.BOOL.fieldOf("locked").forGetter(LevelSettings$DifficultySettings::locked)
         )
         .apply(i, LevelSettings$DifficultySettings::new)
   );
}
