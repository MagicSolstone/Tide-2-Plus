package net.minecraft.client.model.object.bell;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Direction;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public record BellModel$State(float ticks, @Nullable Direction shakeDirection) {
}
