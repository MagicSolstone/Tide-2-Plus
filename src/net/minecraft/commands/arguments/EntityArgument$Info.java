package net.minecraft.commands.arguments;

import com.google.gson.JsonObject;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.network.FriendlyByteBuf;

public class EntityArgument$Info implements ArgumentTypeInfo<EntityArgument, EntityArgument$Info$Template> {
   private static final byte FLAG_SINGLE = 1;
   private static final byte FLAG_PLAYERS_ONLY = 2;

   public void serializeToNetwork(final EntityArgument$Info$Template template, final FriendlyByteBuf out) {
      int flags = 0;
      if (template.single) {
         flags |= 1;
      }

      if (template.playersOnly) {
         flags |= 2;
      }

      out.writeByte(flags);
   }

   public EntityArgument$Info$Template deserializeFromNetwork(final FriendlyByteBuf in) {
      byte flags = in.readByte();
      return new EntityArgument$Info$Template(this, (flags & 1) != 0, (flags & 2) != 0);
   }

   public void serializeToJson(final EntityArgument$Info$Template template, final JsonObject out) {
      out.addProperty("amount", template.single ? "single" : "multiple");
      out.addProperty("type", template.playersOnly ? "players" : "entities");
   }

   public EntityArgument$Info$Template unpack(final EntityArgument argument) {
      return new EntityArgument$Info$Template(this, argument.single, argument.playersOnly);
   }
}
