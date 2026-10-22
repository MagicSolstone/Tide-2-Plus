package net.minecraft.client.multiplayer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public enum ServerData$ServerPackStatus {
   ENABLED("enabled"),
   DISABLED("disabled"),
   PROMPT("prompt");

   public static final MapCodec<ServerData$ServerPackStatus> FIELD_CODEC = Codec.BOOL
      .optionalFieldOf("acceptTextures")
      .xmap(acceptTextures -> acceptTextures.<ServerData$ServerPackStatus>map(b -> b ? ENABLED : DISABLED).orElse(PROMPT), status -> {
         return switch (status) {
            case ENABLED -> Optional.of(true);
            case DISABLED -> Optional.of(false);
            case PROMPT -> Optional.empty();
         };
      });
   private final Component name;

   private ServerData$ServerPackStatus(final String name) {
      this.name = Component.translatable("manageServer.resourcePack." + name);
   }

   public Component getName() {
      return this.name;
   }
}
