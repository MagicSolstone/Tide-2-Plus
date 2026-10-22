package net.minecraft.client.main;

import joptsimple.util.EnumConverter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.PreferredGraphicsApi;

@Environment(EnvType.CLIENT)
class Main$1 extends EnumConverter<PreferredGraphicsApi> {
   Main$1(Class clazz) {
      super(clazz);
   }
}
