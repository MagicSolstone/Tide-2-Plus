package net.minecraft.client.resources.metadata.gui;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.ExtraCodecs;

@Environment(EnvType.CLIENT)
public record GuiSpriteScaling$Tile(int width, int height) implements GuiSpriteScaling {
   public static final MapCodec<GuiSpriteScaling$Tile> CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            ExtraCodecs.POSITIVE_INT.fieldOf("width").forGetter(GuiSpriteScaling$Tile::width),
            ExtraCodecs.POSITIVE_INT.fieldOf("height").forGetter(GuiSpriteScaling$Tile::height)
         )
         .apply(i, GuiSpriteScaling$Tile::new)
   );

   @Override
   public GuiSpriteScaling$Type type() {
      return GuiSpriteScaling$Type.TILE;
   }
}
