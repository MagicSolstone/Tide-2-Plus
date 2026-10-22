package net.minecraft.client.gui.screens.worldselection;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.datafixers.util.Pair;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.SelectableEntry;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.screens.AlertScreen;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.FaviconTexture;
import net.minecraft.client.gui.screens.GenericMessageScreen;
import net.minecraft.client.gui.screens.NoticeWithLinkScreen;
import net.minecraft.client.gui.screens.ProgressScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.nbt.NbtException;
import net.minecraft.nbt.ReportedNbtException;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.LevelStorageSource$LevelStorageAccess;
import net.minecraft.world.level.storage.LevelSummary;
import net.minecraft.world.level.storage.LevelSummary$CorruptedLevelSummary;
import net.minecraft.world.level.storage.LevelSummary$SymlinkLevelSummary;
import net.minecraft.world.level.validation.ContentValidationException;
import net.minecraft.world.level.validation.ForbiddenSymlinkInfo;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public final class WorldSelectionList$WorldListEntry extends WorldSelectionList$Entry implements SelectableEntry {
   private static final int ICON_SIZE = 32;
   private final WorldSelectionList list;
   private final Minecraft minecraft;
   private final Screen screen;
   private final LevelSummary summary;
   private final FaviconTexture icon;
   private final StringWidget worldNameText;
   private final StringWidget idAndLastPlayedText;
   private final StringWidget infoText;
   @Nullable
   private Path iconFile;

   public WorldSelectionList$WorldListEntry(final WorldSelectionList this$0, final WorldSelectionList list, final LevelSummary summary) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
      this.list = list;
      this.minecraft = WorldSelectionList.access$000(list);
      this.screen = list.getScreen();
      this.summary = summary;
      this.icon = FaviconTexture.forWorld(this.minecraft.getTextureManager(), summary.getLevelId());
      this.iconFile = summary.getIcon();
      int maxTextWidth = list.getRowWidth() - this.getTextX() - 2;
      Component worldNameComponent = Component.literal(summary.getLevelName());
      this.worldNameText = new StringWidget(worldNameComponent, this.minecraft.font);
      this.worldNameText.setMaxWidth(maxTextWidth);
      if (this.minecraft.font.width(worldNameComponent) > maxTextWidth) {
         this.worldNameText.setTooltip(Tooltip.create(worldNameComponent));
      }

      String levelIdAndDate = summary.getLevelId();
      long lastPlayed = summary.getLastPlayed();
      if (lastPlayed != -1L) {
         ZonedDateTime lastPlayedTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(lastPlayed), ZoneId.systemDefault());
         levelIdAndDate = levelIdAndDate + " (" + WorldSelectionList.DATE_FORMAT.format(lastPlayedTime) + ")";
      }

      Component levelIdAndDateComponent = Component.literal(levelIdAndDate).withColor(-8355712);
      this.idAndLastPlayedText = new StringWidget(levelIdAndDateComponent, this.minecraft.font);
      this.idAndLastPlayedText.setMaxWidth(maxTextWidth);
      if (this.minecraft.font.width(levelIdAndDate) > maxTextWidth) {
         this.idAndLastPlayedText.setTooltip(Tooltip.create(levelIdAndDateComponent));
      }

      Component info = ComponentUtils.mergeStyles(summary.getInfo(), Style.EMPTY.withColor(-8355712));
      this.infoText = new StringWidget(info, this.minecraft.font);
      this.infoText.setMaxWidth(maxTextWidth);
      if (this.minecraft.font.width(info) > maxTextWidth) {
         this.infoText.setTooltip(Tooltip.create(info));
      }

      this.validateIconFile();
      this.loadIcon();
   }

   private void validateIconFile() {
      if (this.iconFile != null) {
         try {
            BasicFileAttributes attributes = Files.readAttributes(this.iconFile, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
            if (attributes.isSymbolicLink()) {
               List<ForbiddenSymlinkInfo> issues = this.minecraft.directoryValidator().validateSymlink(this.iconFile);
               if (!issues.isEmpty()) {
                  WorldSelectionList.LOGGER.warn("{}", ContentValidationException.getMessage(this.iconFile, issues));
                  this.iconFile = null;
               } else {
                  attributes = Files.readAttributes(this.iconFile, BasicFileAttributes.class);
               }
            }

            if (!attributes.isRegularFile()) {
               this.iconFile = null;
            }
         } catch (NoSuchFileException var3) {
            this.iconFile = null;
         } catch (IOException var4) {
            WorldSelectionList.LOGGER.error("could not validate symlink", var4);
            this.iconFile = null;
         }
      }
   }

   @Override
   public Component getNarration() {
      Component entryNarration = Component.translatable(
         "narrator.select.world_info", this.summary.getLevelName(), Component.translationArg(new Date(this.summary.getLastPlayed())), this.summary.getInfo()
      );
      if (this.summary.isLocked()) {
         entryNarration = CommonComponents.joinForNarration(entryNarration, WorldSelectionList.WORLD_LOCKED_TOOLTIP);
      }

      if (this.summary.isExperimental()) {
         entryNarration = CommonComponents.joinForNarration(entryNarration, WorldSelectionList.WORLD_EXPERIMENTAL);
      }

      return Component.translatable("narrator.select", entryNarration);
   }

   @Override
   public void extractContent(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final boolean hovered, final float a) {
      int textX = this.getTextX();
      this.worldNameText.setPosition(textX, this.getContentY() + 1);
      this.worldNameText.extractRenderState(graphics, mouseX, mouseY, a);
      this.idAndLastPlayedText.setPosition(textX, this.getContentY() + 9 + 3);
      this.idAndLastPlayedText.extractRenderState(graphics, mouseX, mouseY, a);
      this.infoText.setPosition(textX, this.getContentY() + 9 + 9 + 3);
      this.infoText.extractRenderState(graphics, mouseX, mouseY, a);
      graphics.blit(RenderPipelines.GUI_TEXTURED, this.icon.textureLocation(), this.getContentX(), this.getContentY(), 0.0F, 0.0F, 32, 32, 32, 32);
      if (this.list.entryType == WorldSelectionList$EntryType.SINGLEPLAYER && hovered) {
         graphics.fill(this.getContentX(), this.getContentY(), this.getContentX() + 32, this.getContentY() + 32, -1601138544);
         int relX = mouseX - this.getContentX();
         int relY = mouseY - this.getContentY();
         boolean isOverIcon = this.mouseOverIcon(relX, relY, 32);
         Identifier joinSprite = isOverIcon ? WorldSelectionList.JOIN_HIGHLIGHTED_SPRITE : WorldSelectionList.JOIN_SPRITE;
         Identifier warningSprite = isOverIcon ? WorldSelectionList.WARNING_HIGHLIGHTED_SPRITE : WorldSelectionList.WARNING_SPRITE;
         Identifier errorSprite = isOverIcon ? WorldSelectionList.ERROR_HIGHLIGHTED_SPRITE : WorldSelectionList.ERROR_SPRITE;
         Identifier joinWithErrorSprite = isOverIcon ? WorldSelectionList.MARKED_JOIN_HIGHLIGHTED_SPRITE : WorldSelectionList.MARKED_JOIN_SPRITE;
         if (this.summary instanceof LevelSummary$SymlinkLevelSummary || this.summary instanceof LevelSummary$CorruptedLevelSummary) {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, errorSprite, this.getContentX(), this.getContentY(), 32, 32);
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, joinWithErrorSprite, this.getContentX(), this.getContentY(), 32, 32);
            return;
         }

         if (this.summary.isLocked()) {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, errorSprite, this.getContentX(), this.getContentY(), 32, 32);
            if (isOverIcon) {
               graphics.setTooltipForNextFrame(this.minecraft.font.split(WorldSelectionList.WORLD_LOCKED_TOOLTIP, 175), mouseX, mouseY);
            }
         } else if (this.summary.requiresManualConversion()) {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, errorSprite, this.getContentX(), this.getContentY(), 32, 32);
            if (isOverIcon) {
               graphics.setTooltipForNextFrame(this.minecraft.font.split(WorldSelectionList.WORLD_REQUIRES_CONVERSION, 175), mouseX, mouseY);
            }
         } else if (!this.summary.isCompatible()) {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, errorSprite, this.getContentX(), this.getContentY(), 32, 32);
            if (isOverIcon) {
               graphics.setTooltipForNextFrame(this.minecraft.font.split(WorldSelectionList.INCOMPATIBLE_VERSION_TOOLTIP, 175), mouseX, mouseY);
            }
         } else if (this.summary.shouldBackup()) {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, joinWithErrorSprite, this.getContentX(), this.getContentY(), 32, 32);
            if (this.summary.isDowngrade()) {
               graphics.blitSprite(RenderPipelines.GUI_TEXTURED, errorSprite, this.getContentX(), this.getContentY(), 32, 32);
               if (isOverIcon) {
                  graphics.setTooltipForNextFrame(
                     ImmutableList.of(
                        WorldSelectionList.FROM_NEWER_TOOLTIP_1.getVisualOrderText(), WorldSelectionList.FROM_NEWER_TOOLTIP_2.getVisualOrderText()
                     ),
                     mouseX,
                     mouseY
                  );
               }
            } else if (!SharedConstants.getCurrentVersion().stable()) {
               graphics.blitSprite(RenderPipelines.GUI_TEXTURED, warningSprite, this.getContentX(), this.getContentY(), 32, 32);
               if (isOverIcon) {
                  graphics.setTooltipForNextFrame(
                     ImmutableList.of(WorldSelectionList.SNAPSHOT_TOOLTIP_1.getVisualOrderText(), WorldSelectionList.SNAPSHOT_TOOLTIP_2.getVisualOrderText()),
                     mouseX,
                     mouseY
                  );
               }
            }

            if (isOverIcon) {
               WorldSelectionList.access$100(this.this$0, graphics);
            }
         } else {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, joinSprite, this.getContentX(), this.getContentY(), 32, 32);
            if (isOverIcon) {
               WorldSelectionList.access$200(this.this$0, graphics);
            }
         }
      }
   }

   private int getTextX() {
      return this.getContentX() + 32 + 3;
   }

   @Override
   public boolean mouseClicked(final MouseButtonEvent event, final boolean doubleClick) {
      if (this.canInteract()) {
         int relX = (int)event.x() - this.getContentX();
         int relY = (int)event.y() - this.getContentY();
         if (doubleClick || this.mouseOverIcon(relX, relY, 32) && this.list.entryType == WorldSelectionList$EntryType.SINGLEPLAYER) {
            this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            Consumer<WorldSelectionList$WorldListEntry> onEntryInteract = this.list.onEntryInteract;
            if (onEntryInteract != null) {
               onEntryInteract.accept(this);
               return true;
            }
         }
      }

      return super.mouseClicked(event, doubleClick);
   }

   @Override
   public boolean keyPressed(final KeyEvent event) {
      if (event.isSelection() && this.canInteract()) {
         this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
         Consumer<WorldSelectionList$WorldListEntry> onEntryInteract = this.list.onEntryInteract;
         if (onEntryInteract != null) {
            onEntryInteract.accept(this);
            return true;
         }
      }

      return super.keyPressed(event);
   }

   public boolean canInteract() {
      return this.summary.primaryActionActive() || this.list.entryType == WorldSelectionList$EntryType.UPLOAD_WORLD;
   }

   public void joinWorld() {
      if (this.summary.primaryActionActive()) {
         if (this.summary instanceof LevelSummary$SymlinkLevelSummary) {
            this.minecraft.gui.setScreen(NoticeWithLinkScreen.createWorldSymlinkWarningScreen(() -> this.minecraft.gui.setScreen(this.screen)));
         } else {
            this.minecraft.createWorldOpenFlows().openWorld(this.summary.getLevelId(), this.list::returnToScreen);
         }
      }
   }

   public void deleteWorld() {
      this.minecraft
         .gui
         .setScreen(
            new ConfirmScreen(
               result -> {
                  if (result) {
                     this.minecraft.gui.setScreen(new ProgressScreen(true));
                     this.doDeleteWorld();
                  }

                  this.list.returnToScreen();
               },
               Component.translatable("selectWorld.deleteQuestion"),
               Component.translatable("selectWorld.deleteWarning", this.summary.getLevelName()),
               Component.translatable("selectWorld.deleteButton"),
               CommonComponents.GUI_CANCEL
            )
         );
   }

   public void doDeleteWorld() {
      LevelStorageSource levelSource = this.minecraft.getLevelSource();
      String levelId = this.summary.getLevelId();

      try (LevelStorageSource$LevelStorageAccess access = levelSource.createAccess(levelId)) {
         access.deleteLevel();
      } catch (IOException var8) {
         SystemToast.onWorldDeleteFailure(this.minecraft, levelId);
         WorldSelectionList.LOGGER.error("Failed to delete world {}", levelId, var8);
      }
   }

   public void editWorld() {
      this.queueLoadScreen();
      String levelId = this.summary.getLevelId();

      LevelStorageSource$LevelStorageAccess access;
      try {
         access = this.minecraft.getLevelSource().validateAndCreateAccess(levelId);
      } catch (IOException var6) {
         SystemToast.onWorldAccessFailure(this.minecraft, levelId);
         WorldSelectionList.LOGGER.error("Failed to access level {}", levelId, var6);
         this.list.reloadWorldList();
         return;
      } catch (ContentValidationException var7) {
         WorldSelectionList.LOGGER.warn("{}", var7.getMessage());
         this.minecraft.gui.setScreen(NoticeWithLinkScreen.createWorldSymlinkWarningScreen(() -> this.minecraft.gui.setScreen(this.screen)));
         return;
      }

      EditWorldScreen editScreen;
      try {
         editScreen = EditWorldScreen.create(this.minecraft, access, result -> {
            access.safeClose();
            this.list.returnToScreen();
         });
      } catch (NbtException | ReportedNbtException | IOException var5) {
         access.safeClose();
         SystemToast.onWorldAccessFailure(this.minecraft, levelId);
         WorldSelectionList.LOGGER.error("Failed to load world data {}", levelId, var5);
         this.list.reloadWorldList();
         return;
      }

      this.minecraft.gui.setScreen(editScreen);
   }

   public void recreateWorld() {
      this.queueLoadScreen();

      try (LevelStorageSource$LevelStorageAccess access = this.minecraft.getLevelSource().validateAndCreateAccess(this.summary.getLevelId())) {
         Pair<LevelSettings, WorldCreationContext> recreatedSettings = this.minecraft.createWorldOpenFlows().recreateWorldData(access);
         LevelSettings levelSettings = (LevelSettings)recreatedSettings.getFirst();
         WorldCreationContext creationContext = (WorldCreationContext)recreatedSettings.getSecond();
         Path dataPackDir = CreateWorldScreen.createTempDataPackDirFromExistingWorld(access.getLevelPath(LevelResource.DATAPACK_DIR), this.minecraft);
         creationContext.validate();
         if (creationContext.options().isOldCustomizedWorld()) {
            this.minecraft
               .gui
               .setScreen(
                  new ConfirmScreen(
                     result -> this.minecraft
                        .gui
                        .setScreen(
                           (Screen)(result
                              ? CreateWorldScreen.createFromExisting(this.minecraft, this.list::returnToScreen, levelSettings, creationContext, dataPackDir)
                              : this.screen)
                        ),
                     Component.translatable("selectWorld.recreate.customized.title"),
                     Component.translatable("selectWorld.recreate.customized.text"),
                     CommonComponents.GUI_PROCEED,
                     CommonComponents.GUI_CANCEL
                  )
               );
         } else {
            this.minecraft
               .gui
               .setScreen(CreateWorldScreen.createFromExisting(this.minecraft, this.list::returnToScreen, levelSettings, creationContext, dataPackDir));
         }
      } catch (ContentValidationException var8) {
         WorldSelectionList.LOGGER.warn("{}", var8.getMessage());
         this.minecraft.gui.setScreen(NoticeWithLinkScreen.createWorldSymlinkWarningScreen(() -> this.minecraft.gui.setScreen(this.screen)));
      } catch (Exception var9) {
         WorldSelectionList.LOGGER.error("Unable to recreate world", var9);
         this.minecraft
            .gui
            .setScreen(
               new AlertScreen(
                  () -> this.minecraft.gui.setScreen(this.screen),
                  Component.translatable("selectWorld.recreate.error.title"),
                  Component.translatable("selectWorld.recreate.error.text")
               )
            );
      }
   }

   private void queueLoadScreen() {
      this.minecraft.setScreenAndShow(new GenericMessageScreen(Component.translatable("selectWorld.data_read")));
   }

   private void loadIcon() {
      boolean shouldHaveIcon = this.iconFile != null && Files.isRegularFile(this.iconFile);
      if (shouldHaveIcon) {
         try (InputStream stream = Files.newInputStream(this.iconFile)) {
            this.icon.upload(NativeImage.read(stream));
         } catch (Throwable var7) {
            WorldSelectionList.LOGGER.error("Invalid icon for world {}", this.summary.getLevelId(), var7);
            this.iconFile = null;
         }
      } else {
         this.icon.clear();
      }
   }

   @Override
   public void close() {
      if (!this.icon.isClosed()) {
         this.icon.close();
      }
   }

   public String getLevelName() {
      return this.summary.getLevelName();
   }

   @Override
   public LevelSummary getLevelSummary() {
      return this.summary;
   }
}
