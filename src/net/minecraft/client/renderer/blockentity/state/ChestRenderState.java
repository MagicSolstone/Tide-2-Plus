package net.minecraft.client.renderer.blockentity.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.ChestType;

@Environment(EnvType.CLIENT)
public class ChestRenderState extends BlockEntityRenderState {
   public ChestType type = ChestType.SINGLE;
   public float open;
   public Direction facing = Direction.SOUTH;
   public ChestRenderState$ChestMaterialType material = ChestRenderState$ChestMaterialType.REGULAR;
}
