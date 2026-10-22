package net.minecraft.world.level.storage.loot.providers.number;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.world.level.storage.loot.Validatable;
import net.minecraft.world.level.storage.loot.ValidationContext;

public interface BinaryProvider<Value extends Validatable> extends Validatable {
   static <Value extends Validatable, Self extends BinaryProvider<Value>> MapCodec<Self> mapCodec(
      final Codec<Holder<Value>> valueCodec, final BinaryProvider$Factory<Value, Self> factory
   ) {
      return RecordCodecBuilder.mapCodec(
         i -> i.group(valueCodec.fieldOf("left").forGetter(BinaryProvider::left), valueCodec.fieldOf("right").forGetter(BinaryProvider::right))
            .apply(i, factory::create)
      );
   }

   @Override
   default void validate(final ValidationContext context) {
      Validatable.validateHolder(context, "left", this.left());
      Validatable.validateHolder(context, "right", this.right());
   }

   Holder<Value> left();

   Holder<Value> right();
}
