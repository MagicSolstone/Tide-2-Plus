package net.minecraft.client.renderer.special;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.ShulkerBoxRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.DyeColor;

@Environment(EnvType.CLIENT)
public record ShulkerBoxSpecialRenderer$Unbaked(Identifier texture, float openness) implements NoDataSpecialModelRenderer$Unbaked {
   public static final MapCodec<ShulkerBoxSpecialRenderer$Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            Identifier.CODEC.fieldOf("texture").forGetter(ShulkerBoxSpecialRenderer$Unbaked::texture),
            Codec.FLOAT.optionalFieldOf("openness", 0.0F).forGetter(ShulkerBoxSpecialRenderer$Unbaked::openness)
         )
         .apply(i, ShulkerBoxSpecialRenderer$Unbaked::new)
   );

   public ShulkerBoxSpecialRenderer$Unbaked() {
      this(Identifier.withDefaultNamespace("shulker"), 0.0F);
   }

   public ShulkerBoxSpecialRenderer$Unbaked(final DyeColor color) {
      this(Sheets.colorToShulkerSprite(color), 0.0F);
   }

   @Override
   public MapCodec<ShulkerBoxSpecialRenderer$Unbaked> type() {
      return MAP_CODEC;
   }

   public ShulkerBoxSpecialRenderer bake(final SpecialModelRenderer$BakingContext context) {
      return new ShulkerBoxSpecialRenderer(new ShulkerBoxRenderer(context), this.openness, Sheets.SHULKER_MAPPER.apply(this.texture));
   }
}
