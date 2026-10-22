package net.minecraft.client.resources.model;

import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
record ModelManager$ResolvedModels(ResolvedModel missing, Map<Identifier, ResolvedModel> models) {
}
