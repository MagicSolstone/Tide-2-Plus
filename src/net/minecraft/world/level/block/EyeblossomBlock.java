package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.animal.bee.Bee;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour$Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEvent$Context;

public class EyeblossomBlock extends FlowerBlock {
   private static final int EYEBLOSSOM_XZ_RANGE = 3;
   private static final int EYEBLOSSOM_Y_RANGE = 2;
   private final EyeblossomBlock$Type type;

   public EyeblossomBlock(final EyeblossomBlock$Type type, final BlockBehaviour$Properties properties) {
      super(type.effect, type.effectDuration, properties);
      this.type = type;
   }

   public EyeblossomBlock(final boolean open, final BlockBehaviour$Properties properties) {
      super(EyeblossomBlock$Type.fromBoolean(open).effect, EyeblossomBlock$Type.fromBoolean(open).effectDuration, properties);
      this.type = EyeblossomBlock$Type.fromBoolean(open);
   }

   @Override
   public void animateTick(final BlockState state, final Level level, final BlockPos pos, final RandomSource random) {
      if (this.type.emitSounds() && random.nextInt(700) == 0) {
         BlockState below = level.getBlockState(pos.below());
         if (below.is(Blocks.PALE_MOSS_BLOCK)) {
            level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.EYEBLOSSOM_IDLE, SoundSource.AMBIENT, 1.0F, 1.0F, false);
         }
      }
   }

   @Override
   protected void randomTick(final BlockState state, final ServerLevel level, final BlockPos pos, final RandomSource random) {
      if (this.tryChangingState(state, level, pos, random)) {
         level.playSound(null, pos, this.type.transform().longSwitchSound, SoundSource.BLOCKS, 1.0F, 1.0F);
      }

      super.randomTick(state, level, pos, random);
   }

   @Override
   protected void tick(final BlockState state, final ServerLevel level, final BlockPos pos, final RandomSource random) {
      if (this.tryChangingState(state, level, pos, random)) {
         level.playSound(null, pos, this.type.transform().shortSwitchSound, SoundSource.BLOCKS, 1.0F, 1.0F);
      }

      super.tick(state, level, pos, random);
   }

   private boolean tryChangingState(final BlockState state, final ServerLevel level, final BlockPos pos, final RandomSource random) {
      boolean shouldBeOpen = level.environmentAttributes().getValue(EnvironmentAttributes.EYEBLOSSOM_OPEN, pos).toBoolean(this.type.open);
      if (shouldBeOpen == this.type.open) {
         return false;
      } else {
         EyeblossomBlock$Type newType = this.type.transform();
         level.setBlockAndUpdate(pos, newType.state());
         level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent$Context.of(state));
         newType.spawnTransformParticle(level, pos, random);
         BlockPos minPos = pos.offset(-3, -2, -3);
         BlockPos maxPos = pos.offset(3, 2, 3);
         level.findBlocksIn(minPos, maxPos).filterState(nearbyState -> nearbyState == state).forEach((nearbyPos, var5x) -> {
            double distance = Math.sqrt(pos.distSqr(nearbyPos));
            int delay = random.nextIntBetweenInclusive((int)(distance * 5.0), (int)(distance * 10.0));
            level.scheduleTick(nearbyPos, state.getBlock(), delay);
         });
         return true;
      }
   }

   @Override
   protected void entityInside(
      final BlockState state, final Level level, final BlockPos pos, final Entity entity, final InsideBlockEffectApplier effectApplier, final boolean isPrecise
   ) {
      if (!level.isClientSide()
         && level.getDifficulty() != Difficulty.PEACEFUL
         && entity instanceof Bee bee
         && Bee.attractsBees(state)
         && !bee.hasEffect(MobEffects.POISON)) {
         bee.addEffect(this.getBeeInteractionEffect());
      }
   }

   @Override
   public MobEffectInstance getBeeInteractionEffect() {
      return new MobEffectInstance(MobEffects.POISON, 25);
   }
}
