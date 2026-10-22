package net.minecraft.client.renderer.item;

import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.model.ResolvableModel$Resolver;
import org.joml.Matrix4fc;

@Environment(EnvType.CLIENT)
public record EmptyModel$Unbaked() implements ItemModel$Unbaked {
   public static final MapCodec<EmptyModel$Unbaked> MAP_CODEC = MapCodec.unit(EmptyModel$Unbaked::new);

   @Override
   public void resolveDependencies(final ResolvableModel$Resolver resolver) {
   }

   @Override
   public ItemModel bake(final ItemModel$BakingContext context, final Matrix4fc transformation) {
      return EmptyModel.INSTANCE;
   }

   @Override
   public MapCodec<EmptyModel$Unbaked> type() {
      return MAP_CODEC;
   }
}
