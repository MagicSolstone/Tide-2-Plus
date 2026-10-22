package net.minecraft.advancements.triggers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.advancements.predicates.StatePropertiesPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.codec.RegistryCodecs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public record EnterBlockTrigger$TriggerInstance(
   Optional<Holder<LootItemCondition>> player, Optional<HolderSet<Block>> block, Optional<StatePropertiesPredicate> state
) implements SimpleCriterionTrigger$SimpleInstance {
   public static final Codec<EnterBlockTrigger$TriggerInstance> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            LootItemCondition.CODEC.optionalFieldOf("player").forGetter(EnterBlockTrigger$TriggerInstance::player),
            RegistryCodecs.holderSet(Registries.BLOCK).optionalFieldOf("blocks").forGetter(EnterBlockTrigger$TriggerInstance::block),
            StatePropertiesPredicate.CODEC.optionalFieldOf("state").forGetter(EnterBlockTrigger$TriggerInstance::state)
         )
         .apply(i, EnterBlockTrigger$TriggerInstance::new)
   );

   public static Criterion<EnterBlockTrigger$TriggerInstance> entersBlock(final HolderGetter<Block> blocks, final Block block) {
      return entersBlock(HolderSet.direct(block.builtInRegistryHolder()));
   }

   public static Criterion<EnterBlockTrigger$TriggerInstance> entersBlock(final HolderSet<Block> block) {
      return CriteriaTriggers.ENTER_BLOCK.createCriterion(new EnterBlockTrigger$TriggerInstance(Optional.empty(), Optional.of(block), Optional.empty()));
   }

   public boolean matches(final BlockState state) {
      return this.block.isPresent() && !state.is(this.block.get()) ? false : !this.state.isPresent() || this.state.get().matches(state);
   }
}
