package net.minecraft.client.renderer.block;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.model.BlockModel$Unbaked;

@Environment(EnvType.CLIENT)
public record SelectBlockModel$SwitchCase<T>(List<T> values, BlockModel$Unbaked model) {
}
