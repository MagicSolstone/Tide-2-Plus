package net.minecraft.world.level.storage.loot.functions;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.Property;

public class CopyBlockState$Builder extends LootItemConditionalFunction$Builder<CopyBlockState$Builder> {
   private final Holder<Block> block;
   private final Builder<Property<?>> properties = ImmutableSet.builder();

   private CopyBlockState$Builder(final Block block) {
      this.block = block.builtInRegistryHolder();
   }

   public CopyBlockState$Builder copy(final Property<?> property) {
      if (!this.block.value().getStateDefinition().getProperties().contains(property)) {
         throw new IllegalStateException("Property " + property + " is not present on block " + this.block);
      } else {
         this.properties.add(property);
         return this;
      }
   }

   protected CopyBlockState$Builder getThis() {
      return this;
   }

   @Override
   public LootItemFunction build() {
      return new CopyBlockState(this.getCondition(), this.block, this.properties.build());
   }
}
