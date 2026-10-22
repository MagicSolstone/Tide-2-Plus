package net.minecraft.client.resources.metadata.gui;

import com.mojang.serialization.Codec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface GuiSpriteScaling {
   Codec<GuiSpriteScaling> CODEC = GuiSpriteScaling$Type.CODEC.dispatch(GuiSpriteScaling::type, GuiSpriteScaling$Type::codec);
   GuiSpriteScaling DEFAULT = new GuiSpriteScaling$Stretch();

   GuiSpriteScaling$Type type();
}
