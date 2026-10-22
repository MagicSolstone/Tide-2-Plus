package net.minecraft.world.level.storage.loot.providers.nbt;

import net.minecraft.advancements.predicates.NbtPredicate;
import net.minecraft.nbt.Tag;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContextArg$Getter;

record ContextNbtProvider$EntitySource(ContextKey<? extends Entity> contextParam) implements LootContextArg$Getter<Entity, Tag> {
   public Tag get(final Entity entity) {
      return NbtPredicate.getEntityTagToCompare(entity);
   }
}
