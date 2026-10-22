package net.minecraft.client.renderer.entity.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.world.entity.Crackiness$Level;

@Environment(EnvType.CLIENT)
public class IronGolemRenderState extends LivingEntityRenderState {
   public float attackTicksRemaining;
   public int offerFlowerTick;
   public final BlockModelRenderState flowerBlock = new BlockModelRenderState();
   public Crackiness$Level crackiness = Crackiness$Level.NONE;
}
