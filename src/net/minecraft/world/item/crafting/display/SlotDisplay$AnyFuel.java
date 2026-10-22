package net.minecraft.world.item.crafting.display;

import com.mojang.serialization.MapCodec;
import java.util.stream.Stream;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.context.ContextMap;

public class SlotDisplay$AnyFuel implements SlotDisplay {
   public static final SlotDisplay$AnyFuel INSTANCE = new SlotDisplay$AnyFuel();
   public static final MapCodec<SlotDisplay$AnyFuel> MAP_CODEC = MapCodec.unit(INSTANCE);
   public static final StreamCodec<RegistryFriendlyByteBuf, SlotDisplay$AnyFuel> STREAM_CODEC = StreamCodec.unit(INSTANCE);
   public static final SlotDisplay$Type<SlotDisplay$AnyFuel> TYPE = new SlotDisplay$Type<>(MAP_CODEC, STREAM_CODEC);

   private SlotDisplay$AnyFuel() {
   }

   @Override
   public SlotDisplay$Type<SlotDisplay$AnyFuel> type() {
      return TYPE;
   }

   @Override
   public String toString() {
      return "<any fuel>";
   }

   @Override
   public <T> Stream<T> resolve(final ContextMap context, final DisplayContentsFactory<T> factory) {
      if (factory instanceof DisplayContentsFactory$ForStacks<T> stacks) {
         RegistryAccess registries = context.get(SlotDisplayContext.REGISTRIES);
         if (registries != null) {
            return registries.lookupOrThrow(Registries.ITEM).componentLookup().findAll(DataComponents.COOKING_FUEL).stream().map(stacks::forStack);
         }
      }

      return Stream.empty();
   }
}
