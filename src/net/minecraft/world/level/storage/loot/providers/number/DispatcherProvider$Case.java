package net.minecraft.world.level.storage.loot.providers.number;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Validatable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public record DispatcherProvider$Case<Value extends Validatable>(Holder<LootItemCondition> condition, Holder<Value> value) implements Validatable {
   public static <Value extends Validatable> Codec<DispatcherProvider$Case<Value>> codec(final Codec<Holder<Value>> valueCodec) {
      return RecordCodecBuilder.create(
         i -> i.group(
               LootItemCondition.CODEC.fieldOf("condition").forGetter(DispatcherProvider$Case::condition),
               valueCodec.fieldOf("value").forGetter(DispatcherProvider$Case::value)
            )
            .apply(i, DispatcherProvider$Case::new)
      );
   }

   public boolean test(final LootContext context) {
      return this.condition.value().test(context);
   }

   @Override
   public void validate(final ValidationContext context) {
      Validatable.validateHolder(context, "condition", this.condition);
      Validatable.validateHolder(context, "value", this.value);
   }
}
