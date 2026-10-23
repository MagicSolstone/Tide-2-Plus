package com.li64.tide.registries.entities.misc;

import com.li64.tide.data.loot.LootTableRef;
import com.li64.tide.registries.TideEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.Vec3;

public class LootCrateEntity extends FallingBlockEntity {
    private LootTableRef tableKey;
    private LootParams lootParams;

    public LootCrateEntity(EntityType<FallingBlockEntity> entityType, Level level) {
        super(entityType, level);
    }

    public static void create(Level level, BlockPos pos, BlockState blockState,
                              double dx, double dy, double dz, LootParams lootParams, LootTableRef tableKey) {
        LootCrateEntity entity = new LootCrateEntity(level, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5,
                blockState.hasProperty(BlockStateProperties.WATERLOGGED)
                        ? blockState.setValue(BlockStateProperties.WATERLOGGED, false) : blockState,
                lootParams, tableKey);
        entity.setDeltaMovement(dx, dy, dz);
        level.setBlock(pos, blockState.getFluidState().createLegacyBlock(), 3);
        level.addFreshEntity(entity);
    }

    private LootCrateEntity(Level level, double x, double y, double z,
                            BlockState state, LootParams lootParams, LootTableRef tableKey) {
        this(TideEntityTypes.LOOT_CRATE, level);
        this.blockState = state;
        this.blocksBuilding = true;
        this.setPos(x, y, z);
        this.setDeltaMovement(Vec3.ZERO);
        this.xo = x;
        this.yo = y;
        this.zo = z;
        this.lootParams = lootParams;
        this.tableKey = tableKey;
        this.setStartPos(this.blockPosition());
    }

    @Override
    public void tick() {
        if (this.blockState.isAir()) {
            this.discard();
        } else {
            Block block = this.blockState.getBlock();
            ++this.time;
            if (!this.isNoGravity()) this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.04, 0.0));
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.handlePortal();
            Level var3 = this.level();
            if (var3 instanceof ServerLevel serverLevel) {
                if (this.isAlive() || this.forceTickAfterTeleportToDuplicate) {
                    BlockPos pos = this.blockPosition();
                    if (!this.onGround()) {
                        if (this.time > 100 && (pos.getY() <= this.level().getMinY() || pos.getY() > (this.level().getMaxY() + 1)) || this.time > 600) {
                            if (this.dropItem && serverLevel.getGameRules().get(GameRules.ENTITY_DROPS)) {
                                this.spawnAtLocation(serverLevel, block);
                            }

                            this.discard();
                        }
                    } else {
                        BlockState blockstate = this.level().getBlockState(pos);
                        this.setDeltaMovement(this.getDeltaMovement().multiply(0.7, -0.5, 0.7));
                        if (!blockstate.is(Blocks.MOVING_PISTON)) {
                            boolean canReplace = blockstate.canBeReplaced(new DirectionalPlaceContext(this.level(), pos, Direction.DOWN, ItemStack.EMPTY, Direction.UP));
                            boolean isFree = FallingBlock.isFree(this.level().getBlockState(pos.below()));
                            boolean canSurvive = this.blockState.canSurvive(this.level(), pos) && !isFree;
                            if (canReplace && canSurvive) {
                                if (this.blockState.hasProperty(BlockStateProperties.WATERLOGGED) && this.level().getFluidState(pos).getType() == Fluids.WATER) {
                                    this.blockState = this.blockState.setValue(BlockStateProperties.WATERLOGGED, true);
                                }

                                if (!this.level().setBlock(pos, this.blockState, 3)) {
                                    if (this.dropItem && serverLevel.getGameRules().get(GameRules.ENTITY_DROPS)) {
                                        this.discard();
                                        this.callOnBrokenAfterFall(block, pos);
                                        this.spawnAtLocation(serverLevel, block);
                                    }
                                } else {
                                    ((ServerLevel) this.level()).getChunkSource().chunkMap.sendToTrackingPlayers(this, new ClientboundBlockUpdatePacket(pos, this.level().getBlockState(pos)));
                                    this.discard();
                                    BlockEntity blockEntity = this.level().getBlockEntity(pos);
                                    if (blockEntity != null) {
                                        if (blockEntity instanceof Container container) addCrateLoot(container);
                                    }
                                }
                            } else {
                                this.discard();
                                if (this.dropItem && serverLevel.getGameRules().get(GameRules.ENTITY_DROPS)) {
                                    this.dropCrateLoot();
                                    this.callOnBrokenAfterFall(block, pos);
                                    this.spawnAtLocation(serverLevel, block);
                                }
                            }
                        }
                    }
                }
            }

            this.setDeltaMovement(this.getDeltaMovement().scale(0.98));
        }
    }

    public void dropCrateLoot() {
        if (level() instanceof ServerLevel serverLevel && tableKey != null) {
            LootTable lootTable = tableKey.getTable(serverLevel.getServer());
            lootTable.getRandomItems(lootParams).forEach(stack -> { grade(stack); this.spawnAtLocation(serverLevel, stack); });
        }
    }

    /** Fish from a crate get a quality (and size) too, rolled for whoever fished the crate up. */
    private void grade(net.minecraft.world.item.ItemStack stack) {
        if (stack.isEmpty() || lootParams == null || com.li64.tide.data.item.TideItemData.FISH_QUALITY.isPresent(stack)) return;
        if (lootParams.contextMap().get(net.minecraft.world.level.storage.loot.parameters.LootContextParams.THIS_ENTITY)
                instanceof com.li64.tide.registries.entities.misc.fishing.TideFishingHook hook
                && hook.getPlayerOwner() instanceof net.minecraft.server.level.ServerPlayer player)
            com.li64.tide.data.angler.CatchQuality.apply(player, stack, null);
    }

    public void addCrateLoot(Container container) {
        if (level() instanceof ServerLevel serverLevel && tableKey != null) {
            LootTable lootTable = tableKey.getTable(serverLevel.getServer());
            lootTable.fill(container, lootParams, random.nextLong());
            for (int i = 0; i < container.getContainerSize(); i++) grade(container.getItem(i));
        }
    }
}
