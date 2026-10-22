package net.minecraft.server;

import net.minecraft.core.HolderLookup$Provider;
import net.minecraft.core.RegistryAccess$Frozen;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.WorldDataConfiguration;

public record WorldLoader$DataLoadContext(
   ResourceManager resources, WorldDataConfiguration dataConfiguration, HolderLookup$Provider datapackWorldRegistries, RegistryAccess$Frozen datapackDimensions
) {
}
