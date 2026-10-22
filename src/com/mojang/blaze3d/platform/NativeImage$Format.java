package com.mojang.blaze3d.platform;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public enum NativeImage$Format {
   RGBA(4, true, true, true, false, true, 0, 8, 16, 255, 24),
   RGB(3, true, true, true, false, false, 0, 8, 16, 255, 255),
   LUMINANCE_ALPHA(2, false, false, false, true, true, 255, 255, 255, 0, 8),
   LUMINANCE(1, false, false, false, true, false, 0, 0, 0, 0, 255);

   private final int components;
   private final boolean hasRed;
   private final boolean hasGreen;
   private final boolean hasBlue;
   private final boolean hasLuminance;
   private final boolean hasAlpha;
   private final int redOffset;
   private final int greenOffset;
   private final int blueOffset;
   private final int luminanceOffset;
   private final int alphaOffset;

   private NativeImage$Format(
      final int components,
      final boolean hasRed,
      final boolean hasGreen,
      final boolean hasBlue,
      final boolean hasLuminance,
      final boolean hasAlpha,
      final int redOffset,
      final int greenOffset,
      final int blueOffset,
      final int luminanceOffset,
      final int alphaOffset
   ) {
      this.components = components;
      this.hasRed = hasRed;
      this.hasGreen = hasGreen;
      this.hasBlue = hasBlue;
      this.hasLuminance = hasLuminance;
      this.hasAlpha = hasAlpha;
      this.redOffset = redOffset;
      this.greenOffset = greenOffset;
      this.blueOffset = blueOffset;
      this.luminanceOffset = luminanceOffset;
      this.alphaOffset = alphaOffset;
   }

   public int components() {
      return this.components;
   }

   public boolean hasRed() {
      return this.hasRed;
   }

   public boolean hasGreen() {
      return this.hasGreen;
   }

   public boolean hasBlue() {
      return this.hasBlue;
   }

   public boolean hasLuminance() {
      return this.hasLuminance;
   }

   public boolean hasAlpha() {
      return this.hasAlpha;
   }

   public int redOffset() {
      return this.redOffset;
   }

   public int greenOffset() {
      return this.greenOffset;
   }

   public int blueOffset() {
      return this.blueOffset;
   }

   public int luminanceOffset() {
      return this.luminanceOffset;
   }

   public int alphaOffset() {
      return this.alphaOffset;
   }

   public boolean hasLuminanceOrRed() {
      return this.hasLuminance || this.hasRed;
   }

   public boolean hasLuminanceOrGreen() {
      return this.hasLuminance || this.hasGreen;
   }

   public boolean hasLuminanceOrBlue() {
      return this.hasLuminance || this.hasBlue;
   }

   public boolean hasLuminanceOrAlpha() {
      return this.hasLuminance || this.hasAlpha;
   }

   public int luminanceOrRedOffset() {
      return this.hasLuminance ? this.luminanceOffset : this.redOffset;
   }

   public int luminanceOrGreenOffset() {
      return this.hasLuminance ? this.luminanceOffset : this.greenOffset;
   }

   public int luminanceOrBlueOffset() {
      return this.hasLuminance ? this.luminanceOffset : this.blueOffset;
   }

   public int luminanceOrAlphaOffset() {
      return this.hasLuminance ? this.luminanceOffset : this.alphaOffset;
   }

   private static NativeImage$Format getStbFormat(final int i) {
      switch (i) {
         case 1:
            return LUMINANCE;
         case 2:
            return LUMINANCE_ALPHA;
         case 3:
            return RGB;
         case 4:
         default:
            return RGBA;
      }
   }
}
