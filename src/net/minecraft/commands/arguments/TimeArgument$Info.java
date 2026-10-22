package net.minecraft.commands.arguments;

import com.google.gson.JsonObject;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.network.FriendlyByteBuf;

public class TimeArgument$Info implements ArgumentTypeInfo<TimeArgument, TimeArgument$Info$Template> {
   public void serializeToNetwork(final TimeArgument$Info$Template template, final FriendlyByteBuf out) {
      out.writeInt(template.min);
   }

   public TimeArgument$Info$Template deserializeFromNetwork(final FriendlyByteBuf in) {
      int min = in.readInt();
      return new TimeArgument$Info$Template(this, min);
   }

   public void serializeToJson(final TimeArgument$Info$Template template, final JsonObject out) {
      out.addProperty("min", template.min);
   }

   public TimeArgument$Info$Template unpack(final TimeArgument argument) {
      return new TimeArgument$Info$Template(this, argument.minimum);
   }
}
