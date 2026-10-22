package net.minecraft.world.level.storage.loot.providers.number;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.util.random.Weighted;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.storage.loot.Validatable;
import net.minecraft.world.level.storage.loot.ValidationContext;

public interface DistributionProvider<Value extends Validatable> extends Validatable {
   WeightedList<Holder<Value>> distribution();

   static <Value extends Validatable, Self extends DistributionProvider<Value>> MapCodec<Self> mapCodec(
      final Codec<Holder<Value>> valueCodec, final DistributionProvider$Factory<Value, Self> factory
   ) {
      return RecordCodecBuilder.mapCodec(
         i -> i.group(WeightedList.nonEmptyCodec(valueCodec).fieldOf("distribution").forGetter(DistributionProvider::distribution)).apply(i, factory::create)
      );
   }

   @Override
   default void validate(final ValidationContext context) {
      Validatable.validateHolder(context, "distribution", this.distribution().unwrap().stream().map(Weighted::value).toList());
   }
}
