package net.minecraft.client.renderer.texture;

import com.mojang.blaze3d.buffers.Std140SizeCalculator;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.platform.Transparency;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.logging.LogUtils;
import com.mojang.renderpearl.api.buffers.GpuBufferSlice;
import com.mojang.renderpearl.api.textures.GpuTexture;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.resources.metadata.animation.AnimationFrame;
import net.minecraft.client.resources.metadata.animation.AnimationMetadataSection;
import net.minecraft.client.resources.metadata.animation.FrameSize;
import net.minecraft.client.resources.metadata.texture.TextureMetadataSection;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.metadata.MetadataSectionType;
import net.minecraft.server.packs.metadata.MetadataSectionType$WithValue;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public class SpriteContents implements AutoCloseable, Stitcher$Entry {
   private static final Logger LOGGER = LogUtils.getLogger();
   public static final int UBO_SIZE = new Std140SizeCalculator().putMat4f().putMat4f().putFloat().putFloat().putInt().get();
   private final Identifier name;
   private final int width;
   private final int height;
   private final NativeImage originalImage;
   private NativeImage[] byMipLevel;
   private final SpriteContents$AnimatedTexture animatedTexture;
   private final List<MetadataSectionType$WithValue<?>> additionalMetadata;
   private final MipmapStrategy mipmapStrategy;
   private final float alphaCutoffBias;
   private final Transparency transparency;

   public SpriteContents(final Identifier name, final FrameSize frameSize, final NativeImage image) {
      this(name, frameSize, image, Optional.empty(), List.of(), Optional.empty());
   }

   public SpriteContents(
      final Identifier name,
      final FrameSize frameSize,
      final NativeImage image,
      final Optional<AnimationMetadataSection> animationInfo,
      final List<MetadataSectionType$WithValue<?>> additionalMetadata,
      final Optional<TextureMetadataSection> textureInfo
   ) {
      this.name = name;
      this.width = frameSize.width();
      this.height = frameSize.height();
      this.additionalMetadata = additionalMetadata;
      this.animatedTexture = animationInfo.<SpriteContents$AnimatedTexture>map(
            animation -> this.createAnimatedTexture(frameSize, image.getWidth(), image.getHeight(), animation)
         )
         .orElse(null);
      this.originalImage = image;
      this.byMipLevel = new NativeImage[]{this.originalImage};
      this.mipmapStrategy = textureInfo.map(TextureMetadataSection::mipmapStrategy).orElse(MipmapStrategy.AUTO);
      this.alphaCutoffBias = textureInfo.map(TextureMetadataSection::alphaCutoffBias).orElse(0.0F);
      this.transparency = image.computeTransparency();
   }

   public void increaseMipLevel(final int mipLevel) {
      try {
         this.byMipLevel = MipmapGenerator.generateMipLevels(this.name, this.byMipLevel, mipLevel, this.mipmapStrategy, this.alphaCutoffBias, this.transparency);
      } catch (Throwable var5) {
         CrashReport report = CrashReport.forThrowable(var5, "Generating mipmaps for frame");
         CrashReportCategory frameCategory = report.addCategory("Frame being iterated");
         frameCategory.setDetail("Sprite name", this.name);
         frameCategory.setDetail("Sprite size", () -> this.width + " x " + this.height);
         frameCategory.setDetail("Sprite frames", () -> this.getFrameCount() + " frames");
         frameCategory.setDetail("Mipmap levels", mipLevel);
         frameCategory.setDetail("Original image size", () -> this.originalImage.getWidth() + "x" + this.originalImage.getHeight());
         throw new ReportedException(report);
      }
   }

   private int getFrameCount() {
      return this.animatedTexture != null ? this.animatedTexture.frames.size() : 1;
   }

   public boolean isAnimated() {
      return this.getFrameCount() > 1;
   }

   public Transparency transparency() {
      return this.transparency;
   }

   private SpriteContents$AnimatedTexture createAnimatedTexture(
      final FrameSize frameSize, final int fullWidth, final int fullHeight, final AnimationMetadataSection metadata
   ) {
      int frameRowSize = fullWidth / frameSize.width();
      int frameColumnSize = fullHeight / frameSize.height();
      int totalFrameCount = frameRowSize * frameColumnSize;
      int defaultFrameTime = metadata.defaultFrameTime();
      List<SpriteContents$FrameInfo> frames;
      if (metadata.frames().isEmpty()) {
         frames = new ArrayList<>(totalFrameCount);

         for (int i = 0; i < totalFrameCount; i++) {
            frames.add(new SpriteContents$FrameInfo(i, defaultFrameTime));
         }
      } else {
         List<AnimationFrame> metadataFrames = metadata.frames().get();
         frames = new ArrayList<>(metadataFrames.size());

         for (AnimationFrame frame : metadataFrames) {
            frames.add(new SpriteContents$FrameInfo(frame.index(), frame.timeOr(defaultFrameTime)));
         }

         int index = 0;
         IntSet usedFrameIndices = new IntOpenHashSet();

         for (Iterator<SpriteContents$FrameInfo> iterator = frames.iterator(); iterator.hasNext(); index++) {
            SpriteContents$FrameInfo frame = iterator.next();
            boolean isValid = true;
            if (frame.time <= 0) {
               LOGGER.warn("Invalid frame duration on sprite {} frame {}: {}", new Object[]{this.name, index, frame.time});
               isValid = false;
            }

            if (frame.index < 0 || frame.index >= totalFrameCount) {
               LOGGER.warn("Invalid frame index on sprite {} frame {}: {}", new Object[]{this.name, index, frame.index});
               isValid = false;
            }

            if (isValid) {
               usedFrameIndices.add(frame.index);
            } else {
               iterator.remove();
            }
         }

         int[] unusedFrameIndices = IntStream.range(0, totalFrameCount).filter(i -> !usedFrameIndices.contains(i)).toArray();
         if (unusedFrameIndices.length > 0) {
            LOGGER.warn("Unused frames in sprite {}: {}", this.name, Arrays.toString(unusedFrameIndices));
         }
      }

      return frames.size() <= 1
         ? null
         : new SpriteContents$AnimatedTexture(this, (int)List.<SpriteContents$FrameInfo>copyOf(frames), (boolean)frameRowSize, metadata.interpolatedFrames());
   }

   @Override
   public int width() {
      return this.width;
   }

   @Override
   public int height() {
      return this.height;
   }

   @Override
   public Identifier name() {
      return this.name;
   }

   public IntList getUniqueFrames() {
      return this.animatedTexture != null ? this.animatedTexture.getUniqueFrames() : IntList.of(1);
   }

   public SpriteContents$AnimationState createAnimationState(final GpuBufferSlice uboSlice, final int spriteUboSize) {
      return this.animatedTexture != null ? this.animatedTexture.createAnimationState(uboSlice, spriteUboSize) : null;
   }

   public <T> Optional<T> getAdditionalMetadata(final MetadataSectionType<T> type) {
      for (MetadataSectionType$WithValue<?> metadata : this.additionalMetadata) {
         Optional<T> result = metadata.unwrapToType(type);
         if (result.isPresent()) {
            return result;
         }
      }

      return Optional.empty();
   }

   @Override
   public void close() {
      for (NativeImage image : this.byMipLevel) {
         image.close();
      }
   }

   @Override
   public String toString() {
      return "SpriteContents{name=" + this.name + ", frameCount=" + this.getFrameCount() + ", height=" + this.height + ", width=" + this.width + "}";
   }

   public boolean isTransparent(final int frame, final int x, final int y) {
      int actualX = x;
      int actualY = y;
      if (this.animatedTexture != null) {
         actualX = x + this.animatedTexture.getFrameX(frame) * this.width;
         actualY = y + this.animatedTexture.getFrameY(frame) * this.height;
      }

      return ARGB.alpha(this.originalImage.getPixel(actualX, actualY)) == 0;
   }

   public Transparency computeTransparency(final float u0, final float v0, final float u1, final float v1) {
      if (this.transparency.isOpaque()) {
         return this.transparency;
      } else if (u0 == 0.0F && v0 == 0.0F && u1 == 1.0F && v1 == 1.0F) {
         return this.transparency;
      } else {
         int x0 = Mth.floor(u0 * this.width);
         int y0 = Mth.floor(v0 * this.height);
         int x1 = Mth.ceil(u1 * this.width);
         int y1 = Mth.ceil(v1 * this.height);
         if (this.animatedTexture == null) {
            return this.originalImage.computeTransparency(x0, y0, x1, y1);
         } else {
            IntList uniqueFrames = this.animatedTexture.uniqueFrames;
            Transparency transparency = Transparency.NONE;

            for (int i = 0; i < uniqueFrames.size(); i++) {
               int frame = uniqueFrames.getInt(i);
               int frameX = this.animatedTexture.getFrameX(frame) * this.width;
               int frameY = this.animatedTexture.getFrameY(frame) * this.height;
               transparency = transparency.or(this.originalImage.computeTransparency(frameX + x0, frameY + y0, frameX + x1, frameY + y1));
            }

            return transparency;
         }
      }
   }

   public void uploadFirstFrame(final GpuTexture destination, final int level) {
      RenderSystem.getDevice().createCommandEncoder().writeToTexture(destination, this.byMipLevel[level], level, 0, 0, 0);
   }
}
