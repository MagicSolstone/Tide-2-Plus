package net.minecraft.client.gui.screens.worldselection;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.ObjectSelectionList$Entry;
import net.minecraft.world.level.storage.LevelSummary;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public abstract class WorldSelectionList$Entry extends ObjectSelectionList$Entry<WorldSelectionList$Entry> implements AutoCloseable {
   @Override
   public void close() {
   }

   @Nullable
   public LevelSummary getLevelSummary() {
      return null;
   }
}
