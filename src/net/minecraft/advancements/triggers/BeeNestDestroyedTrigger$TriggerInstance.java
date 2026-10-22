package net.minecraft.advancements.triggers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.advancements.predicates.ItemPredicate$Builder;
import net.minecraft.advancements.predicates.MinMaxBounds$Ints;
import net.minecraft.advancements.predicates.StatePropertiesPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.codec.RegistryCodecs;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public record BeeNestDestroyedTrigger$TriggerInstance(
   Optional<Holder<LootItemCondition>> player,
   Optional<HolderSet<Block>> block,
   Optional<StatePropertiesPredicate> state,
   Optional<ItemPredicate> item,
   MinMaxBounds$Ints beesInside
) implements SimpleCriterionTrigger$SimpleInstance {
   public static final Codec<BeeNestDestroyedTrigger$TriggerInstance> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            LootItemCondition.CODEC.optionalFieldOf("player").forGetter(BeeNestDestroyedTrigger$TriggerInstance::player),
            RegistryCodecs.holderSet(Registries.BLOCK).optionalFieldOf("blocks").forGetter(BeeNestDestroyedTrigger$TriggerInstance::block),
            StatePropertiesPredicate.CODEC.optionalFieldOf("state").forGetter(BeeNestDestroyedTrigger$TriggerInstance::state),
            ItemPredicate.CODEC.optionalFieldOf("item").forGetter(BeeNestDestroyedTrigger$TriggerInstance::item),
            MinMaxBounds$Ints.CODEC.optionalFieldOf("num_bees_inside", MinMaxBounds$Ints.ANY).forGetter(BeeNestDestroyedTrigger$TriggerInstance::beesInside)
         )
         .apply(i, BeeNestDestroyedTrigger$TriggerInstance::new)
   );

   public static Criterion<BeeNestDestroyedTrigger$TriggerInstance> destroyedBeeNest(
      final HolderGetter<Block> blocks, final Block block, final ItemPredicate$Builder itemPredicate, final MinMaxBounds$Ints numBeesInside
   ) {
      return destroyedBeeNest(HolderSet.direct(block.builtInRegistryHolder()), itemPredicate, numBeesInside);
   }

   public static Criterion<BeeNestDestroyedTrigger$TriggerInstance> destroyedBeeNest(
      final HolderSet<Block> block, final ItemPredicate$Builder itemPredicate, final MinMaxBounds$Ints numBeesInside
   ) {
      return CriteriaTriggers.BEE_NEST_DESTROYED
         .createCriterion(
            new BeeNestDestroyedTrigger$TriggerInstance(
               Optional.empty(), Optional.of(block), Optional.empty(), Optional.of(itemPredicate.build()), numBeesInside
            )
         );
   }

   public boolean matches(final BlockState state, final ItemStack itemStack, final int numBeesInside) {
      if (this.block.isPresent() && !state.is(this.block.get())) {
         return false;
      } else if (this.state.isPresent() && !this.state.get().matches(state)) {
         return false;
      } else {
         return this.item.isPresent() && !this.item.get().test((ItemInstance)itemStack) ? false : this.beesInside.matches(numBeesInside);
      }
   }
}
