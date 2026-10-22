package net.minecraft.world.scores;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Map;

public record ScoreboardSaveData$Packed(
   List<Objective$Packed> objectives, List<Scoreboard$PackedScore> scores, Map<DisplaySlot, String> displaySlots, List<PlayerTeam$Packed> teams
) {
   public static final ScoreboardSaveData$Packed EMPTY = new ScoreboardSaveData$Packed(List.of(), List.of(), Map.of(), List.of());
   public static final Codec<ScoreboardSaveData$Packed> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            Objective$Packed.CODEC.listOf().optionalFieldOf("Objectives", List.of()).forGetter(ScoreboardSaveData$Packed::objectives),
            Scoreboard$PackedScore.CODEC.listOf().optionalFieldOf("PlayerScores", List.of()).forGetter(ScoreboardSaveData$Packed::scores),
            Codec.unboundedMap(DisplaySlot.CODEC, Codec.STRING).optionalFieldOf("DisplaySlots", Map.of()).forGetter(ScoreboardSaveData$Packed::displaySlots),
            PlayerTeam$Packed.CODEC.listOf().optionalFieldOf("Teams", List.of()).forGetter(ScoreboardSaveData$Packed::teams)
         )
         .apply(i, ScoreboardSaveData$Packed::new)
   );
}
