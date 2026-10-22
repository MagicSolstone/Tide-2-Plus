package net.minecraft.world.level.block;

import com.google.common.annotations.VisibleForTesting;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

public class MultifaceSpreader {
   public static final MultifaceSpreader$SpreadType[] DEFAULT_SPREAD_ORDER = new MultifaceSpreader$SpreadType[]{
      MultifaceSpreader$SpreadType.SAME_POSITION, MultifaceSpreader$SpreadType.SAME_PLANE, MultifaceSpreader$SpreadType.WRAP_AROUND
   };
   private final MultifaceSpreader$SpreadConfig config;

   public MultifaceSpreader(final MultifaceBlock multifaceBlock) {
      this(new MultifaceSpreader$DefaultSpreaderConfig(multifaceBlock));
   }

   public MultifaceSpreader(final MultifaceSpreader$SpreadConfig config) {
      this.config = config;
   }

   public boolean canSpreadInAnyDirection(final BlockState state, final BlockGetter level, final BlockPos pos, final Direction startingFace) {
      return Direction.stream()
         .anyMatch(
            spreadDirection -> this.getSpreadFromFaceTowardDirection(state, level, pos, startingFace, spreadDirection, this.config::canSpreadInto).isPresent()
         );
   }

   public Optional<MultifaceSpreader$SpreadPos> spreadFromRandomFaceTowardRandomDirection(
      final BlockState state, final LevelAccessor level, final BlockPos pos, final RandomSource random
   ) {
      return Direction.allShuffled(random)
         .stream()
         .filter(faceDirection -> this.config.canSpreadFrom(state, faceDirection))
         .map(faceDirection -> this.spreadFromFaceTowardRandomDirection(state, level, pos, faceDirection, random, false))
         .filter(Optional::isPresent)
         .findFirst()
         .orElse(Optional.empty());
   }

   public long spreadAll(final BlockState state, final LevelAccessor level, final BlockPos pos, final boolean postProcess) {
      return Direction.stream()
         .filter(faceDirection -> this.config.canSpreadFrom(state, faceDirection))
         .map(faceDirection -> this.spreadFromFaceTowardAllDirections(state, level, pos, faceDirection, postProcess))
         .reduce(0L, Long::sum);
   }

   public Optional<MultifaceSpreader$SpreadPos> spreadFromFaceTowardRandomDirection(
      final BlockState state, final LevelAccessor level, final BlockPos pos, final Direction startingFace, final RandomSource random, final boolean postProcess
   ) {
      return Direction.allShuffled(random)
         .stream()
         .map(spreadDirection -> this.spreadFromFaceTowardDirection(state, level, pos, startingFace, spreadDirection, postProcess))
         .filter(Optional::isPresent)
         .findFirst()
         .orElse(Optional.empty());
   }

   private long spreadFromFaceTowardAllDirections(
      final BlockState state, final LevelAccessor level, final BlockPos pos, final Direction startingFace, final boolean postProcess
   ) {
      return Direction.stream()
         .map(spreadDirection -> this.spreadFromFaceTowardDirection(state, level, pos, startingFace, spreadDirection, postProcess))
         .filter(Optional::isPresent)
         .count();
   }

   @VisibleForTesting
   public Optional<MultifaceSpreader$SpreadPos> spreadFromFaceTowardDirection(
      final BlockState state,
      final LevelAccessor level,
      final BlockPos pos,
      final Direction fromFace,
      final Direction spreadDirection,
      final boolean postProcess
   ) {
      return this.getSpreadFromFaceTowardDirection(state, level, pos, fromFace, spreadDirection, this.config::canSpreadInto)
         .flatMap(spreadPos -> this.spreadToFace(level, spreadPos, postProcess));
   }

   public Optional<MultifaceSpreader$SpreadPos> getSpreadFromFaceTowardDirection(
      final BlockState state,
      final BlockGetter level,
      final BlockPos pos,
      final Direction startingFace,
      final Direction spreadDirection,
      final MultifaceSpreader$SpreadPredicate canSpreadInto
   ) {
      if (spreadDirection.getAxis() == startingFace.getAxis()) {
         return Optional.empty();
      } else if (this.config.isOtherBlockValidAsSource(state) || this.config.hasFace(state, startingFace) && !this.config.hasFace(state, spreadDirection)) {
         for (MultifaceSpreader$SpreadType type : this.config.getSpreadTypes()) {
            MultifaceSpreader$SpreadPos spreadPos = type.getSpreadPos(pos, spreadDirection, startingFace);
            if (canSpreadInto.test(level, pos, spreadPos)) {
               return Optional.of(spreadPos);
            }
         }

         return Optional.empty();
      } else {
         return Optional.empty();
      }
   }

   public Optional<MultifaceSpreader$SpreadPos> spreadToFace(final LevelAccessor level, final MultifaceSpreader$SpreadPos spreadPos, final boolean postProcess) {
      BlockState oldState = level.getBlockState(spreadPos.pos());
      return this.config.placeBlock(level, spreadPos, oldState, postProcess) ? Optional.of(spreadPos) : Optional.empty();
   }
}
