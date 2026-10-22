package net.minecraft.world.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Mth;

public record BlocksAttacks$ItemDamageFunction(float threshold, float base, float factor) {
   public static final Codec<BlocksAttacks$ItemDamageFunction> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            ExtraCodecs.NON_NEGATIVE_FLOAT.fieldOf("threshold").forGetter(BlocksAttacks$ItemDamageFunction::threshold),
            Codec.FLOAT.fieldOf("base").forGetter(BlocksAttacks$ItemDamageFunction::base),
            Codec.FLOAT.fieldOf("factor").forGetter(BlocksAttacks$ItemDamageFunction::factor)
         )
         .apply(i, BlocksAttacks$ItemDamageFunction::new)
   );
   public static final StreamCodec<ByteBuf, BlocksAttacks$ItemDamageFunction> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.FLOAT,
      BlocksAttacks$ItemDamageFunction::threshold,
      ByteBufCodecs.FLOAT,
      BlocksAttacks$ItemDamageFunction::base,
      ByteBufCodecs.FLOAT,
      BlocksAttacks$ItemDamageFunction::factor,
      BlocksAttacks$ItemDamageFunction::new
   );
   public static final BlocksAttacks$ItemDamageFunction DEFAULT = new BlocksAttacks$ItemDamageFunction(1.0F, 0.0F, 1.0F);

   public int apply(final float dealtDamage) {
      return dealtDamage < this.threshold ? 0 : Mth.floor(this.base + this.factor * dealtDamage);
   }
}
