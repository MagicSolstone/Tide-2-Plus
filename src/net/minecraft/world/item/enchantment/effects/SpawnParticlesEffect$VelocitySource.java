package net.minecraft.world.item.enchantment.effects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.FloatProviders;

public record SpawnParticlesEffect$VelocitySource(float movementScale, FloatProvider base) {
   public static final MapCodec<SpawnParticlesEffect$VelocitySource> CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            Codec.FLOAT.optionalFieldOf("movement_scale", 0.0F).forGetter(SpawnParticlesEffect$VelocitySource::movementScale),
            FloatProviders.CODEC.optionalFieldOf("base", ConstantFloat.ZERO).forGetter(SpawnParticlesEffect$VelocitySource::base)
         )
         .apply(i, SpawnParticlesEffect$VelocitySource::new)
   );

   public double getVelocity(final double movement, final RandomSource random) {
      return movement * this.movementScale + this.base.sample(random);
   }
}
