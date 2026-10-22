package com.li64.tide.registries.entities.fish.special;

import com.google.common.collect.Maps;
import com.li64.tide.data.fishing.mediums.FishingMedium;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.PathNavigationRegion;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class VoidSwimNodeEvaluator extends NodeEvaluator {
    private final boolean allowBreaching;
    private final Long2ObjectMap<PathType> pathTypesByPosCache = new Long2ObjectOpenHashMap<>();

    public VoidSwimNodeEvaluator(boolean allowBreaching) {
        this.allowBreaching = allowBreaching;
    }

    public void prepare(@NotNull PathNavigationRegion region, @NotNull Mob mob) {
        super.prepare(region, mob);
        this.pathTypesByPosCache.clear();
    }

    public void done() {
        super.done();
        this.pathTypesByPosCache.clear();
    }

    public @NotNull Node getStart() {
        return this.getNode(Mth.floor(this.mob.getBoundingBox().minX), Mth.floor(this.mob.getBoundingBox().minY + (double)0.5F), Mth.floor(this.mob.getBoundingBox().minZ));
    }

    public int getNeighbors(Node @NotNull [] arr, @NotNull Node node) {
        int i = 0;
        Map<Direction, Node> map = Maps.newEnumMap(Direction.class);

        for (Direction direction : Direction.values()) {
            Node node1 = this.findAcceptedNode(node.x + direction.getStepX(), node.y + direction.getStepY(), node.z + direction.getStepZ());
            map.put(direction, node1);
            if (this.isNodeValid(node1)) arr[i++] = node1;
        }

        for (Direction direction1 : Direction.Plane.HORIZONTAL) {
            Direction direction2 = direction1.getClockWise();
            if (hasMalus(map.get(direction1)) && hasMalus(map.get(direction2))) {
                Node node1 = this.findAcceptedNode(node.x + direction1.getStepX() + direction2.getStepX(), node.y, node.z + direction1.getStepZ() + direction2.getStepZ());
                if (this.isNodeValid(node1)) arr[i++] = node1;
            }
        }

        return i;
    }

    protected boolean isNodeValid(@Nullable Node node) {
        return node != null && !node.closed;
    }

    private static boolean hasMalus(@Nullable Node node) {
        return node != null && node.costMalus >= 0.0F;
    }

    public @NotNull Target getTarget(double x, double y, double z) {
        return this.getTargetNodeAt(x, y, z);
    }

    @Nullable
    protected Node findAcceptedNode(int x, int y, int z) {
        Node node = null;
        PathType pathType = this.getCachedBlockType(x, y, z);
        if (this.allowBreaching && pathType == PathType.BREACH || pathType == PathType.WATER) {
            float f = this.mob.getPathfindingMalus(pathType);
            if (f >= 0.0F) {
                node = this.getNode(x, y, z);
                node.type = pathType;
                node.costMalus = Math.max(node.costMalus, f);
                if (this.currentContext.level().getFluidState(new BlockPos(x, y, z)).isEmpty()) {
                    node.costMalus += 8.0F;
                }
            }
        }

        return node;
    }

    protected PathType getCachedBlockType(int x, int y, int z) {
        return this.pathTypesByPosCache.computeIfAbsent(BlockPos.asLong(x, y, z), l -> this.getPathType(this.currentContext, x, y, z));
    }

    public @NotNull PathType getPathType(@NotNull PathfindingContext ctx, int x, int y, int z) {
        return this.getPathTypeOfMob(ctx, x, y, z, this.mob);
    }

    public @NotNull PathType getPathTypeOfMob(@NotNull PathfindingContext ctx, int x, int y, int z, @NotNull Mob mob) {
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        mutablePos.set(x, y, z);
        boolean isInVoid = FishingMedium.VOID.isInVoid(mutablePos, mob.level());
        if (!mob.level().isInWorldBounds(mutablePos) && isInVoid) return PathType.WATER;
        BlockState blockState = ctx.getBlockState(mutablePos);
        FluidState fluidState = blockState.getFluidState();
        if (!isInVoid && fluidState.isEmpty() && blockState.isPathfindable(PathComputationType.WATER) && blockState.isAir()) return PathType.BREACH;
        return fluidState.is(FluidTags.WATER) || isInVoid ? PathType.WATER : PathType.BLOCKED;
    }

}