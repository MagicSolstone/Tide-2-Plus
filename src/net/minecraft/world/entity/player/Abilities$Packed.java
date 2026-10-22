package net.minecraft.world.entity.player;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;

public record Abilities$Packed(boolean invulnerable, boolean flying, boolean mayFly, boolean instabuild, boolean mayBuild, float flyingSpeed, float walkingSpeed) {
   public static final Codec<Abilities$Packed> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            ExtraCodecs.optionalAlwaysPresentFieldOf(Codec.BOOL, "invulnerable", false).forGetter(Abilities$Packed::invulnerable),
            ExtraCodecs.optionalAlwaysPresentFieldOf(Codec.BOOL, "flying", false).forGetter(Abilities$Packed::flying),
            ExtraCodecs.optionalAlwaysPresentFieldOf(Codec.BOOL, "mayfly", false).forGetter(Abilities$Packed::mayFly),
            ExtraCodecs.optionalAlwaysPresentFieldOf(Codec.BOOL, "instabuild", false).forGetter(Abilities$Packed::instabuild),
            ExtraCodecs.optionalAlwaysPresentFieldOf(Codec.BOOL, "mayBuild", true).forGetter(Abilities$Packed::mayBuild),
            ExtraCodecs.optionalAlwaysPresentFieldOf(Codec.FLOAT, "flySpeed", 0.05F).forGetter(Abilities$Packed::flyingSpeed),
            ExtraCodecs.optionalAlwaysPresentFieldOf(Codec.FLOAT, "walkSpeed", 0.1F).forGetter(Abilities$Packed::walkingSpeed)
         )
         .apply(i, Abilities$Packed::new)
   );
}
