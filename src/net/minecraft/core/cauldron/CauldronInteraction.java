package net.minecraft.core.cauldron;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

@FunctionalInterface
public interface CauldronInteraction {
   CauldronInteraction DEFAULT = (var0, var1, var2, var3, var4, var5) -> InteractionResult.TRY_WITH_EMPTY_HAND;

   InteractionResult interact(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack itemInHand);
}
