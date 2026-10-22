package net.minecraft.network.protocol.status;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;

public record ServerStatus(
   Component description,
   Optional<ServerStatus$Players> players,
   Optional<ServerStatus$Version> version,
   Optional<ServerStatus$Favicon> favicon,
   boolean enforcesSecureChat
) {
   public static final Codec<ServerStatus> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            ComponentSerialization.CODEC.lenientOptionalFieldOf("description", CommonComponents.EMPTY).forGetter(ServerStatus::description),
            ServerStatus$Players.CODEC.lenientOptionalFieldOf("players").forGetter(ServerStatus::players),
            ServerStatus$Version.CODEC.lenientOptionalFieldOf("version").forGetter(ServerStatus::version),
            ServerStatus$Favicon.CODEC.lenientOptionalFieldOf("favicon").forGetter(ServerStatus::favicon),
            Codec.BOOL.lenientOptionalFieldOf("enforcesSecureChat", false).forGetter(ServerStatus::enforcesSecureChat)
         )
         .apply(i, ServerStatus::new)
   );
}
