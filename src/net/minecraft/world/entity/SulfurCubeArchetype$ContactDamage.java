package net.minecraft.world.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.FloatProviders;
import net.minecraft.world.damagesource.DamageType;

public record SulfurCubeArchetype$ContactDamage(Holder<DamageType> damageType, FloatProvider amount, boolean attributeToSource) {
   public static final Codec<SulfurCubeArchetype$ContactDamage> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            DamageType.CODEC.fieldOf("damage_type").forGetter(SulfurCubeArchetype$ContactDamage::damageType),
            FloatProviders.codec(0.0F).fieldOf("amount").forGetter(SulfurCubeArchetype$ContactDamage::amount),
            Codec.BOOL.fieldOf("attribute_to_source").forGetter(SulfurCubeArchetype$ContactDamage::attributeToSource)
         )
         .apply(i, SulfurCubeArchetype$ContactDamage::new)
   );
}
