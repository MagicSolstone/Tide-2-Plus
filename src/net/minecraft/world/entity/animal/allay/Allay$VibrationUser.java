package net.minecraft.world.entity.animal.allay;

import java.util.Objects;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.GameEventTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.level.gameevent.EntityPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEvent$Context;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem$User;
import org.jspecify.annotations.Nullable;

class Allay$VibrationUser implements VibrationSystem$User {
   private static final int VIBRATION_EVENT_LISTENER_RANGE = 16;
   private final PositionSource positionSource;

   private Allay$VibrationUser(final Allay this$0) {
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
   public boolean canReceiveVibration(final ServerLevel level, final BlockPos pos, final Holder<GameEvent> event, final GameEvent$Context context) {
      if (this.this$0.isNoAi()) {
         return false;
      } else {
         Optional<GlobalPos> maybeGlobalPos = this.this$0.getBrain().getMemory(MemoryModuleType.LIKED_NOTEBLOCK_POSITION);
         if (maybeGlobalPos.isEmpty()) {
            return true;
         } else {
            GlobalPos globalPos = maybeGlobalPos.get();
            return globalPos.isCloseEnough(level.dimension(), this.this$0.blockPosition(), 1024) && globalPos.pos().equals(pos);
         }
      }
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
      if (event.is(GameEvent.NOTE_BLOCK_PLAY)) {
         AllayAi.hearNoteblock(this.this$0, pos.immutable());
      }
   }

   @Override
   public TagKey<GameEvent> getListenableEvents() {
      return GameEventTags.ALLAY_CAN_LISTEN;
   }
}
