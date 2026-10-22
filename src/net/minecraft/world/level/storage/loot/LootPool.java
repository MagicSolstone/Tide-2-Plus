package net.minecraft.world.level.storage.loot;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntries;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntry;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProvider;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProviders;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;
import org.apache.commons.lang3.mutable.MutableInt;

public class LootPool implements Validatable {
   public static final Codec<LootPool> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            LootPoolEntries.CODEC.listOf().fieldOf("entries").forGetter(p -> p.entries),
            LootItemCondition.CODEC.optionalFieldOf("condition").forGetter(p -> p.condition),
            LootItemFunctions.CODEC.optionalFieldOf("modifier").forGetter(p -> p.modifier),
            ContextIntProviders.CODEC.fieldOf("rolls").forGetter(p -> p.rolls),
            ContextFloatProviders.CODEC.optionalFieldOf("bonus_rolls", ContextFloatProviders.exactly(0.0F)).forGetter(p -> p.bonusRolls)
         )
         .apply(i, LootPool::new)
   );
   private final List<LootPoolEntryContainer> entries;
   private final Optional<Holder<LootItemCondition>> condition;
   private final Optional<Holder<LootItemFunction>> modifier;
   private final Holder<ContextIntProvider> rolls;
   private final Holder<ContextFloatProvider> bonusRolls;

   private LootPool(
      final List<LootPoolEntryContainer> entries,
      final Optional<Holder<LootItemCondition>> condition,
      final Optional<Holder<LootItemFunction>> modifier,
      final Holder<ContextIntProvider> rolls,
      final Holder<ContextFloatProvider> bonusRolls
   ) {
      this.entries = entries;
      this.condition = condition;
      this.modifier = modifier;
      this.rolls = rolls;
      this.bonusRolls = bonusRolls;
   }

   private void addRandomItem(final Consumer<ItemStack> result, final LootContext context) {
      RandomSource random = context.getRandom();
      List<LootPoolEntry> validEntries = Lists.newArrayList();
      MutableInt totalWeight = new MutableInt();

      for (LootPoolEntryContainer entry : this.entries) {
         entry.expand(context, e -> {
            int weight = e.getWeight(context.getLuck());
            if (weight > 0) {
               validEntries.add(e);
               totalWeight.add(weight);
            }
         });
      }

      int entryCount = validEntries.size();
      if (totalWeight.intValue() != 0 && entryCount != 0) {
         if (entryCount == 1) {
            validEntries.get(0).createItemStack(result, context);
         } else {
            int index = random.nextInt(totalWeight.intValue());

            for (LootPoolEntry entry : validEntries) {
               index -= entry.getWeight(context.getLuck());
               if (index < 0) {
                  entry.createItemStack(result, context);
                  return;
               }
            }
         }
      }
   }

   public void addRandomItems(final Consumer<ItemStack> result, final LootContext context) {
      if (!this.condition.isPresent() || this.condition.get().value().test(context)) {
         Consumer<ItemStack> decoratedConsumer = LootItemFunction.decorate(this.modifier, result, context);
         int count = this.rolls.value().getInt(context) + Mth.floor(this.bonusRolls.value().getFloat(context) * context.getLuck());

         for (int i = 0; i < count; i++) {
            this.addRandomItem(decoratedConsumer, context);
         }
      }
   }

   @Override
   public void validate(final ValidationContext output) {
      Validatable.validateHolder(output, "condition", this.condition);
      Validatable.validateHolder(output, "modifier", this.modifier);
      Validatable.validate(output, "entries", this.entries);
      Validatable.validateHolder(output, "rolls", this.rolls);
      Validatable.validateHolder(output, "bonus_rolls", this.bonusRolls);
   }

   public static LootPool$Builder lootPool() {
      return new LootPool$Builder();
   }
}
