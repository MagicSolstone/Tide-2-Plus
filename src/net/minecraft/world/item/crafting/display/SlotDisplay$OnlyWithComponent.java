package net.minecraft.world.item.crafting.display;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.stream.Stream;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.context.ContextMap;

public record SlotDisplay$OnlyWithComponent(SlotDisplay source, DataComponentType<?> component) implements SlotDisplay {
   public static final MapCodec<SlotDisplay$OnlyWithComponent> MAP_CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            SlotDisplay.CODEC.fieldOf("contents").forGetter(SlotDisplay$OnlyWithComponent::source),
            DataComponentType.CODEC.fieldOf("component").forGetter(SlotDisplay$OnlyWithComponent::component)
         )
         .apply(i, SlotDisplay$OnlyWithComponent::new)
   );
   public static final StreamCodec<RegistryFriendlyByteBuf, SlotDisplay$OnlyWithComponent> STREAM_CODEC = StreamCodec.composite(
      SlotDisplay.STREAM_CODEC,
      SlotDisplay$OnlyWithComponent::source,
      DataComponentType.STREAM_CODEC,
      SlotDisplay$OnlyWithComponent::component,
      SlotDisplay$OnlyWithComponent::new
   );
   public static final SlotDisplay$Type<SlotDisplay$OnlyWithComponent> TYPE = new SlotDisplay$Type<>(MAP_CODEC, STREAM_CODEC);

   @Override
   public <T> Stream<T> resolve(final ContextMap context, final DisplayContentsFactory<T> builder) {
      return builder instanceof DisplayContentsFactory$ForStacks<T> stacks
         ? this.source.resolve(context, SlotDisplay$ItemStackContentsFactory.INSTANCE).filter(s -> s.has(this.component)).map(stacks::forStack)
         : Stream.empty();
   }

   @Override
   public SlotDisplay$Type<SlotDisplay$OnlyWithComponent> type() {
      return TYPE;
   }
}
