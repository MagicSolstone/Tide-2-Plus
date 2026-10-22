package net.minecraft.world.level.storage.loot.providers.number.ints;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import java.util.Optional;
import net.minecraft.core.Holder$Reference;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootContext;

public record ResolvableInt$Reference(ResourceKey<ContextIntProvider> key) implements ResolvableInt {
   private static final Codec<ResolvableInt$Reference> CODEC = ResourceKey.codec(Registries.CONTEXT_INT_PROVIDER)
      .xmap(ResolvableInt$Reference::new, ResolvableInt$Reference::key);
   private static final StreamCodec<ByteBuf, ResolvableInt$Reference> STREAM_CODEC = ResourceKey.streamCodec(Registries.CONTEXT_INT_PROVIDER)
      .map(ResolvableInt$Reference::new, ResolvableInt$Reference::key);

   @Override
   public int get(final LootContext context, final int defaultValue) {
      return this.getProvider(context).map(provider -> provider.getInt(context)).orElse(defaultValue);
   }

   private Optional<ContextIntProvider> getProvider(final LootContext context) {
      return context.getResolver().lookupOrThrow(Registries.CONTEXT_INT_PROVIDER).get(this.key).map(Holder$Reference::value);
   }
}
