package net.minecraft.client.resources;

import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

// $VF: synthetic class
@Environment(EnvType.CLIENT)
class SkinManager$2 {
   static {
      try {
         $SwitchMap$com$mojang$authlib$minecraft$MinecraftProfileTexture$Type[Type.SKIN.ordinal()] = 1;
      } catch (NoSuchFieldError var3) {
      }

      try {
         $SwitchMap$com$mojang$authlib$minecraft$MinecraftProfileTexture$Type[Type.CAPE.ordinal()] = 2;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$com$mojang$authlib$minecraft$MinecraftProfileTexture$Type[Type.ELYTRA.ordinal()] = 3;
      } catch (NoSuchFieldError var1) {
      }
   }
}
