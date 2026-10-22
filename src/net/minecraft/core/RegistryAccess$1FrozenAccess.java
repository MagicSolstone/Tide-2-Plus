package net.minecraft.core;

import java.util.Objects;
import java.util.stream.Stream;

class RegistryAccess$1FrozenAccess extends RegistryAccess$ImmutableRegistryAccess implements RegistryAccess$Frozen {
   protected RegistryAccess$1FrozenAccess(final Stream<RegistryAccess$RegistryEntry<?>> this$0, final Stream entries) {
      Objects.requireNonNull(this$0);
      super(entries);
   }
}
