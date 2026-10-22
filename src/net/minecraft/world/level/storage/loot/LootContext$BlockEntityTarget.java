package net.minecraft.world.level.storage.loot;

import net.minecraft.util.StringRepresentable;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public enum LootContext$BlockEntityTarget implements StringRepresentable, LootContextArg$SimpleGetter<BlockEntity> {
   BLOCK_ENTITY("block_entity", LootContextParams.BLOCK_ENTITY);

   private final String name;
   private final ContextKey<? extends BlockEntity> param;

   private LootContext$BlockEntityTarget(final String name, final ContextKey<? extends BlockEntity> param) {
      this.name = name;
      this.param = param;
   }

   @Override
   public ContextKey<? extends BlockEntity> contextParam() {
      return this.param;
   }

   @Override
   public String getSerializedName() {
      return this.name;
   }
}
