package net.minecraft.client.renderer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Set;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;

@Environment(EnvType.CLIENT)
public record PostChainConfig$TextureInput(String samplerName, Identifier location, int width, int height, boolean bilinear) implements PostChainConfig$Input {
   public static final Codec<PostChainConfig$TextureInput> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            Codec.STRING.fieldOf("sampler_name").forGetter(PostChainConfig$TextureInput::samplerName),
            Identifier.CODEC.fieldOf("location").forGetter(PostChainConfig$TextureInput::location),
            ExtraCodecs.POSITIVE_INT.fieldOf("width").forGetter(PostChainConfig$TextureInput::width),
            ExtraCodecs.POSITIVE_INT.fieldOf("height").forGetter(PostChainConfig$TextureInput::height),
            Codec.BOOL.optionalFieldOf("bilinear", false).forGetter(PostChainConfig$TextureInput::bilinear)
         )
         .apply(i, PostChainConfig$TextureInput::new)
   );

   @Override
   public Set<Identifier> referencedTargets() {
      return Set.of();
   }
}
