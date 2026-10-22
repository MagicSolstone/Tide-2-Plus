package net.minecraft.world.item.slot;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.codec.RegistryCodecs;

public interface SlotSources {
   Codec<SlotSource> TYPED_CODEC = BuiltInRegistries.SLOT_SOURCE_TYPE.byNameCodec().dispatch(SlotSource::codec, c -> c);
   Codec<SlotSource> DIRECT_CODEC = Codec.lazyInitialized(
      () -> Codec.either(TYPED_CODEC, GroupSlotSource.INLINE_CODEC)
         .xmap(
            typedOrInline -> (SlotSource)typedOrInline.map(e -> e, e -> e),
            slotSource -> slotSource instanceof GroupSlotSource composite ? Either.right(composite) : Either.left(slotSource)
         )
   );
   Codec<Holder<SlotSource>> CODEC = RegistryCodecs.holder(Registries.SLOT_SOURCE, DIRECT_CODEC);
   Codec<HolderSet<SlotSource>> LIST_CODEC = RegistryCodecs.holderSet(Registries.SLOT_SOURCE, TYPED_CODEC);

   static MapCodec<? extends SlotSource> bootstrap(final Registry<MapCodec<? extends SlotSource>> registry) {
      Registry.register(registry, "group", GroupSlotSource.MAP_CODEC);
      Registry.register(registry, "filtered", FilteredSlotSource.MAP_CODEC);
      Registry.register(registry, "limit_slots", LimitSlotSource.MAP_CODEC);
      Registry.register(registry, "slot_range", RangeSlotSource.MAP_CODEC);
      Registry.register(registry, "contents", ContentsSlotSource.MAP_CODEC);
      return Registry.register(registry, "empty", EmptySlotSource.MAP_CODEC);
   }
}
