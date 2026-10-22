package net.minecraft.world.entity.monster.illager;

import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction$Axis;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEvent$Context;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

class Evoker$EvokerAttackSpellGoal extends SpellcasterIllager$SpellcasterUseSpellGoal {
   private Evoker$EvokerAttackSpellGoal(final Evoker this$0) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(this$0x);
   }

   @Override
   protected int getCastingTime() {
      return 40;
   }

   @Override
   protected int getCastingInterval() {
      return 100;
   }

   @Override
   protected void performSpellCasting() {
      LivingEntity target = this.this$0.getTarget();
      double minY = Math.min(target.getY(), this.this$0.getY());
      double maxY = Math.max(target.getY(), this.this$0.getY()) + 1.0;
      float angleTowardsTarget = (float)Mth.atan2(target.getZ() - this.this$0.getZ(), target.getX() - this.this$0.getX());
      if (this.this$0.distanceToSqr(target) < 9.0) {
         for (int i = 0; i < 5; i++) {
            float angle = angleTowardsTarget + i * (float) Math.PI * 0.4F;
            this.createSpellEntity(this.this$0.getX() + Mth.cos(angle) * 1.5, this.this$0.getZ() + Mth.sin(angle) * 1.5, minY, maxY, angle, 0);
         }

         for (int i = 0; i < 8; i++) {
            float angle = angleTowardsTarget + i * (float) Math.PI * 2.0F / 8.0F + (float) (Math.PI * 2.0 / 5.0);
            this.createSpellEntity(this.this$0.getX() + Mth.cos(angle) * 2.5, this.this$0.getZ() + Mth.sin(angle) * 2.5, minY, maxY, angle, 3);
         }
      } else {
         for (int i = 0; i < 16; i++) {
            double reach = 1.25 * (i + 1);
            int spellSpeed = 1 * i;
            this.createSpellEntity(
               this.this$0.getX() + Mth.cos(angleTowardsTarget) * reach,
               this.this$0.getZ() + Mth.sin(angleTowardsTarget) * reach,
               minY,
               maxY,
               angleTowardsTarget,
               spellSpeed
            );
         }
      }
   }

   private void createSpellEntity(final double x, final double z, final double minY, final double maxY, final float angle, final int delayTicks) {
      BlockPos pos = BlockPos.containing(x, maxY, z);
      boolean success = false;
      double topOffset = 0.0;

      do {
         BlockPos below = pos.below();
         BlockState belowState = this.this$0.level().getBlockState(below);
         if (belowState.isFaceSturdy(this.this$0.level(), below, Direction.UP)) {
            if (!this.this$0.level().isEmptyBlock(pos)) {
               BlockState blockState = this.this$0.level().getBlockState(pos);
               VoxelShape shape = blockState.getCollisionShape(this.this$0.level(), pos);
               if (!shape.isEmpty()) {
                  topOffset = shape.max(Direction$Axis.Y);
               }
            }

            success = true;
            break;
         }

         pos = pos.below();
      } while (pos.getY() >= Mth.floor(minY) - 1);

      if (success) {
         this.this$0.level().addFreshEntity(new EvokerFangs(this.this$0.level(), x, pos.getY() + topOffset, z, angle, delayTicks, this.this$0));
         this.this$0.level().gameEvent(GameEvent.ENTITY_PLACE, new Vec3(x, pos.getY() + topOffset, z), GameEvent$Context.of(this.this$0));
      }
   }

   @Override
   protected SoundEvent getSpellPrepareSound() {
      return SoundEvents.EVOKER_PREPARE_ATTACK;
   }

   @Override
   protected SpellcasterIllager$IllagerSpell getSpell() {
      return SpellcasterIllager$IllagerSpell.FANGS;
   }
}
