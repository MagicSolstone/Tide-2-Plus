package net.minecraft.client.gui.screens.worldselection;

import java.util.function.UnaryOperator;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.level.levelgen.WorldOptions;

@Environment(EnvType.CLIENT)
public interface WorldCreationContext$OptionsModifier extends UnaryOperator<WorldOptions> {
}
