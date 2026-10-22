package net.minecraft.world.level.storage.loot.functions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Validatable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class FilteredFunction extends LootItemConditionalFunction {
   public static final MapCodec<FilteredFunction> MAP_CODEC = RecordCodecBuilder.mapCodec(
      i -> commonFields(i)
         .and(
            i.group(
               ItemPredicate.CODEC.fieldOf("item_filter").forGetter(f -> f.filter),
               LootItemFunctions.CODEC.optionalFieldOf("on_pass").forGetter(f -> f.onPass),
               LootItemFunctions.CODEC.optionalFieldOf("on_fail").forGetter(f -> f.onFail)
            )
         )
         .apply(i, FilteredFunction::new)
   );
   private final ItemPredicate filter;
   private final Optional<Holder<LootItemFunction>> onPass;
   private final Optional<Holder<LootItemFunction>> onFail;

   private FilteredFunction(
      final Optional<Holder<LootItemCondition>> condition,
      final ItemPredicate filter,
      final Optional<Holder<LootItemFunction>> onPass,
      final Optional<Holder<LootItemFunction>> onFail
   ) {
      super(condition);
      this.filter = filter;
      this.onPass = onPass;
      this.onFail = onFail;
   }

   @Override
   public MapCodec<FilteredFunction> codec() {
      return MAP_CODEC;
   }

   @Override
   public ItemStack run(final ItemStack itemStack, final LootContext context) {
      Optional<Holder<LootItemFunction>> function = this.filter.test((ItemInstance)itemStack) ? this.onPass : this.onFail;
      return function.isPresent() ? function.get().value().apply(itemStack, context) : itemStack;
   }

   @Override
   public void validate(final ValidationContext context) {
      super.validate(context);
      Validatable.validateHolder(context, "on_pass", this.onPass);
      Validatable.validateHolder(context, "on_fail", this.onFail);
   }

   public static FilteredFunction$Builder filtered(final ItemPredicate predicate) {
      return new FilteredFunction$Builder(predicate);
   }
}
