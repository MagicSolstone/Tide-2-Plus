package net.minecraft.client.renderer.block.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class BlockDisplayContext {
   private BlockDisplayContext() {
   }

   public static BlockDisplayContext create() {
      return new BlockDisplayContext();
   }
}
