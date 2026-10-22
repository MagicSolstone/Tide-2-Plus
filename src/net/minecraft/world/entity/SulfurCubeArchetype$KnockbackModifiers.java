package net.minecraft.world.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record SulfurCubeArchetype$KnockbackModifiers(float horizontalPower, float verticalPower) {
   public static final Codec<SulfurCubeArchetype$KnockbackModifiers> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            Codec.FLOAT.fieldOf("horizontal_power").forGetter(SulfurCubeArchetype$KnockbackModifiers::horizontalPower),
            Codec.FLOAT.fieldOf("vertical_power").forGetter(SulfurCubeArchetype$KnockbackModifiers::verticalPower)
         )
         .apply(i, SulfurCubeArchetype$KnockbackModifiers::new)
   );
}
