package com.mojang.realmsclient.gui.screens.configuration;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ObjectSelectionList$Entry;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
class RealmsBackupInfoScreen$BackupInfoListEntry extends ObjectSelectionList$Entry<RealmsBackupInfoScreen$BackupInfoListEntry> {
   private static final Component TEMPLATE_NAME = Component.translatable("mco.backup.entry.templateName");
   private static final Component GAME_DIFFICULTY = Component.translatable("mco.backup.entry.gameDifficulty");
   private static final Component NAME = Component.translatable("mco.backup.entry.name");
   private static final Component GAME_SERVER_VERSION = Component.translatable("mco.backup.entry.gameServerVersion");
   private static final Component UPLOADED = Component.translatable("mco.backup.entry.uploaded");
   private static final Component ENABLED_PACK = Component.translatable("mco.backup.entry.enabledPack");
   private static final Component DESCRIPTION = Component.translatable("mco.backup.entry.description");
   private static final Component GAME_MODE = Component.translatable("mco.backup.entry.gameMode");
   private static final Component SEED = Component.translatable("mco.backup.entry.seed");
   private static final Component WORLD_TYPE = Component.translatable("mco.backup.entry.worldType");
   private static final Component UNDEFINED = Component.translatable("mco.backup.entry.undefined");
   private final String key;
   private final String value;
   private final Component keyComponent;
   private final Component valueComponent;

   public RealmsBackupInfoScreen$BackupInfoListEntry(final RealmsBackupInfoScreen this$0, final String key, final String value) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
      this.key = key;
      this.value = value;
      this.keyComponent = this.translateKey(key);
      this.valueComponent = this$0x.checkForSpecificMetadata(key, value);
   }

   @Override
   public void extractContent(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final boolean hovered, final float a) {
      graphics.text(RealmsBackupInfoScreen.access$000(this.this$0), this.keyComponent, this.getContentX(), this.getContentY(), -6250336);
      graphics.text(RealmsBackupInfoScreen.access$100(this.this$0), this.valueComponent, this.getContentX(), this.getContentY() + 12, -1);
   }

   private Component translateKey(final String key) {
      return switch (key) {
         case "template_name" -> TEMPLATE_NAME;
         case "game_difficulty" -> GAME_DIFFICULTY;
         case "name" -> NAME;
         case "game_server_version" -> GAME_SERVER_VERSION;
         case "uploaded" -> UPLOADED;
         case "enabled_packs" -> ENABLED_PACK;
         case "description" -> DESCRIPTION;
         case "game_mode" -> GAME_MODE;
         case "seed" -> SEED;
         case "world_type" -> WORLD_TYPE;
         default -> UNDEFINED;
      };
   }

   @Override
   public Component getNarration() {
      return Component.translatable("narrator.select", this.key + " " + this.value);
   }
}
