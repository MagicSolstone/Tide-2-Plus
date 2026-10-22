package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.util.Pair;
import net.minecraft.util.datafix.schemas.NamespacedSchema;

record ThrownPotionSplitFix$ItemIdFinder(OpticFinder<?> itemFinder, OpticFinder<Pair<String, String>> itemIdFinder) {
   public String getItemId(final Typed<?> entity) {
      return entity.getOptionalTyped(this.itemFinder)
         .flatMap(item -> item.getOptional(this.itemIdFinder))
         .<String>map(Pair::getSecond)
         .map(NamespacedSchema::ensureNamespaced)
         .orElse("");
   }
}
