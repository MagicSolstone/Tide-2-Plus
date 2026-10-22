package net.minecraft.client.multiplayer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.advancements.AdvancementHolder;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public interface ClientAdvancements$Listener {
   void onAdvancementsUpdated();

   void onAdvancementsCleared();

   void onSelectedTabChanged(@Nullable AdvancementHolder selectedTab);
}
