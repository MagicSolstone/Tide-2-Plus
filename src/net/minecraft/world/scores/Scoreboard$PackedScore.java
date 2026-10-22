package net.minecraft.world.scores;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record Scoreboard$PackedScore(String owner, String objective, Score$Packed score) {
   public static final Codec<Scoreboard$PackedScore> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            Codec.STRING.fieldOf("Name").forGetter(Scoreboard$PackedScore::owner),
            Codec.STRING.fieldOf("Objective").forGetter(Scoreboard$PackedScore::objective),
            Score$Packed.MAP_CODEC.forGetter(Scoreboard$PackedScore::score)
         )
         .apply(i, Scoreboard$PackedScore::new)
   );
}
