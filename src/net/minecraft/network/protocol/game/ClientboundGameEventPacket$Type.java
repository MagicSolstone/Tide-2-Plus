package net.minecraft.network.protocol.game;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public class ClientboundGameEventPacket$Type {
   private static final Int2ObjectMap<ClientboundGameEventPacket$Type> TYPES = new Int2ObjectOpenHashMap();
   private final int id;

   public ClientboundGameEventPacket$Type(final int id) {
      this.id = id;
      TYPES.put(id, this);
   }
}
