package net.minecraft.world.level.storage.loot.providers.number;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.world.level.storage.loot.Validatable;
import net.minecraft.world.level.storage.loot.ValidationContext;

public interface PowerProvider<Value extends Validatable> extends Validatable {
   static <Value extends Validatable, Self extends PowerProvider<Value>> MapCodec<Self> mapCodec(
      final Codec<Holder<Value>> valueCodec, final PowerProvider$Factory<Value, Self> factory
   ) {
      return RecordCodecBuilder.mapCodec(
         i -> i.group(valueCodec.fieldOf("base").forGetter(PowerProvider::base), valueCodec.fieldOf("exponent").forGetter(PowerProvider::exponent))
            .apply(i, factory::create)
      );
   }

   @Override
   default void validate(final ValidationContext context) {
      Validatable.validateHolder(context, "base", this.base());
      Validatable.validateHolder(context, "exponent", this.exponent());
   }

   Holder<Value> base();

   Holder<Value> exponent();
}
