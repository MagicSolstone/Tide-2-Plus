package net.minecraft.world.level.levelgen.structure.structures;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record RuinedPortalPiece$Properties(boolean cold, float mossiness, boolean airPocket, boolean overgrown, boolean vines, boolean replaceWithBlackstone) {
   public static final Codec<RuinedPortalPiece$Properties> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            Codec.BOOL.fieldOf("cold").forGetter(RuinedPortalPiece$Properties::cold),
            Codec.FLOAT.fieldOf("mossiness").forGetter(RuinedPortalPiece$Properties::mossiness),
            Codec.BOOL.fieldOf("air_pocket").forGetter(RuinedPortalPiece$Properties::airPocket),
            Codec.BOOL.fieldOf("overgrown").forGetter(RuinedPortalPiece$Properties::overgrown),
            Codec.BOOL.fieldOf("vines").forGetter(RuinedPortalPiece$Properties::vines),
            Codec.BOOL.fieldOf("replace_with_blackstone").forGetter(RuinedPortalPiece$Properties::replaceWithBlackstone)
         )
         .apply(i, RuinedPortalPiece$Properties::new)
   );
}
