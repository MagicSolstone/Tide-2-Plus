package net.minecraft.client.renderer.texture;

import java.util.ArrayList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class Stitcher$Region<T extends Stitcher$Entry> {
   private final int originX;
   private final int originY;
   private final int width;
   private final int height;
   @Nullable
   private List<Stitcher$Region<T>> subSlots;
   @Nullable
   private Stitcher$Holder<T> holder;

   public Stitcher$Region(final int originX, final int originY, final int width, final int height) {
      this.originX = originX;
      this.originY = originY;
      this.width = width;
      this.height = height;
   }

   public int getX() {
      return this.originX;
   }

   public int getY() {
      return this.originY;
   }

   public boolean add(final Stitcher$Holder<T> holder) {
      if (this.holder != null) {
         return false;
      } else {
         int textureWidth = holder.width;
         int textureHeight = holder.height;
         if (textureWidth <= this.width && textureHeight <= this.height) {
            if (textureWidth == this.width && textureHeight == this.height) {
               this.holder = holder;
               return true;
            } else {
               if (this.subSlots == null) {
                  this.subSlots = new ArrayList<>(1);
                  this.subSlots.add(new Stitcher$Region<>(this.originX, this.originY, textureWidth, textureHeight));
                  int spareWidth = this.width - textureWidth;
                  int spareHeight = this.height - textureHeight;
                  if (spareHeight > 0 && spareWidth > 0) {
                     int right = Math.max(this.height, spareWidth);
                     int bottom = Math.max(this.width, spareHeight);
                     if (right >= bottom) {
                        this.subSlots.add(new Stitcher$Region<>(this.originX, this.originY + textureHeight, textureWidth, spareHeight));
                        this.subSlots.add(new Stitcher$Region<>(this.originX + textureWidth, this.originY, spareWidth, this.height));
                     } else {
                        this.subSlots.add(new Stitcher$Region<>(this.originX + textureWidth, this.originY, spareWidth, textureHeight));
                        this.subSlots.add(new Stitcher$Region<>(this.originX, this.originY + textureHeight, this.width, spareHeight));
                     }
                  } else if (spareWidth == 0) {
                     this.subSlots.add(new Stitcher$Region<>(this.originX, this.originY + textureHeight, textureWidth, spareHeight));
                  } else if (spareHeight == 0) {
                     this.subSlots.add(new Stitcher$Region<>(this.originX + textureWidth, this.originY, spareWidth, textureHeight));
                  }
               }

               for (Stitcher$Region<T> subSlot : this.subSlots) {
                  if (subSlot.add(holder)) {
                     return true;
                  }
               }

               return false;
            }
         } else {
            return false;
         }
      }
   }

   public void walk(final Stitcher$SpriteLoader<T> output, final int padding) {
      if (this.holder != null) {
         output.load(this.holder.entry, this.getX(), this.getY(), padding);
      } else if (this.subSlots != null) {
         for (Stitcher$Region<T> subSlot : this.subSlots) {
            subSlot.walk(output, padding);
         }
      }
   }

   @Override
   public String toString() {
      return "Slot{originX="
         + this.originX
         + ", originY="
         + this.originY
         + ", width="
         + this.width
         + ", height="
         + this.height
         + ", texture="
         + this.holder
         + ", subSlots="
         + this.subSlots
         + "}";
   }
}
