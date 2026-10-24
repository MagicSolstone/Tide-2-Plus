package com.li64.tide.registries.blocks;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.li64.tide.registries.blocks.entities.FishDisplayBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.MapCodec;

import java.util.Map;

@SuppressWarnings({"deprecation", "unused"})
public class FishDisplayBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
    public static final Map<Direction, VoxelShape> SHAPES_1x1 = Maps.newEnumMap(
            ImmutableMap.of(
                    Direction.NORTH, Block.box(1, 2, 15, 15, 14, 16),
                    Direction.SOUTH, Block.box(1, 2, 0, 15, 14, 1),
                    Direction.EAST, Block.box(0, 2, 1, 1, 14, 15),
                    Direction.WEST, Block.box(15, 2, 1, 16, 14, 15)
            ));
    public static final Map<Direction, VoxelShape> SHAPES_2x1 = Maps.newEnumMap(
            ImmutableMap.of(
                    Direction.NORTH, Block.box(0, 2, 15, 16, 14, 16),
                    Direction.SOUTH, Block.box(0, 2, 0, 16, 14, 1),
                    Direction.EAST, Block.box(0, 2, 0, 1, 14, 16),
                    Direction.WEST, Block.box(15, 2, 0, 16, 14, 16)
            ));
    public static final Map<Direction, VoxelShape> SHAPES_3x1 = Maps.newEnumMap(
            ImmutableMap.of(
                    Direction.NORTH, Block.box(0, 1, 15, 16, 15, 16),
                    Direction.SOUTH, Block.box(0, 1, 0, 16, 15, 1),
                    Direction.EAST, Block.box(0, 1, 0, 1, 15, 16),
                    Direction.WEST, Block.box(15, 1, 0, 16, 15, 16)
            ));
    public static final EnumProperty<FishDisplayShape> SHAPE = EnumProperty.create(
            "fish_display_shape", FishDisplayShape.class, FishDisplayShape.values());

    public FishDisplayBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED, SHAPE);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new FishDisplayBlockEntity(pos, state);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState blockState = this.defaultBlockState();
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());

        for (Direction direction : context.getNearestLookingDirections()) {
            if (direction.getAxis().isHorizontal()) {
                Direction opposite = direction.getOpposite();
                blockState = blockState.setValue(FACING, opposite);
                if (blockState.canSurvive(context.getLevel(), context.getClickedPos()))
                    return blockState.setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
            }
        }

        return null;
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, @NotNull BlockGetter level,
                                        @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return state.getOptionalValue(SHAPE).orElse(FishDisplayShape.SHAPE_1x1).getShape(state.getValue(FACING));
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.relative(state.getValue(FACING).getOpposite())).isSolid();
    }

    @Override
    protected @NotNull BlockState updateShape(BlockState state, LevelReader level, net.minecraft.world.level.ScheduledTickAccess ticks,
                                              BlockPos pos, Direction dir, BlockPos pos2, BlockState state2, net.minecraft.util.RandomSource random) {
        return dir.getOpposite() == state.getValue(FACING) && !state.canSurvive(level, pos)
                ? Blocks.AIR.defaultBlockState()
                : super.updateShape(state, level, ticks, pos, dir, pos2, state2, random);
    }

    @Override
    public @NotNull BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public @NotNull BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public @NotNull FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }


    @Override
    protected @NotNull InteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state,
                                                       @NotNull Level level, @NotNull BlockPos pos,
                                                       @NotNull Player player, @NotNull InteractionHand hand,
                                                       @NotNull BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof FishDisplayBlockEntity display) {
            ItemStack displayed = stack.copyWithCount(1);
            if (display.setDisplayStack(displayed)) {
                if (!player.hasInfiniteMaterials()) stack.shrink(1);
                level.playSound(null, pos, SoundEvents.COD_FLOP, SoundSource.BLOCKS, 1.0f, 1.0f);
                return InteractionResult.SUCCESS;
            }
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, @NotNull Level level,
                                                        @NotNull BlockPos pos, @NotNull Player player,
                                                        @NotNull BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof FishDisplayBlockEntity display) {
            if (!display.isEmpty()) {
                ItemStack stack = display.takeDisplayStack();
                if (!player.addItem(stack)) player.drop(stack, false, net.minecraft.util.Prediction.SERVER_ONLY);
                level.playSound(null, pos, SoundEvents.COD_FLOP, SoundSource.BLOCKS, 1.0f, 1.0f);
                return InteractionResult.SUCCESS;
            }
        }
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    protected @NotNull ItemStack getCloneItemStack(@NotNull LevelReader level, @NotNull BlockPos pos, @NotNull BlockState state, boolean includeData) {
        if (level.getBlockEntity(pos) instanceof FishDisplayBlockEntity display) {
            if (!display.isEmpty()) return display.getDisplayStack().copy();
        }
        return super.getCloneItemStack(level, pos, state, includeData);
    }


    /** A Trophy on the wall gives off the odd purple sparkle; a Mythic, gold ones. */
    @Override
    public void animateTick(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, net.minecraft.util.RandomSource random) {
        if (!(level.getBlockEntity(pos) instanceof FishDisplayBlockEntity display) || display.isEmpty()) return;
        int q = com.li64.tide.data.item.TideItemData.FISH_QUALITY.getOrDefault(display.getDisplayStack(), -1);
        boolean mythic = q == com.li64.tide.data.angler.CatchQuality.MYTHIC.ordinal(), trophy = q == com.li64.tide.data.angler.CatchQuality.TROPHY.ordinal();
        if (!mythic && !(trophy && random.nextInt(3) == 0)) return;
        Direction facing = state.getValue(FACING);
        for (int i = 0; i < (mythic ? 2 : 1); i++) {
            double x = pos.getX() + 0.5 + facing.getStepX() * 0.35 + (random.nextDouble() - 0.5) * (facing.getStepX() == 0 ? 1.1 : 0.3);
            double y = pos.getY() + 0.2 + random.nextDouble() * 0.6;
            double z = pos.getZ() + 0.5 + facing.getStepZ() * 0.35 + (random.nextDouble() - 0.5) * (facing.getStepZ() == 0 ? 1.1 : 0.3);
            level.addParticle(mythic ? net.minecraft.core.particles.ParticleTypes.WAX_ON : net.minecraft.core.particles.ParticleTypes.WITCH,
                    x, y, z, 0, mythic ? 0.02 : 0, 0);
        }
    }

}
