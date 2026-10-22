package net.minecraft.world.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.codec.RegistryCodecs;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;

public record BlocksAttacks$DamageReduction(float horizontalBlockingAngle, Optional<HolderSet<DamageType>> type, float base, float factor) {
   public static final Codec<BlocksAttacks$DamageReduction> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            ExtraCodecs.POSITIVE_FLOAT.optionalFieldOf("horizontal_blocking_angle", 90.0F).forGetter(BlocksAttacks$DamageReduction::horizontalBlockingAngle),
            RegistryCodecs.holderSet(Registries.DAMAGE_TYPE).optionalFieldOf("type").forGetter(BlocksAttacks$DamageReduction::type),
            Codec.FLOAT.fieldOf("base").forGetter(BlocksAttacks$DamageReduction::base),
            Codec.FLOAT.fieldOf("factor").forGetter(BlocksAttacks$DamageReduction::factor)
         )
         .apply(i, BlocksAttacks$DamageReduction::new)
   );
   public static final StreamCodec<RegistryFriendlyByteBuf, BlocksAttacks$DamageReduction> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.FLOAT,
      BlocksAttacks$DamageReduction::horizontalBlockingAngle,
      ByteBufCodecs.holderSet(Registries.DAMAGE_TYPE).apply(ByteBufCodecs::optional),
      BlocksAttacks$DamageReduction::type,
      ByteBufCodecs.FLOAT,
      BlocksAttacks$DamageReduction::base,
      ByteBufCodecs.FLOAT,
      BlocksAttacks$DamageReduction::factor,
      BlocksAttacks$DamageReduction::new
   );

   public float resolve(final DamageSource source, final float dealtDamage, final double angle) {
      if (angle > (float) (Math.PI / 180.0) * this.horizontalBlockingAngle) {
         return 0.0F;
      } else {
         return this.type.isPresent() && !this.type.get().contains(source.typeHolder())
            ? 0.0F
            : Mth.clamp(this.base + this.factor * dealtDamage, 0.0F, dealtDamage);
      }
   }
}
