package net.minecraft.world.entity.monster.warden;

import java.util.Objects;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.GameEventTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.level.gameevent.EntityPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEvent$Context;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem$User;
import org.jspecify.annotations.Nullable;

class Warden$VibrationUser implements VibrationSystem$User {
   private static final int GAME_EVENT_LISTENER_RANGE = 16;
   private final PositionSource positionSource;

   private Warden$VibrationUser(final Warden this$0) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
      this.positionSource = new EntityPositionSource(this.this$0, this.this$0.getEyeHeight());
   }

   @Override
   public int getListenerRadius() {
      return 16;
   }

   @Override
   public PositionSource getPositionSource() {
      return this.positionSource;
   }

   @Override
   public TagKey<GameEvent> getListenableEvents() {
      return GameEventTags.WARDEN_CAN_LISTEN;
   }

   @Override
   public boolean canTriggerAvoidVibration() {
      return true;
   }

   @Override
   public boolean canReceiveVibration(final ServerLevel level, final BlockPos pos, final Holder<GameEvent> event, final GameEvent$Context context) {
      return !this.this$0.isNoAi()
            && !this.this$0.isDeadOrDying()
            && !this.this$0.getBrain().hasMemoryValue(MemoryModuleType.VIBRATION_COOLDOWN)
            && !this.this$0.isDiggingOrEmerging()
            && level.getWorldBorder().isWithinBounds(pos)
         ? !(context.sourceEntity() instanceof LivingEntity livingEntity && !this.this$0.canTargetEntity(livingEntity))
         : false;
   }

   @Override
   public void onReceiveVibration(
      final ServerLevel level,
      final BlockPos pos,
      final Holder<GameEvent> event,
      @Nullable final Entity sourceEntity,
      @Nullable final Entity projectileOwner,
      final float receivingDistance
   ) {
      if (!this.this$0.isDeadOrDying()) {
         Warden.access$000(this.this$0).setMemoryWithExpiry(MemoryModuleType.VIBRATION_COOLDOWN, Unit.INSTANCE, 40L);
         level.broadcastEntityEvent(this.this$0, (byte)61);
         this.this$0.playSound(SoundEvents.WARDEN_TENDRIL_CLICKS, 5.0F, this.this$0.getVoicePitch());
         BlockPos suspiciousPos = pos;
         if (projectileOwner != null) {
            if (this.this$0.closerThan(projectileOwner, 30.0)) {
               if (this.this$0.getBrain().hasMemoryValue(MemoryModuleType.RECENT_PROJECTILE)) {
                  if (this.this$0.canTargetEntity(projectileOwner)) {
                     suspiciousPos = projectileOwner.blockPosition();
                  }

                  this.this$0.increaseAngerAt(projectileOwner);
               } else {
                  this.this$0.increaseAngerAt(projectileOwner, 10, true);
               }
            }

            this.this$0.getBrain().setMemoryWithExpiry(MemoryModuleType.RECENT_PROJECTILE, Unit.INSTANCE, 100L);
         } else {
            this.this$0.increaseAngerAt(sourceEntity);
         }

         if (!this.this$0.getAngerLevel().isAngry()) {
            Optional<LivingEntity> activeEntity = this.this$0.angerManagement.getActiveEntity();
            if (projectileOwner != null || activeEntity.isEmpty() || activeEntity.get() == sourceEntity) {
               WardenAi.setDisturbanceLocation(this.this$0, suspiciousPos);
            }
         }
      }
   }
}
