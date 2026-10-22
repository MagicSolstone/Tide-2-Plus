package net.minecraft.advancements.predicates;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

public record LightPredicate(MinMaxBounds$Ints composite) {
   public static final Codec<LightPredicate> CODEC = RecordCodecBuilder.create(
      i -> i.group(MinMaxBounds$Ints.CODEC.optionalFieldOf("light", MinMaxBounds$Ints.ANY).forGetter(LightPredicate::composite)).apply(i, LightPredicate::new)
   );

   public boolean matches(final ServerLevel level, final BlockPos pos) {
      return !level.isLoaded(pos) ? false : this.composite.matches(level.getMaxLocalRawBrightness(pos));
   }
}
