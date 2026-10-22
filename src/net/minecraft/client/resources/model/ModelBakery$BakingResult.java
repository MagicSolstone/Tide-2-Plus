package net.minecraft.client.resources.model;

import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.item.ClientItem$Properties;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.BlockState;

@Environment(EnvType.CLIENT)
public record ModelBakery$BakingResult(
   ModelBakery$MissingModels missingModels,
   Map<BlockState, BlockStateModel> blockStateModels,
   Map<Identifier, ItemModel> itemStackModels,
   Map<Identifier, ClientItem$Properties> itemProperties
) {
   public BlockStateModel getBlockStateModel(final BlockState blockState) {
      return this.blockStateModels.getOrDefault(blockState, this.missingModels.block);
   }
}
