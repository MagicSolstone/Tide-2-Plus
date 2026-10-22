package net.minecraft.client.resources.metadata.gui;

import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public record GuiSpriteScaling$Stretch() implements GuiSpriteScaling {
   public static final MapCodec<GuiSpriteScaling$Stretch> CODEC = MapCodec.unit(GuiSpriteScaling$Stretch::new);

   @Override
   public GuiSpriteScaling$Type type() {
      return GuiSpriteScaling$Type.STRETCH;
   }
}
