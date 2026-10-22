package net.minecraft.world.level;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour$BlockStateBase;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public enum ClipContext$Block implements ClipContext$ShapeGetter {
   COLLIDER(BlockBehaviour$BlockStateBase::getCollisionShape),
   OUTLINE(BlockBehaviour$BlockStateBase::getShape),
   VISUAL(BlockBehaviour$BlockStateBase::getVisualShape),
   FALLDAMAGE_RESETTING(
      (state, level, pos, collisionContext) -> {
         if (state.is(BlockTags.FALL_DAMAGE_RESETTING)) {
            return Shapes.block();
         } else {
            if (collisionContext instanceof EntityCollisionContext entityCollisionContext
               && entityCollisionContext.getEntity() != null
               && entityCollisionContext.getEntity().is(EntityTypes.PLAYER)) {
               if (state.is(Blocks.END_GATEWAY) || state.is(Blocks.END_PORTAL)) {
                  return Shapes.block();
               }

               if (level instanceof ServerLevel serverLevel
                  && state.is(Blocks.NETHER_PORTAL)
                  && serverLevel.getGameRules().get(GameRules.PLAYERS_NETHER_PORTAL_DEFAULT_DELAY) == 0) {
                  return Shapes.block();
               }
            }

            return Shapes.empty();
         }
      }
   );

   private final ClipContext$ShapeGetter shapeGetter;

   private ClipContext$Block(final ClipContext$ShapeGetter getShape) {
      this.shapeGetter = getShape;
   }

   @Override
   public VoxelShape get(final BlockState state, final BlockGetter level, final BlockPos pos, final CollisionContext context) {
      return this.shapeGetter.get(state, level, pos, context);
   }
}
