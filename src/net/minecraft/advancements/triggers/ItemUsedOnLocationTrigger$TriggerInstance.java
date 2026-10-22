package net.minecraft.advancements.triggers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.advancements.predicates.ItemPredicate$Builder;
import net.minecraft.advancements.predicates.LocationPredicate$Builder;
import net.minecraft.advancements.predicates.StatePropertiesPredicate$Builder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Validatable;
import net.minecraft.world.level.storage.loot.ValidationContextSource;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.AllOfCondition;
import net.minecraft.world.level.storage.loot.predicates.LocationCheck;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition$Builder;
import net.minecraft.world.level.storage.loot.predicates.MatchBlock;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;

public record ItemUsedOnLocationTrigger$TriggerInstance(Optional<Holder<LootItemCondition>> player, Optional<Holder<LootItemCondition>> location)
   implements SimpleCriterionTrigger$SimpleInstance {
   public static final Codec<ItemUsedOnLocationTrigger$TriggerInstance> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            LootItemCondition.CODEC.optionalFieldOf("player").forGetter(ItemUsedOnLocationTrigger$TriggerInstance::player),
            LootItemCondition.CODEC.optionalFieldOf("location").forGetter(ItemUsedOnLocationTrigger$TriggerInstance::location)
         )
         .apply(i, ItemUsedOnLocationTrigger$TriggerInstance::new)
   );

   public static Criterion<ItemUsedOnLocationTrigger$TriggerInstance> placedBlock(final HolderGetter<Block> blocks, final Block block) {
      Holder<LootItemCondition> location = Holder.direct(MatchBlock.blockMatches(blocks, block).build());
      return CriteriaTriggers.PLACED_BLOCK.createCriterion(new ItemUsedOnLocationTrigger$TriggerInstance(Optional.empty(), Optional.of(location)));
   }

   public static Criterion<ItemUsedOnLocationTrigger$TriggerInstance> placedBlock(final LootItemCondition$Builder locationCondition) {
      return CriteriaTriggers.PLACED_BLOCK
         .createCriterion(new ItemUsedOnLocationTrigger$TriggerInstance(Optional.empty(), Optional.of(Holder.direct(locationCondition.build()))));
   }

   public static <T extends Comparable<T>> Criterion<ItemUsedOnLocationTrigger$TriggerInstance> placedBlockWithProperties(
      final HolderGetter<Block> blocks, final Block block, final Property<T> property, final String propertyValue
   ) {
      StatePropertiesPredicate$Builder predicateBuilder = StatePropertiesPredicate$Builder.properties().hasProperty(property, propertyValue);
      Holder<LootItemCondition> location = Holder.direct(MatchBlock.blockMatches(blocks, block, predicateBuilder).build());
      return CriteriaTriggers.PLACED_BLOCK.createCriterion(new ItemUsedOnLocationTrigger$TriggerInstance(Optional.empty(), Optional.of(location)));
   }

   public static Criterion<ItemUsedOnLocationTrigger$TriggerInstance> placedBlockWithProperties(
      final HolderGetter<Block> blocks, final Block block, final Property<Boolean> property, final boolean propertyValue
   ) {
      return placedBlockWithProperties(blocks, block, property, String.valueOf(propertyValue));
   }

   public static Criterion<ItemUsedOnLocationTrigger$TriggerInstance> placedBlockWithProperties(
      final HolderGetter<Block> blocks, final Block block, final Property<Integer> property, final int propertyValue
   ) {
      return placedBlockWithProperties(blocks, block, property, String.valueOf(propertyValue));
   }

   public static <T extends Comparable<T> & StringRepresentable> Criterion<ItemUsedOnLocationTrigger$TriggerInstance> placedBlockWithProperties(
      final HolderGetter<Block> blocks, final Block block, final Property<T> properties, final T propertyValue
   ) {
      return placedBlockWithProperties(blocks, block, properties, propertyValue.getSerializedName());
   }

   private static ItemUsedOnLocationTrigger$TriggerInstance itemUsedOnLocation(final LocationPredicate$Builder location, final ItemPredicate$Builder item) {
      Holder<LootItemCondition> predicate = Holder.direct(AllOfCondition.allOf(LocationCheck.checkLocation(location), MatchTool.toolMatches(item)).build());
      return new ItemUsedOnLocationTrigger$TriggerInstance(Optional.empty(), Optional.of(predicate));
   }

   public static Criterion<ItemUsedOnLocationTrigger$TriggerInstance> itemUsedOnBlock(
      final LocationPredicate$Builder location, final ItemPredicate$Builder item
   ) {
      return CriteriaTriggers.ITEM_USED_ON_BLOCK.createCriterion(itemUsedOnLocation(location, item));
   }

   public static Criterion<ItemUsedOnLocationTrigger$TriggerInstance> allayDropItemOnBlock(
      final LocationPredicate$Builder location, final ItemPredicate$Builder item
   ) {
      return CriteriaTriggers.ALLAY_DROP_ITEM_ON_BLOCK.createCriterion(itemUsedOnLocation(location, item));
   }

   public boolean matches(final LootContext locationContext) {
      return this.location.isEmpty() || this.location.get().value().test(locationContext);
   }

   @Override
   public void validate(final ValidationContextSource validator) {
      SimpleCriterionTrigger$SimpleInstance.super.validate(validator);
      Validatable.validateHolder(validator.context(LootContextParamSets.ADVANCEMENT_LOCATION), "location", this.location);
   }
}
