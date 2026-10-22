package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.BlockState;

public interface PlainSignBlock {
   PlainSignBlock$Attachment attachmentPoint(BlockState state);

   static PlainSignBlock$Attachment getAttachmentPoint(final BlockState blockState) {
      return blockState.getBlock() instanceof PlainSignBlock plainSignBlock ? plainSignBlock.attachmentPoint(blockState) : PlainSignBlock$Attachment.GROUND;
   }
}
