package net.minecraft.commands.synchronization.brigadier;

import com.google.gson.JsonObject;
import com.mojang.brigadier.arguments.FloatArgumentType;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentUtils;
import net.minecraft.network.FriendlyByteBuf;

public class FloatArgumentInfo implements ArgumentTypeInfo<FloatArgumentType, FloatArgumentInfo$Template> {
   public void serializeToNetwork(final FloatArgumentInfo$Template template, final FriendlyByteBuf out) {
      boolean hasMin = template.min != -Float.MAX_VALUE;
      boolean hasMax = template.max != Float.MAX_VALUE;
      out.writeByte(ArgumentUtils.createNumberFlags(hasMin, hasMax));
      if (hasMin) {
         out.writeFloat(template.min);
      }

      if (hasMax) {
         out.writeFloat(template.max);
      }
   }

   public FloatArgumentInfo$Template deserializeFromNetwork(final FriendlyByteBuf in) {
      byte flags = in.readByte();
      float min = ArgumentUtils.numberHasMin(flags) ? in.readFloat() : -Float.MAX_VALUE;
      float max = ArgumentUtils.numberHasMax(flags) ? in.readFloat() : Float.MAX_VALUE;
      return new FloatArgumentInfo$Template(this, min, max);
   }

   public void serializeToJson(final FloatArgumentInfo$Template template, final JsonObject out) {
      if (template.min != -Float.MAX_VALUE) {
         out.addProperty("min", template.min);
      }

      if (template.max != Float.MAX_VALUE) {
         out.addProperty("max", template.max);
      }
   }

   public FloatArgumentInfo$Template unpack(final FloatArgumentType argument) {
      return new FloatArgumentInfo$Template(this, argument.getMinimum(), argument.getMaximum());
   }
}
