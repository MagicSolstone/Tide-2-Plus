package net.minecraft.world.level.gameevent.vibrations;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ClipBlockStateContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEvent$Context;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.phys.HitResult$Type;
import net.minecraft.world.phys.Vec3;

public class VibrationSystem$Listener implements GameEventListener {
   private final VibrationSystem system;

   public VibrationSystem$Listener(final VibrationSystem system) {
      this.system = system;
   }

   @Override
   public PositionSource getListenerSource() {
      return this.system.getVibrationUser().getPositionSource();
   }

   @Override
   public int getListenerRadius() {
      return this.system.getVibrationUser().getListenerRadius();
   }

   @Override
   public boolean handleGameEvent(final ServerLevel level, final Holder<GameEvent> event, final GameEvent$Context context, final Vec3 sourcePosition) {
      VibrationSystem$Data data = this.system.getVibrationData();
      VibrationSystem$User user = this.system.getVibrationUser();
      if (data.getCurrentVibration() != null) {
         return false;
      } else if (!user.isValidVibration(event, context)) {
         return false;
      } else {
         Optional<Vec3> listenerSourcePos = user.getPositionSource().getPosition(level);
         if (listenerSourcePos.isEmpty()) {
            return false;
         } else {
            Vec3 destination = listenerSourcePos.get();
            if (!user.canReceiveVibration(level, BlockPos.containing(sourcePosition), event, context)) {
               return false;
            } else if (isOccluded(level, sourcePosition, destination)) {
               return false;
            } else {
               this.scheduleVibration(level, data, event, context, sourcePosition, destination);
               return true;
            }
         }
      }
   }

   public void forceScheduleVibration(final ServerLevel level, final Holder<GameEvent> event, final GameEvent$Context context, final Vec3 origin) {
      this.system
         .getVibrationUser()
         .getPositionSource()
         .getPosition(level)
         .ifPresent(p -> this.scheduleVibration(level, this.system.getVibrationData(), event, context, origin, p));
   }

   private void scheduleVibration(
      final ServerLevel level,
      final VibrationSystem$Data data,
      final Holder<GameEvent> event,
      final GameEvent$Context context,
      final Vec3 origin,
      final Vec3 dest
   ) {
      data.selectionStrategy.addCandidate(new VibrationInfo(event, (float)origin.distanceTo(dest), origin, context.sourceEntity()), level.getGameTime());
   }

   public static float distanceBetweenInBlocks(final BlockPos origin, final BlockPos dest) {
      return (float)Math.sqrt(origin.distSqr(dest));
   }

   private static boolean isOccluded(final Level level, final Vec3 origin, final Vec3 dest) {
      Vec3 from = new Vec3(Mth.floor(origin.x) + 0.5, Mth.floor(origin.y) + 0.5, Mth.floor(origin.z) + 0.5);
      Vec3 to = new Vec3(Mth.floor(dest.x) + 0.5, Mth.floor(dest.y) + 0.5, Mth.floor(dest.z) + 0.5);

      for (Direction direction : Direction.values()) {
         Vec3 nudgedSource = from.relative(direction, 1.0E-5F);
         if (level.isBlockInLine(new ClipBlockStateContext(nudgedSource, to, state -> state.is(BlockTags.OCCLUDES_VIBRATION_SIGNALS))).getType()
            != HitResult$Type.BLOCK) {
            return false;
         }
      }

      return true;
   }
}
