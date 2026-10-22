package net.minecraft.client.gui.screens.worldselection;

import java.util.function.BiFunction;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.RegistryAccess$Frozen;
import net.minecraft.world.level.levelgen.WorldDimensions;

@FunctionalInterface
@Environment(EnvType.CLIENT)
public interface WorldCreationContext$DimensionsUpdater extends BiFunction<RegistryAccess$Frozen, WorldDimensions, WorldDimensions> {
}
