package net.minecraft.client.gui.screens.worldselection;

import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.level.storage.LevelSummary;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class WorldSelectionList$Builder {
   private final Minecraft minecraft;
   private final Screen screen;
   private int width;
   private int height;
   private String filter = "";
   private WorldSelectionList$EntryType type = WorldSelectionList$EntryType.SINGLEPLAYER;
   @Nullable
   private WorldSelectionList oldList = null;
   @Nullable
   private Consumer<LevelSummary> onEntrySelect = null;
   @Nullable
   private Consumer<WorldSelectionList$WorldListEntry> onEntryInteract = null;

   public WorldSelectionList$Builder(final Minecraft minecraft, final Screen screen) {
      this.minecraft = minecraft;
      this.screen = screen;
   }

   public WorldSelectionList$Builder width(final int width) {
      this.width = width;
      return this;
   }

   public WorldSelectionList$Builder height(final int height) {
      this.height = height;
      return this;
   }

   public WorldSelectionList$Builder filter(final String filter) {
      this.filter = filter;
      return this;
   }

   public WorldSelectionList$Builder oldList(@Nullable final WorldSelectionList oldList) {
      this.oldList = oldList;
      return this;
   }

   public WorldSelectionList$Builder onEntrySelect(final Consumer<LevelSummary> onEntrySelect) {
      this.onEntrySelect = onEntrySelect;
      return this;
   }

   public WorldSelectionList$Builder onEntryInteract(final Consumer<WorldSelectionList$WorldListEntry> onEntryInteract) {
      this.onEntryInteract = onEntryInteract;
      return this;
   }

   public WorldSelectionList$Builder uploadWorld() {
      this.type = WorldSelectionList$EntryType.UPLOAD_WORLD;
      return this;
   }

   public WorldSelectionList build() {
      return new WorldSelectionList(
         this.screen, this.minecraft, this.width, this.height, this.filter, this.oldList, this.onEntrySelect, this.onEntryInteract, this.type
      );
   }
}
