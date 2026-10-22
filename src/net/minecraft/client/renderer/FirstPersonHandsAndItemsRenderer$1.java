package net.minecraft.client.renderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.SwingAnimationType;

// $VF: synthetic class
@Environment(EnvType.CLIENT)
class FirstPersonHandsAndItemsRenderer$1 {
   static {
      try {
         $SwitchMap$net$minecraft$world$item$SwingAnimationType[SwingAnimationType.NONE.ordinal()] = 1;
      } catch (NoSuchFieldError var12) {
      }

      try {
         $SwitchMap$net$minecraft$world$item$SwingAnimationType[SwingAnimationType.WHACK.ordinal()] = 2;
      } catch (NoSuchFieldError var11) {
      }

      try {
         $SwitchMap$net$minecraft$world$item$SwingAnimationType[SwingAnimationType.STAB.ordinal()] = 3;
      } catch (NoSuchFieldError var10) {
      }

      $SwitchMap$net$minecraft$world$item$ItemUseAnimation = new int[ItemUseAnimation.values().length];

      try {
         $SwitchMap$net$minecraft$world$item$ItemUseAnimation[ItemUseAnimation.NONE.ordinal()] = 1;
      } catch (NoSuchFieldError var9) {
      }

      try {
         $SwitchMap$net$minecraft$world$item$ItemUseAnimation[ItemUseAnimation.EAT.ordinal()] = 2;
      } catch (NoSuchFieldError var8) {
      }

      try {
         $SwitchMap$net$minecraft$world$item$ItemUseAnimation[ItemUseAnimation.DRINK.ordinal()] = 3;
      } catch (NoSuchFieldError var7) {
      }

      try {
         $SwitchMap$net$minecraft$world$item$ItemUseAnimation[ItemUseAnimation.BLOCK.ordinal()] = 4;
      } catch (NoSuchFieldError var6) {
      }

      try {
         $SwitchMap$net$minecraft$world$item$ItemUseAnimation[ItemUseAnimation.BOW.ordinal()] = 5;
      } catch (NoSuchFieldError var5) {
      }

      try {
         $SwitchMap$net$minecraft$world$item$ItemUseAnimation[ItemUseAnimation.TRIDENT.ordinal()] = 6;
      } catch (NoSuchFieldError var4) {
      }

      try {
         $SwitchMap$net$minecraft$world$item$ItemUseAnimation[ItemUseAnimation.BRUSH.ordinal()] = 7;
      } catch (NoSuchFieldError var3) {
      }

      try {
         $SwitchMap$net$minecraft$world$item$ItemUseAnimation[ItemUseAnimation.BUNDLE.ordinal()] = 8;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$net$minecraft$world$item$ItemUseAnimation[ItemUseAnimation.SPEAR.ordinal()] = 9;
      } catch (NoSuchFieldError var1) {
      }
   }
}
