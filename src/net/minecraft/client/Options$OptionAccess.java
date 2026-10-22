package net.minecraft.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
interface Options$OptionAccess {
   <T> void process(String name, OptionInstance<T> option);
}
