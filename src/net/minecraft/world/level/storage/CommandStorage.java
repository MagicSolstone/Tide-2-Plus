package net.minecraft.world.level.storage;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

public class CommandStorage {
   private static final String COMMAND_STORAGE = "command_storage";
   private final Map<String, CommandStorage$Container> namespaces = new HashMap<>();
   private final SavedDataStorage savedDataStorage;

   public CommandStorage(final SavedDataStorage savedDataStorage) {
      this.savedDataStorage = savedDataStorage;
   }

   public CompoundTag get(final Identifier id) {
      CommandStorage$Container container = this.getContainer(id.getNamespace());
      return container != null ? container.get(id.getPath()) : new CompoundTag();
   }

   @Nullable
   private CommandStorage$Container getContainer(final String namespace) {
      CommandStorage$Container container = this.namespaces.get(namespace);
      if (container != null) {
         return container;
      } else {
         CommandStorage$Container newContainer = this.savedDataStorage.get(CommandStorage$Container.type(namespace));
         if (newContainer != null) {
            this.namespaces.put(namespace, newContainer);
         }

         return newContainer;
      }
   }

   private CommandStorage$Container getOrCreateContainer(final String namespace) {
      CommandStorage$Container container = this.namespaces.get(namespace);
      if (container != null) {
         return container;
      } else {
         CommandStorage$Container newContainer = this.savedDataStorage.computeIfAbsent(CommandStorage$Container.type(namespace));
         this.namespaces.put(namespace, newContainer);
         return newContainer;
      }
   }

   public void set(final Identifier id, final CompoundTag contents) {
      this.getOrCreateContainer(id.getNamespace()).put(id.getPath(), contents);
   }

   public Stream<Identifier> keys() {
      return this.namespaces.entrySet().stream().flatMap(e -> e.getValue().getKeys(e.getKey()));
   }
}
