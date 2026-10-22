package net.minecraft.client.data.models.model;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;

@FunctionalInterface
@Environment(EnvType.CLIENT)
public interface TexturedModel$Provider {
   TexturedModel get(final Block block);

   default Identifier create(final Block block, final BiConsumer<Identifier, ModelInstance> modelOutput) {
      return this.get(block).create(block, modelOutput);
   }

   default Identifier createWithSuffix(final Block block, final String suffix, final BiConsumer<Identifier, ModelInstance> modelOutput) {
      return this.get(block).createWithSuffix(block, suffix, modelOutput);
   }

   default TexturedModel$Provider updateTexture(final Consumer<TextureMapping> mutator) {
      return block -> this.get(block).updateTextures(mutator);
   }
}
