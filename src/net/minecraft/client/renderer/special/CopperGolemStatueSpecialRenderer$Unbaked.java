package net.minecraft.client.renderer.special;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.object.statue.CopperGolemStatueModel;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.animal.golem.CopperGolemOxidationLevels;
import net.minecraft.world.level.block.CopperGolemStatueBlock$Pose;
import net.minecraft.world.level.block.WeatheringCopper$WeatherState;

@Environment(EnvType.CLIENT)
public record CopperGolemStatueSpecialRenderer$Unbaked(Identifier texture, CopperGolemStatueBlock$Pose pose) implements NoDataSpecialModelRenderer$Unbaked {
   public static final MapCodec<CopperGolemStatueSpecialRenderer$Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            Identifier.CODEC.fieldOf("texture").forGetter(CopperGolemStatueSpecialRenderer$Unbaked::texture),
            CopperGolemStatueBlock$Pose.CODEC.fieldOf("pose").forGetter(CopperGolemStatueSpecialRenderer$Unbaked::pose)
         )
         .apply(i, CopperGolemStatueSpecialRenderer$Unbaked::new)
   );

   public CopperGolemStatueSpecialRenderer$Unbaked(final WeatheringCopper$WeatherState state, final CopperGolemStatueBlock$Pose pose) {
      this(CopperGolemOxidationLevels.getOxidationLevel(state).texture(), pose);
   }

   @Override
   public MapCodec<CopperGolemStatueSpecialRenderer$Unbaked> type() {
      return MAP_CODEC;
   }

   public CopperGolemStatueSpecialRenderer bake(final SpecialModelRenderer$BakingContext context) {
      CopperGolemStatueModel model = new CopperGolemStatueModel(context.entityModelSet().bakeLayer(getModel(this.pose)));
      return new CopperGolemStatueSpecialRenderer(model, this.texture);
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private static ModelLayerLocation getModel(final CopperGolemStatueBlock$Pose pose) {
      return switch (pose) {
         case STANDING -> ModelLayers.COPPER_GOLEM;
         case SITTING -> ModelLayers.COPPER_GOLEM_SITTING;
         case STAR -> ModelLayers.COPPER_GOLEM_STAR;
         case RUNNING -> ModelLayers.COPPER_GOLEM_RUNNING;
      };
   }
}
