package net.minecraft.network.chat;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record HoverEvent$ShowEntity(HoverEvent$EntityTooltipInfo entity) implements HoverEvent {
   public static final MapCodec<HoverEvent$ShowEntity> CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(HoverEvent$EntityTooltipInfo.CODEC.forGetter(HoverEvent$ShowEntity::entity)).apply(i, HoverEvent$ShowEntity::new)
   );

   @Override
   public HoverEvent$Action action() {
      return HoverEvent$Action.SHOW_ENTITY;
   }
}
