package net.minecraft.world.level.block.entity;

import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.GameEventTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.SculkShriekerBlock;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEvent$Context;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem$User;
import org.jspecify.annotations.Nullable;

class SculkShriekerBlockEntity$VibrationUser implements VibrationSystem$User {
   private static final int LISTENER_RADIUS = 8;
   private final PositionSource positionSource;

   public SculkShriekerBlockEntity$VibrationUser(final SculkShriekerBlockEntity this$0) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
      this.positionSource = new BlockPositionSource(this$0x.worldPosition);
   }

   @Override
   public int getListenerRadius() {
      return 8;
   }

   @Override
   public PositionSource getPositionSource() {
      return this.positionSource;
   }

   @Override
   public TagKey<GameEvent> getListenableEvents() {
      return GameEventTags.SHRIEKER_CAN_LISTEN;
   }

   @Override
   public boolean canReceiveVibration(final ServerLevel level, final BlockPos pos, final Holder<GameEvent> event, final GameEvent$Context context) {
      return !this.this$0.getBlockState().getValue(SculkShriekerBlock.SHRIEKING) && SculkShriekerBlockEntity.tryGetPlayer(context.sourceEntity()) != null;
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
      this.this$0.tryShriek(level, SculkShriekerBlockEntity.tryGetPlayer(projectileOwner != null ? projectileOwner : sourceEntity));
   }

   @Override
   public void onDataChanged() {
      this.this$0.setChanged();
   }

   @Override
   public boolean requiresAdjacentChunksToBeTicking() {
      return true;
   }
}
