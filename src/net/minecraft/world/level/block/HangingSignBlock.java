package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.BlockState;

public interface HangingSignBlock {
   HangingSignBlock$Attachment attachmentPoint(BlockState state);

   static HangingSignBlock$Attachment getAttachmentPoint(final BlockState blockState) {
      return blockState.getBlock() instanceof HangingSignBlock hangingSignBlock
         ? hangingSignBlock.attachmentPoint(blockState)
         : HangingSignBlock$Attachment.CEILING;
   }
}
