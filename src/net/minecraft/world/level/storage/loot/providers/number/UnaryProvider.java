package net.minecraft.world.level.storage.loot.providers.number;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.world.level.storage.loot.Validatable;
import net.minecraft.world.level.storage.loot.ValidationContext;

public interface UnaryProvider<Value extends Validatable> extends Validatable {
   static <Value extends Validatable, Self extends UnaryProvider<Value>> MapCodec<Self> codec(
      final Codec<Holder<Value>> valueCodec, final UnaryProvider$Factory<Value, Self> factory
   ) {
      return RecordCodecBuilder.mapCodec(i -> i.group(valueCodec.fieldOf("input").forGetter(UnaryProvider::input)).apply(i, factory::create));
   }

   Holder<Value> input();

   @Override
   default void validate(final ValidationContext context) {
      Validatable.validateHolder(context, "input", this.input());
   }
}
