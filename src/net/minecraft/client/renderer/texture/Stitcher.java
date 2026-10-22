package net.minecraft.client.renderer.texture;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
public class Stitcher<T extends Stitcher$Entry> {
   private static final Comparator<Stitcher$Holder<?>> HOLDER_COMPARATOR = Comparator.<Stitcher$Holder<?>, Integer>comparing(h -> -h.height)
      .thenComparing(h -> -h.width)
      .thenComparing(h -> h.entry.name());
   private final int mipLevel;
   private final List<Stitcher$Holder<T>> texturesToBeStitched = new ArrayList<>();
   private final List<Stitcher$Region<T>> storage = new ArrayList<>();
   private int storageX;
   private int storageY;
   private final int maxWidth;
   private final int maxHeight;
   private final int padding;

   public Stitcher(final int maxWidth, final int maxHeight, final int mipLevel, final int anisotropyBit) {
      this.mipLevel = mipLevel;
      this.maxWidth = maxWidth;
      this.maxHeight = maxHeight;
      this.padding = 1 << mipLevel << Mth.clamp(anisotropyBit - 1, 0, 4);
   }

   public int getWidth() {
      return this.storageX;
   }

   public int getHeight() {
      return this.storageY;
   }

   public void registerSprite(final T entry) {
      Stitcher$Holder<T> holder = new Stitcher$Holder<>(
         entry,
         smallestFittingMinTexel(entry.width() + this.padding * 2, this.mipLevel),
         smallestFittingMinTexel(entry.height() + this.padding * 2, this.mipLevel)
      );
      this.texturesToBeStitched.add(holder);
   }

   public void stitch() {
      List<Stitcher$Holder<T>> holders = new ArrayList<>(this.texturesToBeStitched);
      holders.sort(HOLDER_COMPARATOR);

      for (Stitcher$Holder<T> holder : holders) {
         if (!this.addToStorage(holder)) {
            throw new StitcherException(holder.entry, holders.stream().map(h -> h.entry).collect(ImmutableList.toImmutableList()));
         }
      }
   }

   public void gatherSprites(final Stitcher$SpriteLoader<T> loader) {
      for (Stitcher$Region<T> topRegion : this.storage) {
         topRegion.walk(loader, this.padding);
      }
   }

   private static int smallestFittingMinTexel(final int input, final int maxMipLevel) {
      return (input >> maxMipLevel) + ((input & (1 << maxMipLevel) - 1) == 0 ? 0 : 1) << maxMipLevel;
   }

   private boolean addToStorage(final Stitcher$Holder<T> holder) {
      for (Stitcher$Region<T> region : this.storage) {
         if (region.add(holder)) {
            return true;
         }
      }

      return this.expand(holder);
   }

   private boolean expand(final Stitcher$Holder<T> holder) {
      int xCurrentSize = Mth.smallestEncompassingPowerOfTwo(this.storageX);
      int yCurrentSize = Mth.smallestEncompassingPowerOfTwo(this.storageY);
      int xNewSize = Mth.smallestEncompassingPowerOfTwo(this.storageX + holder.width);
      int yNewSize = Mth.smallestEncompassingPowerOfTwo(this.storageY + holder.height);
      boolean xCanGrow = xNewSize <= this.maxWidth;
      boolean yCanGrow = yNewSize <= this.maxHeight;
      if (!xCanGrow && !yCanGrow) {
         return false;
      } else {
         boolean xWillGrow = xCanGrow && xCurrentSize != xNewSize;
         boolean yWillGrow = yCanGrow && yCurrentSize != yNewSize;
         boolean growOnX;
         if (xWillGrow ^ yWillGrow) {
            growOnX = xWillGrow;
         } else {
            growOnX = xCanGrow && xCurrentSize <= yCurrentSize;
         }

         Stitcher$Region<T> slot;
         if (growOnX) {
            if (this.storageY == 0) {
               this.storageY = yNewSize;
            }

            slot = new Stitcher$Region<>(this.storageX, 0, xNewSize - this.storageX, this.storageY);
            this.storageX = xNewSize;
         } else {
            slot = new Stitcher$Region<>(0, this.storageY, this.storageX, yNewSize - this.storageY);
            this.storageY = yNewSize;
         }

         slot.add(holder);
         this.storage.add(slot);
         return true;
      }
   }
}
