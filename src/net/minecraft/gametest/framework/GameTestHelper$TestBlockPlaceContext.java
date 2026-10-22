package net.minecraft.gametest.framework;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

class GameTestHelper$TestBlockPlaceContext extends BlockPlaceContext {
   private final Direction placeDirection;

   public GameTestHelper$TestBlockPlaceContext(
      final GameTestHelper this$0,
      final Level level,
      final InteractionHand hand,
      final ItemStack itemStackInHand,
      final BlockHitResult hitResult,
      final Direction placeDirection
   ) {
      Objects.requireNonNull(this$0);
      super(level, null, hand, itemStackInHand, hitResult);
      this.placeDirection = placeDirection;
   }

   @Override
   public Direction getNearestLookingDirection() {
      return this.placeDirection;
   }

   @Override
   public Direction[] getNearestLookingDirections() {
      Direction[] directions = Direction.values();
      Arrays.sort(directions, Comparator.comparingDouble(d -> d.getUnitVec3().distanceTo(this.placeDirection.getUnitVec3())));
      return directions;
   }
}
