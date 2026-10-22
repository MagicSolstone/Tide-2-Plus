package net.minecraft.client.particle;

import java.util.List;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
record ParticleResources$1ParticleDefinition(Identifier id, Optional<List<Identifier>> sprites) {
}
