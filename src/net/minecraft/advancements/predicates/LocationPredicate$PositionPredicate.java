package net.minecraft.advancements.predicates;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;

record LocationPredicate$PositionPredicate(MinMaxBounds$Doubles x, MinMaxBounds$Doubles y, MinMaxBounds$Doubles z) {
   public static final Codec<LocationPredicate$PositionPredicate> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            MinMaxBounds$Doubles.CODEC.optionalFieldOf("x", MinMaxBounds$Doubles.ANY).forGetter(LocationPredicate$PositionPredicate::x),
            MinMaxBounds$Doubles.CODEC.optionalFieldOf("y", MinMaxBounds$Doubles.ANY).forGetter(LocationPredicate$PositionPredicate::y),
            MinMaxBounds$Doubles.CODEC.optionalFieldOf("z", MinMaxBounds$Doubles.ANY).forGetter(LocationPredicate$PositionPredicate::z)
         )
         .apply(i, LocationPredicate$PositionPredicate::new)
   );

   private static Optional<LocationPredicate$PositionPredicate> of(final MinMaxBounds$Doubles x, final MinMaxBounds$Doubles y, final MinMaxBounds$Doubles z) {
      return x.isAny() && y.isAny() && z.isAny() ? Optional.empty() : Optional.of(new LocationPredicate$PositionPredicate(x, y, z));
   }

   public boolean matches(final double x, final double y, final double z) {
      return this.x.matches(x) && this.y.matches(y) && this.z.matches(z);
   }
}
