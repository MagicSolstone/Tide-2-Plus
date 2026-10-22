package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.TrailParticleOption;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public enum EyeblossomBlock$Type {
   OPEN(true, MobEffects.BLINDNESS, 11.0F, SoundEvents.EYEBLOSSOM_OPEN_LONG, SoundEvents.EYEBLOSSOM_OPEN, 16545810),
   CLOSED(false, MobEffects.NAUSEA, 7.0F, SoundEvents.EYEBLOSSOM_CLOSE_LONG, SoundEvents.EYEBLOSSOM_CLOSE, 6250335);

   private final boolean open;
   private final Holder<MobEffect> effect;
   private final float effectDuration;
   private final SoundEvent longSwitchSound;
   private final SoundEvent shortSwitchSound;
   private final int particleColor;

   private EyeblossomBlock$Type(
      final boolean open,
      final Holder<MobEffect> effect,
      final float duration,
      final SoundEvent longSwitchSound,
      final SoundEvent shortSwitchSound,
      final int particleColor
   ) {
      this.open = open;
      this.effect = effect;
      this.effectDuration = duration;
      this.longSwitchSound = longSwitchSound;
      this.shortSwitchSound = shortSwitchSound;
      this.particleColor = particleColor;
   }

   public Block block() {
      return this.open ? Blocks.OPEN_EYEBLOSSOM : Blocks.CLOSED_EYEBLOSSOM;
   }

   public BlockState state() {
      return this.block().defaultBlockState();
   }

   public EyeblossomBlock$Type transform() {
      return fromBoolean(!this.open);
   }

   public boolean emitSounds() {
      return this.open;
   }

   public static EyeblossomBlock$Type fromBoolean(final boolean open) {
      return open ? OPEN : CLOSED;
   }

   public void spawnTransformParticle(final ServerLevel level, final BlockPos pos, final RandomSource random) {
      Vec3 start = Vec3.atCenterOf(pos);
      double lifetime = 0.5 + random.nextDouble();
      Vec3 velocity = new Vec3(random.nextDouble() - 0.5, random.nextDouble() + 1.0, random.nextDouble() - 0.5);
      Vec3 target = start.add(velocity.scale(lifetime));
      TrailParticleOption particle = new TrailParticleOption(target, this.particleColor, (int)(20.0 * lifetime));
      level.sendParticles(particle, start.x, start.y, start.z, 1, 0.0, 0.0, 0.0, 0.0);
   }

   public SoundEvent longSwitchSound() {
      return this.longSwitchSound;
   }
}
