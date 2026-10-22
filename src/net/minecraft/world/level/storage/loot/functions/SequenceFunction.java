package net.minecraft.world.level.storage.loot.functions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Validatable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class SequenceFunction extends LootItemConditionalFunction {
   private static final BiFunction<ItemStack, LootContext, ItemStack> IDENTITY = (stack, var1) -> stack;
   public static final MapCodec<SequenceFunction> MAP_CODEC = RecordCodecBuilder.mapCodec(
      i -> commonFields(i).and(LootItemFunctions.LIST_CODEC.fieldOf("functions").forGetter(f -> f.functions)).apply(i, SequenceFunction::new)
   );
   public static final Codec<SequenceFunction> INLINE_CODEC = LootItemFunctions.LIST_CODEC.xmap(SequenceFunction::new, f -> f.functions);
   private final HolderSet<LootItemFunction> functions;
   private final BiFunction<ItemStack, LootContext, ItemStack> compositeFunction;

   public SequenceFunction(final HolderSet<LootItemFunction> functions) {
      this(Optional.empty(), functions);
   }

   public SequenceFunction(final Optional<Holder<LootItemCondition>> condition, final HolderSet<LootItemFunction> functions) {
      super(condition);
      this.functions = functions;
      this.compositeFunction = compose(functions);
   }

   public boolean canUseInlineCodec() {
      return this.condition.isEmpty();
   }

   private static BiFunction<ItemStack, LootContext, ItemStack> compose(final HolderSet<LootItemFunction> functions) {
      if (!functions.isBound()) {
         return (itemStack, context) -> {
            for (Holder<LootItemFunction> function : functions) {
               itemStack = function.value().apply(itemStack, context);
            }

            return itemStack;
         };
      } else {
         return switch (functions.size()) {
            case 0 -> IDENTITY;
            case 1 -> {
               Holder<LootItemFunction> function = functions.get(0);
               yield (stack, context) -> function.value().apply(stack, context);
            }
            case 2 -> {
               Holder<LootItemFunction> first = functions.get(0);
               Holder<LootItemFunction> second = functions.get(1);
               yield (itemStack, context) -> second.value().apply(first.value().apply(itemStack, context), context);
            }
            default -> (itemStack, context) -> {
               for (Holder<LootItemFunction> function : functions) {
                  itemStack = function.value().apply(itemStack, context);
               }

               return itemStack;
            };
         };
      }
   }

   public static SequenceFunction of(final List<Holder<LootItemFunction>> functions) {
      return new SequenceFunction(Optional.empty(), HolderSet.direct(functions));
   }

   @Override
   public ItemStack run(final ItemStack stack, final LootContext context) {
      return this.compositeFunction.apply(stack, context);
   }

   @Override
   public void validate(final ValidationContext output) {
      super.validate(output);
      Validatable.validateHolderSet(output, "functions", this.functions);
   }

   @Override
   public MapCodec<SequenceFunction> codec() {
      return MAP_CODEC;
   }
}
