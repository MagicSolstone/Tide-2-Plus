package net.minecraft.client.renderer.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.monster.illager.IllagerModel;
import net.minecraft.client.renderer.entity.state.EvokerRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.monster.illager.SpellcasterIllager;

@Environment(EnvType.CLIENT)
public class EvokerRenderer<T extends SpellcasterIllager> extends IllagerRenderer<T, EvokerRenderState> {
   private static final Identifier EVOKER_ILLAGER = Identifier.withDefaultNamespace("textures/entity/illager/evoker.png");

   public EvokerRenderer(final EntityRendererProvider$Context context) {
      super(context, new IllagerModel<>(context.bakeLayer(ModelLayers.EVOKER)), 0.5F);
      this.addLayer(new EvokerRenderer$1(this, this));
   }

   public Identifier getTextureLocation(final EvokerRenderState state) {
      return EVOKER_ILLAGER;
   }

   public EvokerRenderState createRenderState() {
      return new EvokerRenderState();
   }

   public void extractRenderState(final T entity, final EvokerRenderState state, final float partialTicks) {
      super.extractRenderState(entity, state, partialTicks);
      state.isCastingSpell = entity.isCastingSpell();
   }
}
