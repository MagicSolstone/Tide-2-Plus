package net.minecraft.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.StainedGlassBlock;
import net.minecraft.world.level.block.StainedGlassPaneBlock;
import net.minecraft.world.level.block.state.BlockState;

public interface SpawnUtil$Strategy {
   @Deprecated
   SpawnUtil$Strategy LEGACY_IRON_GOLEM = (level, pos, blockState, abovePos, aboveState) -> !blockState.is(Blocks.COBWEB)
         && !blockState.is(Blocks.CACTUS)
         && !blockState.is(Blocks.GLASS_PANE)
         && !(blockState.getBlock() instanceof StainedGlassPaneBlock)
         && !(blockState.getBlock() instanceof StainedGlassBlock)
         && !(blockState.getBlock() instanceof LeavesBlock)
         && !blockState.is(Blocks.CONDUIT)
         && !blockState.is(Blocks.ICE)
         && !blockState.is(Blocks.TNT)
         && !blockState.is(Blocks.GLOWSTONE)
         && !blockState.is(Blocks.BEACON)
         && !blockState.is(Blocks.SEA_LANTERN)
         && !blockState.is(Blocks.FROSTED_ICE)
         && !blockState.is(Blocks.TINTED_GLASS)
         && !blockState.is(Blocks.GLASS)
      ? (aboveState.isAir() || aboveState.liquid()) && (blockState.isSolid() || blockState.is(Blocks.POWDER_SNOW))
      : false;
   SpawnUtil$Strategy ON_TOP_OF_COLLIDER = (level, pos, blockState, abovePos, aboveState) -> aboveState.getCollisionShape(level, abovePos).isEmpty()
      && Block.isFaceFull(blockState.getCollisionShape(level, pos), Direction.UP);
   SpawnUtil$Strategy ON_TOP_OF_COLLIDER_NO_LEAVES = (level, pos, blockState, abovePos, aboveState) -> aboveState.getCollisionShape(level, abovePos).isEmpty()
      && !blockState.is(BlockTags.LEAVES)
      && Block.isFaceFull(blockState.getCollisionShape(level, pos), Direction.UP);

   boolean canSpawnOn(ServerLevel level, BlockPos pos, BlockState blockState, BlockPos abovePos, BlockState aboveState);
}
