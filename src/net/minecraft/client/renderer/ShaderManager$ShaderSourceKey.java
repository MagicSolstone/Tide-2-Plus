package net.minecraft.client.renderer;

import com.mojang.renderpearl.api.pipeline.ShaderType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
record ShaderManager$ShaderSourceKey(Identifier id, ShaderType type) {
   @Override
   public String toString() {
      return this.id + " (" + this.type + ")";
   }
}
