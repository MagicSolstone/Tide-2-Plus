package net.minecraft.server.network;

public record ConfigurationTask$Type(String id) {
   @Override
   public String toString() {
      return this.id;
   }
}
