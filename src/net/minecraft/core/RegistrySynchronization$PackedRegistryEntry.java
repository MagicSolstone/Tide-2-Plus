package net.minecraft.core;

import io.netty.buffer.ByteBuf;
import java.util.Optional;
import net.minecraft.nbt.Tag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

public record RegistrySynchronization$PackedRegistryEntry(Identifier id, Optional<Tag> data) {
   public static final StreamCodec<ByteBuf, RegistrySynchronization$PackedRegistryEntry> STREAM_CODEC = StreamCodec.composite(
      Identifier.STREAM_CODEC,
      RegistrySynchronization$PackedRegistryEntry::id,
      ByteBufCodecs.TAG.apply(ByteBufCodecs::optional),
      RegistrySynchronization$PackedRegistryEntry::data,
      RegistrySynchronization$PackedRegistryEntry::new
   );
}
