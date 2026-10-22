package net.minecraft.tags;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.core.Registry;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

public record TagNetworkSerialization$NetworkPayload(Map<Identifier, IntList> tags) {
   public static final TagNetworkSerialization$NetworkPayload EMPTY = new TagNetworkSerialization$NetworkPayload(Map.of());
   private static final StreamCodec<ByteBuf, IntList> ID_LIST_STREAM_CODEC = ByteBufCodecs.VAR_INT.apply(ByteBufCodecs.collection(IntArrayList::new));
   public static final StreamCodec<ByteBuf, TagNetworkSerialization$NetworkPayload> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.map(HashMap::new, Identifier.STREAM_CODEC, ID_LIST_STREAM_CODEC),
      TagNetworkSerialization$NetworkPayload::tags,
      TagNetworkSerialization$NetworkPayload::new
   );

   public boolean isEmpty() {
      return this.tags.isEmpty();
   }

   public int size() {
      return this.tags.size();
   }

   public <T> TagLoader$LoadResult<T> resolve(final Registry<T> registry) {
      return TagNetworkSerialization.deserializeTagsFromNetwork(registry, this);
   }
}
