package net.minecraft.world.item.enchantment.effects;

import com.mojang.serialization.Codec;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;

public enum SpawnParticlesEffect$PositionSourceType implements StringRepresentable {
   ENTITY_POSITION("entity_position", (pos, center, bbSpan, random) -> pos),
   BOUNDING_BOX("in_bounding_box", (pos, center, bbSpan, random) -> center + (random.nextDouble() - 0.5) * bbSpan);

   public static final Codec<SpawnParticlesEffect$PositionSourceType> CODEC = StringRepresentable.fromEnum(SpawnParticlesEffect$PositionSourceType::values);
   private final String id;
   private final SpawnParticlesEffect$PositionSourceType$CoordinateSource source;

   private SpawnParticlesEffect$PositionSourceType(final String id, final SpawnParticlesEffect$PositionSourceType$CoordinateSource source) {
      this.id = id;
      this.source = source;
   }

   public double getCoordinate(final double position, final double center, final float boundingBoxSpan, final RandomSource random) {
      return this.source.getCoordinate(position, center, boundingBoxSpan, random);
   }

   @Override
   public String getSerializedName() {
      return this.id;
   }
}
