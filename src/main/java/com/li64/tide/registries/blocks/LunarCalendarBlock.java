package com.li64.tide.registries.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Map;

public class LunarCalendarBlock extends HorizontalDirectionalBlock {
    public static final VoxelShape SHAPE_NORTH = Shapes.box(0.125, 0.125, 0.9375, 0.875, 0.875, 1);
    public static final VoxelShape SHAPE_SOUTH = Shapes.box(0.125, 0.125, 0, 0.875, 0.875, 0.0625);
    public static final VoxelShape SHAPE_EAST = Shapes.box(0, 0.125, 0.125, 0.0625, 0.875, 0.875);
    public static final VoxelShape SHAPE_WEST = Shapes.box(0.9375, 0.125, 0.125, 1, 0.875, 0.875);
    public static final Map<Direction, VoxelShape> SHAPES = Map.of(
            Direction.NORTH, SHAPE_NORTH, Direction.SOUTH, SHAPE_SOUTH,
            Direction.EAST, SHAPE_EAST, Direction.WEST, SHAPE_WEST
    );

    public LunarCalendarBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    public static final MapCodec<LunarCalendarBlock> CODEC = simpleCodec(LunarCalendarBlock::new);
    @Override protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES.get(state.getValue(FACING));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = this.defaultBlockState();
        for (Direction dir : context.getNearestLookingDirections()) {
            if (dir.getAxis().isHorizontal()) {
                Direction opposite = dir.getOpposite();
                state = state.setValue(FACING, opposite);
                if (state.canSurvive(context.getLevel(), context.getClickedPos())) return state;
            }
        }
        return null;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.relative(state.getValue(FACING).getOpposite())).isSolid();
    }
}
