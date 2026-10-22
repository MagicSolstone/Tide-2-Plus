package net.minecraft.client.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.entity.animal.parrot.Parrot$Variant;
import net.minecraft.world.entity.player.PlayerSkin;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public interface ClientAvatarEntity {
   ClientAvatarState avatarState();

   PlayerSkin getSkin();

   @Nullable
   Parrot$Variant getParrotVariantOnShoulder(boolean left);

   boolean showExtraEars();
}
