package net.minecraft.client.gui.screens.debug;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;

@Environment(EnvType.CLIENT)
enum GameModeSwitcherScreen$GameModeIcon {
   CREATIVE(Component.translatable("gameMode.creative"), GameType.CREATIVE, new ItemStack(Blocks.GRASS_BLOCK)),
   SURVIVAL(Component.translatable("gameMode.survival"), GameType.SURVIVAL, new ItemStack(Items.IRON_SWORD)),
   ADVENTURE(Component.translatable("gameMode.adventure"), GameType.ADVENTURE, new ItemStack(Items.BURIED_TREASURE_MAP)),
   SPECTATOR(Component.translatable("gameMode.spectator"), GameType.SPECTATOR, new ItemStack(Items.ENDER_EYE));

   private static final GameModeSwitcherScreen$GameModeIcon[] VALUES = values();
   private static final int ICON_AREA = 16;
   private static final int ICON_TOP_LEFT = 5;
   private final Component name;
   private final GameType mode;
   private final ItemStack renderStack;

   private GameModeSwitcherScreen$GameModeIcon(final Component name, final GameType mode, final ItemStack renderStack) {
      this.name = name;
      this.mode = mode;
      this.renderStack = renderStack;
   }

   private void extractIcon(final GuiGraphicsExtractor graphics, final int x, final int y) {
      graphics.item(this.renderStack, x, y);
   }

   private GameModeSwitcherScreen$GameModeIcon getNext() {
      return switch (this) {
         case CREATIVE -> SURVIVAL;
         case SURVIVAL -> ADVENTURE;
         case ADVENTURE -> SPECTATOR;
         case SPECTATOR -> CREATIVE;
      };
   }

   private static GameModeSwitcherScreen$GameModeIcon getFromGameType(final GameType gameType) {
      return switch (gameType) {
         case SPECTATOR -> SPECTATOR;
         case SURVIVAL -> SURVIVAL;
         case CREATIVE -> CREATIVE;
         case ADVENTURE -> ADVENTURE;
      };
   }
}
