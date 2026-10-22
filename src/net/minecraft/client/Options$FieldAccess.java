package net.minecraft.client;

import java.util.function.Function;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
interface Options$FieldAccess extends Options$OptionAccess {
   int process(String name, int value);

   boolean process(String name, boolean value);

   String process(String name, String value);

   float process(String name, float value);

   <T> T process(String name, T value, Function<String, T> reader, Function<T, String> writer);
}
