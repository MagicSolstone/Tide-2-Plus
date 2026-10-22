package net.minecraft.client.renderer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Set;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public record PostChainConfig$TargetInput(String samplerName, Identifier targetId, boolean useDepthBuffer, boolean bilinear) implements PostChainConfig$Input {
   public static final Codec<PostChainConfig$TargetInput> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            Codec.STRING.fieldOf("sampler_name").forGetter(PostChainConfig$TargetInput::samplerName),
            Identifier.CODEC.fieldOf("target").forGetter(PostChainConfig$TargetInput::targetId),
            Codec.BOOL.optionalFieldOf("use_depth_buffer", false).forGetter(PostChainConfig$TargetInput::useDepthBuffer),
            Codec.BOOL.optionalFieldOf("bilinear", false).forGetter(PostChainConfig$TargetInput::bilinear)
         )
         .apply(i, PostChainConfig$TargetInput::new)
   );

   @Override
   public Set<Identifier> referencedTargets() {
      return Set.of(this.targetId);
   }
}
