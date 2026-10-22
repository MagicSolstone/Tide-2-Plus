package net.minecraft.client.renderer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public record PostChainConfig(Map<Identifier, PostChainConfig$InternalTarget> internalTargets, List<PostChainConfig$Pass> passes) {
   public static final Codec<PostChainConfig> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            Codec.unboundedMap(Identifier.CODEC, PostChainConfig$InternalTarget.CODEC)
               .optionalFieldOf("targets", Map.of())
               .forGetter(PostChainConfig::internalTargets),
            PostChainConfig$Pass.CODEC.listOf().optionalFieldOf("passes", List.of()).forGetter(PostChainConfig::passes)
         )
         .apply(i, PostChainConfig::new)
   );
}
