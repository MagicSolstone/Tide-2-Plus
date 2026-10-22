package net.minecraft.client.renderer;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import java.util.Set;
import java.util.function.Function;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public sealed interface PostChainConfig$Input permits PostChainConfig$TargetInput, PostChainConfig$TextureInput {
   Codec<PostChainConfig$Input> CODEC = Codec.xor(PostChainConfig$TextureInput.CODEC, PostChainConfig$TargetInput.CODEC)
      .xmap(either -> (PostChainConfig$Input)either.map(Function.identity(), Function.identity()), input -> {
         return switch (input) {
            case PostChainConfig$TextureInput texture -> Either.left(texture);
            case PostChainConfig$TargetInput target -> Either.right(target);
            default -> throw new MatchException(null, null);
         };
      });

   String samplerName();

   Set<Identifier> referencedTargets();
}
