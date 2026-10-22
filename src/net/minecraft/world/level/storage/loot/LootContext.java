package net.minecraft.world.level.storage.loot;

import com.google.common.collect.Sets;
import java.util.Set;
import java.util.function.Consumer;
import net.minecraft.core.HolderGetter$Provider;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.slot.SlotSource;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.jspecify.annotations.Nullable;

public class LootContext {
   private final LootParams params;
   private final RandomSource random;
   private final HolderGetter$Provider lootDataResolver;
   private final Set<LootContext$VisitedEntry<?>> visitedElements = Sets.newLinkedHashSet();

   private LootContext(final LootParams params, final RandomSource random, final HolderGetter$Provider lootDataResolver) {
      this.params = params;
      this.random = random;
      this.lootDataResolver = lootDataResolver;
   }

   public boolean hasParameter(final ContextKey<?> key) {
      return this.params.contextMap().has(key);
   }

   @Nullable
   public <T> T getOptional(final ContextKey<T> key) {
      return this.params.contextMap().get(key);
   }

   public void addDynamicDrops(final Identifier location, final Consumer<ItemStack> output) {
      this.params.addDynamicDrops(location, output);
   }

   public boolean hasVisitedElement(final LootContext$VisitedEntry<?> element) {
      return this.visitedElements.contains(element);
   }

   public boolean pushVisitedElement(final LootContext$VisitedEntry<?> element) {
      return this.visitedElements.add(element);
   }

   public void popVisitedElement(final LootContext$VisitedEntry<?> element) {
      this.visitedElements.remove(element);
   }

   public HolderGetter$Provider getResolver() {
      return this.lootDataResolver;
   }

   public RandomSource getRandom() {
      return this.random;
   }

   public float getLuck() {
      return this.params.getLuck();
   }

   public ServerLevel getLevel() {
      return this.params.getLevel();
   }

   public static LootContext$VisitedEntry<LootTable> createVisitedEntry(final LootTable table) {
      return new LootContext$VisitedEntry<>(LootDataType.TABLE, table);
   }

   public static LootContext$VisitedEntry<LootItemCondition> createVisitedEntry(final LootItemCondition predicate) {
      return new LootContext$VisitedEntry<>(LootDataType.PREDICATE, predicate);
   }

   public static LootContext$VisitedEntry<LootItemFunction> createVisitedEntry(final LootItemFunction modifier) {
      return new LootContext$VisitedEntry<>(LootDataType.MODIFIER, modifier);
   }

   public static LootContext$VisitedEntry<SlotSource> createVisitedEntry(final SlotSource slotSource) {
      return new LootContext$VisitedEntry<>(LootDataType.SLOT_SOURCE, slotSource);
   }
}
