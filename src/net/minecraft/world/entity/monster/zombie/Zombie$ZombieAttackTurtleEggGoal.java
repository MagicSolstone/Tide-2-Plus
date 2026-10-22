package net.minecraft.world.entity.monster.zombie;

import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.RemoveBlockGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;

class Zombie$ZombieAttackTurtleEggGoal extends RemoveBlockGoal {
   public Zombie$ZombieAttackTurtleEggGoal(final Zombie this$0, final PathfinderMob mob, final double speedModifier, final int verticalSearchRange) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(Blocks.TURTLE_EGG, mob, speedModifier, verticalSearchRange);
   }

   @Override
   public void playDestroyProgressSound(final LevelAccessor level, final BlockPos pos) {
      level.playSound(null, pos, SoundEvents.ZOMBIE_DESTROY_EGG, SoundSource.HOSTILE, 0.5F, 0.9F + Zombie.access$000(this.this$0).nextFloat() * 0.2F);
   }

   @Override
   public void playBreakSound(final Level level, final BlockPos pos) {
      level.playSound(null, pos, SoundEvents.TURTLE_EGG_BREAK, SoundSource.BLOCKS, 0.7F, 0.9F + level.getRandom().nextFloat() * 0.2F);
   }

   @Override
   public double acceptedDistance() {
      return 1.14;
   }
}
