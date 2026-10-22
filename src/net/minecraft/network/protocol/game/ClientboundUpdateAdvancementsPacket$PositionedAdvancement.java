package net.minecraft.network.protocol.game;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementNode;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record ClientboundUpdateAdvancementsPacket$PositionedAdvancement(AdvancementHolder advancement, float x, float y) {
   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundUpdateAdvancementsPacket$PositionedAdvancement> STREAM_CODEC = StreamCodec.composite(
      AdvancementHolder.STREAM_CODEC,
      ClientboundUpdateAdvancementsPacket$PositionedAdvancement::advancement,
      ByteBufCodecs.FLOAT,
      ClientboundUpdateAdvancementsPacket$PositionedAdvancement::x,
      ByteBufCodecs.FLOAT,
      ClientboundUpdateAdvancementsPacket$PositionedAdvancement::y,
      ClientboundUpdateAdvancementsPacket$PositionedAdvancement::new
   );

   public static ClientboundUpdateAdvancementsPacket$PositionedAdvancement fromNode(final AdvancementNode node) {
      return new ClientboundUpdateAdvancementsPacket$PositionedAdvancement(node.holder(), node.x(), node.y());
   }
}
