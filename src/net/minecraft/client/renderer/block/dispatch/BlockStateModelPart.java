package net.minecraft.client.renderer.block.dispatch;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.geometry.BakedQuad$MaterialFlags;
import net.minecraft.client.resources.model.sprite.Material$Baked;
import net.minecraft.core.Direction;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public interface BlockStateModelPart {
   List<BakedQuad> getQuads(@Nullable Direction direction);

   boolean useAmbientOcclusion();

   Material$Baked particleMaterial();

   @BakedQuad$MaterialFlags
   int materialFlags();
}
