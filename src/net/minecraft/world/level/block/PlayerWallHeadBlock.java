package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.BlockBehaviour$Properties;

public class PlayerWallHeadBlock extends WallSkullBlock {
   protected PlayerWallHeadBlock(final BlockBehaviour$Properties properties) {
      super(SkullBlock$Types.PLAYER, properties);
   }
}
