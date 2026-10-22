package net.minecraft.world.level.storage.loot.providers.number.floats;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import java.util.Optional;
import net.minecraft.core.Holder$Reference;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootContext;

public record ResolvableFloat$Reference(ResourceKey<ContextFloatProvider> key) implements ResolvableFloat {
   private static final Codec<ResolvableFloat$Reference> CODEC = ResourceKey.codec(Registries.CONTEXT_FLOAT_PROVIDER)
      .xmap(ResolvableFloat$Reference::new, ResolvableFloat$Reference::key);
   private static final StreamCodec<ByteBuf, ResolvableFloat$Reference> STREAM_CODEC = ResourceKey.streamCodec(Registries.CONTEXT_FLOAT_PROVIDER)
      .map(ResolvableFloat$Reference::new, ResolvableFloat$Reference::key);

   @Override
   public float get(final LootContext context, final float defaultValue) {
      return this.getProvider(context).map(provider -> provider.getFloat(context)).orElse(defaultValue);
   }

   private Optional<ContextFloatProvider> getProvider(final LootContext context) {
      return context.getResolver().lookupOrThrow(Registries.CONTEXT_FLOAT_PROVIDER).get(this.key).map(Holder$Reference::value);
   }
}
