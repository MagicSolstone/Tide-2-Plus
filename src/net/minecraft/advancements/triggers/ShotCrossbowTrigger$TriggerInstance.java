package net.minecraft.advancements.triggers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.advancements.predicates.ItemPredicate$Builder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public record ShotCrossbowTrigger$TriggerInstance(Optional<Holder<LootItemCondition>> player, Optional<ItemPredicate> item)
   implements SimpleCriterionTrigger$SimpleInstance {
   public static final Codec<ShotCrossbowTrigger$TriggerInstance> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            LootItemCondition.CODEC.optionalFieldOf("player").forGetter(ShotCrossbowTrigger$TriggerInstance::player),
            ItemPredicate.CODEC.optionalFieldOf("item").forGetter(ShotCrossbowTrigger$TriggerInstance::item)
         )
         .apply(i, ShotCrossbowTrigger$TriggerInstance::new)
   );

   public static Criterion<ShotCrossbowTrigger$TriggerInstance> shotCrossbow(final Optional<ItemPredicate> item) {
      return CriteriaTriggers.SHOT_CROSSBOW.createCriterion(new ShotCrossbowTrigger$TriggerInstance(Optional.empty(), item));
   }

   public static Criterion<ShotCrossbowTrigger$TriggerInstance> shotCrossbow(final HolderGetter<Item> items, final ItemLike itemlike) {
      return CriteriaTriggers.SHOT_CROSSBOW
         .createCriterion(new ShotCrossbowTrigger$TriggerInstance(Optional.empty(), Optional.of(ItemPredicate$Builder.item().of(items, itemlike).build())));
   }

   public boolean matches(final ItemStack itemStack) {
      return this.item.isEmpty() || this.item.get().test((ItemInstance)itemStack);
   }
}
