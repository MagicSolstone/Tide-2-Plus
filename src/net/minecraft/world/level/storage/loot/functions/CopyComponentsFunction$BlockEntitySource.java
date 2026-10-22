package net.minecraft.world.level.storage.loot.functions;

import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.loot.LootContextArg$Getter;

record CopyComponentsFunction$BlockEntitySource(ContextKey<? extends BlockEntity> contextParam)
   implements LootContextArg$Getter<BlockEntity, DataComponentGetter> {
   public DataComponentGetter get(final BlockEntity blockEntity) {
      return blockEntity.collectComponents();
   }
}
