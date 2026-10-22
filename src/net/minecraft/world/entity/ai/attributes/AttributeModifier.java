package net.minecraft.world.entity.ai.attributes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

public record AttributeModifier(Identifier id, double amount, AttributeModifier$Operation operation) {
   public static final MapCodec<AttributeModifier> MAP_CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            Identifier.CODEC.fieldOf("id").forGetter(AttributeModifier::id),
            Codec.DOUBLE.fieldOf("amount").forGetter(AttributeModifier::amount),
            AttributeModifier$Operation.CODEC.fieldOf("operation").forGetter(AttributeModifier::operation)
         )
         .apply(i, AttributeModifier::new)
   );
   public static final Codec<AttributeModifier> CODEC = MAP_CODEC.codec();
   public static final StreamCodec<ByteBuf, AttributeModifier> STREAM_CODEC = StreamCodec.composite(
      Identifier.STREAM_CODEC,
      AttributeModifier::id,
      ByteBufCodecs.DOUBLE,
      AttributeModifier::amount,
      AttributeModifier$Operation.STREAM_CODEC,
      AttributeModifier::operation,
      AttributeModifier::new
   );

   public boolean is(final Identifier id) {
      return id.equals(this.id);
   }
}
