package net.minecraft.world.item.enchantment.effects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;

public record SpawnParticlesEffect$PositionSource(SpawnParticlesEffect$PositionSourceType type, float offset, float scale) {
   public static final MapCodec<SpawnParticlesEffect$PositionSource> CODEC = RecordCodecBuilder.mapCodec(
         i -> i.group(
               SpawnParticlesEffect$PositionSourceType.CODEC.fieldOf("type").forGetter(SpawnParticlesEffect$PositionSource::type),
               Codec.FLOAT.optionalFieldOf("offset", 0.0F).forGetter(SpawnParticlesEffect$PositionSource::offset),
               ExtraCodecs.POSITIVE_FLOAT.optionalFieldOf("scale", 1.0F).forGetter(SpawnParticlesEffect$PositionSource::scale)
            )
            .apply(i, SpawnParticlesEffect$PositionSource::new)
      )
      .validate(
         positioning -> positioning.type() == SpawnParticlesEffect$PositionSourceType.ENTITY_POSITION && positioning.scale() != 1.0F
            ? DataResult.error(() -> "Cannot scale an entity position coordinate source")
            : DataResult.success(positioning)
      );

   public double getCoordinate(final double position, final double center, final float boundingBoxSpan, final RandomSource random) {
      return this.type.getCoordinate(position, center, boundingBoxSpan * this.scale, random) + this.offset;
   }
}
