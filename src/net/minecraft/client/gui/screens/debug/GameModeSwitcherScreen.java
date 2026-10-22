package net.minecraft.client.gui.screens.debug;

import com.google.common.collect.Lists;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.game.ServerboundChangeGameModePacket;
import net.minecraft.resources.Identifier;
import net.minecraft.server.commands.GameModeCommand;
import net.minecraft.world.level.GameType;

@Environment(EnvType.CLIENT)
public class GameModeSwitcherScreen extends Screen {
   private static final Identifier SLOT_SPRITE = Identifier.withDefaultNamespace("gamemode_switcher/slot");
   private static final Identifier SELECTION_SPRITE = Identifier.withDefaultNamespace("gamemode_switcher/selection");
   private static final Identifier GAMEMODE_SWITCHER_LOCATION = Identifier.withDefaultNamespace("textures/gui/container/gamemode_switcher.png");
   private static final int SPRITE_SHEET_WIDTH = 128;
   private static final int SPRITE_SHEET_HEIGHT = 128;
   private static final int SLOT_AREA = 26;
   private static final int SLOT_PADDING = 5;
   private static final int SLOT_AREA_PADDED = 31;
   private static final int HELP_TIPS_OFFSET_Y = 5;
   private static final int ALL_SLOTS_WIDTH = GameModeSwitcherScreen$GameModeIcon.values().length * 31 - 5;
   private final GameModeSwitcherScreen$GameModeIcon previousHovered;
   private GameModeSwitcherScreen$GameModeIcon currentlyHovered;
   private int firstMouseX;
   private int firstMouseY;
   private boolean setFirstMousePos;
   private final List<GameModeSwitcherScreen$GameModeSlot> slots = Lists.newArrayList();

   public GameModeSwitcherScreen() {
      super(GameNarrator.NO_TITLE);
      this.previousHovered = GameModeSwitcherScreen$GameModeIcon.getFromGameType(this.getDefaultSelected());
      this.currentlyHovered = this.previousHovered;
   }

   private GameType getDefaultSelected() {
      MultiPlayerGameMode gameMode = Minecraft.getInstance().gameMode;
      GameType previous = gameMode.getPreviousPlayerMode();
      if (previous != null) {
         return previous;
      } else {
         return gameMode.getPlayerMode() == GameType.CREATIVE ? GameType.SURVIVAL : GameType.CREATIVE;
      }
   }

   @Override
   protected void init() {
      super.init();
      this.slots.clear();
      this.currentlyHovered = this.previousHovered;

      for (int i = 0; i < GameModeSwitcherScreen$GameModeIcon.VALUES.length; i++) {
         GameModeSwitcherScreen$GameModeIcon icon = GameModeSwitcherScreen$GameModeIcon.VALUES[i];
         this.slots.add(new GameModeSwitcherScreen$GameModeSlot(icon, this.width / 2 - ALL_SLOTS_WIDTH / 2 + i * 31, this.height / 2 - 31));
      }
   }

   @Override
   public void extractRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
      graphics.centeredText(this.font, this.currentlyHovered.name, this.width / 2, this.height / 2 - 31 - 20, -1);
      MutableComponent selectKey = Component.translatable(
         "debug.gamemodes.select_next", this.minecraft.options.keyDebugSwitchGameMode.getTranslatedKeyMessage().copy().withStyle(ChatFormatting.AQUA)
      );
      graphics.centeredText(this.font, selectKey, this.width / 2, this.height / 2 + 5, -1);
      if (!this.setFirstMousePos) {
         this.firstMouseX = mouseX;
         this.firstMouseY = mouseY;
         this.setFirstMousePos = true;
      }

      boolean sameAsFirstMousePos = this.firstMouseX == mouseX && this.firstMouseY == mouseY;

      for (GameModeSwitcherScreen$GameModeSlot slot : this.slots) {
         slot.extractRenderState(graphics, mouseX, mouseY, a);
         slot.setSelected(this.currentlyHovered == slot.icon);
         if (!sameAsFirstMousePos && slot.isHoveredOrFocused()) {
            this.currentlyHovered = slot.icon;
         }
      }
   }

   @Override
   public void extractBackground(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
      int xo = this.width / 2 - 62;
      int yo = this.height / 2 - 31 - 27;
      graphics.blit(RenderPipelines.GUI_TEXTURED, GAMEMODE_SWITCHER_LOCATION, xo, yo, 0.0F, 0.0F, 125, 75, 128, 128);
   }

   private void switchToHoveredGameMode() {
      switchToHoveredGameMode(this.minecraft, this.currentlyHovered);
   }

   private static void switchToHoveredGameMode(final Minecraft minecraft, final GameModeSwitcherScreen$GameModeIcon toGameMode) {
      if (minecraft.canSwitchGameMode()) {
         GameModeSwitcherScreen$GameModeIcon currentGameMode = GameModeSwitcherScreen$GameModeIcon.getFromGameType(minecraft.gameMode.getPlayerMode());
         if (toGameMode != currentGameMode && GameModeCommand.PERMISSION_CHECK.check(minecraft.player.permissions())) {
            minecraft.player.connection.send(new ServerboundChangeGameModePacket(toGameMode.mode));
         }
      }
   }

   @Override
   public boolean keyPressed(final KeyEvent event) {
      if (this.minecraft.options.keyDebugSwitchGameMode.matches(event)) {
         this.setFirstMousePos = false;
         this.currentlyHovered = this.currentlyHovered.getNext();
         return true;
      } else {
         return super.keyPressed(event);
      }
   }

   @Override
   public boolean keyReleased(final KeyEvent event) {
      if (this.minecraft.options.keyDebugModifier.matches(event)) {
         this.switchToHoveredGameMode();
         this.minecraft.gui.setScreen(null);
         return true;
      } else {
         return super.keyReleased(event);
      }
   }

   @Override
   public boolean mouseReleased(final MouseButtonEvent event) {
      if (this.minecraft.options.keyDebugModifier.matchesMouse(event)) {
         this.switchToHoveredGameMode();
         this.minecraft.gui.setScreen(null);
         return true;
      } else {
         return super.mouseReleased(event);
      }
   }

   @Override
   public boolean isPauseScreen() {
      return false;
   }
}
