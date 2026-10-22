package net.minecraft.world.level.block;

import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.LidBlockEntity;

class ChestBlock$3 implements DoubleBlockCombiner$Combiner<ChestBlockEntity, Float2FloatFunction> {
   ChestBlock$3(final LidBlockEntity val$entity) {
      this.val$entity = val$entityx;
   }

   public Float2FloatFunction acceptDouble(final ChestBlockEntity first, final ChestBlockEntity second) {
      return partialTickTime -> Math.max(first.getOpenNess(partialTickTime), second.getOpenNess(partialTickTime));
   }

   public Float2FloatFunction acceptSingle(final ChestBlockEntity single) {
      return single::getOpenNess;
   }

   public Float2FloatFunction acceptNone() {
      return this.val$entity::getOpenNess;
   }
}
