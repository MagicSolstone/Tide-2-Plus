package net.minecraft.world.level.levelgen.structure.structures;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;

public record RuinedPortalStructure$Setup(
   RuinedPortalPiece$VerticalPlacement placement,
   float airPocketProbability,
   float mossiness,
   boolean overgrown,
   boolean vines,
   boolean canBeCold,
   boolean replaceWithBlackstone,
   float weight
) {
   public static final Codec<RuinedPortalStructure$Setup> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            RuinedPortalPiece$VerticalPlacement.CODEC.fieldOf("placement").forGetter(RuinedPortalStructure$Setup::placement),
            Codec.floatRange(0.0F, 1.0F).fieldOf("air_pocket_probability").forGetter(RuinedPortalStructure$Setup::airPocketProbability),
            Codec.floatRange(0.0F, 1.0F).fieldOf("mossiness").forGetter(RuinedPortalStructure$Setup::mossiness),
            Codec.BOOL.fieldOf("overgrown").forGetter(RuinedPortalStructure$Setup::overgrown),
            Codec.BOOL.fieldOf("vines").forGetter(RuinedPortalStructure$Setup::vines),
            Codec.BOOL.fieldOf("can_be_cold").forGetter(RuinedPortalStructure$Setup::canBeCold),
            Codec.BOOL.fieldOf("replace_with_blackstone").forGetter(RuinedPortalStructure$Setup::replaceWithBlackstone),
            ExtraCodecs.POSITIVE_FLOAT.fieldOf("weight").forGetter(RuinedPortalStructure$Setup::weight)
         )
         .apply(i, RuinedPortalStructure$Setup::new)
   );
}
