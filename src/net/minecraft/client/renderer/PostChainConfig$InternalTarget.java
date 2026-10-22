package net.minecraft.client.renderer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.ExtraCodecs;

@Environment(EnvType.CLIENT)
public record PostChainConfig$InternalTarget(Optional<Integer> width, Optional<Integer> height, boolean persistent, int clearColor) {
   public static final Codec<PostChainConfig$InternalTarget> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            ExtraCodecs.POSITIVE_INT.optionalFieldOf("width").forGetter(PostChainConfig$InternalTarget::width),
            ExtraCodecs.POSITIVE_INT.optionalFieldOf("height").forGetter(PostChainConfig$InternalTarget::height),
            Codec.BOOL.optionalFieldOf("persistent", false).forGetter(PostChainConfig$InternalTarget::persistent),
            ExtraCodecs.ARGB_COLOR_CODEC.optionalFieldOf("clear_color", 0).forGetter(PostChainConfig$InternalTarget::clearColor)
         )
         .apply(i, PostChainConfig$InternalTarget::new)
   );
}
