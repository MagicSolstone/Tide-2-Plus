package net.minecraft.client.renderer.special;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.object.chest.ChestModel;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.properties.ChestType;

@Environment(EnvType.CLIENT)
public record ChestSpecialRenderer$Unbaked(Identifier texture, float openness, ChestType chestType) implements NoDataSpecialModelRenderer$Unbaked {
   public static final MapCodec<ChestSpecialRenderer$Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            Identifier.CODEC.fieldOf("texture").forGetter(ChestSpecialRenderer$Unbaked::texture),
            Codec.FLOAT.optionalFieldOf("openness", 0.0F).forGetter(ChestSpecialRenderer$Unbaked::openness),
            ChestType.CODEC.optionalFieldOf("chest_type", ChestType.SINGLE).forGetter(ChestSpecialRenderer$Unbaked::chestType)
         )
         .apply(i, ChestSpecialRenderer$Unbaked::new)
   );

   public ChestSpecialRenderer$Unbaked(final Identifier texture, final ChestType chestType) {
      this(texture, 0.0F, chestType);
   }

   public ChestSpecialRenderer$Unbaked(final Identifier texture) {
      this(texture, 0.0F, ChestType.SINGLE);
   }

   @Override
   public MapCodec<ChestSpecialRenderer$Unbaked> type() {
      return MAP_CODEC;
   }

   public ChestSpecialRenderer bake(final SpecialModelRenderer$BakingContext context) {
      ChestModel model = new ChestModel(context.entityModelSet().bakeLayer(ChestRenderer.LAYERS.select(this.chestType)));
      SpriteId fullTexture = Sheets.CHEST_MAPPER.apply(this.texture);
      return new ChestSpecialRenderer(context.sprites(), model, fullTexture, this.openness);
   }
}
