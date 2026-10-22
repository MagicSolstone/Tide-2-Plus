package net.minecraft.util.datafix.schemas;

import com.mojang.datafixers.types.templates.Hook.HookFunction;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;

class V99$1 implements HookFunction {
   public <T> T apply(final DynamicOps<T> ops, final T value) {
      return V99.addNames(new Dynamic(ops, value), V99.ITEM_TO_BLOCKENTITY, V99.ITEM_TO_ENTITY);
   }
}
