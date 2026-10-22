package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour$Properties;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class WitherWallSkullBlock extends WallSkullBlock {
   protected WitherWallSkullBlock(final BlockBehaviour$Properties properties) {
      super(SkullBlock$Types.WITHER_SKELETON, properties);
   }

   @Override
   public void setPlacedBy(final Level level, final BlockPos pos, final BlockState state, @Nullable final LivingEntity by, final ItemStack itemStack) {
      WitherSkullBlock.checkSpawn(level, pos);
   }
}
