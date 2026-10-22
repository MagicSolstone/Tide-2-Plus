package net.minecraft.network.protocol.game;

import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.List;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;

public class ClientboundUpdateAttributesPacket implements Packet<ClientGamePacketListener> {
   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundUpdateAttributesPacket> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.VAR_INT,
      ClientboundUpdateAttributesPacket::getEntityId,
      ClientboundUpdateAttributesPacket$AttributeSnapshot.STREAM_CODEC.apply(ByteBufCodecs.list(128)),
      ClientboundUpdateAttributesPacket::getValues,
      ClientboundUpdateAttributesPacket::new
   );
   private final int entityId;
   private final List<ClientboundUpdateAttributesPacket$AttributeSnapshot> attributes;

   public ClientboundUpdateAttributesPacket(final int entityId, final Collection<AttributeInstance> values) {
      this.entityId = entityId;
      this.attributes = Lists.newArrayList();

      for (AttributeInstance value : values) {
         this.attributes.add(new ClientboundUpdateAttributesPacket$AttributeSnapshot(value.getAttribute(), value.getBaseValue(), value.getModifiers()));
      }
   }

   private ClientboundUpdateAttributesPacket(final int entityId, final List<ClientboundUpdateAttributesPacket$AttributeSnapshot> attributes) {
      this.entityId = entityId;
      this.attributes = attributes;
   }

   @Override
   public PacketType<ClientboundUpdateAttributesPacket> type() {
      return GamePacketTypes.CLIENTBOUND_UPDATE_ATTRIBUTES;
   }

   public void handle(final ClientGamePacketListener listener) {
      listener.handleUpdateAttributes(this);
   }

   public int getEntityId() {
      return this.entityId;
   }

   public List<ClientboundUpdateAttributesPacket$AttributeSnapshot> getValues() {
      return this.attributes;
   }
}
