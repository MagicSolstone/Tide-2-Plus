package com.li64.tide.registries.blocks;

import com.li64.tide.client.gui.menus.AnglingTableMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CraftingTableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class AnglingTableBlock extends CraftingTableBlock {
    private static final Component CONTAINER_TITLE = Component.translatable("container.tide.angling_table");

    public AnglingTableBlock(Properties properties) {
        super(properties);
    }

    public @NotNull MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        return new SimpleMenuProvider((containerId, inventory, player) -> new AnglingTableMenu(
                containerId, inventory, ContainerLevelAccess.create(level, pos)), CONTAINER_TITLE);
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult pHitResult) {
        if (level.isClientSide()) return InteractionResult.SUCCESS;
        player.openMenu(state.getMenuProvider(level, pos));
        return InteractionResult.CONSUME;
    }
}
