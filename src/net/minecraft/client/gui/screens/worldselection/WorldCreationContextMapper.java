package net.minecraft.client.gui.screens.worldselection;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.server.RegistryLayer;
import net.minecraft.server.ReloadableServerResources;

@FunctionalInterface
@Environment(EnvType.CLIENT)
public interface WorldCreationContextMapper {
   WorldCreationContext apply(
      final ReloadableServerResources managers, final LayeredRegistryAccess<RegistryLayer> registries, final DataPackReloadCookie cookie
   );
}
