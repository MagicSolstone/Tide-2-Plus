package net.minecraft.client.renderer.block.dispatch.multipart;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import java.util.BitSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.resources.model.geometry.BakedQuad$MaterialFlags;
import net.minecraft.client.resources.model.sprite.Material$Baked;
import net.minecraft.world.level.block.state.BlockState;

@Environment(EnvType.CLIENT)
final class MultiPartModel$SharedBakedState {
   private final List<MultiPartModel$Selector<BlockStateModel>> selectors;
   private final Material$Baked particleMaterial;
   @BakedQuad$MaterialFlags
   private final int materialFlags;
   private final Map<BitSet, List<BlockStateModel>> subsets = new ConcurrentHashMap<>();

   private static BlockStateModel getFirstModel(final List<MultiPartModel$Selector<BlockStateModel>> selectors) {
      if (selectors.isEmpty()) {
         throw new IllegalArgumentException("Model must have at least one selector");
      } else {
         return selectors.getFirst().model();
      }
   }

   @BakedQuad$MaterialFlags
   private static int computeMaterialFlags(final List<MultiPartModel$Selector<BlockStateModel>> selectors) {
      int flags = 0;

      for (MultiPartModel$Selector<BlockStateModel> selector : selectors) {
         flags |= selector.model.materialFlags();
      }

      return flags;
   }

   public MultiPartModel$SharedBakedState(final List<MultiPartModel$Selector<BlockStateModel>> selectors) {
      this.selectors = selectors;
      BlockStateModel firstModel = getFirstModel(selectors);
      this.particleMaterial = firstModel.particleMaterial();
      this.materialFlags = computeMaterialFlags(selectors);
   }

   public List<BlockStateModel> selectModels(final BlockState state) {
      BitSet selectedModels = new BitSet();

      for (int i = 0; i < this.selectors.size(); i++) {
         if (this.selectors.get(i).condition.test(state)) {
            selectedModels.set(i);
         }
      }

      return this.subsets.computeIfAbsent(selectedModels, selected -> {
         Builder<BlockStateModel> result = ImmutableList.builder();

         for (int ix = 0; ix < this.selectors.size(); ix++) {
            if (selected.get(ix)) {
               result.add(this.selectors.get(ix).model);
            }
         }

         return result.build();
      });
   }
}
