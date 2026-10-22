package net.minecraft.world.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;

public record SulfurCubeArchetype$ExplosionData(int power, boolean causesFire, int fuse) {
   public static final Codec<SulfurCubeArchetype$ExplosionData> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("power").forGetter(SulfurCubeArchetype$ExplosionData::power),
            Codec.BOOL.fieldOf("causes_fire").forGetter(SulfurCubeArchetype$ExplosionData::causesFire),
            ExtraCodecs.POSITIVE_INT.fieldOf("fuse").forGetter(SulfurCubeArchetype$ExplosionData::fuse)
         )
         .apply(i, SulfurCubeArchetype$ExplosionData::new)
   );
}
