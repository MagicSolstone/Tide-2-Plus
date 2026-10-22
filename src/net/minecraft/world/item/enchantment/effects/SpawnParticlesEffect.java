package net.minecraft.world.item.enchantment.effects;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.FloatProviders;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.phys.Vec3;

public record SpawnParticlesEffect(
   ParticleOptions particle,
   SpawnParticlesEffect$PositionSource horizontalPosition,
   SpawnParticlesEffect$PositionSource verticalPosition,
   SpawnParticlesEffect$VelocitySource horizontalVelocity,
   SpawnParticlesEffect$VelocitySource verticalVelocity,
   FloatProvider speed
) implements EnchantmentEntityEffect {
   public static final MapCodec<SpawnParticlesEffect> CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            ParticleTypes.CODEC.fieldOf("particle").forGetter(SpawnParticlesEffect::particle),
            SpawnParticlesEffect$PositionSource.CODEC.fieldOf("horizontal_position").forGetter(SpawnParticlesEffect::horizontalPosition),
            SpawnParticlesEffect$PositionSource.CODEC.fieldOf("vertical_position").forGetter(SpawnParticlesEffect::verticalPosition),
            SpawnParticlesEffect$VelocitySource.CODEC.fieldOf("horizontal_velocity").forGetter(SpawnParticlesEffect::horizontalVelocity),
            SpawnParticlesEffect$VelocitySource.CODEC.fieldOf("vertical_velocity").forGetter(SpawnParticlesEffect::verticalVelocity),
            FloatProviders.CODEC.optionalFieldOf("speed", ConstantFloat.ZERO).forGetter(SpawnParticlesEffect::speed)
         )
         .apply(i, SpawnParticlesEffect::new)
   );

   public static SpawnParticlesEffect$PositionSource offsetFromEntityPosition(final float offset) {
      return new SpawnParticlesEffect$PositionSource(SpawnParticlesEffect$PositionSourceType.ENTITY_POSITION, offset, 1.0F);
   }

   public static SpawnParticlesEffect$PositionSource inBoundingBox() {
      return new SpawnParticlesEffect$PositionSource(SpawnParticlesEffect$PositionSourceType.BOUNDING_BOX, 0.0F, 1.0F);
   }

   public static SpawnParticlesEffect$VelocitySource movementScaled(final float scale) {
      return new SpawnParticlesEffect$VelocitySource(scale, ConstantFloat.ZERO);
   }

   public static SpawnParticlesEffect$VelocitySource fixedVelocity(final FloatProvider provider) {
      return new SpawnParticlesEffect$VelocitySource(0.0F, provider);
   }

   @Override
   public void apply(final ServerLevel serverLevel, final int enchantmentLevel, final EnchantedItemInUse item, final Entity entity, final Vec3 position) {
      RandomSource random = entity.getRandom();
      Vec3 movement = entity.getKnownMovement();
      float bbWidth = entity.getBbWidth();
      float bbHeight = entity.getBbHeight();
      serverLevel.sendParticles(
         this.particle,
         this.horizontalPosition.getCoordinate(position.x(), position.x(), bbWidth, random),
         this.verticalPosition.getCoordinate(position.y(), position.y() + bbHeight / 2.0F, bbHeight, random),
         this.horizontalPosition.getCoordinate(position.z(), position.z(), bbWidth, random),
         0,
         this.horizontalVelocity.getVelocity(movement.x(), random),
         this.verticalVelocity.getVelocity(movement.y(), random),
         this.horizontalVelocity.getVelocity(movement.z(), random),
         this.speed.sample(random)
      );
   }

   @Override
   public MapCodec<SpawnParticlesEffect> codec() {
      return CODEC;
   }
}
