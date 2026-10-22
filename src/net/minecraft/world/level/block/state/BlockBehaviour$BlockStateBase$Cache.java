package net.minecraft.world.level.block.state;

import java.util.Arrays;
import java.util.Locale;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction$Axis;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

final class BlockBehaviour$BlockStateBase$Cache {
   private static final Direction[] DIRECTIONS = Direction.values();
   private static final int SUPPORT_TYPE_COUNT = SupportType.values().length;
   public final VoxelShape collisionShape;
   public final boolean largeCollisionShape;
   private final boolean[] faceSturdy;
   public final boolean isCollisionShapeFullBlock;

   private BlockBehaviour$BlockStateBase$Cache(final BlockState state) {
      Block block = state.getBlock();
      this.collisionShape = block.getCollisionShape(state, EmptyBlockGetter.INSTANCE, BlockPos.ZERO, CollisionContext.empty());
      if (!this.collisionShape.isEmpty() && state.hasOffsetFunction()) {
         throw new IllegalStateException(
            String.format(
               Locale.ROOT,
               "%s has a collision shape and an offset type, but is not marked as dynamicShape in its properties.",
               BuiltInRegistries.BLOCK.getKey(block)
            )
         );
      } else {
         this.largeCollisionShape = Arrays.stream(Direction$Axis.values())
            .anyMatch(axis -> this.collisionShape.min(axis) < 0.0 || this.collisionShape.max(axis) > 1.0);
         this.faceSturdy = new boolean[DIRECTIONS.length * SUPPORT_TYPE_COUNT];

         for (Direction direction : DIRECTIONS) {
            for (SupportType type : SupportType.values()) {
               this.faceSturdy[getFaceSupportIndex(direction, type)] = type.isSupporting(state, EmptyBlockGetter.INSTANCE, BlockPos.ZERO, direction);
            }
         }

         this.isCollisionShapeFullBlock = Block.isShapeFullBlock(state.getCollisionShape(EmptyBlockGetter.INSTANCE, BlockPos.ZERO));
      }
   }

   public boolean isFaceSturdy(final Direction direction, final SupportType supportType) {
      return this.faceSturdy[getFaceSupportIndex(direction, supportType)];
   }

   private static int getFaceSupportIndex(final Direction direction, final SupportType supportType) {
      return direction.ordinal() * SUPPORT_TYPE_COUNT + supportType.ordinal();
   }
}
