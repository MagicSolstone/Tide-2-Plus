package net.minecraft.client.renderer.block.dispatch;

import com.mojang.serialization.Codec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ResolvableModel$Resolver;

@Environment(EnvType.CLIENT)
public record SingleVariant$Unbaked(Variant variant) implements BlockStateModel$Unbaked {
   public static final Codec<SingleVariant$Unbaked> CODEC = Variant.CODEC.xmap(SingleVariant$Unbaked::new, SingleVariant$Unbaked::variant);

   @Override
   public BlockStateModel bake(final ModelBaker modelBakery) {
      return new SingleVariant(this.variant.bake(modelBakery));
   }

   @Override
   public void resolveDependencies(final ResolvableModel$Resolver resolver) {
      this.variant.resolveDependencies(resolver);
   }
}
