package net.minecraft.world.level.storage;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

class CommandStorage$Container extends SavedData {
   public static final Codec<CommandStorage$Container> CODEC = RecordCodecBuilder.create(
      i -> i.group(Codec.unboundedMap(ExtraCodecs.RESOURCE_PATH_CODEC, CompoundTag.CODEC).fieldOf("contents").forGetter(container -> container.storage))
         .apply(i, CommandStorage$Container::new)
   );
   private final Map<String, CompoundTag> storage;

   private CommandStorage$Container(final Map<String, CompoundTag> storage) {
      this.storage = new HashMap<>(storage);
   }

   private CommandStorage$Container() {
      this(new HashMap<>());
   }

   public static SavedDataType<CommandStorage$Container> type(final String namespace) {
      return new SavedDataType<>(
         Identifier.fromNamespaceAndPath(namespace, "command_storage"), CommandStorage$Container::new, CODEC, DataFixTypes.SAVED_DATA_COMMAND_STORAGE
      );
   }

   public CompoundTag get(final String id) {
      CompoundTag result = this.storage.get(id);
      return result != null ? result : new CompoundTag();
   }

   public void put(final String id, final CompoundTag contents) {
      if (contents.isEmpty()) {
         this.storage.remove(id);
      } else {
         this.storage.put(id, contents);
      }

      this.setDirty();
   }

   public Stream<Identifier> getKeys(final String namespace) {
      return this.storage.keySet().stream().map(p -> Identifier.fromNamespaceAndPath(namespace, p));
   }
}
