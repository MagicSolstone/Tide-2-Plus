package net.minecraft.server.network;

import java.util.function.Consumer;
import net.minecraft.network.protocol.Packet;

public interface ConfigurationTask {
   void start(Consumer<Packet<?>> connection);

   default boolean tick() {
      return false;
   }

   ConfigurationTask$Type type();
}
