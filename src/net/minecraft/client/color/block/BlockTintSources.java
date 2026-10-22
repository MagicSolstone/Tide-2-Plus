package net.minecraft.client.color.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class BlockTintSources {
   public static BlockTintSource constant(final int color) {
      return var1 -> color;
   }

   public static BlockTintSource constant(final int colorInHand, final int colorInWorld) {
      return new BlockTintSources$1(colorInHand, colorInWorld);
   }

   public static BlockTintSource doubleTallGrass() {
      return new BlockTintSources$2();
   }

   public static BlockTintSource grass() {
      return new BlockTintSources$3();
   }

   public static BlockTintSource grassBlock() {
      return new BlockTintSources$4();
   }

   public static BlockTintSource sugarCane() {
      return new BlockTintSources$5();
   }

   public static BlockTintSource foliage() {
      return new BlockTintSources$6();
   }

   public static BlockTintSource dryFoliage() {
      return new BlockTintSources$7();
   }

   public static BlockTintSource water() {
      return new BlockTintSources$8();
   }

   public static BlockTintSource waterParticles() {
      return new BlockTintSources$9();
   }

   public static BlockTintSource redstone() {
      return new BlockTintSources$10();
   }

   public static BlockTintSource stem() {
      return new BlockTintSources$11();
   }
}
