package net.minecraft.advancements.triggers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import net.minecraft.advancements.predicates.DataComponentMatchers;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.advancements.predicates.ItemPredicate$Builder;
import net.minecraft.advancements.predicates.MinMaxBounds$Ints;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public record InventoryChangeTrigger$TriggerInstance(
   Optional<Holder<LootItemCondition>> player, InventoryChangeTrigger$TriggerInstance$Slots slots, List<ItemPredicate> items
) implements SimpleCriterionTrigger$SimpleInstance {
   public static final Codec<InventoryChangeTrigger$TriggerInstance> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            LootItemCondition.CODEC.optionalFieldOf("player").forGetter(InventoryChangeTrigger$TriggerInstance::player),
            InventoryChangeTrigger$TriggerInstance$Slots.CODEC
               .optionalFieldOf("slots", InventoryChangeTrigger$TriggerInstance$Slots.ANY)
               .forGetter(InventoryChangeTrigger$TriggerInstance::slots),
            ItemPredicate.CODEC.listOf().optionalFieldOf("items", List.of()).forGetter(InventoryChangeTrigger$TriggerInstance::items)
         )
         .apply(i, InventoryChangeTrigger$TriggerInstance::new)
   );

   public static Criterion<InventoryChangeTrigger$TriggerInstance> hasItems(final ItemPredicate$Builder... items) {
      return hasItems(Stream.of(items).map(ItemPredicate$Builder::build).toArray(ItemPredicate[]::new));
   }

   public static Criterion<InventoryChangeTrigger$TriggerInstance> hasItems(final ItemPredicate... items) {
      return CriteriaTriggers.INVENTORY_CHANGED
         .createCriterion(new InventoryChangeTrigger$TriggerInstance(Optional.empty(), InventoryChangeTrigger$TriggerInstance$Slots.ANY, List.of(items)));
   }

   public static Criterion<InventoryChangeTrigger$TriggerInstance> hasItems(final ItemLike... items) {
      ItemPredicate[] predicates = new ItemPredicate[items.length];

      for (int i = 0; i < items.length; i++) {
         predicates[i] = new ItemPredicate(
            Optional.of(HolderSet.direct(items[i].asItem().builtInRegistryHolder())), MinMaxBounds$Ints.ANY, DataComponentMatchers.ANY
         );
      }

      return hasItems(predicates);
   }

   public boolean matches(final Inventory inventory, final ItemStack changedItem, final int slotsFull, final int slotsEmpty, final int slotsOccupied) {
      if (!this.slots.matches(slotsFull, slotsEmpty, slotsOccupied)) {
         return false;
      } else if (this.items.isEmpty()) {
         return true;
      } else if (this.items.size() != 1) {
         List<ItemPredicate> predicates = new ObjectArrayList(this.items);
         int count = inventory.getContainerSize();

         for (int slot = 0; slot < count; slot++) {
            if (predicates.isEmpty()) {
               return true;
            }

            ItemStack itemStack = inventory.getItem(slot);
            if (!itemStack.isEmpty()) {
               predicates.removeIf(predicate -> predicate.test((ItemInstance)itemStack));
            }
         }

         return predicates.isEmpty();
      } else {
         return !changedItem.isEmpty() && this.items.get(0).test((ItemInstance)changedItem);
      }
   }
}
