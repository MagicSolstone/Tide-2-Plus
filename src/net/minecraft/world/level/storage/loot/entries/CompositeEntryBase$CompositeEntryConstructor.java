package net.minecraft.world.level.storage.loot.entries;

import java.util.List;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

@FunctionalInterface
public interface CompositeEntryBase$CompositeEntryConstructor<T extends CompositeEntryBase> {
   T create(List<LootPoolEntryContainer> children, Optional<Holder<LootItemCondition>> condition, Optional<Holder<LootItemFunction>> modifier);
}
