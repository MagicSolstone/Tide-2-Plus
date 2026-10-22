package net.minecraft.server.packs.metadata;

import java.util.Optional;

public record MetadataSectionType$WithValue<T>(MetadataSectionType<T> type, T value) {
   public <U> Optional<U> unwrapToType(final MetadataSectionType<U> type) {
      return type == this.type ? Optional.of(this.value) : Optional.empty();
   }
}
