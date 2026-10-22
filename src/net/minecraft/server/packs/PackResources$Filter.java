package net.minecraft.server.packs;

import net.minecraft.resources.Identifier;

@FunctionalInterface
public interface PackResources$Filter {
   boolean isFiltered(Identifier resourceId);
}
