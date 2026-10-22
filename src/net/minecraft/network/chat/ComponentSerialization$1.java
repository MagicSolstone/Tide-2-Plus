package net.minecraft.network.chat;

import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.RegistryOps;
import net.minecraft.util.GsonHelper;

class ComponentSerialization$1 implements Codec<Component> {
   ComponentSerialization$1(final int val$maxFlatSize) {
      this.val$maxFlatSize = val$maxFlatSizex;
   }

   public <T> DataResult<Pair<Component, T>> decode(final DynamicOps<T> ops, final T input) {
      return ComponentSerialization.CODEC
         .decode(ops, input)
         .flatMap(
            pair -> this.isTooLarge(ops, (Component)pair.getFirst())
               ? DataResult.error(() -> "Component was too large: greater than max size " + maxFlatSize)
               : DataResult.success(pair)
         );
   }

   public <T> DataResult<T> encode(final Component input, final DynamicOps<T> ops, final T prefix) {
      return ComponentSerialization.CODEC.encodeStart(ops, input);
   }

   private <T> boolean isTooLarge(final DynamicOps<T> ops, final Component input) {
      DataResult<JsonElement> json = ComponentSerialization.CODEC.encodeStart(asJsonOps(ops), input);
      return json.isSuccess() && GsonHelper.encodesLongerThan((JsonElement)json.getOrThrow(), this.val$maxFlatSize);
   }

   private static <T> DynamicOps<JsonElement> asJsonOps(final DynamicOps<T> ops) {
      return (DynamicOps<JsonElement>)(ops instanceof RegistryOps<T> registryOps ? registryOps.withParent(JsonOps.INSTANCE) : JsonOps.INSTANCE);
   }
}
