package net.minecraft.client.main;

import java.io.File;
import java.nio.file.Path;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.IndexedAssetSource;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class GameConfig$FolderData {
   public final File gameDirectory;
   public final File resourcePackDirectory;
   public final File assetDirectory;
   @Nullable
   public final String assetIndex;

   public GameConfig$FolderData(final File gameDirectory, final File resourcePackDirectory, final File assetDirectory, @Nullable final String assetIndex) {
      this.gameDirectory = gameDirectory;
      this.resourcePackDirectory = resourcePackDirectory;
      this.assetDirectory = assetDirectory;
      this.assetIndex = assetIndex;
   }

   public Path getExternalAssetSource() {
      return this.assetIndex == null ? this.assetDirectory.toPath() : IndexedAssetSource.createIndexFs(this.assetDirectory.toPath(), this.assetIndex);
   }
}
