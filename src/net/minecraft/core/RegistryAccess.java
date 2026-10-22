package net.minecraft.core;

import com.mojang.logging.LogUtils;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import net.minecraft.resources.ResourceKey;
import org.slf4j.Logger;

public interface RegistryAccess extends HolderLookup$Provider {
   Logger LOGGER = LogUtils.getLogger();
   RegistryAccess$Frozen EMPTY = new RegistryAccess$ImmutableRegistryAccess(Map.of()).freeze();

   @Override
   <E> Optional<Registry<E>> lookup(final ResourceKey<? extends Registry<? extends E>> registryKey);

   default <E> Registry<E> lookupOrThrow(final ResourceKey<? extends Registry<? extends E>> name) {
      return this.lookup(name).orElseThrow(() -> new IllegalStateException("Missing registry: " + name));
   }

   Stream<RegistryAccess$RegistryEntry<?>> registries();

   @Override
   default Stream<ResourceKey<? extends Registry<?>>> listRegistryKeys() {
      return this.registries().map(e -> e.key);
   }

   static RegistryAccess$Frozen fromRegistryOfRegistries(final Registry<? extends Registry<?>> registries) {
      return new RegistryAccess$1(registries);
   }

   default RegistryAccess$Frozen freeze() {
      return new RegistryAccess$1FrozenAccess(this, this.registries().map(RegistryAccess$RegistryEntry::freeze));
   }
}
