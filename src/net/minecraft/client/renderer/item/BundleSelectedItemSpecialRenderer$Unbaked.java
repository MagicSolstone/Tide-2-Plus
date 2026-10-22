package net.minecraft.client.renderer.item;

import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.model.ResolvableModel$Resolver;
import org.joml.Matrix4fc;

@Environment(EnvType.CLIENT)
public record BundleSelectedItemSpecialRenderer$Unbaked() implements ItemModel$Unbaked {
   public static final MapCodec<BundleSelectedItemSpecialRenderer$Unbaked> MAP_CODEC = MapCodec.unit(new BundleSelectedItemSpecialRenderer$Unbaked());

   @Override
   public MapCodec<BundleSelectedItemSpecialRenderer$Unbaked> type() {
      return MAP_CODEC;
   }

   @Override
   public ItemModel bake(final ItemModel$BakingContext context, final Matrix4fc transformation) {
      return BundleSelectedItemSpecialRenderer.INSTANCE;
   }

   @Override
   public void resolveDependencies(final ResolvableModel$Resolver resolver) {
   }
}
