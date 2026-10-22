package net.minecraft.network.protocol.status;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.SharedConstants;
import net.minecraft.WorldVersion;

public record ServerStatus$Version(String name, int protocol) {
   public static final Codec<ServerStatus$Version> CODEC = RecordCodecBuilder.create(
      i -> i.group(Codec.STRING.fieldOf("name").forGetter(ServerStatus$Version::name), Codec.INT.fieldOf("protocol").forGetter(ServerStatus$Version::protocol))
         .apply(i, ServerStatus$Version::new)
   );

   public static ServerStatus$Version current() {
      WorldVersion version = SharedConstants.getCurrentVersion();
      return new ServerStatus$Version(version.name(), version.protocolVersion());
   }
}
