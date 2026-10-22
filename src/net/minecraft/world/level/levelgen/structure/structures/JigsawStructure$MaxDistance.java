package net.minecraft.world.level.levelgen.structure.structures;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.function.Function;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.dimension.DimensionType;

public record JigsawStructure$MaxDistance(int horizontal, int vertical) {
   private static final Codec<Integer> HORIZONTAL_VALUE_CODEC = Codec.intRange(1, 128);
   private static final Codec<JigsawStructure$MaxDistance> FULL_CODEC = RecordCodecBuilder.create(
      i -> i.group(
            HORIZONTAL_VALUE_CODEC.fieldOf("horizontal").forGetter(JigsawStructure$MaxDistance::horizontal),
            ExtraCodecs.intRange(1, DimensionType.Y_SIZE).optionalFieldOf("vertical", DimensionType.Y_SIZE).forGetter(JigsawStructure$MaxDistance::vertical)
         )
         .apply(i, JigsawStructure$MaxDistance::new)
   );
   public static final Codec<JigsawStructure$MaxDistance> CODEC = Codec.either(FULL_CODEC, HORIZONTAL_VALUE_CODEC)
      .xmap(
         either -> (JigsawStructure$MaxDistance)either.map(Function.identity(), JigsawStructure$MaxDistance::new),
         distance -> distance.horizontal == distance.vertical ? Either.right(distance.horizontal) : Either.left(distance)
      );

   public JigsawStructure$MaxDistance(final int value) {
      this(value, value);
   }
}
