package net.minecraft.client.renderer.texture;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.buffers.GpuBufferSlice;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.textures.FilterMode;
import com.mojang.renderpearl.api.textures.GpuSampler;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.List;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderPipelines;

@Environment(EnvType.CLIENT)
public class SpriteContents$AnimationState implements AutoCloseable {
   private int frame;
   private int subFrame;
   private final SpriteContents$AnimatedTexture animationInfo;
   private final Int2ObjectMap<GpuTextureView> frameTexturesByIndex;
   private final GpuBufferSlice[] spriteUbosByMip;
   private boolean isDirty;

   private SpriteContents$AnimationState(
      final SpriteContents$AnimatedTexture this$0,
      final Int2ObjectMap<GpuTextureView> animationInfo,
      final GpuBufferSlice[] frameTexturesByIndex,
      final GpuBufferSlice[] spriteUbosByMip
   ) {
      Objects.requireNonNull(this$0);
      super();
      this.isDirty = true;
      this.animationInfo = animationInfo;
      this.frameTexturesByIndex = frameTexturesByIndex;
      this.spriteUbosByMip = spriteUbosByMip;
   }

   public void tick() {
      this.subFrame++;
      this.isDirty = false;
      SpriteContents$FrameInfo currentFrame = this.animationInfo.frames.get(this.frame);
      if (this.subFrame >= currentFrame.time) {
         int oldFrame = currentFrame.index;
         this.frame = (this.frame + 1) % this.animationInfo.frames.size();
         this.subFrame = 0;
         int newFrame = this.animationInfo.frames.get(this.frame).index;
         if (oldFrame != newFrame) {
            this.isDirty = true;
         }
      }
   }

   public GpuBufferSlice getDrawUbo(final int level) {
      return this.spriteUbosByMip[level];
   }

   public boolean needsToDraw() {
      return this.animationInfo.interpolateFrames || this.isDirty;
   }

   public void drawToAtlas(final RenderPass renderPass, final GpuBufferSlice ubo) {
      GpuSampler sampler = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST, true);
      List<SpriteContents$FrameInfo> frames = this.animationInfo.frames;
      int oldFrame = frames.get(this.frame).index;
      float frameProgress = (float)this.subFrame / this.animationInfo.frames.get(this.frame).time;
      int frameProgressAsInt = (int)(frameProgress * 1000.0F);
      if (this.animationInfo.interpolateFrames) {
         int newFrame = frames.get((this.frame + 1) % frames.size()).index;
         renderPass.setPipeline(RenderSystem.getCompiledPipeline(RenderPipelines.ANIMATE_SPRITE_INTERPOLATE));
         renderPass.setUniform("CurrentSprite", (GpuTextureView)this.frameTexturesByIndex.get(oldFrame), sampler);
         renderPass.setUniform("NextSprite", (GpuTextureView)this.frameTexturesByIndex.get(newFrame), sampler);
      } else if (this.isDirty) {
         renderPass.setPipeline(RenderSystem.getCompiledPipeline(RenderPipelines.ANIMATE_SPRITE_BLIT));
         renderPass.setUniform("Sprite", (GpuTextureView)this.frameTexturesByIndex.get(oldFrame), sampler);
      }

      renderPass.setUniform("SpriteAnimationInfo", ubo);
      renderPass.draw(6, 1, frameProgressAsInt << 3, 0);
   }

   @Override
   public void close() {
      ObjectIterator var1 = this.frameTexturesByIndex.values().iterator();

      while (var1.hasNext()) {
         GpuTextureView view = (GpuTextureView)var1.next();
         view.texture().close();
         view.close();
      }
   }
}
