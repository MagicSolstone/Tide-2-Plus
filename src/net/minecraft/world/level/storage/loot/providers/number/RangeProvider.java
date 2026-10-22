package net.minecraft.world.level.storage.loot.providers.number;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.world.level.storage.loot.Validatable;
import net.minecraft.world.level.storage.loot.ValidationContext;

public interface RangeProvider<Value extends Validatable> extends Validatable {
   static <Value extends Validatable, Self extends RangeProvider<Value>> MapCodec<Self> mapCodec(
      final Codec<Holder<Value>> valueCodec, final RangeProvider$Factory<Value, Self> factory
   ) {
      return RecordCodecBuilder.mapCodec(
         i -> i.group(valueCodec.fieldOf("min").forGetter(RangeProvider::min), valueCodec.fieldOf("max").forGetter(RangeProvider::max))
            .apply(i, factory::create)
      );
   }

   Holder<Value> min();

   Holder<Value> max();

   @Override
   default void validate(final ValidationContext context) {
      Validatable.validateHolder(context, "min", this.min());
      Validatable.validateHolder(context, "max", this.max());
   }
}
