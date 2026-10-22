package net.minecraft.client.renderer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public record PostChainConfig$Pass(
   Identifier vertexShaderId,
   Identifier fragmentShaderId,
   List<PostChainConfig$Input> inputs,
   Identifier outputTarget,
   Map<String, List<UniformValue>> uniforms
) {
   private static final Codec<List<PostChainConfig$Input>> INPUTS_CODEC = PostChainConfig$Input.CODEC.listOf().validate(inputs -> {
      Set<String> samplerName = new ObjectArraySet(inputs.size());

      for (PostChainConfig$Input input : inputs) {
         if (!samplerName.add(input.samplerName())) {
            return DataResult.error(() -> "Encountered repeated sampler name: " + input.samplerName());
         }
      }

      return DataResult.success(inputs);
   });
   private static final Codec<Map<String, List<UniformValue>>> UNIFORM_BLOCKS_CODEC = Codec.unboundedMap(Codec.STRING, UniformValue.CODEC.listOf());
   public static final Codec<PostChainConfig$Pass> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            Identifier.CODEC.fieldOf("vertex_shader").forGetter(PostChainConfig$Pass::vertexShaderId),
            Identifier.CODEC.fieldOf("fragment_shader").forGetter(PostChainConfig$Pass::fragmentShaderId),
            INPUTS_CODEC.optionalFieldOf("inputs", List.of()).forGetter(PostChainConfig$Pass::inputs),
            Identifier.CODEC.fieldOf("output").forGetter(PostChainConfig$Pass::outputTarget),
            UNIFORM_BLOCKS_CODEC.optionalFieldOf("uniforms", Map.of()).forGetter(PostChainConfig$Pass::uniforms)
         )
         .apply(i, PostChainConfig$Pass::new)
   );

   public Stream<Identifier> referencedTargets() {
      Stream<Identifier> inputTargets = this.inputs.stream().flatMap(input -> input.referencedTargets().stream());
      return Stream.concat(inputTargets, Stream.of(this.outputTarget));
   }
}
