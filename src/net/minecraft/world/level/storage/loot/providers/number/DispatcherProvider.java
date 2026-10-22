package net.minecraft.world.level.storage.loot.providers.number;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Validatable;
import net.minecraft.world.level.storage.loot.ValidationContext;

public interface DispatcherProvider<Value extends Validatable> extends Validatable {
   static <Value extends Validatable, Self extends DispatcherProvider<Value>> MapCodec<Self> mapCodec(
      final Codec<Holder<Value>> valueCodec, final Holder<Value> defaultCase, final DispatcherProvider$Factory<Value, Self> factory
   ) {
      return RecordCodecBuilder.mapCodec(
         i -> i.group(
               DispatcherProvider$Case.codec(valueCodec).listOf().fieldOf("cases").forGetter(DispatcherProvider::cases),
               valueCodec.optionalFieldOf("default", defaultCase).forGetter(DispatcherProvider::defaultValue)
            )
            .apply(i, factory::create)
      );
   }

   default Holder<Value> selectValue(final LootContext context) {
      for (DispatcherProvider$Case<Value> aCase : this.cases()) {
         if (aCase.test(context)) {
            return aCase.value;
         }
      }

      return this.defaultValue();
   }

   List<DispatcherProvider$Case<Value>> cases();

   Holder<Value> defaultValue();

   @Override
   default void validate(final ValidationContext context) {
      Validatable.validate(context, "cases", this.cases());
      Validatable.validateHolder(context, "default", this.defaultValue());
   }
}
