package net.minecraft.client.resources.metadata.gui;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.StringRepresentable;

@Environment(EnvType.CLIENT)
public enum GuiSpriteScaling$Type implements StringRepresentable {
   STRETCH("stretch", GuiSpriteScaling$Stretch.CODEC),
   TILE("tile", GuiSpriteScaling$Tile.CODEC),
   NINE_SLICE("nine_slice", GuiSpriteScaling$NineSlice.CODEC);

   public static final Codec<GuiSpriteScaling$Type> CODEC = StringRepresentable.fromEnum(GuiSpriteScaling$Type::values);
   private final String key;
   private final MapCodec<? extends GuiSpriteScaling> codec;

   private GuiSpriteScaling$Type(final String key, final MapCodec<? extends GuiSpriteScaling> codec) {
      this.key = key;
      this.codec = codec;
   }

   @Override
   public String getSerializedName() {
      return this.key;
   }

   public MapCodec<? extends GuiSpriteScaling> codec() {
      return this.codec;
   }
}
