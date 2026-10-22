package net.minecraft.server;

import net.minecraft.server.packs.resources.CloseableResourceManager;

record MinecraftServer$ReloadableResources(CloseableResourceManager resourceManager, ReloadableServerResources managers) implements AutoCloseable {
   @Override
   public void close() {
      this.resourceManager.close();
   }
}
