package net.minecraft.client.renderer.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public record ClientItem$Properties(boolean handAnimationOnSwap, boolean oversizedInGui, float swapAnimationScale) {
   public static final ClientItem$Properties DEFAULT = new ClientItem$Properties(true, false, 1.0F);
   public static final MapCodec<ClientItem$Properties> MAP_CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            Codec.BOOL.optionalFieldOf("hand_animation_on_swap", true).forGetter(ClientItem$Properties::handAnimationOnSwap),
            Codec.BOOL.optionalFieldOf("oversized_in_gui", false).forGetter(ClientItem$Properties::oversizedInGui),
            Codec.FLOAT.optionalFieldOf("swap_animation_scale", 1.0F).forGetter(ClientItem$Properties::swapAnimationScale)
         )
         .apply(i, ClientItem$Properties::new)
   );
}
