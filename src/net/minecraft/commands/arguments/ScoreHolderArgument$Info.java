package net.minecraft.commands.arguments;

import com.google.gson.JsonObject;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.network.FriendlyByteBuf;

public class ScoreHolderArgument$Info implements ArgumentTypeInfo<ScoreHolderArgument, ScoreHolderArgument$Info$Template> {
   private static final byte FLAG_MULTIPLE = 1;

   public void serializeToNetwork(final ScoreHolderArgument$Info$Template template, final FriendlyByteBuf out) {
      int flags = 0;
      if (template.multiple) {
         flags |= 1;
      }

      out.writeByte(flags);
   }

   public ScoreHolderArgument$Info$Template deserializeFromNetwork(final FriendlyByteBuf in) {
      byte flags = in.readByte();
      boolean multiple = (flags & 1) != 0;
      return new ScoreHolderArgument$Info$Template(this, multiple);
   }

   public void serializeToJson(final ScoreHolderArgument$Info$Template template, final JsonObject out) {
      out.addProperty("amount", template.multiple ? "multiple" : "single");
   }

   public ScoreHolderArgument$Info$Template unpack(final ScoreHolderArgument argument) {
      return new ScoreHolderArgument$Info$Template(this, argument.multiple);
   }
}
