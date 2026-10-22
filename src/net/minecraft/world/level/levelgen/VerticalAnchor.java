package net.minecraft.world.level.levelgen;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import java.util.function.Function;

public interface VerticalAnchor {
   Codec<VerticalAnchor> CODEC = Codec.xor(
         VerticalAnchor$Absolute.CODEC,
         Codec.xor(VerticalAnchor$AboveBottom.CODEC, Codec.xor(VerticalAnchor$BelowTop.CODEC, VerticalAnchor$RelativeToSeaLevel.CODEC))
      )
      .xmap(VerticalAnchor::merge, VerticalAnchor::split);
   VerticalAnchor BOTTOM = aboveBottom(0);
   VerticalAnchor TOP = belowTop(0);

   static VerticalAnchor absolute(final int value) {
      return new VerticalAnchor$Absolute(value);
   }

   static VerticalAnchor aboveBottom(final int offset) {
      return new VerticalAnchor$AboveBottom(offset);
   }

   static VerticalAnchor belowTop(final int offset) {
      return new VerticalAnchor$BelowTop(offset);
   }

   static VerticalAnchor bottom() {
      return BOTTOM;
   }

   static VerticalAnchor top() {
      return TOP;
   }

   static VerticalAnchor relativeToSeaLevel(final int offset) {
      return new VerticalAnchor$RelativeToSeaLevel(offset);
   }

   static VerticalAnchor seaLevel() {
      return relativeToSeaLevel(0);
   }

   private static VerticalAnchor merge(
      final Either<VerticalAnchor$Absolute, Either<VerticalAnchor$AboveBottom, Either<VerticalAnchor$BelowTop, VerticalAnchor$RelativeToSeaLevel>>> either
   ) {
      return (VerticalAnchor)either.map(Function.identity(), e -> (Record)e.map(Function.identity(), Either::unwrap));
   }

   private static Either<VerticalAnchor$Absolute, Either<VerticalAnchor$AboveBottom, Either<VerticalAnchor$BelowTop, VerticalAnchor$RelativeToSeaLevel>>> split(
      final VerticalAnchor anchor
   ) {
      if (anchor instanceof VerticalAnchor$Absolute absolute) {
         return Either.left(absolute);
      } else if (anchor instanceof VerticalAnchor$AboveBottom aboveBottom) {
         return Either.right(Either.left(aboveBottom));
      } else {
         return anchor instanceof VerticalAnchor$BelowTop belowTop
            ? Either.right(Either.right(Either.left(belowTop)))
            : Either.right(Either.right(Either.right((VerticalAnchor$RelativeToSeaLevel)anchor)));
      }
   }

   int resolveY(final WorldGenerationContext heightAccessor);
}
