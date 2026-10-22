package net.minecraft.client.renderer.block.model;

import java.util.function.Function;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.PlayerSkinRenderCache;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.special.SpecialModelRenderer$BakingContext;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.world.level.block.state.BlockState;

@Environment(EnvType.CLIENT)
public record BlockModel$BakingContext(
   EntityModelSet entityModelSet,
   SpriteGetter sprites,
   PlayerSkinRenderCache playerSkinRenderCache,
   Function<BlockState, BlockStateModel> modelGetter,
   BlockModel missingBlockModel
) implements SpecialModelRenderer$BakingContext {
}
