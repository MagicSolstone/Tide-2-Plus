package net.minecraft.client.renderer.texture;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.GpuFormat;
import com.mojang.renderpearl.api.buffers.GpuBufferSlice;
import com.mojang.renderpearl.api.commands.CommandEncoder;
import com.mojang.renderpearl.api.device.GpuDevice;
import com.mojang.renderpearl.api.textures.GpuTexture;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
class SpriteContents$AnimatedTexture {
   private final List<SpriteContents$FrameInfo> frames;
   private final IntList uniqueFrames;
   private final int frameRowSize;
   private final boolean interpolateFrames;

   private SpriteContents$AnimatedTexture(
      final List<SpriteContents$FrameInfo> this$0, final int frames, final boolean frameRowSize, final boolean interpolateFrames
   ) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
      this.frames = frames;
      this.frameRowSize = frameRowSize;
      this.interpolateFrames = interpolateFrames;
      this.uniqueFrames = IntArrayList.toList(frames.stream().mapToInt(SpriteContents$FrameInfo::index).distinct());
   }

   private int getFrameX(final int index) {
      return index % this.frameRowSize;
   }

   private int getFrameY(final int index) {
      return index / this.frameRowSize;
   }

   public SpriteContents$AnimationState createAnimationState(final GpuBufferSlice uboSlice, final int spriteUboSize) {
      GpuDevice device = RenderSystem.getDevice();
      Int2ObjectMap<GpuTextureView> frameTexturesByIndex = new Int2ObjectOpenHashMap();
      GpuBufferSlice[] spriteUbosByMip = new GpuBufferSlice[this.this$0.byMipLevel.length];
      CommandEncoder encoder = device.createCommandEncoder();
      List<GpuBufferSlice> stagingBuffers = encoder.transientMemory()
         .multiUploadStaging(Arrays.stream(this.this$0.byMipLevel).map(NativeImage::getPixelBytes).toList(), 1L, 16);

      for (int i = 0; i < this.uniqueFrames.size(); i++) {
         int frame = this.uniqueFrames.getInt(i);
         GpuTexture texture = device.createTexture(
            () -> this.this$0.name + " animation frame " + frame,
            5,
            GpuFormat.RGBA8_UNORM,
            this.this$0.width,
            this.this$0.height,
            1,
            this.this$0.byMipLevel.length
         );
         int offsetX = this.getFrameX(frame) * this.this$0.width;
         int offsetY = this.getFrameY(frame) * this.this$0.height;

         for (int level = 0; level < this.this$0.byMipLevel.length; level++) {
            encoder.copyBufferToTexture(
               stagingBuffers.get(level),
               offsetX >> level,
               offsetY >> level,
               this.this$0.byMipLevel[level].getWidth(),
               this.this$0.byMipLevel[level].getHeight(),
               texture,
               0,
               0,
               this.this$0.width >> level,
               this.this$0.height >> level,
               level,
               0
            );
         }

         frameTexturesByIndex.put(frame, RenderSystem.getDevice().createTextureView(texture));
      }

      for (int level = 0; level < this.this$0.byMipLevel.length; level++) {
         spriteUbosByMip[level] = uboSlice.slice(level * spriteUboSize, spriteUboSize);
      }

      return new SpriteContents$AnimationState(this.this$0, this, frameTexturesByIndex, spriteUbosByMip);
   }

   public IntList getUniqueFrames() {
      return this.uniqueFrames;
   }
}
