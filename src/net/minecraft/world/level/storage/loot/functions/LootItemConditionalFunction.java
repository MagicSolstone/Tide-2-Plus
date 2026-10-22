package net.minecraft.world.level.storage.loot.functions;

import com.mojang.datafixers.Products.P1;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder.Instance;
import com.mojang.serialization.codecs.RecordCodecBuilder.Mu;
import java.util.Optional;
import java.util.function.Function;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Validatable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public abstract class LootItemConditionalFunction implements LootItemFunction {
   protected final Optional<Holder<LootItemCondition>> condition;

   protected LootItemConditionalFunction(final Optional<Holder<LootItemCondition>> condition) {
      this.condition = condition;
   }

   @Override
   public abstract MapCodec<? extends LootItemConditionalFunction> codec();

   protected static <T extends LootItemConditionalFunction> P1<Mu<T>, Optional<Holder<LootItemCondition>>> commonFields(final Instance<T> i) {
      return i.group(LootItemCondition.CODEC.optionalFieldOf("condition").forGetter(f -> f.condition));
   }

   public final ItemStack apply(final ItemStack itemStack, final LootContext context) {
      return !this.condition.isEmpty() && !this.condition.get().value().test(context) ? itemStack : this.run(itemStack, context);
   }

   protected abstract ItemStack run(final ItemStack itemStack, final LootContext context);

   @Override
   public void validate(final ValidationContext context) {
      LootItemFunction.super.validate(context);
      Validatable.validateHolder(context, "condition", this.condition);
   }

   protected static LootItemConditionalFunction$Builder<?> simpleBuilder(final Function<Optional<Holder<LootItemCondition>>, LootItemFunction> constructor) {
      return new LootItemConditionalFunction$DummyBuilder(constructor);
   }
}
