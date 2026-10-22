package net.minecraft.world.level.storage.loot;

import net.minecraft.util.StringRepresentable;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public enum LootContext$ItemStackTarget implements StringRepresentable, LootContextArg$SimpleGetter<ItemInstance> {
   TOOL("tool", LootContextParams.TOOL);

   private final String name;
   private final ContextKey<? extends ItemInstance> param;

   private LootContext$ItemStackTarget(final String name, final ContextKey<? extends ItemInstance> param) {
      this.name = name;
      this.param = param;
   }

   @Override
   public ContextKey<? extends ItemInstance> contextParam() {
      return this.param;
   }

   @Override
   public String getSerializedName() {
      return this.name;
   }
}
