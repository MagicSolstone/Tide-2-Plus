package net.minecraft.client.renderer.special;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.rendertype.RenderTypes;

@Environment(EnvType.CLIENT)
public record EndCubeSpecialRenderer$Unbaked(EndCubeSpecialRenderer$Type effect) implements NoDataSpecialModelRenderer$Unbaked {
   public static final MapCodec<EndCubeSpecialRenderer$Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(EndCubeSpecialRenderer$Type.CODEC.fieldOf("effect").forGetter(EndCubeSpecialRenderer$Unbaked::effect))
         .apply(i, EndCubeSpecialRenderer$Unbaked::new)
   );

   @Override
   public SpecialModelRenderer<Void> bake(final SpecialModelRenderer$BakingContext context) {
      return new EndCubeSpecialRenderer(switch (this.effect) {
         case PORTAL -> RenderTypes.endPortal();
         case GATEWAY -> RenderTypes.endGateway();
      });
   }

   @Override
   public MapCodec<EndCubeSpecialRenderer$Unbaked> type() {
      return MAP_CODEC;
   }
}
