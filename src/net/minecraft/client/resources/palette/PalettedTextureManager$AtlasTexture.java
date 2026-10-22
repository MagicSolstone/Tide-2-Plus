package net.minecraft.client.resources.palette;

import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.GpuFormat;
import com.mojang.renderpearl.api.device.GpuDevice;
import com.mojang.renderpearl.api.textures.FilterMode;
import java.nio.file.Path;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.Dumpable;
import net.minecraft.client.renderer.texture.DynamicAtlasTree;
import net.minecraft.client.renderer.texture.DynamicAtlasTreeSlot;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
class PalettedTextureManager$AtlasTexture extends AbstractTexture implements Dumpable {
   private static final int SIZE = 512;
   private final Identifier location;
   private final DynamicAtlasTree tree = new DynamicAtlasTree(0, 0, 512, 512);

   private PalettedTextureManager$AtlasTexture(final Identifier location) {
      GpuDevice device = RenderSystem.getDevice();
      this.texture = device.createTexture(() -> "Paletted Atlas", 7, GpuFormat.RGBA8_UNORM, 512, 512, 1, 1);
      this.textureView = device.createTextureView(this.texture);
      this.location = location;
      this.sampler = RenderSystem.getSamplerCache().getRepeat(FilterMode.NEAREST);
   }

   @Nullable
   public DynamicAtlasTreeSlot tryAllocateSlot(final int width, final int height) {
      return this.tree.insert(width, height, 0);
   }

   @Override
   public void dumpContents(final Identifier selfId, final Path dir) {
      if (this.texture != null) {
         String outputId = selfId.toDebugFileName();
         TextureUtil.writeAsPNG(dir, outputId, this.texture, 0, argb -> ARGB.alpha(argb) == 0 ? -16777216 : argb);
      }
   }
}
