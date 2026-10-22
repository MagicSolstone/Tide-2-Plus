package net.minecraft.client.gui.screens.multiplayer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.ObjectSelectionList$Entry;

@Environment(EnvType.CLIENT)
public abstract class ServerSelectionList$Entry extends ObjectSelectionList$Entry<ServerSelectionList$Entry> implements AutoCloseable {
   @Override
   public void close() {
   }

   protected abstract boolean matches(final ServerSelectionList$Entry other);

   public abstract void join();
}
