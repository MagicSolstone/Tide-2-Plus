package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.Material$Baked;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.data.AtlasIds;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStackTemplate;

@Environment(EnvType.CLIENT)
public abstract class BreakingItemParticle$ItemParticleProvider<T extends ParticleOptions> implements ParticleProvider<T> {
   private final ItemStackRenderState scratchRenderState = new ItemStackRenderState();

   protected TextureAtlasSprite getSprite(final ItemStackTemplate item, final ClientLevel level, final RandomSource random) {
      Minecraft.getInstance().getItemModelResolver().updateForTopItem(this.scratchRenderState, item.create(), ItemDisplayContext.GROUND, level, null, 0);
      Material$Baked material = this.scratchRenderState.pickParticleMaterial(random);
      return material != null ? material.sprite() : Minecraft.getInstance().getAtlasManager().getAtlasOrThrow(AtlasIds.ITEMS).missingSprite();
   }
}
