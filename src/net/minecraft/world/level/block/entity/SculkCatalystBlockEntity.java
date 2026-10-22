package net.minecraft.world.level.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.level.gameevent.GameEventListener$Provider;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class SculkCatalystBlockEntity extends BlockEntity implements GameEventListener$Provider<SculkCatalystBlockEntity$CatalystListener> {
   private final SculkCatalystBlockEntity$CatalystListener catalystListener;

   public SculkCatalystBlockEntity(final BlockPos worldPosition, final BlockState blockState) {
      super(BlockEntityTypes.SCULK_CATALYST, worldPosition, blockState);
      this.catalystListener = new SculkCatalystBlockEntity$CatalystListener(blockState, new BlockPositionSource(worldPosition));
   }

   public static void serverTick(final Level level, final BlockPos pos, final BlockState state, final SculkCatalystBlockEntity entity) {
      entity.catalystListener.getSculkSpreader().updateCursors(level, pos, level.getRandom(), true);
   }

   @Override
   protected void loadAdditional(final ValueInput input) {
      super.loadAdditional(input);
      this.catalystListener.sculkSpreader.load(input);
   }

   @Override
   protected void saveAdditional(final ValueOutput output) {
      this.catalystListener.sculkSpreader.save(output);
      super.saveAdditional(output);
   }

   public SculkCatalystBlockEntity$CatalystListener getListener() {
      return this.catalystListener;
   }
}
