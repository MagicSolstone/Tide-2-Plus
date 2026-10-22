package net.minecraft.client.gui.screens.worldselection;

import com.mojang.logging.LogUtils;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.CrashReport;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.ErrorScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.level.storage.LevelStorageException;
import net.minecraft.world.level.storage.LevelStorageSource$LevelCandidates;
import net.minecraft.world.level.storage.LevelSummary;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public class WorldSelectionList extends ObjectSelectionList<WorldSelectionList$Entry> {
   public static final DateTimeFormatter DATE_FORMAT = Util.localizedDateFormatter(FormatStyle.SHORT);
   private static final Identifier ERROR_HIGHLIGHTED_SPRITE = Identifier.withDefaultNamespace("world_list/error_highlighted");
   private static final Identifier ERROR_SPRITE = Identifier.withDefaultNamespace("world_list/error");
   private static final Identifier MARKED_JOIN_HIGHLIGHTED_SPRITE = Identifier.withDefaultNamespace("world_list/marked_join_highlighted");
   private static final Identifier MARKED_JOIN_SPRITE = Identifier.withDefaultNamespace("world_list/marked_join");
   private static final Identifier WARNING_HIGHLIGHTED_SPRITE = Identifier.withDefaultNamespace("world_list/warning_highlighted");
   private static final Identifier WARNING_SPRITE = Identifier.withDefaultNamespace("world_list/warning");
   private static final Identifier JOIN_HIGHLIGHTED_SPRITE = Identifier.withDefaultNamespace("world_list/join_highlighted");
   private static final Identifier JOIN_SPRITE = Identifier.withDefaultNamespace("world_list/join");
   private static final Logger LOGGER = LogUtils.getLogger();
   private static final Component FROM_NEWER_TOOLTIP_1 = Component.translatable("selectWorld.tooltip.fromNewerVersion1").withStyle(ChatFormatting.RED);
   private static final Component FROM_NEWER_TOOLTIP_2 = Component.translatable("selectWorld.tooltip.fromNewerVersion2").withStyle(ChatFormatting.RED);
   private static final Component SNAPSHOT_TOOLTIP_1 = Component.translatable("selectWorld.tooltip.snapshot1").withStyle(ChatFormatting.GOLD);
   private static final Component SNAPSHOT_TOOLTIP_2 = Component.translatable("selectWorld.tooltip.snapshot2").withStyle(ChatFormatting.GOLD);
   private static final Component WORLD_LOCKED_TOOLTIP = Component.translatable("selectWorld.locked").withStyle(ChatFormatting.RED);
   private static final Component WORLD_REQUIRES_CONVERSION = Component.translatable("selectWorld.conversion.tooltip").withStyle(ChatFormatting.RED);
   private static final Component INCOMPATIBLE_VERSION_TOOLTIP = Component.translatable("selectWorld.incompatible.tooltip").withStyle(ChatFormatting.RED);
   private static final Component WORLD_EXPERIMENTAL = Component.translatable("selectWorld.experimental");
   private final Screen screen;
   private CompletableFuture<List<LevelSummary>> pendingLevels;
   @Nullable
   private List<LevelSummary> currentlyDisplayedLevels;
   private final WorldSelectionList$LoadingHeader loadingHeader;
   private final WorldSelectionList$EntryType entryType;
   private String filter;
   private boolean hasPolled;
   @Nullable
   private final Consumer<LevelSummary> onEntrySelect;
   @Nullable
   private final Consumer<WorldSelectionList$WorldListEntry> onEntryInteract;

   private WorldSelectionList(
      final Screen screen,
      final Minecraft minecraft,
      final int width,
      final int height,
      final String filter,
      @Nullable final WorldSelectionList oldList,
      @Nullable final Consumer<LevelSummary> onEntrySelect,
      @Nullable final Consumer<WorldSelectionList$WorldListEntry> onEntryInteract,
      final WorldSelectionList$EntryType entryType
   ) {
      super(minecraft, width, height, 0, 36);
      this.screen = screen;
      this.loadingHeader = new WorldSelectionList$LoadingHeader(minecraft);
      this.filter = filter;
      this.onEntrySelect = onEntrySelect;
      this.onEntryInteract = onEntryInteract;
      this.entryType = entryType;
      if (oldList != null) {
         this.pendingLevels = oldList.pendingLevels;
      } else {
         this.pendingLevels = this.loadLevels();
      }

      this.addEntry(this.loadingHeader);
      this.handleNewLevels(this.pollLevelsIgnoreErrors());
   }

   @Override
   protected void clearEntries() {
      this.children().forEach(WorldSelectionList$Entry::close);
      super.clearEntries();
   }

   @Nullable
   private List<LevelSummary> pollLevelsIgnoreErrors() {
      try {
         List<LevelSummary> completedLevels = this.pendingLevels.getNow(null);
         if (this.entryType == WorldSelectionList$EntryType.UPLOAD_WORLD) {
            if (completedLevels == null || this.hasPolled) {
               return null;
            }

            this.hasPolled = true;
            completedLevels = completedLevels.stream().filter(LevelSummary::canUpload).toList();
         }

         return completedLevels;
      } catch (CancellationException | CompletionException var2) {
         return null;
      }
   }

   public void reloadWorldList() {
      this.pendingLevels = this.loadLevels();
   }

   @Override
   public void extractWidgetRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
      List<LevelSummary> newLevels = this.pollLevelsIgnoreErrors();
      if (newLevels != this.currentlyDisplayedLevels) {
         this.handleNewLevels(newLevels);
      }

      super.extractWidgetRenderState(graphics, mouseX, mouseY, a);
   }

   private void handleNewLevels(@Nullable final List<LevelSummary> levels) {
      if (levels != null) {
         if (levels.isEmpty()) {
            switch (this.entryType) {
               case SINGLEPLAYER:
                  CreateWorldScreen.openFresh(this.minecraft, () -> this.minecraft.gui.setScreen(null));
                  break;
               case UPLOAD_WORLD:
                  this.clearEntries();
                  this.addEntry(new WorldSelectionList$NoWorldsEntry(Component.translatable("mco.upload.select.world.none"), this.screen.getFont()));
            }
         } else {
            this.fillLevels(this.filter, levels);
            this.currentlyDisplayedLevels = levels;
         }
      }
   }

   public void updateFilter(final String newFilter) {
      if (this.currentlyDisplayedLevels != null && !newFilter.equals(this.filter)) {
         this.fillLevels(newFilter, this.currentlyDisplayedLevels);
      }

      this.filter = newFilter;
   }

   private CompletableFuture<List<LevelSummary>> loadLevels() {
      LevelStorageSource$LevelCandidates levelCandidates;
      try {
         levelCandidates = this.minecraft.getLevelSource().findLevelCandidates();
      } catch (LevelStorageException var3) {
         LOGGER.error("Couldn't load level list", var3);
         this.handleLevelLoadFailure(var3.getMessageComponent());
         return CompletableFuture.failedFuture(var3);
      }

      return this.minecraft.getLevelSource().loadLevelSummaries(levelCandidates).exceptionally(throwable -> {
         this.minecraft.delayCrash(CrashReport.forThrowable(throwable, "Couldn't load level list"));
         return List.of();
      });
   }

   private void fillLevels(final String filter, final List<LevelSummary> levels) {
      List<WorldSelectionList$Entry> worldEntries = new ArrayList<>();
      Optional<WorldSelectionList$WorldListEntry> selectedOpt = this.getSelectedOpt();
      WorldSelectionList$WorldListEntry entryToSelect = null;

      for (LevelSummary level : levels.stream().filter(levelx -> this.filterAccepts(filter.toLowerCase(Locale.ROOT), levelx)).toList()) {
         WorldSelectionList$WorldListEntry worldListEntry = new WorldSelectionList$WorldListEntry(this, this, level);
         if (selectedOpt.isPresent() && selectedOpt.get().getLevelSummary().getLevelId().equals(worldListEntry.getLevelSummary().getLevelId())) {
            entryToSelect = worldListEntry;
         }

         worldEntries.add(worldListEntry);
      }

      this.removeEntries(this.children().stream().filter(entry -> !worldEntries.contains(entry)).toList());
      worldEntries.forEach(entry -> {
         if (!this.children().contains(entry)) {
            this.addEntry(entry);
         }
      });
      this.setSelected((WorldSelectionList$Entry)entryToSelect);
      this.notifyListUpdated();
   }

   private boolean filterAccepts(final String filter, final LevelSummary level) {
      return level.getLevelName().toLowerCase(Locale.ROOT).contains(filter) || level.getLevelId().toLowerCase(Locale.ROOT).contains(filter);
   }

   private void notifyListUpdated() {
      this.refreshScrollAmount();
      this.screen.triggerImmediateNarration(true);
   }

   private void handleLevelLoadFailure(final Component message) {
      this.minecraft.gui.setScreen(new ErrorScreen(Component.translatable("selectWorld.unable_to_load"), message));
   }

   @Override
   public int getRowWidth() {
      return 270;
   }

   public void setSelected(@Nullable final WorldSelectionList$Entry selected) {
      super.setSelected(selected);
      if (this.onEntrySelect != null) {
         this.onEntrySelect.accept(selected instanceof WorldSelectionList$WorldListEntry entry ? entry.summary : null);
      }
   }

   public Optional<WorldSelectionList$WorldListEntry> getSelectedOpt() {
      WorldSelectionList$Entry selected = this.getSelected();
      return selected instanceof WorldSelectionList$WorldListEntry worldEntry ? Optional.of(worldEntry) : Optional.empty();
   }

   public void returnToScreen() {
      this.reloadWorldList();
      this.minecraft.gui.setScreen(this.screen);
   }

   public Screen getScreen() {
      return this.screen;
   }

   @Override
   public void updateWidgetNarration(final NarrationElementOutput output) {
      if (this.children().contains(this.loadingHeader)) {
         this.loadingHeader.updateNarration(output);
      } else {
         super.updateWidgetNarration(output);
      }
   }
}
