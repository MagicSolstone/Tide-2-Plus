package net.minecraft.network.protocol.game;

import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.Collection;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier$Operation;

public record ClientboundUpdateAttributesPacket$AttributeSnapshot(Holder<Attribute> attribute, double base, Collection<AttributeModifier> modifiers) {
   public static final StreamCodec<ByteBuf, AttributeModifier> MODIFIER_STREAM_CODEC = StreamCodec.composite(
      Identifier.STREAM_CODEC,
      AttributeModifier::id,
      ByteBufCodecs.DOUBLE,
      AttributeModifier::amount,
      AttributeModifier$Operation.STREAM_CODEC,
      AttributeModifier::operation,
      AttributeModifier::new
   );
   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundUpdateAttributesPacket$AttributeSnapshot> STREAM_CODEC = StreamCodec.composite(
      Attribute.STREAM_CODEC,
      ClientboundUpdateAttributesPacket$AttributeSnapshot::attribute,
      ByteBufCodecs.DOUBLE,
      ClientboundUpdateAttributesPacket$AttributeSnapshot::base,
      MODIFIER_STREAM_CODEC.apply(ByteBufCodecs.collection(ArrayList::new)),
      ClientboundUpdateAttributesPacket$AttributeSnapshot::modifiers,
      ClientboundUpdateAttributesPacket$AttributeSnapshot::new
   );
}
