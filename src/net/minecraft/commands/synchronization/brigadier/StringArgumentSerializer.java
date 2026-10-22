package net.minecraft.commands.synchronization.brigadier;

import com.google.gson.JsonObject;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType.StringType;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.network.FriendlyByteBuf;

public class StringArgumentSerializer implements ArgumentTypeInfo<StringArgumentType, StringArgumentSerializer$Template> {
   public void serializeToNetwork(final StringArgumentSerializer$Template template, final FriendlyByteBuf out) {
      out.writeEnum(template.type);
   }

   public StringArgumentSerializer$Template deserializeFromNetwork(final FriendlyByteBuf in) {
      StringType type = in.readEnum(StringType.class);
      return new StringArgumentSerializer$Template(this, type);
   }

   public void serializeToJson(final StringArgumentSerializer$Template template, final JsonObject out) {
      out.addProperty("type", switch (template.type) {
         case SINGLE_WORD -> "word";
         case QUOTABLE_PHRASE -> "phrase";
         case GREEDY_PHRASE -> "greedy";
         default -> throw new MatchException(null, null);
      });
   }

   public StringArgumentSerializer$Template unpack(final StringArgumentType argument) {
      return new StringArgumentSerializer$Template(this, argument.getType());
   }
}
