package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.Typed;
import com.mojang.serialization.Dynamic;
import java.util.function.Function;

@FunctionalInterface
interface EntitySpawnerItemVariantComponentFix$Fixer extends Function<Typed<?>, Typed<?>> {
   default Typed<?> apply(final Typed<?> components) {
      return components.update(DSL.remainderFinder(), this::fixRemainder);
   }

   default <T> Dynamic<T> fixRemainder(final Dynamic<T> remainder) {
      return remainder.get("minecraft:bucket_entity_data").result().map(bucketData -> this.fixRemainder(remainder, (Dynamic<T>)bucketData)).orElse(remainder);
   }

   <T> Dynamic<T> fixRemainder(Dynamic<T> remainder, Dynamic<T> bucketData);
}
