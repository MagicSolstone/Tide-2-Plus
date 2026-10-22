package net.minecraft.client.multiplayer.chat;

import com.mojang.serialization.MapCodec;
import java.util.function.Supplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.StringRepresentable;

@Environment(EnvType.CLIENT)
public enum LoggedChatEvent$Type implements StringRepresentable {
   PLAYER("player", () -> LoggedChatMessage$Player.CODEC),
   SYSTEM("system", () -> LoggedChatMessage$System.CODEC);

   private final String serializedName;
   private final Supplier<MapCodec<? extends LoggedChatEvent>> codec;

   private LoggedChatEvent$Type(final String serializedName, final Supplier<MapCodec<? extends LoggedChatEvent>> codec) {
      this.serializedName = serializedName;
      this.codec = codec;
   }

   private MapCodec<? extends LoggedChatEvent> codec() {
      return this.codec.get();
   }

   @Override
   public String getSerializedName() {
      return this.serializedName;
   }
}
