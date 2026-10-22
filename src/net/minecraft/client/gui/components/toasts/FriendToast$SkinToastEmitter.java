package net.minecraft.client.gui.components.toasts;

import java.util.UUID;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;

@FunctionalInterface
@Environment(EnvType.CLIENT)
public interface FriendToast$SkinToastEmitter {
   void emit(Minecraft minecraft, String playerName, UUID playerId);
}
