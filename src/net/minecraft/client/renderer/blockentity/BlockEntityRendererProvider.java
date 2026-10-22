package net.minecraft.client.renderer.blockentity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.world.level.block.entity.BlockEntity;

@FunctionalInterface
@Environment(EnvType.CLIENT)
public interface BlockEntityRendererProvider<T extends BlockEntity, S extends BlockEntityRenderState> {
   BlockEntityRenderer<T, S> create(BlockEntityRendererProvider$Context context);
}
