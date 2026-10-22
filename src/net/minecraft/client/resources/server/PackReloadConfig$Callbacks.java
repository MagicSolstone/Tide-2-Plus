package net.minecraft.client.resources.server;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface PackReloadConfig$Callbacks {
   void onSuccess();

   void onFailure(boolean isRecovery);

   List<PackReloadConfig$IdAndPath> packsToLoad();
}
