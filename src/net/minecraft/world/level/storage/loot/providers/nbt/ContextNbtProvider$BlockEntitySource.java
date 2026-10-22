package net.minecraft.world.level.storage.loot.providers.nbt;

import net.minecraft.nbt.Tag;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.loot.LootContextArg$Getter;

record ContextNbtProvider$BlockEntitySource(ContextKey<? extends BlockEntity> contextParam) implements LootContextArg$Getter<BlockEntity, Tag> {
   public Tag get(final BlockEntity blockEntity) {
      return blockEntity.saveWithFullMetadata(blockEntity.getLevel().registryAccess());
   }
}
