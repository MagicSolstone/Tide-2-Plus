package net.minecraft.server.packs.resources;

import net.minecraft.resources.Identifier;

@FunctionalInterface
public interface ResourceManager$Selector {
   boolean isIncluded(Identifier resourceId);
}
