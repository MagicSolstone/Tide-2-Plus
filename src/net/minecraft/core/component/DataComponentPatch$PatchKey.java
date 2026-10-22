package net.minecraft.core.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

record DataComponentPatch$PatchKey(DataComponentType<?> type, boolean removed) {
   public static final Codec<DataComponentPatch$PatchKey> CODEC = Codec.STRING
      .flatXmap(
         string -> {
            boolean removed = string.startsWith("!");
            if (removed) {
               string = string.substring("!".length());
            }

            Identifier id = Identifier.tryParse(string);
            DataComponentType<?> type = BuiltInRegistries.DATA_COMPONENT_TYPE.getValue(id);
            if (type == null) {
               return DataResult.error(() -> "No component with type: '" + id + "'");
            } else {
               return type.isTransient()
                  ? DataResult.error(() -> "'" + id + "' is not a persistent component")
                  : DataResult.success(new DataComponentPatch$PatchKey(type, removed));
            }
         },
         key -> {
            DataComponentType<?> type = key.type();
            Identifier id = BuiltInRegistries.DATA_COMPONENT_TYPE.getKey(type);
            return id == null ? DataResult.error(() -> "Unregistered component: " + type) : DataResult.success(key.removed() ? "!" + id : id.toString());
         }
      );

   public Codec<?> valueCodec() {
      return this.removed ? Removed.CODEC : this.type.codecOrThrow();
   }
}
