package net.minecraft.client.renderer.block.dispatch.multipart;

import java.util.List;
import java.util.function.Predicate;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Util;

@Environment(EnvType.CLIENT)
enum CombinedCondition$Operation$1 {
   private CombinedCondition$Operation$1(final String name) {
   }

   @Override
   public <V> Predicate<V> apply(final List<Predicate<V>> terms) {
      return Util.allOf(terms);
   }
}
